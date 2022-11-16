package kimble.automation.domain;

import java.util.List;

public class ResourcedActivity {

	public boolean
	hideForecastTimeEntries,
	canSelfAssign,
	calendarIsPrivate;
	
	public String
	name,
	usageUnits,
	allocationType,
	usageTrackingModel,
	utilisationRule,
	timeEntryRule,
	calendarShowAs,
	scheduledTimeRule,
	externalId,
	toilFactor;

	public String businessDayStandardToilFactor;
	public String businessDayOvertimeToilFactor;
	public String nonBusinessDayStandardToilFactor;
	public String nonBusinessDayOvertimeToilFactor;
}
