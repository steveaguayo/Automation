package kimble.automation.domain;

public class Risk {
	// linking the risk object to a stage in the test flow
	public String stage;
	// to be populated after creation
	public String id;
	
	public String reference;
	public String summary;
	public String raisedBy;
	public String raisedDate;
	public String impact;
	public String probability;
	public String category;
	public String owner;
	public String status;
	public boolean internalOnly;
	
	public String proposal;
	public String deliveryGroup;
	public String severity;
}
