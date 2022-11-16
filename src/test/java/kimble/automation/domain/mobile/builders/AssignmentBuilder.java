package kimble.automation.domain.mobile.builders;

import kimble.automation.domain.mobile.ActivityMob;
import kimble.automation.domain.mobile.AssignmentMob;
import kimble.automation.domain.mobile.CreateMob;
import kimble.automation.domain.mobile.general.Deserializers;

/**
 * Created by Benjamin on 6/25/2015.
 */
public class AssignmentBuilder extends EntityBuilderA<AssignmentBuilder, AssignmentMob> {

    public AssignmentBuilder() throws Exception {
        e = new AssignmentMob();
        e.setId(Deserializers.generateId());
        e.setName(DefaultValues.assignmentName);
        e.setActivity(CreateMob.Activity().build());
        e.setDeleted(false);
        e.setStartDate(DefaultValues.startDate);
        e.setEndDate(DefaultValues.endDate);
        e.setHoursPerDay(DefaultValues.hoursPerDay);
        e.setUsageEntryFormat(DefaultValues.usageEntryFormat);
    }

    public AssignmentBuilder withId(String aId) {
        e.setId(aId);
        return this;
    }

    public AssignmentBuilder withName(String aName) {
        e.setName(aName);
        return this;
    }

    public AssignmentBuilder withActivity(ActivityMob aActivity) {
        e.setActivity(aActivity);
        return this;
    }

    public AssignmentBuilder withDeleted(boolean aIsDeleted) {
        e.setDeleted(aIsDeleted);
        return this;
    }

    public AssignmentBuilder withStartDate(String aStartDate) {
        e.setStartDate(aStartDate);
        return this;
    }

    public AssignmentBuilder withEndDate(String aEndDate) {
        e.setEndDate(aEndDate);
        return this;
    }

    public AssignmentBuilder withHoursPerDay(double aHoursPerDay) {
        e.setHoursPerDay(aHoursPerDay);
        return this;
    }

    public AssignmentBuilder withUsageEntryFormat(int aUsageEntryFormat) {
        e.setUsageEntryFormat(aUsageEntryFormat);
        return this;
    }

    public AssignmentMob build() throws Exception { return e; }

}
