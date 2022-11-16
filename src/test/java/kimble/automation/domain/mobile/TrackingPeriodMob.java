package kimble.automation.domain.mobile;

import kimble.automation.domain.mobile.general.Entities.StringIdEntity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Benjamin on 6/10/2015.
 */

public class TrackingPeriodMob 
extends
StringIdEntity<TrackingPeriodMob>
{

    String startDate;
    String endDate;
    String name;
    boolean isOpen;

    @JsonProperty("StartDate")
    public String getStartDate() { return startDate;  }
    public void setStartDate(String startDate) { this.startDate = startDate;  }

    @JsonProperty("EndDate")
    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) {  this.endDate = endDate; }

    @JsonProperty("Name")
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    @JsonProperty("IsOpen")
    public boolean getIsOpen() { return isOpen; }
    public void setIsOpen(boolean isOpen) { this.isOpen = isOpen; }

    @JsonIgnore
    public boolean getDeleted() { return false; }
}
