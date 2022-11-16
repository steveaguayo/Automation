package kimble.automation.domain.mobile.builders;

import kimble.automation.domain.mobile.ActivityMob;
import kimble.automation.domain.mobile.ActivityRateBandMob;
import kimble.automation.domain.mobile.CreateMob;
import kimble.automation.domain.mobile.TypesMob.PeriodType;
import kimble.automation.domain.mobile.TypesMob.UsageAllocationType;
import kimble.automation.domain.mobile.general.Deserializers;

/**
 * Created by Benjamin on 6/25/2015.
 */
public class ActivityRateBandBuilder extends EntityBuilderA<ActivityRateBandBuilder, ActivityRateBandMob> {

    public ActivityRateBandBuilder() throws Exception {
        e = new ActivityRateBandMob();
        e.setId(Deserializers.generateId());
        e.setDeleted(false);
        e.setActivity(CreateMob.Activity().build());
        e.setMaxUsage(DefaultValues.maxUsage);
        e.setName(DefaultValues.activityName);
        e.setPeriodType(DefaultValues.periodType);
        e.setPriority(DefaultValues.priority);
        e.setUsageAllocationType(DefaultValues.usageAllocationType);
    }

    public ActivityRateBandBuilder withId(String aId) throws Exception {
        e.setId(aId);
        return this;
    }

    public ActivityRateBandBuilder withDeleted(boolean aIsDeleted) throws Exception {
        e.setDeleted(aIsDeleted);
        return this;
    }

    public ActivityRateBandBuilder withActivity(ActivityMob aActivity) throws Exception {
        e.setActivity(aActivity);
        return this;
    }

    public ActivityRateBandBuilder withMaxUsage(int aMaxUsage) throws Exception {
        e.setMaxUsage(aMaxUsage);
        return this;
    }

    public ActivityRateBandBuilder withName(String aName) throws Exception {
        e.setName(aName);
        return this;
    }

    public ActivityRateBandBuilder withPeriodType(PeriodType aPeriodType) throws Exception {
        e.setPeriodType(aPeriodType);
        return this;
    }

    public ActivityRateBandBuilder withPriority(int aPriority) throws Exception {
        e.setPriority(aPriority);
        return this;
    }

    public ActivityRateBandBuilder withUsageAllocationType(UsageAllocationType aUsageAllocationType) throws Exception {
        e.setUsageAllocationType(aUsageAllocationType);
        return this;
    }

    public ActivityRateBandMob build() throws Exception { return e; }

}
