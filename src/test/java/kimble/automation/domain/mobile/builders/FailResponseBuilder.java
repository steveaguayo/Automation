package kimble.automation.domain.mobile.builders;

import java.util.Collections;
import java.util.List;

import kimble.automation.domain.mobile.general.Rest;

/**
 * Created by Benjamin on 7/7/2015.
 */
public class FailResponseBuilder extends BuilderA<Rest.FailResponse> {

    Rest.FailResponse r;

    public FailResponseBuilder() {
        r = new Rest.FailResponse();
        r.errorCode = "ENTITY_IS_DELETED";
        r.fields = Collections.emptyList();
        r.message = "entity is deleted";
    }

    public FailResponseBuilder withErrorCode(String aErrorCode) {
        r.errorCode = aErrorCode;
        return this;
    }

    public FailResponseBuilder withFields(List<String> aFields) {
        r.fields = aFields;
        return this;
    }

    public FailResponseBuilder withMessage(String aMessage) {
        r.message = aMessage;
        return this;
    }

    public Rest.FailResponse build() { return r; }

}
