package kimble.automation.domain;

import java.util.List;

public class CreditNote {
	public String stage;
	public String reference;
	public String reasonCode;
	public String reasonDescription;
	public String grossTotal;
	public String taxAmount;
	public String netAmount;
	public String creditNoteDate;
	public boolean isPartial;
	public List<CreditNoteLine> lines;
}
