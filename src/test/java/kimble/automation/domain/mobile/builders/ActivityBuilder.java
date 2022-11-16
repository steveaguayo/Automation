package kimble.automation.domain.mobile.builders;

import kimble.automation.domain.mobile.ActivityMob;
import kimble.automation.domain.mobile.TypesMob.ActivityType;
import kimble.automation.domain.mobile.general.Deserializers;

public class ActivityBuilder extends EntityBuilderA<ActivityBuilder, ActivityMob> {

	public ActivityBuilder() throws Exception {
		e = new ActivityMob();
		e.setId(Deserializers.generateId());
		e.setUsageUnitType(DefaultValues.usageUnitType);
		e.setType(DefaultValues.activityType);
		e.setDeleted(false);
	}
	
	public ActivityBuilder withUsageUnitType(String val) { e.setUsageUnitType(val); return this; }
	public ActivityBuilder withType(ActivityType val) { e.setType(val); return this; }
	public ActivityBuilder withDeleted(boolean val) { e.setDeleted(val); return this; }

	public ActivityMob build() throws Exception { return e; }
	
}
