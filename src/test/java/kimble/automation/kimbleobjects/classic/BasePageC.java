package kimble.automation.kimbleobjects.classic;

import kimble.automation.helpers.SeleniumHelper;

import org.openqa.selenium.support.PageFactory;

public class BasePageC {
	protected SeleniumHelper theSH;
	
	boolean initialised;
	
	protected BasePageC(SeleniumHelper seleniumHelperInstance){
		initialise(seleniumHelperInstance);
	}
	
	private void initialise(SeleniumHelper seleniumHelperInstance){
		if(!initialised){
			theSH = seleniumHelperInstance;
			
			initWebElements();
			initialised = true;
		}
	}

	private void initWebElements() {
		PageFactory.initElements(theSH.getWD(), this);
	}
}
