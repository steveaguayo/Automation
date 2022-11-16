package kimble.automation.domain.mobile;

import kimble.automation.domain.mobile.builders.ActivityBuilder;
import kimble.automation.domain.mobile.builders.ActivityRateBandBuilder;
import kimble.automation.domain.mobile.builders.AssignmentBuilder;
import kimble.automation.domain.mobile.builders.CreateResponseBuilder;
import kimble.automation.domain.mobile.builders.ExpenseCategoryBuilder;
import kimble.automation.domain.mobile.builders.ExpenseClaimBuilder;
import kimble.automation.domain.mobile.builders.ExpenseItemBuilder;
import kimble.automation.domain.mobile.builders.PlanTaskAssignmentBuilder;
import kimble.automation.domain.mobile.builders.RemotelySaveableEntityBuilder;
import kimble.automation.domain.mobile.builders.TaxCodeBuilder;
import kimble.automation.domain.mobile.builders.TimeEntryBuilder;
import kimble.automation.domain.mobile.builders.TrackingPeriodBuilder;

public class CreateMob {
	public static ActivityBuilder Activity() throws Exception { return new ActivityBuilder(); }
	public static ExpenseCategoryBuilder ExpenseCategory() throws Exception { return new ExpenseCategoryBuilder(); } 
	public static ExpenseClaimBuilder ExpenseClaim() throws Exception { return new ExpenseClaimBuilder(); } 
	public static ExpenseItemBuilder ExpenseItem() throws Exception { return new ExpenseItemBuilder(); } 
	public static TaxCodeBuilder TaxCode() throws Exception { return new TaxCodeBuilder(); }
	public static TimeEntryBuilder TimeEntry() throws Exception { return new TimeEntryBuilder(); }
	public static ActivityRateBandBuilder ActivityRateBand() throws Exception { return new ActivityRateBandBuilder(); }
	public static AssignmentBuilder Assignment() throws Exception { return new AssignmentBuilder(); }
	public static PlanTaskAssignmentBuilder PlanTaskAssignment() throws Exception { return new PlanTaskAssignmentBuilder(); }
	public static TrackingPeriodBuilder TrackingPeriod() throws Exception { return new TrackingPeriodBuilder(); }
	public static RemotelySaveableEntityBuilder RemotelySaveableEntity() throws Exception { return new RemotelySaveableEntityBuilder(); }
	public static CreateResponseBuilder CreateResponse() throws Exception { return new CreateResponseBuilder(); }
}
