package kimble.automation.helpers;

import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import kimble.automation.domain.JsTreeItemPath;
import kimble.automation.domain.UserCredentials;
import kimble.automation.kimbleobjects.classic.LoginPageC;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

public class General {
	
	public static final SimpleDateFormat trackingPeriodFormatter = new SimpleDateFormat("MMM / W / yyyy");
	public static final SimpleDateFormat forecastingPeriodFormatter = new SimpleDateFormat("MMMM yyyy");
	
	static final Properties rerunnableTests = new Properties();
	
	public static String objectId = null;
	public static String timePeriodId= null;
	public static String encodedValue = null;
	
	public static int counter = 1;
	public static int counter2 = 0;
	
	static {
		trackingPeriodFormatter.getCalendar().setFirstDayOfWeek(Calendar.MONDAY);
		forecastingPeriodFormatter.getCalendar().setFirstDayOfWeek(Calendar.MONDAY);
	}
	
	public static enum Month { January, February, March, April, May, June, July, August, September, October, November, December }
	
	public static final By 
	userNavigationButton = By.cssSelector("#userNavButton"),
	logoutLink = By.xpath("//div[@id='userNav-menuItems']/a[contains(@href, '/secur/logout.jsp')]"),
	copyrightSpan = By.cssSelector("span.brandQuaternaryFgr");
	
	// jstree item selectors
	public static final String 
	itemTree = "//ul[@class='jstree-container-ul jstree-children jstree-no-icons']",
	rootItem = itemTree + "/li[1]",
	
	itemList = "/ul[1]",
	itemLink = "/a[1][normalize-space(text())=\"{{item}}\"]",
	itemValue = "/div/span[normalize-space(text())=\"{{value}}\"]",
	itemCheckbox = "/i[@class='jstree-icon jstree-checkbox']",
	
	rootItemLink = rootItem + "/a[1]",
	
	childItemLink = itemList + "/li" + itemLink,
	childItem = childItemLink + "/..";
		
	public static synchronized void setRerunProperty(String property, String value) {
		rerunnableTests.put(property, value);
	}
	
	public static void scheduleForRerun(String testName, boolean aSchedule) {
		setRerunProperty(testName, "" + aSchedule);
	}
	
	public static synchronized void saveRerunSchedule() {
		FileWriter out = null;
		try {
			out = new FileWriter("rerun.properties");
			rerunnableTests.store(out, "This is a list of the tests that should be re-run in case of glitches");
		} catch(Exception e) {
			throw new RuntimeException("Failed to save the rerunnable tests properties file");
		} finally {
			if(out != null) {
				try {
					out.close();
				} catch(Exception e) {
					throw new RuntimeException("Failed to close the rerunnable tests properties file stream");
				}
			}
		}
	}
	
	public static synchronized void deleteRerunSchedule() {
		File rerunFile = new File("rerun.properties");
		if(rerunFile.exists())
			rerunFile.delete();
	}
	
	public static Date getDateForTrackingPeriod(String periodName) {
		try {
			// If you ask me this special case has to be added because of a java bug. 
			// I'm looking for the date for the first week of January 2013, but getting the first day of the first week of 2013
			// which is in 2013. Hence the special case.
			String pn = periodName.trim();
			if(pn.startsWith("Jan / 1")) {
				Calendar c = Calendar.getInstance();
				c.set(Integer.parseInt(pn.substring(pn.length()-4)), 0, 1);
				return c.getTime();
			}
			return trackingPeriodFormatter.parse(periodName);
		} catch (ParseException e) { throw new RuntimeException("Failed to parse the tracking period: " + periodName, e); }
	}
	
	public static Date getDateForForecastingPeriod(String periodName) {
		try {
			return forecastingPeriodFormatter.parse(periodName);
		} catch (ParseException e) { throw new RuntimeException("Failed to parse the tracking period: " + periodName, e); }
	}
	
	public static interface DateParts {
		int getDay();
		int getMonth();
		String getMonthName();
		int getYear();
	}
	
	public static DateParts newDateParts(final int d, final int m, final int y) {
		return new DateParts() {
			public int getDay() { return d; }
			public int getMonth() { return m; }
			public String getMonthName() { return General.getMonthName(m); }
			public int getYear() { return y; }
		};
	}
	
	public static String getMonthName(int monthNumber) {
		if(monthNumber < 1 || monthNumber > 12)
			throw new RuntimeException("The month number is out of range [1-12]");
		return Month.values()[monthNumber - 1].name();
	}

