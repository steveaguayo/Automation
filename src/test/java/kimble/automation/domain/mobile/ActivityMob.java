package kimble.automation.domain.mobile;

import java.util.List;

import kimble.automation.domain.mobile.general.Entities.StringIdEntity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import static kimble.automation.domain.mobile.TypesMob.*;

public class ActivityMob 
extends
StringIdEntity<ActivityMob>
{
	String usageUnitType;
	ActivityType type;
    boolean isDeleted;
    String timeEntryRule;
	List<ActivityRateBandMob> activityRateBands;
	List<AssignmentMob> assignments;
	List<TimeCategoryMob> timeCategories;
	List<ExpenseCategoryMob> expenseCategories;
	boolean AllowNewTimeEntries;
	boolean AllowNewExpenseItems;
	
	@JsonProperty("AllowNewTimeEntries")
	public boolean getAllowNewTimeEntries() { return AllowNewTimeEntries; }
	public void setAllowNewTimeEntries(boolean AllowNewTimeEntries) { this.AllowNewTimeEntries = AllowNewTimeEntries; }
	
	@JsonProperty("AllowNewExpenseItems")
	public boolean getAllowNewExpenseItems() { return AllowNewExpenseItems; }
	public void setAllowNewExpenseItems(boolean AllowNewExpenseItems) { this.AllowNewExpenseItems = AllowNewExpenseItems; }

	@JsonProperty("UsageUnitType")
	public String getUsageUnitType() { return usageUnitType; }
	public void setUsageUnitType(String usageUnitType) { this.usageUnitType = usageUnitType; }
	
	@JsonProperty("Type")
	public ActivityType getType() { return type; }
	public void setType(ActivityType type) { this.type = type; }
	
	@JsonProperty("IsDeleted")
	public boolean getDeleted() { return isDeleted; }
	public void setDeleted(boolean isDeleted) { this.isDeleted = isDeleted; }

	@JsonProperty("TimeEntryRule")
	public String getTimeEntryRule() { return timeEntryRule; }
	public void setTimeEntryRule(String rule) { this.timeEntryRule = rule; }
	
	@JsonIgnore
	public List<AssignmentMob> getAssignments() { return assignments; }

	@JsonIgnore
	public List<ActivityRateBandMob> getActivityRateBands() { return activityRateBands; }

	@JsonIgnore
	public List<TimeCategoryMob> getTimeCategories() { return timeCategories; }

	@JsonIgnore
	public List<ExpenseCategoryMob> getExpenseCategories() { return expenseCategories; }

}