package kimble.automation.domain.mobile.builders;

import kimble.automation.domain.mobile.general.Entities;
import kimble.automation.domain.mobile.general.Identifiers;

/**
 * Created by Benjamin on 6/29/2015.
 */
public abstract class EntityBuilderA<S extends EntityBuilderA<?,T>, T extends Entities.Entity> extends BuilderA<T> {

    T e;

    public S withId(String aId) throws Exception {
        e.setId(aId);
        return (S)this;
    }

    public S withIdentifier(Identifiers.Identifier aIdentifier) throws Exception {
        e.setIdentifier(aIdentifier);
        return (S)this;
    }

}