	public static int getMonthNumber(String monthName) {
		return Month.valueOf(monthName).ordinal() + 1;
	}

	public static DateParts getDateParts(String date) {
		String[] strParts = date.trim().split("/");
		if(strParts.length != 3)
			throw new RuntimeException("The date has an incorrect number of parts: [" + strParts.length + "] expected [3]");
		
		final int d = Integer.parseInt(strParts[0]);
		final int m = Integer.parseInt(strParts[1]);
		final int y = Integer.parseInt(strParts[2]);
		
		return newDateParts(d, m, y);
	}
	
	public static int getDifferenceInMonths(int fromM, int fromY, int toM, int toY) {
		return toM - fromM + (toY - fromY)*12;
	}
	
	public static int getDifferenceInMonths(DateParts fromD, DateParts toD) {
		return getDifferenceInMonths(fromD.getMonth(), fromD.getYear(), toD.getMonth(), toD.getYear());
	}
	
	public static void logout(SeleniumHelper sh) {
		sh.clickAndWaitSequenceWithRefreshRetry(7, userNavigationButton, logoutLink);
		sh.clickLink(logoutLink);
		sh.waitForPageLoadComplete(20);
	}
	
	public static void relogin(SeleniumHelper sh, UserCredentials creds) {
		String currentUrl = sh.getCurrentUrl();
		logout(sh);
		LoginPageC.NavigateToLoginPageAndDirect(sh, creds.loginUrl, null);
		new LoginPageC(sh).fastLogin(creds);
		sh.sleep(3000);
		sh.waitForPageLoadComplete(20);
		creds.nodeName = (String) ((JavascriptExecutor) sh.getWD()).executeScript("return window.location.origin;");
		sh.goToUrl(currentUrl);
	}
	
	public static void removeWalkMeElement(SeleniumHelper sh) {
		try {
			sh.getJsExecutor().executeScript("document.getElementById('walkme-player').remove();");
		} catch(Exception e){}
	}
	
	public static String getRootItemName(SeleniumHelper sh) {
		return sh.getWebElementTextOrNull(By.xpath(General.rootItemLink));
	}

	public static void selectJsTreeItems(List<JsTreeItemPath> aGroup, String rootName, SeleniumHelper sh) {
		for(JsTreeItemPath p : aGroup)
			selectJsTreeItem(p, rootName, sh);
	}
	
	public static void counterReset() {
		counter = 1;
		counter2 = 0;
	}
	
	public static void selectJsTreeItem(JsTreeItemPath groupPath, String rootName, SeleniumHelper sh) {
		String[] parts = groupPath.path.split("\\s*>\\s*");
		if(!parts[0].trim().equals(rootName))
			return;
		String itemCheckboxSelectorStr = rootItem;
		for(int i = 1; i < parts.length; i++)
			itemCheckboxSelectorStr += childItem.replace("{{item}}", parts[i].trim());
		if(groupPath.value == null){	
			itemCheckboxSelectorStr += "/a[1]" + itemCheckbox;
			sh.clickLink(By.xpath(itemCheckboxSelectorStr));
		}else{
			List<WebElement> invItemDes = sh.getWebElements(By.xpath("//div[contains(@class,'selectRevenueItems jstree-grid-col-3 ')]"));       
			int maxItems = invItemDes.size();  
			while(counter < maxItems){
				counter++;
				counter2++;
	            WebElement invoiceableItem = sh.getWebElement(By.xpath("//li[@role='none'][" + counter + "]//i[@class='jstree-icon jstree-themeicon']"));
	            WebElement invoiceableAmount = sh.getWebElement(By.xpath("//li[@class='jstree-node  jstree-closed'][" + counter2 + "]/a/i[2]"));
	            WebElement checkBox = sh.getWebElement(By.xpath("//li[@class='jstree-node  jstree-closed'][" + counter2 + "]//i[@class='jstree-icon jstree-checkbox']"));
	                            
	            if (invoiceableItem.getText().contains(groupPath.path) && (invoiceableAmount.getText().equals(groupPath.value))); {
	            	checkBox.click();
	             	break;
	            }     
			}  
		}

	}
	
	public static void scrollToCopyrightSpan(SeleniumHelper sh) {
		Actions actions = new Actions(sh.getWD());
		actions.moveToElement(sh.getWebElement(copyrightSpan));
		actions.perform();
	}
	
