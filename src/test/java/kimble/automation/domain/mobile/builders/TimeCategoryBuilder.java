package kimble.automation.domain.mobile.builders;

import kimble.automation.domain.mobile.ActivityMob;
import kimble.automation.domain.mobile.CreateMob;
import kimble.automation.domain.mobile.TimeCategoryMob;
import kimble.automation.domain.mobile.TypesMob.DisplayType;
import kimble.automation.domain.mobile.general.Deserializers;

/**
 * Created by Benjamin on 7/1/2015.
 */
public class TimeCategoryBuilder extends EntityBuilderA<TimeCategoryBuilder, TimeCategoryMob> {

    public TimeCategoryBuilder() throws Exception {
        e = new TimeCategoryMob();
        e.setId(Deserializers.generateId());
        e.setDeleted(false);
        e.setName(DefaultValues.timeCategoryName);
        e.setActivity(CreateMob.Activity().build());
        e.setDisplaySequence(DefaultValues.displaySequence);
        e.setDisplayType(DefaultValues.displayType);
        e.setMandatory(DefaultValues.mandatory);
        e.setValues(DefaultValues.timeEntryValues);
    }

    public TimeCategoryBuilder withId(String aId) throws Exception {
        e.setId(aId);
        return this;
    }

    public TimeCategoryBuilder withDeleted(boolean aIsDeleted) {
        e.setDeleted(aIsDeleted);
        return this;
    }

    public TimeCategoryBuilder withName(String aName) {
        e.setName(aName);
        return this;
    }

    public TimeCategoryBuilder withActivity(ActivityMob aActivity) {
        e.setActivity(aActivity);
        return this;
    }

    public TimeCategoryBuilder withDisplaySequence(int aSequence) {
        e.setDisplaySequence(aSequence);
        return this;
    }

    public TimeCategoryBuilder withDisplayType(DisplayType aType) {
        e.setDisplayType(aType);
        return this;
    }

    public TimeCategoryBuilder withMandatory(boolean aMandatory) {
        e.setMandatory(aMandatory);
        return this;
    }

    public TimeCategoryBuilder withValues(String aValues) {
        e.setValues(aValues);
        return this;
    }

    public TimeCategoryMob build() throws Exception { return e; }

}
