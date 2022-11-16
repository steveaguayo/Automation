package kimble.automation.domain.mobile.builders;

import kimble.automation.domain.mobile.ActivityMob;
import kimble.automation.domain.mobile.ExpenseCategoryMob;
import kimble.automation.domain.mobile.general.Deserializers;

public class ExpenseCategoryBuilder extends EntityBuilderA<ExpenseCategoryBuilder, ExpenseCategoryMob> {

	public ExpenseCategoryBuilder() throws Exception {
		e = new ExpenseCategoryMob();
		e.setId(Deserializers.generateId());
		e.setUnitType(DefaultValues.expenseUnitType);
		e.setName(DefaultValues.expenseCategoryName);
		e.setDeleted(false);
		e.setActive(DefaultValues.active);
		e.setHasStartLocation(DefaultValues.hasStartLocation);
		e.setHasEndLocation(DefaultValues.hasEndLocation);
		e.setHasAttendees(DefaultValues.hasAttendees);
		e.setCurrency(DefaultValues.currency);
		e.setCanEditTaxCode(DefaultValues.canEditTaxCode);
		e.setCanEditTaxAmount(DefaultValues.canEditTaxAmount);
	}
	
	public ExpenseCategoryBuilder withUnitType(String val) { e.setUnitType(val); return this; }
	public ExpenseCategoryBuilder withName(String val) { e.setName(val); return this; }
	public ExpenseCategoryBuilder withDeleted(boolean val) { e.setDeleted(val); return this; }
	public ExpenseCategoryBuilder withActive(boolean val) { e.setActive(val); return this; }
	public ExpenseCategoryBuilder withHasStartLocation(boolean val) { e.setHasStartLocation(val); return this; }
	public ExpenseCategoryBuilder withHasEndLocation(boolean val) { e.setHasEndLocation(val); return this; }
	public ExpenseCategoryBuilder withHasAttendees(boolean val) { e.setHasAttendees(val); return this; }
	public ExpenseCategoryBuilder withCurrency(String val) { e.setCurrency(val); return this; }
	public ExpenseCategoryBuilder withCanEditTaxCode(boolean val) { e.setCanEditTaxCode(val); return this; }
	public ExpenseCategoryBuilder withCanEditTaxAmount(boolean val) { e.setCanEditTaxAmount(val); return this; }
	public ExpenseCategoryBuilder withActivity(ActivityMob val) { e.setActivity(val); return this; }

	public ExpenseCategoryMob build() throws Exception { return e; }
	
}
