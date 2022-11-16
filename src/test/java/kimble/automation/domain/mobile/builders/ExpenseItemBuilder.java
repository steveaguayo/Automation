package kimble.automation.domain.mobile.builders;

import kimble.automation.domain.mobile.CreateMob;
import kimble.automation.domain.mobile.ExpenseCategoryMob;
import kimble.automation.domain.mobile.ExpenseClaimMob;
import kimble.automation.domain.mobile.ExpenseItemMob;
import kimble.automation.domain.mobile.TaxCodeMob;
import kimble.automation.domain.mobile.general.Deserializers;

public class ExpenseItemBuilder extends EntityBuilderA<ExpenseItemBuilder, ExpenseItemMob> {

	public ExpenseItemBuilder() throws Exception {
		e = new ExpenseItemMob();
		e.setId(Deserializers.generateId());
		e.setStartLocation(DefaultValues.startLocation);
		e.setNotes(DefaultValues.expenseNote);
		e.setReceipted(DefaultValues.receipted);
		e.setReceiptChanged(false);
		e.setReceiptUrl(DefaultValues.receiptUrl);
		e.setImported(DefaultValues.imported);
		e.setDeleted(false);
		e.setCompanyPaid(DefaultValues.companyPaid);
		e.setIncurredTaxAmount(DefaultValues.incurredTaxAmount);
		e.setIncurredDate(DefaultValues.incurredDate);
		e.setIncurredCurrency(DefaultValues.currency);
		e.setIncurredAmount(DefaultValues.incurredAmount);
		e.setExpenseUnits(DefaultValues.expenseUnits);
		e.setExpenseGrossAmount(DefaultValues.expenseGrossAmount);
		e.setExchangeRateConversionFactor(DefaultValues.exchangeRateConversionFactor);
		e.setEndLocation(DefaultValues.endLocation);
		e.setAttendees(DefaultValues.attendees);
		e.setAttachmentId(DefaultValues.attachmentId);
		e.setExpenseClaim(CreateMob.ExpenseClaim().build());
		e.setActivityExpenseCategory(CreateMob.ExpenseCategory().build());
	}
	
	public ExpenseItemBuilder withTaxCode(TaxCodeMob val) { e.setTaxCode(val); return this; }
	public ExpenseItemBuilder withStartLocation(String val) { e.setStartLocation(val); return this; }
	public ExpenseItemBuilder withNotes(String val) { e.setNotes(val); return this; }
	public ExpenseItemBuilder withReceipted(boolean val) { e.setReceipted(val); return this; }
	public ExpenseItemBuilder withReceiptChanged(boolean val) { e.setReceiptChanged(val); return this; }
	public ExpenseItemBuilder withReceipUrl(String val) { e.setReceiptUrl(val); return this; }
	public ExpenseItemBuilder withImported(boolean val) { e.setImported(val); return this; }
	public ExpenseItemBuilder withDeleted(boolean val) { e.setDeleted(val); return this; }
	public ExpenseItemBuilder withCompanyPaid(boolean val) { e.setCompanyPaid(val); return this; }
	public ExpenseItemBuilder withIncurredTaxAmount(double val) { e.setIncurredTaxAmount(val); return this; }
	public ExpenseItemBuilder withIncurredDate(String val) { e.setIncurredDate(val); return this; }
	public ExpenseItemBuilder withIncurredCurrency(String val) { e.setIncurredCurrency(val); return this; }
	public ExpenseItemBuilder withIncurredAmount(double val) { e.setIncurredAmount(val); return this; }
	public ExpenseItemBuilder withExpenseUnits(int val) { e.setExpenseUnits(val); return this; }
	public ExpenseItemBuilder withExpenseGrossAmount(double val) { e.setExpenseGrossAmount(val); return this; }
	public ExpenseItemBuilder withExpenseClaim(ExpenseClaimMob val) { e.setExpenseClaim(val); return this; }
	public ExpenseItemBuilder withExchangeRateConversionFactor(double val) { e.setExchangeRateConversionFactor(val); return this; }
	public ExpenseItemBuilder withEndLocation(String val) { e.setEndLocation(val); return this; }
	public ExpenseItemBuilder withCurrency(String val) { e.setCurrency(val); return this; }
	public ExpenseItemBuilder withAttendees(String val) { e.setAttendees(val); return this; }
	public ExpenseItemBuilder withAttachmentId(String val) { e.setAttachmentId(val); return this; }
	public ExpenseItemBuilder withActivityExpenseCategory(ExpenseCategoryMob val) { e.setActivityExpenseCategory(val); return this; }

	public ExpenseItemMob build() throws Exception { return e; }
	
}
