package kimble.automation.domain.mobile.builders;

import java.util.Collection;

import kimble.automation.domain.mobile.ExpenseClaimMob;
import kimble.automation.domain.mobile.ExpenseItemMob;
import kimble.automation.domain.mobile.general.Deserializers;

public class ExpenseClaimBuilder extends EntityBuilderA<ExpenseClaimBuilder, ExpenseClaimMob>{

	public ExpenseClaimBuilder() throws Exception {
		e = new ExpenseClaimMob();
		e.setId(Deserializers.generateId());
		e.setStatus(DefaultValues.expenseClaimStatus);
		e.setResourcedActivityId(DefaultValues.resourcedActivityId);
		e.setName(DefaultValues.expenseClaimName);
		e.setDeleted(false);
		e.setApprovalComments(DefaultValues.approvalComments);
	}
	
	public ExpenseClaimBuilder withStatus(ExpenseClaimMob.Status val) { e.setStatus(val); return this; }
	public ExpenseClaimBuilder withResourcedActivityId(String val) { e.setResourcedActivityId(val); return this; }
	public ExpenseClaimBuilder withName(String val) { e.setName(val); return this; }
	public ExpenseClaimBuilder withDeleted(boolean val) { e.setDeleted(val); return this; }
	public ExpenseClaimBuilder withApprovalComments(String val) { e.setApprovalComments(val); return this; }
	public ExpenseClaimBuilder withExpenseItems(Collection<ExpenseItemMob> aItems) { e.setExpenseItems(aItems); return this; }

	public ExpenseClaimMob build() throws Exception { return e; }
	
}
