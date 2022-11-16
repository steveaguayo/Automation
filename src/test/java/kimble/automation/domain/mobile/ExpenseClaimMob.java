package kimble.automation.domain.mobile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import kimble.automation.domain.mobile.general.Entities.RemotelySaveableEntity;
import kimble.automation.domain.mobile.general.Entities.TimestampedEntity;
import kimble.automation.domain.mobile.general.Identifiers.TimestampedIdentifier;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;


public class ExpenseClaimMob  
extends
TimestampedEntity<ExpenseClaimMob>
implements
RemotelySaveableEntity<ExpenseClaimMob, TimestampedIdentifier>
{
	public enum Status { Draft, ReadyForApproval, Approved, Unapproved, Rejected, PendingPayment, Paid; }

	public ExpenseClaimMob() { status = Status.Draft; }
	
	String name;
	Status status;
	String resourcedActivityId;
	boolean isDeleted;
	String approvalComments;
	List<ExpenseItemMob> expenseItems;
	List<String> attachmentIds;
	
	// Client side only
    boolean isDeletedLocally;
    boolean isDeletedOnServer;
    boolean hasChanged;

	@JsonProperty("AttachmentIds")
	public List<String> getAttachmentIds() { return attachmentIds; }
	public void setAttachmentIds(List<String> attachmentIds) { this.attachmentIds = attachmentIds; }

	@JsonProperty("Name")
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	
	@JsonProperty("Status")
	public Status getStatus() { return status; }
	public void setStatus(Status status) { this.status = status; }
	
	@JsonProperty("ResourcedActivityId")
	public String getResourcedActivityId() { return resourcedActivityId; }
	public void setResourcedActivityId(String resourcedActivityId) { this.resourcedActivityId = resourcedActivityId; }
	
	@JsonProperty("IsDeleted")
	public boolean getDeleted() { return isDeleted; }
	public void setDeleted(boolean isDeleted) { this.isDeleted = isDeleted; }
	
	@JsonProperty("ApprovalComments")
	public String getApprovalComments() { return approvalComments; }
	public void setApprovalComments(String approvalComments) { this.approvalComments = approvalComments; }

	@JsonProperty("ExpenseItems")
	public List<ExpenseItemMob> getExpenseItems() { 
		if(this.expenseItems == null) expenseItems = new ArrayList(); 
		return expenseItems; 
	}
	public void setExpenseItems(Collection<ExpenseItemMob> expenseItems) {
		// This is a workaround to keep the json deserialisation working though the db hasn't been setup
		this.getExpenseItems().clear();
		this.getExpenseItems().addAll(expenseItems);
	}
	public void addExpenseItem(ExpenseItemMob aItem) { this.getExpenseItems().add(aItem); }

	// Client side only
	@JsonIgnore
	public boolean isDeletedLocally() { return isDeletedLocally; }
	public void setDeletedLocally(boolean isDeletedLocally) { this.isDeletedLocally = isDeletedLocally; }
	
	@JsonIgnore
	public boolean isDeletedOnServer() { return isDeletedOnServer;}
	public void setDeletedOnServer(boolean isDeletedOnServer) { this.isDeletedOnServer = isDeletedOnServer; }
	
	@JsonIgnore
	public boolean hasChanged() { return hasChanged; }
	public void setHasChanged(boolean hasChanged) { this.hasChanged = hasChanged; }
	
}
