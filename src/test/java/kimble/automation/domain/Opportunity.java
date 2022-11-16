package kimble.automation.domain;

import java.util.List;

public class Opportunity {
	public String name;
	public String accountName;
	public String stage;
	public String closeDate;
	
	public List<Proposal> proposals;
}
