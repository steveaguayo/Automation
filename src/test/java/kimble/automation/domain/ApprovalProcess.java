package kimble.automation.domain;

import java.util.List;

public class ApprovalProcess {
	public String targetObject;
	public String name;
	public String uniqueName;
	
	public boolean removeKimbleOnePackageName;
	
	public String entryCriteriaType;
	public String entryCriteriaFormula;
	
	public FilterCriteria entryCriteria1;
	public String emailTemplate;
	
	public List<SelectorField> approvalPageAdditionalFields;
	
	public List<ApprovalStep> approvalSteps;
	
	public FieldUpdateAction finalApprovalAction;
	public FieldUpdateAction finalRejectionAction;
}
