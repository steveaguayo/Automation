package kimble.automation.domain.mobile.builders;

import kimble.automation.domain.mobile.ExpenseClaimMob;
import kimble.automation.domain.mobile.TimeEntryMob;
import kimble.automation.domain.mobile.TypesMob.ActivityType;
import kimble.automation.domain.mobile.TypesMob.DisplayType;
import kimble.automation.domain.mobile.TypesMob.PeriodType;
import kimble.automation.domain.mobile.TypesMob.UsageAllocationType;

import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;

/**
 * Created by Benjamin on 6/25/2015.
 */
public class DefaultValues {

    public static final String startDate = "2015-06-25";
    public static final String endDate = "2015-07-25";
    public static final String entryDate = "2015-07-01";
    public static final TimeEntryMob.Status timeEntryStatus = TimeEntryMob.Status.Draft;
    public static final String timeCategoryName = "New Time Category";
    public static final DisplayType displayType = DisplayType.Text;
    public static final String timeEntryValues = "a,b,c";
    public static final UsageAllocationType usageAllocationType = UsageAllocationType.Activity;
    public static final String usageUnitType = "Hour";
    public static final String expenseUnitType = "Mile";
    public static final String expenseCategoryName = "Train";
    public static final boolean active = false;
    public static final boolean hasStartLocation = false;
    public static final boolean hasEndLocation = false;
    public static final boolean hasAttendees = false;
    public static final boolean canEditTaxCode = false;
    public static final boolean canEditTaxAmount = false;
    public static final String currency = "GBP";
    public static final ActivityType activityType = ActivityType.Other;
    public static final double maxUsage = 20.0;
    public static final String activityName = "New Activity";
    public static final int displaySequence = 1;
    public static final boolean mandatory = false;
    public static final PeriodType periodType = PeriodType.Month;
    public static final int priority = 1;
    public static final String assignmentName = "New Assignment";
    public static final double hoursPerDay = 7.5;
    public static final int usageEntryFormat = 1;
    public static final ExpenseClaimMob.Status expenseClaimStatus = ExpenseClaimMob.Status.Draft;
    public static final String resourcedActivityId = "resourcedActivity_id";
    public static final String expenseClaimName = "Expense Claim Name";
    public static final String approvalComments = "This was approved because of.. whatever";
    public static final String startLocation = "London(12.3,45.6)";
    public static final String endLocation = "Viitasaari(65.4,32.1)";
    public static final String expenseNote = "This is an expense note.";
    public static final boolean receipted = false;
    public static final boolean imported = false;
    public static final boolean companyPaid = false;
    public static final double incurredTaxAmount = 20.0;
    public static final String incurredDate = "2015-06-01";
    public static final double incurredAmount = 53.75;
    public static final int expenseUnits = 0;
    public static final double expenseGrossAmount = 33.75;
    public static final double exchangeRateConversionFactor = 0.625;
    public static final String attendees = "peter,mary,jack";
    public static final String attachmentId = "attachment_id";
    public static final String planTaskAssignmentName = "Plan Task Assignment Name";
    public static final String taxCodeName = "Europe";
    public static final String category1 = "category 1";
    public static final String category2 = "category 2";
    public static final String category3 = "category 3";
    public static final String category4 = "category 4";
    public static final String category5 = "category 5";
    public static final String category6 = "category 6";
    public static final boolean deletedLocally = false;
    public static final boolean deletedOnServer = false;
    public static final boolean hasChanged = false;
    public static final int draftRemainingEffort = 10;
    public static final String timeEntryNotes = "default notes";
    public static final String trackingPeriodName = "Tracking Period Name";
    public static final String receiptUrl = "./receipts";
    public static final boolean isOpen = true;

    public static StatusLine statusOk = new StatusLine() {
        public ProtocolVersion getProtocolVersion() { return HttpVersion.HTTP_1_1; }
        public int getStatusCode() { return HttpStatus.SC_OK; }
        public String getReasonPhrase() { return "Ok"; }
    };

    public static StatusLine statusServerError = new StatusLine() {
        public ProtocolVersion getProtocolVersion() { return HttpVersion.HTTP_1_1; }
        public int getStatusCode() { return HttpStatus.SC_INTERNAL_SERVER_ERROR; }
        public String getReasonPhrase() { return "Internal Server Error"; }
    };

    public static StatusLine statusNotFound = new StatusLine() {
        public ProtocolVersion getProtocolVersion() { return HttpVersion.HTTP_1_1; }
        public int getStatusCode() { return HttpStatus.SC_NOT_FOUND; }
        public String getReasonPhrase() { return "Not Found"; }
    };

}
