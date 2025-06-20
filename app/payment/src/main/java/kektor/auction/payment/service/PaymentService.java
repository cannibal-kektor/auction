package kektor.auction.payment.service;

import kektor.auction.payment.dto.PaymentAccountDto;
import kektor.auction.payment.dto.ReplenishmentDto;
import kektor.auction.payment.dto.ReservationDto;
import kektor.auction.payment.exception.*;
import kektor.auction.payment.mapper.BalanceOperationMapper;
import kektor.auction.payment.mapper.PaymentAccountMapper;
import kektor.auction.payment.model.CreditOperation;
import kektor.auction.payment.model.DebitOperation;
import kektor.auction.payment.model.PaymentAccount;
import kektor.auction.payment.repository.BalanceOperationRepository;
import kektor.auction.payment.repository.PaymentAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;


@Service
@RequiredArgsConstructor
public class PaymentService {

    PaymentAccountRepository accountRepository;
    BalanceOperationRepository operationRepository;
    PaymentAccountMapper accountMapper;
    BalanceOperationMapper balanceMapper;

    //TODO Cacheable
    @Transactional(readOnly = true)
    public PaymentAccountDto getAccount(Long accountId, boolean withOperations) {
        var account = (withOperations ?
                accountRepository.findWithOperationsById(accountId) :
                accountRepository.findById(accountId))
                .orElseThrow(() -> new PaymentAccountNotFoundByIdException(accountId));

        return withOperations ? accountMapper.toDtoWithOps(account) : accountMapper.toDto(account);
    }

    @Transactional
    public PaymentAccountDto createAccount(Long userId) {
        var account = new PaymentAccount();
        account.setUserId(userId);
        account = accountRepository.saveAndFlush(account);
        return accountMapper.toDto(account);
    }

    @Transactional
    public PaymentAccountDto replenishAmount(ReplenishmentDto replenishmentDto) {
        PaymentAccount account = accountRepository.findByUserId(replenishmentDto.userId())
                .orElseThrow(() -> new PaymentAccountNotFoundByUserIdException(replenishmentDto.userId()));

        Long submittedV = replenishmentDto.version();
        Long currentV = account.getVersion();
        if (!submittedV.equals(currentV)) {
            throw new StalePaymentAccountVersionException(account.getId(), currentV, submittedV);
        }

        DebitOperation debitOperation = balanceMapper.toModel(replenishmentDto);

        debitOperation.setPaymentAccount(account);
        operationRepository.save(debitOperation);

        BigDecimal newBalance = account.getBalance().add(debitOperation.getAmount());
        account.setBalance(newBalance);
        accountRepository.saveAndFlush(account);
        return accountMapper.toDto(account);
    }

    @Transactional
    public PaymentAccountDto reserveAmount(ReservationDto reservationDto) {
        PaymentAccount account = accountRepository.findByUserId(reservationDto.userId())
                .orElseThrow(() -> new PaymentAccountNotFoundByUserIdException(reservationDto.userId()));

        BigDecimal accountBalance = account.getBalance();
        BigDecimal requestedAmount = reservationDto.amount();
        if (accountBalance.compareTo(requestedAmount) < 0) {
            throw new NotEnoughAccountFundException(reservationDto.bidId(), account.getUserId(),
                    accountBalance, requestedAmount);
        }

        Long submittedV = reservationDto.version();
        Long currentV = account.getVersion();
        if (!submittedV.equals(currentV)) {
            throw new StalePaymentAccountVersionException(account.getId(), currentV,
                    submittedV, reservationDto.sagaId());
        }

        CreditOperation creditOperation = balanceMapper.toModel(reservationDto);

        creditOperation.setPaymentAccount(account);
        operationRepository.save(creditOperation);

        BigDecimal newBalance = accountBalance.subtract(requestedAmount);
        account.setBalance(newBalance);
        accountRepository.saveAndFlush(account);
        return accountMapper.toDto(account);
    }

    @Transactional
    public void commitReservation(Long sagaId) {
        operationRepository.findCreditOperationBySagaId(sagaId)
                .map(operation -> operation.setStatus(CreditOperation.Status.ACCEPTED))
                .orElseThrow(() -> new OperationNotFoundBySagaIdException(sagaId));
    }

    @Transactional
    public void cancelReservation(Long sagaId) {
        CreditOperation operation = operationRepository.findCreditOperationBySagaIdFetchAccount(sagaId)
                .orElseThrow(() -> new OperationNotFoundBySagaIdException(sagaId));
        if (operation.getStatus() == CreditOperation.Status.CANCELLED) {
            return;
        }
        PaymentAccount account = operation.getPaymentAccount();
        operation.setStatus(CreditOperation.Status.CANCELLED);
        account.setBalance(account.getBalance().add(operation.getAmount()));
    }


}
