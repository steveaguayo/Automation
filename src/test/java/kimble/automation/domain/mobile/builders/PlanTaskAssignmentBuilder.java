package kimble.automation.domain.mobile.builders;

import kimble.automation.domain.mobile.AssignmentMob;
import kimble.automation.domain.mobile.CreateMob;
import kimble.automation.domain.mobile.PlanTaskAssignmentMob;
import kimble.automation.domain.mobile.general.Deserializers;

/**
 * Created by Benjamin on 6/25/2015.
 */
public class PlanTaskAssignmentBuilder extends EntityBuilderA<PlanTaskAssignmentBuilder, PlanTaskAssignmentMob> {

    public PlanTaskAssignmentBuilder() throws Exception {
        e = new PlanTaskAssignmentMob();
        e.setId(Deserializers.generateId());
        e.setStartDate(DefaultValues.startDate);
        e.setEndDate(DefaultValues.endDate);
        e.setName(DefaultValues.planTaskAssignmentName);
        e.setAssignment(CreateMob.Assignment().build());
    }

    public PlanTaskAssignmentBuilder withId(String aId) throws Exception {
        e.setId(aId);
        return this;
    }

    public PlanTaskAssignmentBuilder withStartDate(String aStartDate) throws Exception {
        e.setStartDate(aStartDate);
        return this;
    }

    public PlanTaskAssignmentBuilder withEndDate(String aEndDate) throws Exception {
        e.setEndDate(aEndDate);
        return this;
    }

    public PlanTaskAssignmentBuilder withName(String aName) throws Exception {
        e.setName(aName);
        return this;
    }

    public PlanTaskAssignmentBuilder withAssignment(AssignmentMob aAssignment) throws Exception {
        e.setAssignment(aAssignment);
        return this;
    }

    public PlanTaskAssignmentMob build() throws Exception { return e; }

}
