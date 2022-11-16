package kimble.automation.domain.mobile;

import java.sql.Date;
import java.util.List;
import java.util.Properties;

import kimble.automation.domain.mobile.general.Deserializers.ForecastTimeEntryDeserializer;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class PayLoadMob {
	public boolean AllowCompanyPaidExpenses;
	public boolean LocationTrackingEnabled;
	public boolean UseExpenseIncurredToReimbursementCurrency;
	public boolean TimeEntryEnabled;
	public boolean ExpenseEntryEnabled;
	public boolean AllowExpenseReimbursementAmountEntry;
	public boolean AllowExpenseBeforeAssignmentStartDate;
	public boolean ShowRemainingUsageInTimeEntry;
	public boolean ShowWarningForTimeSubmisionBeforeDefaultPeriod;
	public String ExpenseReimbursementCurrency;
	public String EarliestIncompletedPeriodDate;
	public Integer MaxItemsAllowedOnExpenseClaim;
	public Integer HoursPerDay;
	public List<TrackingPeriodMob> TrackingPeriods;
	public List<TimeEntryMob> TimeEntries;
	public List<TimeCategoryMob> TimeCategories;
	public List<TaxRateMob> TaxRates;
	public List<TaxCodeMob> TaxCodes;
	public SynchroniseWindowMob SynchroniseWindow;
	public List<PlanTaskAssignmentMob> PlanTaskAssignments;
	public List<String> NonBusinessDays;
	public List<String> ValidAssignmentIds;
	public Properties Labels;
	@JsonDeserialize(contentUsing = ForecastTimeEntryDeserializer.class)
	public List<TimeEntryMob> ForecastTimeEntries;
	public List<ExpenseClaimMob> ExpenseClaims;
	public List<ExpenseCategoryMob> ExpenseCategories;
	public List<ExchangeRateMob> ExchangeRates;
	public List<AssignmentMob> Assignments;
	public List<ActivityRateBandMob> ActivityRateBands;
	public List<ActivityMob> Activities;
	public Date DefaultTrackingPeriodDate;

	public String SettingsJson;
	
	public void save() throws Exception
	{
		// Make this into a test case
		
//		saveOrDelete(TrackingPeriods);
//		saveOrDelete(TimeEntries);
//		saveOrDelete(TimeCategories);
//		saveOrDelete(TaxRates);
//		saveOrDelete(TaxCodes);
//		saveOrDelete(PlanTaskAssignments);
//
//		List<TimeEntry> oldForecastEntries = TnXContext.getDb().getDao(TimeEntry.class).queryForEq("status", TimeEntry.Status.Forecast);
//		for(TimeEntry te : oldForecastEntries)
//			te.getDao().delete(te);
//
//		saveOrDelete(ForecastTimeEntries);
//		saveOrDeleteClaims(ExpenseClaims);
//		//saveOrDelete(ExpenseClaims);
//		saveOrDelete(ExpenseCategories);
//		saveOrDelete(ExchangeRates);
//		saveOrDelete(Assignments);
//		saveOrDelete(ActivityRateBands);
//		saveOrDelete(Activities);
//		deleteInvalidAssignmentIds(ValidAssignmentIds);
//		//Log4j.log(Utils.prettyJson(SynchroniseWindow));
//		SynchroniseWindow.getDao().createOrUpdate(SynchroniseWindow);
//		//SynchroniseWindow.save();
//		if(Labels != null)
//			TnXContext.setLabels(Labels);
//		if(NonBusinessDays != null)
//			TnXContext.addNonBusinessDays(NonBusinessDays);
//		TnXContext.setAllowCompanyPaidExpenses(AllowCompanyPaidExpenses);
//		if(Labels != null || NonBusinessDays != null)
//			TnXContext.saveSettings();
	}

//	private void deleteInvalidAssignmentIds(List<String> validAssignmentIds) throws Exception {
//
//		Dao<Assignment, String> daoAssignment = TnXContext.getDb().getDao(Assignment.class);
//
//		if (validAssignmentIds != null && validAssignmentIds.size() >0) {
//			ArrayList<Assignment> assignments = (ArrayList<Assignment>) daoAssignment.queryBuilder().where().notIn("id", validAssignmentIds).query();
//			if (assignments.size() > 0) {
//				Log4j.log("Found " + assignments.size() + " Assignments to delete");
//				for (Assignment a : assignments) {
//					a.delete();
//				}
//			}
//		}
//	}
//
//	void saveOrDelete(Collection<? extends Entities.Entity> es) throws Exception{
//		if(es == null)
//			return;
//		for(Entities.Entity e : es) {
//			if(e.getDeleted())
//				e.delete();
//			else
//				e.save();
//		}
//	}
//
//	void saveOrDeleteClaims(Collection<ExpenseClaim> es) throws Exception{
//		if(es == null)
//			return;
//		for(ExpenseClaim e : es) {
//			if(e.getDeleted())
//			{
//				Dao<ExpenseClaim, String> dao = TnXContext.getDb().getDao(ExpenseClaim.class);
//				ArrayList<ExpenseClaim> claims = (ArrayList<ExpenseClaim>) dao.queryBuilder().where().eq("id", e.getId()).query();
//				if (claims.size() > 0) {
//					for (Entities.Entity i : claims.get(0).getExpenseItems())
//						i.delete();
//				}
//				e.delete();
//			}
//			else
//			{
//				saveOrDelete(e.getExpenseItems());
//				e.save();
//			}
//		}
//	}
}

