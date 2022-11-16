package kimble.automation.domain.mobile.builders;

import kimble.automation.domain.mobile.ActivityRateBandMob;
import kimble.automation.domain.mobile.AssignmentMob;
import kimble.automation.domain.mobile.CreateMob;
import kimble.automation.domain.mobile.PlanTaskAssignmentMob;
import kimble.automation.domain.mobile.TimeEntryMob;
import kimble.automation.domain.mobile.general.Deserializers;

/**
 * Created by Benjamin on 6/25/2015.
 */
public class TimeEntryBuilder extends EntityBuilderA<TimeEntryBuilder, TimeEntryMob> {

    public TimeEntryBuilder() throws Exception {
        e = new TimeEntryMob();
        e.setId(Deserializers.generateId());
        e.setStatus(DefaultValues.timeEntryStatus);
        e.setActivityRateBand(CreateMob.ActivityRateBand().build());
        e.setAssignment(CreateMob.Assignment().build());
        e.setApprovalComments(DefaultValues.approvalComments);
        e.setCategory1(DefaultValues.category1);
        e.setCategory2(DefaultValues.category2);
        e.setCategory3(DefaultValues.category3);
        e.setCategory4(DefaultValues.category4);
        e.setCategory5(DefaultValues.category5);
        e.setCategory6(DefaultValues.category6);
        e.setDeleted(false);
        e.setDeletedLocally(DefaultValues.deletedLocally);
        e.setDeletedOnServer(DefaultValues.deletedOnServer);
        e.setDraftRemainingEffort(DefaultValues.draftRemainingEffort);
        e.setEntryDate(DefaultValues.entryDate);
        e.setHasChanged(DefaultValues.hasChanged);
        e.setNotes(DefaultValues.timeEntryNotes);
        e.setPlanTaskAssignment(CreateMob.PlanTaskAssignment().build());
    }

    public TimeEntryBuilder withId(String aId) throws Exception {
        e.setId(aId);
        return this;
    }

    public TimeEntryBuilder withUnits(double aUnits) throws Exception {
        e.setUnits(aUnits);
        return this;
    }

    public TimeEntryBuilder withStatus(TimeEntryMob.Status aStatus) throws Exception {
        e.setStatus(aStatus);
        return this;
    }

    public TimeEntryBuilder withActivityRateBand(ActivityRateBandMob aActivityRateBand) {
        e.setActivityRateBand(aActivityRateBand);
        return this;
    }

    public TimeEntryBuilder withApprovalComments(String aComments) {
        e.setApprovalComments(aComments);
        return this;
    }

    public TimeEntryBuilder withAssignment(AssignmentMob aAssignment) {
        e.setAssignment(aAssignment);
        return this;
    }

    public TimeEntryBuilder withCategory1(String aCategory) {
        e.setCategory1(aCategory);
        return this;
    }

    public TimeEntryBuilder withCategory2(String aCategory) {
        e.setCategory2(aCategory);
        return this;
    }

    public TimeEntryBuilder withCategory3(String aCategory) {
        e.setCategory3(aCategory);
        return this;
    }

    public TimeEntryBuilder withCategory4(String aCategory) {
        e.setCategory4(aCategory);
        return this;
    }

    public TimeEntryBuilder withCategory5(String aCategory) {
        e.setCategory5(aCategory);
        return this;
    }

    public TimeEntryBuilder withCategory6(String aCategory) {
        e.setCategory6(aCategory);
        return this;
    }

    public TimeEntryBuilder withDeleted(boolean aIsDeleted) {
        e.setDeleted(aIsDeleted);
        return this;
    }

    public TimeEntryBuilder withDeletedLocally(boolean aIsDeleted) {
        e.setDeletedLocally(aIsDeleted);
        return this;
    }

    public TimeEntryBuilder withDeletedOnServer(boolean aIsDeleted) {
        e.setDeletedOnServer(aIsDeleted);
        return this;
    }

    public TimeEntryBuilder withDraftRemainingEffort(int aRemainingEffort) {
        e.setDraftRemainingEffort(aRemainingEffort);
        return this;
    }

    public TimeEntryBuilder withEntryDate(String aDate) {
        e.setEntryDate(aDate);
        return this;
    }

    public TimeEntryBuilder withHasChanged(boolean aHasChanged) {
        e.setHasChanged(aHasChanged);
        return this;
    }

    public TimeEntryBuilder withNotes(String aNotes) {
        e.setNotes(aNotes);
        return this;
    }

    public TimeEntryBuilder withPlanTaskAssignment(PlanTaskAssignmentMob aAssignment) {
        e.setPlanTaskAssignment(aAssignment);
        return this;
    }

    public TimeEntryMob build() throws Exception { return e; }

}
