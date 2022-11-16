package kimble.automation.domain.mobile.builders;

import java.util.Collections;
import java.util.List;

import kimble.automation.domain.mobile.general.Rest;

/**
 * Created by Benjamin on 7/7/2015.
 */
public class CreateResponseBuilder extends BuilderA<Rest.CreateResponse> {

    Rest.CreateResponse r;

    public CreateResponseBuilder() {
        r = new Rest.CreateResponse();
        r.errors = Collections.emptyList();
        r.success = true;
        r.id = "1---------------18";
    }

    public CreateResponseBuilder withErrors(List<String> aErrors) {
        r.errors = aErrors;
        return this;
    }

    public CreateResponseBuilder withSuccess(boolean aSuccess) {
        r.success = aSuccess;
        return this;
    }

    public CreateResponseBuilder withId(String aId) {
        r.id = aId;
        return this;
    }

    public Rest.CreateResponse build() { return r; }
}