	public static Date getTrackingPeriodStartDate(Date forDate) {
		Calendar c = Calendar.getInstance();
		c.setTime(forDate);
		
		c.setFirstDayOfWeek(Calendar.MONDAY);
		int currentMonth = c.get(Calendar.MONTH);
		int currentYear = c.get(Calendar.YEAR);
		c.setWeekDate(c.getWeekYear(), c.get(Calendar.WEEK_OF_YEAR), Calendar.MONDAY);
		if(c.get(Calendar.MONTH) < currentMonth || c.get(Calendar.YEAR) < currentYear)
			return clearHoursMinutesAndSeconds(toFirstOfMonth(forDate));
		return clearHoursMinutesAndSeconds(c.getTime());
	}
	
	public static Date clearHoursMinutesAndSeconds(Date d) {
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		c.clear(Calendar.HOUR);
		c.clear(Calendar.MINUTE);
		c.clear(Calendar.SECOND);
		c.clear(Calendar.MILLISECOND);
		return c.getTime();
	}
	
	public static Date toFirstOfMonth(Date d) {
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), 1);
		return c.getTime();
	}
	
	public static <K, V> List<V> getList(K key, Map<K, List<V>> from) {
		if(!from.containsKey(key))
			from.put(key, new ArrayList());
		return from.get(key);
	}
	
	public static String generateLoginUrlFor(String sfUrl, String username, String password) {
		try {
			String encodedUrl = URLEncoder.encode(sfUrl, "UTF-8");
			String encodedUn = URLEncoder.encode(username, "UTF-8");
			String encodedPw = URLEncoder.encode(password, "UTF-8");
			return "https://login.salesforce.com/?startURL=" + encodedUrl + "&un=" + encodedUn + "&pw=" + encodedPw;
		} catch (Exception e) { throw new RuntimeException("failed to generate login url for url: " + sfUrl + ", username: " + username + " and password: " + password); }
	}
	
	public static void setField(Object target, Field field, String serialised) {
		Class t = field.getType();
		try {
			field.set(target, deserialise(field.getType(), serialised));
		} catch(Exception e) { throw new RuntimeException("Failed to set field: " + field.getName() + " of type: " + t.getName() + " on object of type: " + target.getClass().getName()); }
	}
	
	public static <T> T getSystemProperty(Class<T> type, String propertyName) {
		try {
			return deserialise(type, System.getProperty(propertyName));
		} catch(Exception e) { throw new RuntimeException("Failed get system property by the name: " + propertyName + " and type: " + type.getName()); }
	}
	
	public static <T> T deserialise(Class<T> type, String serialised) {
		try {
			if(type == String.class)
				return (T) serialised;
			else if(type == byte.class || type == Byte.class)
				return (T) (serialised != null ? (Byte)Byte.parseByte(serialised) : new Byte((byte) 0));
			else if(type == short.class || type == Short.class)
				return (T) (serialised != null ? (Short)Short.parseShort(serialised) : new Short((short) 0));
			else if(type == int.class || type == Integer.class)
				return (T) (serialised != null ? (Integer)Integer.parseInt(serialised) : (Integer)0);
			else if(type == long.class || type == Long.class)
				return (T) (serialised != null ? (Long)Long.parseLong(serialised) : new Long(0));
			else if(type == float.class || type == Float.class)
				return (T) (serialised != null ? (Float)Float.parseFloat(serialised) : new Float(0));
			else if(type == double.class || type == Double.class)
				return (T) (serialised != null ? (Double)Double.parseDouble(serialised) : new Double(0));
			else if(type == boolean.class || type == Boolean.class)
				return (T) (serialised != null ? (Boolean)Boolean.parseBoolean(serialised) : (Boolean)false);
			else if(type == char.class || type == Character.class)
				return (T) (serialised != null ? (Character)serialised.charAt(0) : new Character('\u0000'));
			else if(type.isEnum()) {
				Class t = type;
			    Class<? extends Enum> enumType = t;
		    	return (T) Enum.valueOf(enumType, serialised);
			}
			else throw new RuntimeException("Can't deserialize value of type: " + type.getName());
		} catch(Exception e) { throw new RuntimeException("Failed to serialise value: " + serialised + " of type: " + type.getName()); }
	}
	
	public static boolean isSimpleType(Class t) {
		if(t == String.class || 
			t == byte.class || t == Byte.class ||
			t == short.class || t == Short.class ||
			t == int.class || t == Integer.class ||
			t == long.class || t == Long.class ||
			t == float.class || t == Float.class ||
			t == double.class || t == Double.class ||
			t == boolean.class || t == Boolean.class ||
			t == char.class || t == Character.class ||
			t.isEnum())
			return true;
		return false;
	}

}
