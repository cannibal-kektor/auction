package kektor.auction.orchestrator.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.RequiredArgsConstructor;
import org.springframework.core.serializer.support.SerializationFailedException;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Component;


@Converter
@Component
@RequiredArgsConstructor
public class ProblemDetailConverter implements AttributeConverter<ProblemDetail, String> {

    final ObjectMapper objectMapper;

    @Override
    public String convertToDatabaseColumn(ProblemDetail attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new SerializationFailedException("ProblemDetail serialization failed", e);
        }
    }

    @Override
    public ProblemDetail convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, ProblemDetail.class);
        } catch (Exception e) {
            throw new SerializationFailedException("ProblemDetail deserialization failed", e);
        }
    }
}
