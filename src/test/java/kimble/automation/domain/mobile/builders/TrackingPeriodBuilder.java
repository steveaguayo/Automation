package kimble.automation.domain.mobile.builders;

import kimble.automation.domain.mobile.TrackingPeriodMob;
import kimble.automation.domain.mobile.general.Deserializers;

/**
 * Created by Benjamin on 6/25/2015.
 */
public class TrackingPeriodBuilder extends EntityBuilderA<TrackingPeriodBuilder, TrackingPeriodMob> {

    public TrackingPeriodBuilder() throws Exception {
        e = new TrackingPeriodMob();
        e.setId(Deserializers.generateId());
        e.setName(DefaultValues.trackingPeriodName);
        e.setStartDate(DefaultValues.startDate);
        e.setEndDate(DefaultValues.endDate);
        e.setIsOpen(DefaultValues.isOpen);
    }

    public TrackingPeriodBuilder withId(String aId) {
        e.setId(aId);
        return this;
    }

    public TrackingPeriodBuilder withName(String aName) {
        e.setName(aName);
        return this;
    }

    public TrackingPeriodBuilder withStartDate(String aStartDate) {
        e.setStartDate(aStartDate);
        return this;
    }

    public TrackingPeriodBuilder withEndDate(String aEndDate) {
        e.setEndDate(aEndDate);
        return this;
    }

    public TrackingPeriodBuilder withOpen(boolean aIsOpen) {
        e.setIsOpen(aIsOpen);
        return this;
    }

    public TrackingPeriodMob build() throws  Exception{ return e; }

}
