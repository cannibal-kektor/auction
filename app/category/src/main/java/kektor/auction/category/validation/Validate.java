package kektor.auction.category.validation;

import jakarta.validation.GroupSequence;
import jakarta.validation.groups.Default;

public interface Validate {

    interface IfUpdate {
    }

    interface IfCreate {
    }

    @GroupSequence({Default.class, IfCreate.class})
    interface Create {
    }

    @GroupSequence({Default.class, IfUpdate.class})
    interface Update {
    }

}
