package kimble.automation.kimbleobjects.classic;

import static kimble.automation.helpers.SequenceActions.*;

import java.util.Map;

import kimble.automation.domain.JourneyAllowanceAdjustment;
import kimble.automation.domain.JourneyAllowanceAllocation;
import kimble.automation.domain.JourneyLeg;
import kimble.automation.helpers.SeleniumHelper;

import org.openqa.selenium.By;

public class JourneysPageC {
	
	public static final By 
	journeyNameInput = By.cssSelector("input.journey-name"),
	departingFromSelect = By.cssSelector("select.departing-from"),
	departingDateInput = By.cssSelector("input.departing-from-date"),
	departingTimeInput = By.cssSelector("input.departing-from-time"),
	arrivingAtSelect = By.cssSelector("select.arriving-at"),
	arrivingDateInput = By.cssSelector("input.arriving-at-date"),
	arrivingTimeInput = By.cssSelector("input.arriving-at-time"),
	legNoteInput = By.cssSelector("textarea.leg-note"),
	activitySelect = By.cssSelector("select.find-activity"),
	
	newButton = By.xpath("//input[normalize-space(@value)='New']"),
	newJourneyButton = By.xpath("//input[normalize-space(@value)='New Journey']"),
	saveNameButton = By.cssSelector("div.title-card-menu.fa-save.burger-menu"),
	saveAndSubmitButton = By.xpath("//input[normalize-space(@value)='Save And Submit']"),
	addJourneyLegButton = By.cssSelector("i.fa-plus-square.add-new-journey-leg"),
	saveJourneyLegButton = By.xpath("//i[normalize-space(@title)='Save Journey Leg'][../../td/select[contains(@class, departing-from)]]"),
	calculateAllowancesLink = By.xpath("//span[normalize-space(text())='Calculate Allowances']"),
	createExpenseItemsLink = By.xpath("//span[normalize-space(text())='Create Expense Items']"),
	
	allAllocatedAllowanceButtons = By.cssSelector("i.allocated-allowance-btn"),
	
	allowancesAndAdjustmentsHeaders = By.xpath("//table[@class='list allowance-period-table']/thead/tr[2]/th"),
	
	clearAllocationAllowanceButton = By.cssSelector("i.clear-allowance-allocation-btn");
	
	// templated selectors
	public static String 
	allowanceDateSpan = "//td[1]/span[1][normalize-space(text())=\"{{date}}\"]",
	adjustmentDateRow = allowanceDateSpan + "/../..",
	
	accommodationAdjustmentCheckbox = adjustmentDateRow + "/td[7]/input",
	localTransportAdjustmentCheckbox = adjustmentDateRow + "/td[8]/input",
	airportTransportAdjustmentCheckbox = adjustmentDateRow + "/td[9]/input",
	
	breakfastAdjustmentCheckbox = adjustmentDateRow + "/td[4]/input",
	lunchAdjustmentCheckbox = adjustmentDateRow + "/td[5]/input",
	dinnerAdjustmentCheckbox = adjustmentDateRow + "/td[6]/input",
			
	allocationAmountInput = adjustmentDateRow + "/td/div/input[normalize-space(@class)='allocation-amount'][normalize-space(@activity-id)='{{activity-id}}']",
	
	allocatedAllowanceButtons = "//table[@class='list allowance-period-table']/tbody/tr/td[{{index}}]/a/i",
	
	addPeriodAllocationButton = adjustmentDateRow + "/td[normalize-space(@class)='add-allocation-cell']/span",
					
	allowanceAmount = adjustmentDateRow + "/td/span[normalize-space(@class)='total-amt']",
	
	activityHeader = "//th[normalize-space(text())=\"{{activity}}\"]";
			
	
	public static void open(SeleniumHelper sh, String journeyName) {
		executeSequenceWithRefreshRetry(sh, 3, () -> {
			clickAndWaitSequence(sh, 20, 
			/* click the journey name link					*/	By.xpath("//a[normalize-space(text())=\"" + journeyName + "\"]"),
			/* wait for add journey leg button				*/	addJourneyLegButton
			);
		});
	}
	
