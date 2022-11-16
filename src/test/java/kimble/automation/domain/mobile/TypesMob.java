package kimble.automation.domain.mobile;

/**
 * Created by Benjamin on 7/1/2015.
 */
public class TypesMob {

    public enum UsageAllocationType { Activity, Task, Reference, Category, TimeEntry, CategoryAndTask; }

    public enum ActivityType { SalesOpportunity, DeliveryElement, Other; }

    public enum PeriodType { Day, BusDay, NonBusDay, Week, Month, Quarter, Year; }

    public enum DisplayType { DisplayType, Dropdown, Text, DynamicDropdown; }

}
