package kimble.automation.domain.mobile;

import kimble.automation.domain.mobile.general.Functions;
import kimble.automation.domain.mobile.general.Deserializers.ExpenseCategoryFromIdDeserializer;
import kimble.automation.domain.mobile.general.Deserializers.ExpenseClaimFromIdDeserializer;
import kimble.automation.domain.mobile.general.Deserializers.TaxCodeFromIdDeserializer;
import kimble.automation.domain.mobile.general.Entities.RemotelySaveableEntity;
import kimble.automation.domain.mobile.general.Entities.TimestampedEntityA;
import kimble.automation.domain.mobile.general.Identifiers.ExpenseItemIdentifier;
import kimble.automation.domain.mobile.general.Identifiers.ExpenseItemIdentifierEmbedded;
import kimble.automation.domain.mobile.general.Serializers.ExpenseCategoryToIdSerializer;
import kimble.automation.domain.mobile.general.Serializers.ExpenseClaimToStringIdSerializer;
import kimble.automation.domain.mobile.general.Serializers.TaxCodeToIdSerializer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;


public class ExpenseItemMob  
extends
TimestampedEntityA<ExpenseItemMob, ExpenseItemIdentifier>
implements
RemotelySaveableEntity<ExpenseItemMob, ExpenseItemIdentifier>
{

	@JsonProperty("Identifier")
	public ExpenseItemIdentifier getIdentifier() {
		if(identifier == null) identifier = new ExpenseItemIdentifierEmbedded(this);
		return super.getIdentifier();
	}
	public void setIdentifier(ExpenseItemIdentifier aIdentifier) throws Exception {
		if(aIdentifier == null) return;
		getIdentifier().setId(aIdentifier.getId());
		getIdentifier().setTimestamp(aIdentifier.getTimestamp());
	}

	TaxCodeMob taxCode;
	String startLocation;
	String endLocation;
	String notes;
	boolean isReceipted;
	boolean isImported;
	boolean isDeleted;
	boolean isCompanyPaid;
	double incurredTaxAmount;
	String incurredDate;
	String incurredCurrency;
	String currency;
	double incurredAmount;
	int expenseUnits;
	double expenseGrossAmount;
	ExpenseClaimMob expenseClaim;
	double exchangeRateConversionFactor;
	String attendees;
	String exchangeRateBasisId;
	String exchangeRateBasis;
	String attachmentId;
	ExpenseCategoryMob activityExpenseCategory;
	
	// Client side only
    String endLocationName;
    double endLocationLatitude;
    double endLocationLongitude;
    String startLocationName;
    double startLocationLatitude;
    double startLocationLongitude;
    boolean isDeletedLocally;
    boolean isDeletedOnServer;
    boolean hasChanged;
    String  receiptUrl;
    boolean receiptChanged;

	
	@JsonProperty("TaxCode")
	@JsonSerialize(using = TaxCodeToIdSerializer.class)
	@JsonDeserialize(using = TaxCodeFromIdDeserializer.class)
	public TaxCodeMob getTaxCode() { return taxCode; }
	public void setTaxCode(TaxCodeMob taxCode) { this.taxCode = taxCode; }
	
	@JsonProperty("StartLocation")
	public String getStartLocation() { return Functions.composeLocation(this.getStartLocationName(), this.getStartLocationLatitude(), this.getStartLocationLongitude()); }

	public String getStartLocationLocally() { return startLocation; }

	public void setStartLocation(String startLocation) { 
		this.startLocation = startLocation; 
		LocationMob l = Functions.parseLocation(this.startLocation);
		this.startLocationName = l.getName();
		this.startLocationLatitude = l.getLatitude();
		this.startLocationLongitude = l.getLongitude();
	}

	public void setStartLocationLocally(String startLocation) {
		this.startLocation = startLocation;
	}

	@JsonProperty("EndLocation")
	public String getEndLocation() { return Functions.composeLocation(this.getEndLocationName(), this.getEndLocationLatitude(), this.getEndLocationLongitude()); }

	public String getEndLocationLocally() { return endLocation; }
	public void setEndLocation(String endLocation) { 
		this.endLocation = endLocation; 
		LocationMob l = Functions.parseLocation(this.endLocation);
		this.endLocationName = l.getName();
		this.endLocationLatitude = l.getLatitude();
		this.endLocationLongitude = l.getLongitude();
	}

	public void setEndLocationLocally(String endLocation) {
		this.endLocation = endLocation;
	}
	
	@JsonProperty("Notes")
	public String getNotes() { return notes; }
	public void setNotes(String notes) { this.notes = notes; }
	
	@JsonProperty("IsReceipted")
	public boolean getIsReceipted() { return isReceipted; }
	public void setReceipted(boolean isReceipted) { this.isReceipted = isReceipted; }
	
	@JsonProperty("IsImported")
	public boolean getIsImported() { return isImported; }
	public void setImported(boolean isImported) { this.isImported = isImported; }
	
	@JsonProperty("IsDeleted")
	public boolean getDeleted() { return isDeleted; }
	public void setDeleted(boolean isDeleted) { this.isDeleted = isDeleted; }
	
	@JsonProperty("IsCompanyPaid")
	public boolean getIsCompanyPaid() { return isCompanyPaid; }
	public void setCompanyPaid(boolean isCompanyPaid) { this.isCompanyPaid = isCompanyPaid; }
	
	@JsonProperty("IncurredTaxAmount")
	public double getIncurredTaxAmount() { return incurredTaxAmount; }
	public void setIncurredTaxAmount(double incurredTaxAmount) { this.incurredTaxAmount = incurredTaxAmount; }
	
	@JsonProperty("IncurredDate")
	public String getIncurredDate() { return incurredDate; }
	public void setIncurredDate(String incurredDate) { this.incurredDate = incurredDate; }
	
	@JsonProperty("IncurredCurrencyISOCode")
	public String getIncurredCurrency() { return incurredCurrency; }
	public void setIncurredCurrency(String incurredCurrency) { this.incurredCurrency = incurredCurrency; }
	
	@JsonProperty("CurrencyISOCode")
	public String getCurrency() { return currency; }
	public void setCurrency(String currency) { this.currency = currency; }
	
	@JsonProperty("IncurredAmount")
	public double getIncurredAmount() { return incurredAmount; }
	public void setIncurredAmount(double incurredAmount) { this.incurredAmount = incurredAmount; }
	
	@JsonProperty("ExpenseUnits")
	public int getExpenseUnits() { return expenseUnits; }
	public void setExpenseUnits(int expenseUnits) { this.expenseUnits = expenseUnits; }
	
	@JsonProperty("ExpenseGrossAmount")
	public double getExpenseGrossAmount() { return expenseGrossAmount; }
	public void setExpenseGrossAmount(double expenseGrossAmount) { this.expenseGrossAmount = expenseGrossAmount; }
	
	@JsonProperty("ExpenseClaimId")
	@JsonSerialize(using = ExpenseClaimToStringIdSerializer.class)
	@JsonDeserialize(using = ExpenseClaimFromIdDeserializer.class)
	public ExpenseClaimMob getExpenseClaim() { return expenseClaim; }
	public void setExpenseClaim(ExpenseClaimMob aExpenseClaim) { expenseClaim = aExpenseClaim; }

	@JsonProperty("ExchangeRateConversionFactor")
	public double getExchangeRateConversionFactor() { return exchangeRateConversionFactor; }
	public void setExchangeRateConversionFactor(double exchangeRateConversionFactor) { this.exchangeRateConversionFactor = exchangeRateConversionFactor; }
	
	@JsonProperty("Attendees")
	public String getAttendees() { return attendees; }
	public void setAttendees(String attendees) { this.attendees = attendees; }

	@JsonProperty("ExchangeRateBasisId")
	public String getExchangeRateBasisId() { return exchangeRateBasisId; }
	public void setExchangeRateBasisId(String exchangeRateBasisId) { this.exchangeRateBasisId = exchangeRateBasisId; }

	@JsonProperty("ExchangeRateBasis")
	public String getExchangeRateBasis() { return exchangeRateBasis; }
	public void setExchangeRateBasis(String exchangeRateBasis) { this.exchangeRateBasis = exchangeRateBasis; }
	
	@JsonProperty("AttachmentId")
	public String getAttachmentId() { return attachmentId; }
	public void setAttachmentId(String attachmentId) {
		if(this.attachmentId != null && !this.attachmentId.equals(attachmentId))
			this.setReceiptUrl(null);
		this.attachmentId = attachmentId;
	}
	
	@JsonProperty("ActivityExpenseCategoryId")
	@JsonSerialize(using = ExpenseCategoryToIdSerializer.class)
	@JsonDeserialize(using = ExpenseCategoryFromIdDeserializer.class)
	public ExpenseCategoryMob getActivityExpenseCategory() { return activityExpenseCategory; }
	public void setActivityExpenseCategory(ExpenseCategoryMob activityExpenseCategory) { this.activityExpenseCategory = activityExpenseCategory; }
	
	// Client side only
	@JsonIgnore
	public String getEndLocationName() { return endLocationName; }
	public void setEndLocationName(String endLocationName) {
		this.endLocationName = endLocationName;
		//this.updateEndLocation();
	}
	
	@JsonIgnore
	public double getEndLocationLatitude() { return endLocationLatitude; }
	public void setEndLocationLatitude(double endLocationLatitude) {
		this.endLocationLatitude = endLocationLatitude;
		//this.updateEndLocation();
	}
	
	@JsonIgnore
	public double getEndLocationLongitude() { return endLocationLongitude; }
	public void setEndLocationLongitude(double endLocationLongitude) {
		this.endLocationLongitude = endLocationLongitude;
		//this.updateEndLocation();
	}
	
	@JsonIgnore
	public String getStartLocationName() { return startLocationName; }
	public void setStartLocationName(String startLocationName) {
		this.startLocationName = startLocationName;
		//this.updateStartLocation();
	}
	
	@JsonIgnore
	public double getStartLocationLatitude() { return startLocationLatitude; }
	public void setStartLocationLatitude(double startLocationLatitude) {
		this.startLocationLatitude = startLocationLatitude;
		//this.updateStartLocation();
	}
	
	@JsonIgnore
	public double getStartLocationLongitude() { return startLocationLongitude; }
	public void setStartLocationLongitude(double startLocationLongitude) {
		this.startLocationLongitude = startLocationLongitude;
		//this.updateStartLocation();
	}
	
	@JsonIgnore
	public boolean isDeletedLocally() { return isDeletedLocally; }
	public void setDeletedLocally(boolean isDeletedLocally) { this.isDeletedLocally = isDeletedLocally; }
	
	@JsonIgnore
	public boolean isDeletedOnServer() { return isDeletedOnServer; }
	public void setDeletedOnServer(boolean isDeletedOnServer) { this.isDeletedOnServer = isDeletedOnServer; }
	
	@JsonIgnore
	public boolean hasChanged() { return hasChanged; }
	public void setHasChanged(boolean hasChanged) { this.hasChanged = hasChanged; }
	
	@JsonIgnore
	public String getReceiptUrl() { return receiptUrl; }
	public void setReceiptUrl(String receiptUrl) { this.receiptUrl = receiptUrl; }
	
	@JsonIgnore
	public boolean isReceiptChanged() { return receiptChanged; }
	public void setReceiptChanged(boolean receiptChanged) { this.receiptChanged = receiptChanged; }

	void updateStartLocation() {
		this.startLocation = Functions.composeLocation(this.getStartLocationName(), this.getStartLocationLatitude(), this.getStartLocationLongitude());
	}

	void updateEndLocation() {
		this.endLocation = Functions.composeLocation(this.getEndLocationName(), this.getEndLocationLatitude(), this.getEndLocationLongitude());
	}

	/**
	 * ExpenseItems can have receipts attached. The deletion off an ExpenseItem must then delete the attachment if it exists.
	 * This method checks iff the thread is in transaction in which case it handles the commit and rollback events ->
	 * putting back the receipt file url on rollback or deleting the file on successful commit.
	 *
	 * @return
	 */
	public ExpenseItemMob deleteWithAttachment() {
		super.delete();
		return this;
	}
}