	public static void startCreateWizard(SeleniumHelper sh) {
		executeSequenceWithRefreshRetry(sh, 3, () -> {
			clickAndWaitSequence(sh, 20, 
			/* click new									*/	newButton,
			/* wait for journey name input					*/	journeyNameInput
			);
		});
	}
	
	public static void enterNameAndFinishWizard(SeleniumHelper sh, String name) {
		executeSequenceWithRefreshRetry(sh, 3, () -> {
			clearAndInputText(sh, journeyNameInput, name);
			clickAndWaitSequence(sh, 20, 
			/* click save									*/	saveNameButton,
			/* wait for add journey leg button				*/	addJourneyLegButton
			);
		});
	}
	
	public static void addLeg(SeleniumHelper sh, JourneyLeg leg) {
		executeSequenceWithRefreshRetry(sh, 3, () -> {
			clickAndWaitSequence(sh, 20, 
			/* click add journey leg						*/	addJourneyLegButton,
			/* wait for departing from select				*/	departingFromSelect
			);
			
			/* input departure location						*/	dropdownSelect(sh, departingFromSelect, leg.departingFrom);
			/* input departure date							*/	clearAndInputText(sh, departingDateInput, leg.departureDate);
			/* input departure time							*/	clearAndInputText(sh, departingTimeInput, leg.departureTime);
			/* input arrival location						*/	dropdownSelect(sh, arrivingAtSelect, leg.arrivingAt);
			/* input arrival date							*/	clearAndInputText(sh, arrivingDateInput, leg.arrivalDate);
			/* input arrival time							*/	clearAndInputText(sh, arrivingTimeInput, leg.arrivalTime);
			/* input notes									*/	clearAndInputText(sh, legNoteInput, leg.notes);
			
			clickAndWaitSequence(sh, 20, 
			/* click save journey							*/	saveJourneyLegButton,
			/* wait for calculate allowances link			*/	calculateAllowancesLink
			);
		});
	}
	
	public static void calculateAllowances(SeleniumHelper sh) {
		clickAndWaitSequence(sh, 20, 
		/* click calculate allowances link					*/	calculateAllowancesLink,
		/* wait for calculate allowances link to reappear	*/	calculateAllowancesLink
		);
	}
	
	public static void adjustAllowance(SeleniumHelper sh, JourneyAllowanceAdjustment adjustment) {
		executeSequenceWithRefreshRetry(sh, 3, () -> {
		/* adjust breakfast allowance						*/	checkboxSelect(sh, By.xpath(breakfastAdjustmentCheckbox.replace("{{date}}", adjustment.period)), adjustment.breakfast);
		/* wait for calculate allowances link to reappear	*/	waitClickable(sh, calculateAllowancesLink, 20);
		/* adjust lunch allowance							*/	checkboxSelect(sh, By.xpath(lunchAdjustmentCheckbox.replace("{{date}}", adjustment.period)), adjustment.lunch);
		/* wait for calculate allowances link to reappear	*/	waitClickable(sh, calculateAllowancesLink, 20);
		/* adjust dinner allowance							*/	checkboxSelect(sh, By.xpath(dinnerAdjustmentCheckbox.replace("{{date}}", adjustment.period)), adjustment.dinner);
		/* wait for calculate allowances link to reappear	*/	waitClickable(sh, calculateAllowancesLink, 20);
		/* adjust accommodation allowance					*/	checkboxSelect(sh, By.xpath(accommodationAdjustmentCheckbox.replace("{{date}}", adjustment.period)), adjustment.accommodation);
		/* wait for calculate allowances link to reappear	*/	waitClickable(sh, calculateAllowancesLink, 20);
		/* adjust local transport allowance					*/	checkboxSelect(sh, By.xpath(localTransportAdjustmentCheckbox.replace("{{date}}", adjustment.period)), adjustment.localTransport);
		/* wait for calculate allowances link to reappear	*/	waitClickable(sh, calculateAllowancesLink, 20);
		/* adjust airport transport allowance				*/	checkboxSelect(sh, By.xpath(airportTransportAdjustmentCheckbox.replace("{{date}}", adjustment.period)), adjustment.airportTransport);
		/* wait for calculate allowances link to reappear	*/	waitClickable(sh, calculateAllowancesLink, 20);
		});
	}

	public static void validateAmounts(SeleniumHelper sh, Map<String, String> amounts) {
		executeSequenceWithRefreshRetry(sh, 3, () -> {
			amounts.forEach((period, amount) -> {
		/* validate allowance amount for each period		*/	validateStringValue(sh, By.xpath(allowanceAmount.replace("{{date}}", period)), amount, "total allowance for period: " + period);
			});
		});
	}
	
	public static void allocateAllowance(SeleniumHelper sh, JourneyAllowanceAllocation allocation) {
		executeSequenceWithRefreshRetry(sh, 3, () -> {
			if(!exists(sh, By.xpath(activityHeader.replace("{{activity}}", allocation.activityName)), 5))
		/* the activity appears as a column after the first allocation is added	*/	allocateFirstAllowance(sh, allocation);
			else
		/* all other allocations need to be added into the column directly		*/	allocateConsecutiveAllowance(sh, allocation);
		});
	}
	
	public static void allocateFirstAllowance(SeleniumHelper sh, JourneyAllowanceAllocation allocation) {
		By addButton = By.xpath(addPeriodAllocationButton.replace("{{date}}", allocation.period));
		/* select the activity								*/	dropdownSelect(sh, activitySelect, allocation.activity);
		/* wait for the add button							*/	waitClickable(sh, addButton, 20);
		/* click to add an allocation						*/	click(sh, addButton);
		/* wait for the loading indicators to disappear		*/	waitHidden(sh, By.cssSelector(".Loading"), 20);
		/* wait for clear allowance button to appear		*/	waitClickable(sh, clearAllocationAllowanceButton, 20);
		allocateConsecutiveAllowance(sh, allocation);
	}
	
	public static void allocateConsecutiveAllowance(SeleniumHelper sh, JourneyAllowanceAllocation allocation) {
		String activityId = sh.getWebElement(By.xpath(activityHeader.replace("{{activity}}", allocation.activityName))).getAttribute("activity-id");
		By allocationInput = By.xpath(allocationAmountInput.replace("{{date}}", allocation.period).replace("{{activity-id}}", activityId));
		/* input the allocation amount						*/	clearAndInputText(sh, allocationInput, allocation.amount);
		/* wait for the loading indicators to disappear		*/	waitHidden(sh, By.cssSelector(".Loading"), 20);
	}
	
	public static void createExpenseItems(SeleniumHelper sh) {
		executeSequenceWithRefreshRetry(sh, 3, () -> {
		/* click to create the expense items				*/	click(sh, createExpenseItemsLink);
		/* wait for the info buttons to appear				*/	waitClickable(sh, allAllocatedAllowanceButtons, 20);
		});
	}
	
	public static void navigateToExpenseClaim(SeleniumHelper sh, String activity) {
		executeSequenceWithRefreshRetry(sh, 3, () -> {
		/* click one of the info buttons					*/	click(sh, By.xpath(allocatedAllowanceButtons.replace("{{index}}", "" + (getActivityColumnIndex(sh, activity)+1))));
		/* wait for the add expense button to appear		*/	waitClickable(sh, ExpensePageC.addExpenseButton, 20);
		});
	}
	
	public static void navigateToSubmittedExpenseClaim(SeleniumHelper sh, String activity) {
		executeSequenceWithRefreshRetry(sh, 3, () -> {
		/* click one of the info buttons					*/	click(sh, By.xpath(allocatedAllowanceButtons.replace("{{index}}", "" + (getActivityColumnIndex(sh, activity)+1))));
		/* wait for the add expense button to appear		*/	waitClickable(sh, ExpensePageC.revertToDraftButton, 20);
		});
	}
	
	public static int getActivityColumnIndex(SeleniumHelper sh, String activity) {
		return sh.getWebElements(allowancesAndAdjustmentsHeaders).indexOf(sh.getWebElement(By.xpath(activityHeader.replace("{{activity}}", activity))));
	}
}
