package kimble.automation.helpers;

import java.io.StringWriter;
import java.net.URI;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import kimble.automation.domain.ExpenseDetail;
import kimble.automation.domain.ExpenseEntry;
import kimble.automation.domain.TestState;
import kimble.automation.domain.TimeEntry;
import kimble.automation.domain.Timesheet;
import kimble.automation.domain.UserCredentials;
import kimble.automation.domain.imports.TimeEntryWithTimes;
import kimble.automation.domain.mobile.ActivityMob;
import kimble.automation.domain.mobile.ActivityRateBandMob;
import kimble.automation.domain.mobile.AssignmentMob;
import kimble.automation.domain.mobile.ExpenseCategoryMob;
import kimble.automation.domain.mobile.ExpenseClaimMob;
import kimble.automation.domain.mobile.ExpenseItemMob;
import kimble.automation.domain.mobile.PayLoadMob;
import kimble.automation.domain.mobile.PlanTaskAssignmentMob;
import kimble.automation.domain.mobile.SynchroniseWindowMob;
import kimble.automation.domain.mobile.TaxCodeMob;
import kimble.automation.domain.mobile.TaxRateMob;
import kimble.automation.domain.mobile.TimeCategoryMob;
import kimble.automation.domain.mobile.TimeEntryMob;
import kimble.automation.domain.mobile.general.Identifiers.ExpenseItemIdentifier;
import kimble.automation.domain.mobile.general.Identifiers.TimestampedIdentifier;
import kimble.automation.kimbleobjects.classic.TimesheetPageC;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import com.google.common.collect.ImmutableMap;

import static kimble.automation.domain.mobile.general.TnXContext.*;

public class MobileRequests {
	
	//public static ObjectMapper mapper;
	public static Map<String, String> jsonHeaders = ImmutableMap.<String, String>builder().put("content-type", "application/json").build();
	public static Map<String, String> formUrlEncodedHeaders = ImmutableMap.<String, String>builder().put("Content-Type", "application/x-www-form-urlencoded").build();
	
	public static class SessionInfo {
		public String origin;
		public String sessionId;
		public String sid;
		public String cookie;
		public String accessToken;
		public String sfdcInstanceUrl;
		
		PayLoadMob mobileData;
		
		public String toString() {
			try {
				return new StringBuilder().
					append("\nSessionInfo:\n").
					append(getMapper().writerWithDefaultPrettyPrinter().writeValueAsString(this)).
					toString();
			} catch(Exception e) {
				throw new RuntimeException("Failed to serialise SessionInfo", e);
			}
		}
	}
	
	public static  SimpleDateFormat automationDateFormatter = new SimpleDateFormat("dd/MM/yyyy");
	public static  SimpleDateFormat mobileDateFormatter = new SimpleDateFormat("yyyy-MM-dd");
	
	static Map<Object, SessionInfo> sessionInfos = new HashMap();
	static Map<Object, Integer> sendRetries = new HashMap();
//	static List<UserCredentials> credentials = new ArrayList();
	
	public static synchronized SessionInfo getSessionInfo(WebDriver driver, UserCredentials creds) throws Exception {
		return getSessionInfo(driver, creds, false);
	}
	
	public static synchronized SessionInfo getSessionInfo(WebDriver driver, UserCredentials creds, boolean clearCache) throws Exception {
		SeleniumHelper sh = new SeleniumHelper(driver);
		SessionInfo info = sessionInfos.get(creds.username);
		if(clearCache)
			General.relogin(sh, creds);
		else if(info != null)
			return info;
		if(!(driver instanceof JavascriptExecutor))
			throw new RuntimeException("The webdriver doesn't implement the JavascriptExecutor interface");
		info = new SessionInfo();
		info.sid = (String)((JavascriptExecutor) driver).executeScript("return document.cookie.match('sid=([^;]*)')[1];");
		info.sessionId = (String)((JavascriptExecutor) driver).executeScript("return __sfdcSessionId;");
		info.origin = creds.nodeName;
		info.cookie = (String)((JavascriptExecutor) driver).executeScript("return document.cookie");
		
		sessionInfos.put(creds.username, info);
		
		sync(driver, creds, null, info);
		
		//  Check whether there is a connected app setup
		if(creds.consumerKey == null)
			return info;
		
		String uri = "https://login.salesforce.com/services/oauth2/token";
		
		String body = "grant_type=password" + 
				"&client_id=" + URLEncoder.encode(creds.consumerKey, "UTF-8") + 
                "&client_secret=" + URLEncoder.encode(creds.consumerSecret, "UTF-8") +
                "&redirect_uri=" + URLEncoder.encode(creds.callbackUrl, "UTF-8") + 
                "&username=" + URLEncoder.encode(creds.username, "UTF-8") + 
                "&password=" + URLEncoder.encode(creds.password + creds.securityToken, "UTF-8");
		
		HttpResponse res = send("POST", uri, body, driver, creds, formUrlEncodedHeaders);
		handleRequestError(body, res, info);
		
//		if (res.getStatusLine().getStatusCode() != 200)
//	        throw new Exception("[HTTP-01] OAuth 2.0 access token request error. Verify username, password, consumer key, consumer secret, isSandbox?  StatusCode=" +
//	        		res.getStatusLine().getStatusCode() + " statusMsg=" + res.getStatusLine().getReasonPhrase());

		Map<String,String> resBody = getMapper().readValue(res.getEntity().getContent(), Map.class);
		
	    info.accessToken = resBody.get("access_token");
	    info.sfdcInstanceUrl = resBody.get("instance_url");
		
		return info;
	}
	
	public static HttpResponse send(String httpMethod, String uri, Object body, WebDriver driver, UserCredentials creds, Map<String, String>... aHeaders) throws Exception {
		HttpRequestBase req = MobileRequests.buildRequest(httpMethod, uri, body, driver, creds, aHeaders);
		HttpResponse res = executeRequest(req);
		int code = res.getStatusLine().getStatusCode();
		if(code >= 200 && code < 300 )
			return res;
		int retries = sendRetries.get(driver) == null ? 0 : sendRetries.get(driver);
		if(retries >= 3)
			return res;
		sendRetries.put(driver, retries + 1);
		
		System.err.println(buildResponseExceptionMessage(body, res, getSessionInfo(driver, creds)));
		
		// refresh session info and retry if the session code is not success
 		getSessionInfo(driver, creds, true);
		req = MobileRequests.buildRequest(httpMethod, uri, body, driver, creds, aHeaders);
		return executeRequest(req);
	}
	
	public static HttpResponse executeRequest(HttpUriRequest request) throws Exception {
		return HttpClientBuilder.create().build().execute(request);
	}
	
	public static HttpRequestBase buildRequest(String method, String uri, Object body, WebDriver driver, UserCredentials creds, Map<String, String>... aHeaders) throws Exception {
		HttpRequestBase req;
		method = method.trim().toLowerCase();
		
		HttpEntity entity = body != null ? toEntity(body) : new StringEntity("");
		
		if("post".equals(method)) {
			req = new HttpPost();
			((HttpPost)req).setEntity(entity);
		}
		else if("put".equals(method)) {
			req = new HttpPut();
			((HttpPut)req).setEntity(entity);
		}
		else if("get".equals(method))
			req = new HttpGet();
		else if("delete".equals(method))
			req = new HttpDelete();
		else
			throw new RuntimeException("The http method: " + method + " is unsupported");
		
		SessionInfo info = getSessionInfo(driver, creds);
		if(info.accessToken != null)
			req.setHeader("Authorization","Bearer " + info.accessToken);
		else
			req.setHeader("Authorization","Bearer " + info.sessionId);
	    if(aHeaders != null)
	    	for(Map<String, String> h : aHeaders)
	    		for(Entry<String, String> e : h.entrySet())
	    			req.addHeader(e.getKey(), e.getValue());
	    
	    if(uri.startsWith("http://") || uri.startsWith("https://"))
			req.setURI(new URI(uri));
		else
			req.setURI(new URI(info.origin + uri));
		
		return req;
	}
	
	public static HttpEntity toEntity(Object body) throws Exception{
		if(body instanceof String)
			return new StringEntity((String)body);
		return new StringEntity(getMapper().writerWithDefaultPrettyPrinter().writeValueAsString(body));
	}
	
	public static String getRestPrefix() {
		return "/services/apexrest" + (SeleniumHelper.isPackagedOrg() ? "/KimbleOne/v1.0" : "/v1.0");
	}
	
	public static PayLoadMob sync(WebDriver driver, UserCredentials creds, SynchroniseWindowMob window, SessionInfo info) {
		try {
			HttpResponse res = send("post", getRestPrefix() + "/TimeAndExpense", window, driver, creds, jsonHeaders);
			handleRequestError(window, res, info);
			PayLoadMob pl = getMapper().readValue(res.getEntity().getContent(), PayLoadMob.class);
			if(info != null)
				info.mobileData = pl;
			populateReferences(driver, creds, pl);
			return pl;
		} catch(Exception e) {
			throw new RuntimeException("Failed to create a sync request", e);
		}
	}

	// TIME ENTRIES CRUD
	public static boolean deleteTimeEntry(WebDriver driver, UserCredentials creds, TimeEntryMob entry) {
		try {
			HttpResponse res = send("delete", getRestPrefix() + "/TimeEntries?id=" + entry.getId() + "&timestamp=" + entry.getTimestamp(), null, driver, creds, jsonHeaders);
			handleRequestError(null, res, getSessionInfo(driver, creds));
			return res.getStatusLine().getStatusCode() == 200;
		} catch(Exception e) {
			throw new RuntimeException("Failed to create a 'delete time entry' request", e);
		}
	}

	public static TimestampedIdentifier createTimeEntry(WebDriver driver, UserCredentials creds, TimeEntryMob entry) {
		try {
			HttpResponse res = send("post", getRestPrefix() + "/TimeEntries?id=" + entry.getId(), entry, driver, creds, jsonHeaders);
			handleRequestError(entry, res, getSessionInfo(driver, creds));
			return getMapper().readValue(res.getEntity().getContent(), TimestampedIdentifier.class);
		} catch(Exception e) {
			throw new RuntimeException("Failed to create a 'create time entry' request", e);
		}
	}

	public static TimestampedIdentifier updateTimeEntry(WebDriver driver, UserCredentials creds, TimeEntryMob entry) {
		try {
			HttpResponse res = send("put", getRestPrefix() + "/TimeEntries?id=" + entry.getId(), entry, driver, creds, jsonHeaders);
			handleRequestError(entry, res, getSessionInfo(driver, creds));
			return getMapper().readValue(res.getEntity().getContent(), TimestampedIdentifier.class);
		} catch(Exception e) {
			throw new RuntimeException("Failed to create an 'update time entry' request", e);
		}
	}

	// EXPENSE ITEMS CRUD
	public static boolean deleteExpenseItem(WebDriver driver, UserCredentials creds, String id) {
		try {
			HttpResponse res = send("delete", getRestPrefix() + "/ExpenseItems?id=" + id, null, driver, creds, jsonHeaders);
			handleRequestError(null, res, getSessionInfo(driver, creds));
			return res.getStatusLine().getStatusCode() == 200;
		} catch(Exception e) {
			throw new RuntimeException("Failed to create a 'delete expense item' request", e);
		}
	}
	
	public static ExpenseItemIdentifier createExpenseItem(WebDriver driver, UserCredentials creds, ExpenseItemMob item) {
		try {
			HttpResponse res = send("post", getRestPrefix() + "/ExpenseItems?id=" + item.getId(), item, driver, creds, jsonHeaders);
			handleRequestError(item, res, getSessionInfo(driver, creds));
			return getMapper().readValue(res.getEntity().getContent(), ExpenseItemIdentifier.class);
		} catch(Exception e) {
			throw new RuntimeException("Failed to create a 'create expense item' request", e);
		}
	}
	
	public static ExpenseItemIdentifier updateExpenseItem(WebDriver driver, UserCredentials creds, ExpenseItemMob item) {
		try {
			HttpResponse res = send("put", getRestPrefix() + "/ExpenseItems?id=" + item.getId(), item, driver, creds, jsonHeaders);
			handleRequestError(item, res, getSessionInfo(driver, creds));
			return getMapper().readValue(res.getEntity().getContent(), ExpenseItemIdentifier.class);
		} catch(Exception e) {
			throw new RuntimeException("Failed to create an 'update expense item' request", e);
		}
	}
	
	// EXPENSE CLAIMS CRUD
	public static boolean deleteExpenseClaim(WebDriver driver, UserCredentials creds, String id) {
		try {
			HttpResponse res = send("delete", getRestPrefix() + "/ExpenseClaims?id=" + id, null, driver, creds, jsonHeaders);
			handleRequestError(null, res, getSessionInfo(driver, creds));
			return res.getStatusLine().getStatusCode() == 200;
		} catch(Exception e) {
			throw new RuntimeException("Failed to create a 'delete expense claim' request", e);
		}
	}
	
	public static TimestampedIdentifier createExpenseClaim(WebDriver driver, UserCredentials creds, ExpenseClaimMob claim) {
		try {
			HttpResponse res = send("post", getRestPrefix() + "/ExpenseClaims?id=" + claim.getId(), claim, driver, creds, jsonHeaders);
			handleRequestError(claim, res, getSessionInfo(driver, creds));
			return getMapper().readValue(res.getEntity().getContent(), TimestampedIdentifier.class);
		} catch(Exception e) {
			throw new RuntimeException("Failed to create a 'create expense claim' request", e);
		}
	}
	
	public static TimestampedIdentifier updateExpenseClaim(WebDriver driver, UserCredentials creds, ExpenseClaimMob claim) {
		try {
			HttpResponse res = send("put", getRestPrefix() + "/ExpenseClaims?id=" + claim.getId(), claim, driver, creds, jsonHeaders);
			handleRequestError(claim, res, getSessionInfo(driver, creds));
			return getMapper().readValue(res.getEntity().getContent(), TimestampedIdentifier.class);
		} catch(Exception e) {
			throw new RuntimeException("Failed to create an 'update expense claim' request", e);
		}
	}
	
	// EXPENSE CLAIMS SUBMIT
	public static ExpenseClaimMob submitExpenseClaim(WebDriver driver, UserCredentials creds, TimestampedIdentifier identifier) {
		try {
			HttpResponse res = send("post", getRestPrefix() + "/SubmitExpenseClaim", identifier, driver, creds, jsonHeaders);
			handleRequestError(identifier, res, getSessionInfo(driver, creds));
			return getMapper().readValue(res.getEntity().getContent(), ExpenseClaimMob.class);
		} catch(Exception e) {
			throw new RuntimeException("Failed to create a 'submit expense claim' request", e);
		}
	}
	
	// TIMESHEETs SUBMIT
	public static TimeEntryMob[] submitTimeEntries(WebDriver driver, UserCredentials creds,  TimestampedIdentifier... ids) {
		try {
			Object body = new Object() { 
				@SuppressWarnings("unused") public Object TheEntries = ids; };
				
			HttpResponse res = send("post", getRestPrefix() + "/SubmitTimesheet", body, driver, creds, jsonHeaders);
			
			handleRequestError(body, res, getSessionInfo(driver, creds));
			return getMapper().readValue(res.getEntity().getContent(), TimeEntryMob[].class);
		} catch(Exception e) {
			throw new RuntimeException("Failed to create a 'submit timesheets' request", e);
		}
	}
	
	// TIMESHEETS SUBMIT WITH START AND END TIME
	public static String submitTimeEntriesWithStartAndEndTime(WebDriver driver, UserCredentials creds, List<TimeEntryWithTimes> entries) {
		try {
//			(TnXContext.getMapper().writerWithDefaultPrettyPrinter().writeValueAsString(entries));
			HttpResponse res = send("post", getRestPrefix() + "/Import/TimeEntryImport", entries, driver, creds, jsonHeaders);
			handleRequestError(entries, res, getSessionInfo(driver, creds));
			return IOUtils.toString(res.getEntity().getContent(), "UTF-8");//getMapper().readValue(res.getEntity().getContent(), String.class);
		} catch(Exception e) {
			RuntimeException ex = new RuntimeException("Failed to submit the time entries with start and end times", e);
			ex.printStackTrace();
			throw ex;
		}
	}
	
	// Proxy methods from test domain objects to mobile objects
	
	public static void enterTimesheet(WebDriver driver, UserCredentials creds, TestState state, Timesheet ts) {
		try {
			for(int i = 0; i < ts.timeEntries.size(); i++) {
				TimeEntry te = ts.timeEntries.get(i);
				TimeEntryMob tem = convertTimeEntry(driver, creds, state, te);
				tem.setIdentifier(createTimeEntry(driver, creds, tem));
				mapIds(state, te.localId, tem.getId());
			}
			sync(driver, creds, null, getSessionInfo(driver, creds));
		} catch(Exception e) {
			throw new RuntimeException("Failed to enter a timesheet", e);
		}
	}
	
	public static void submitTimesheet(WebDriver driver, UserCredentials creds, TestState state, Timesheet ts) {
		try {
			String[] localIds = new String[ts.timeEntries.size()];
			for(int i = 0; i < ts.timeEntries.size(); i++)
				localIds[i] = ts.timeEntries.get(i).localId;
			submitTimeEntries(driver, creds, getTimeEntryIdentifiers(driver, creds, state, localIds));
			sync(driver, creds, null, getSessionInfo(driver, creds));
		} catch(Exception e) {
			throw new RuntimeException("Failed to submit a timesheet", e);
		}
	}
	
	public static void editTimesheet(WebDriver driver, UserCredentials creds, TestState state, Timesheet ts) {
		try {
			for(int i = 0; i < ts.timeEntries.size(); i++) {
				TimeEntry te = ts.timeEntries.get(i);
				TimeEntryMob tem = convertTimeEntry(driver, creds, state, te);
				tem.setIdentifier(updateTimeEntry(driver, creds, tem));
				mapIds(state, te.localId, tem.getId());
			}
			sync(driver, creds, null, getSessionInfo(driver, creds));
		} catch(Exception e) {
			throw new RuntimeException("Failed to enter a timesheet", e);
		}
	}
	
	public static void deleteTimesheet(WebDriver driver, UserCredentials creds, TestState state, Timesheet ts) {
		try {
			for(int i = 0; i < ts.timeEntries.size(); i++) {
				String remoteId = getRemoteId(state, ts.timeEntries.get(i).localId);
				deleteTimeEntry(driver, creds, getTimeEntryById(driver, creds, remoteId));
			}
			sync(driver, creds, null, getSessionInfo(driver, creds));
		} catch(Exception e) {
			throw new RuntimeException("Failed to enter a timesheet", e);
		}
	}
	
	public static void enterExpenseClaim(WebDriver driver, UserCredentials creds, TestState state, ExpenseEntry ec) {
		try {
			ExpenseClaimMob ecm = convertExpenseClaim(driver, creds, state, ec);
			TimestampedIdentifier identifier = createExpenseClaim(driver, creds, ecm);
			ecm.setIdentifier(identifier);
			mapIds(state, ec.localId, ecm.getId());
			sync(driver, creds, null, getSessionInfo(driver, creds));
		} catch(Exception e) {
			throw new RuntimeException("Failed to enter a claim", e);
		}
	}
	
	public static void submitExpenseClaim(WebDriver driver, UserCredentials creds, TestState state, ExpenseEntry ec) {
		try {
			submitExpenseClaim(driver, creds, getExpenseClaimById(driver, creds, getRemoteId(state, ec.localId)).getIdentifier());
			sync(driver, creds, null, getSessionInfo(driver, creds));
		} catch(Exception e) {
			throw new RuntimeException("Failed to submit a claim", e);
		}
	}
	
	public static void editExpenseClaim(WebDriver driver, UserCredentials creds, TestState state, ExpenseEntry ec) {
		try {
			ExpenseClaimMob ecm = convertExpenseClaim(driver, creds, state, ec);
			updateExpenseClaim(driver, creds, ecm);
			sync(driver, creds, null, getSessionInfo(driver, creds));
		} catch(Exception e) {
			throw new RuntimeException("Failed to edit a claim", e);
		}
	}
	
	public static void deleteExpenseClaim(WebDriver driver, UserCredentials creds, TestState state, ExpenseEntry ec) {
		try {
			deleteExpenseClaim(driver, creds, getRemoteId(state, ec.localId));
			sync(driver, creds, null, getSessionInfo(driver, creds));
		} catch(Exception e) {
			throw new RuntimeException("Failed to delete a claim", e);
		}
	}
	
	public static void validateExpenseClaim(SeleniumHelper sh, UserCredentials creds, TestState state, ExpenseEntry ec) {
		try {
			new TimesheetPageC(sh).validateMobileExpenseClaim(state, ec, getExpenseClaimById(sh.getWD(), creds, getRemoteId(state, ec.localId)));
		} catch(Exception e) {
			throw new RuntimeException("Failed to validate a claim", e);
		}
	}
	
	public static void addExpenseItem(WebDriver driver, UserCredentials creds, TestState state, ExpenseEntry ee, ExpenseDetail ei) {
		try {
			ExpenseClaimMob ecm = convertExpenseClaim(driver, creds, state, ee);
			ExpenseItemMob eim = convertExpenseItem(driver, creds, state, ei, ecm);
			ExpenseItemIdentifier identifier = createExpenseItem(driver, creds, eim);
			eim.setIdentifier(identifier);
			mapIds(state, ei.localId, eim.getId());
			sync(driver, creds, null, getSessionInfo(driver, creds));
		} catch(Exception e) {
			throw new RuntimeException("Failed to add an expense item", e);
		}
	}
	
	public static void editExpenseItem(WebDriver driver, UserCredentials creds, TestState state, ExpenseEntry ee, ExpenseDetail ei) {
		try {
			ExpenseClaimMob ecm = convertExpenseClaim(driver, creds, state, ee);
			ExpenseItemMob eim = convertExpenseItem(driver, creds, state, ei, ecm);
			updateExpenseItem(driver, creds, eim);
			sync(driver, creds, null, getSessionInfo(driver, creds));
		} catch(Exception e) {
			throw new RuntimeException("Failed to add an expense item", e);
		}
	}
	
	public static void deleteExpenseItem(WebDriver driver, UserCredentials creds, TestState state, ExpenseDetail ei) {
		try {
			deleteExpenseItem(driver, creds, getRemoteId(state, ei.localId));
			sync(driver, creds, null, getSessionInfo(driver, creds));
		} catch(Exception e) {
			throw new RuntimeException("Failed to add an expense item", e);
		}
	}
	
	// Conversion methods from test domain objects to mobile objects
	
	public static TimeEntryMob convertTimeEntry(WebDriver driver, UserCredentials creds, TestState state, TimeEntry te) throws Exception {
		TimeEntryMob tem;
		
		String remoteId = null;
		if(te.localId != null)
			remoteId = getRemoteId(state, te.localId);

		if(remoteId != null)
			tem = getTimeEntryById(driver, creds, remoteId);
		else
			tem = new TimeEntryMob();

		if(te.startDate != null) tem.setEntryDate(convertToMobileDateFormat(te.startDate));
		if(te.entryUnits != null) tem.setUnits(Double.parseDouble(te.entryUnits));
		if(te.task != null) {
			AssignmentMob assignment = getAssignment(driver, creds, te.task);
			tem.setAssignment(assignment);
			if(te.rateBand != null)
				tem.setActivityRateBand(getActivityRateBand(driver, creds, te.rateBand, assignment.getId()));
		}
		if(te.notes != null)
			tem.setNotes(te.notes);
		
		return tem;
	}
	
	public static ExpenseClaimMob convertExpenseClaim(WebDriver driver, UserCredentials creds, TestState state, ExpenseEntry ee) throws Exception {
		ExpenseClaimMob claim;
		
		String remoteId = null;
		if(ee.localId != null)
			remoteId = getRemoteId(state, ee.localId);
		
		if(remoteId != null)
			claim = getExpenseClaimById(driver, creds, remoteId);
		else
			claim = new ExpenseClaimMob();
		
		if(ee.name != null) claim.setName(ee.name);
		if(ee.activityName != null) {
			String activityId = getAssignment(driver, creds, ee.activityName).getActivity().getId();
			claim.setResourcedActivityId(activityId);
		}

		if(ee.expenseDetails != null)
			for(ExpenseDetail ed : ee.expenseDetails)
				claim.addExpenseItem(convertExpenseItem(driver, creds, state, ed, claim));
		
		return claim;
	}
	
	public static ExpenseItemMob convertExpenseItem(WebDriver driver, UserCredentials creds, TestState state, ExpenseDetail ei, ExpenseClaimMob claimMob) throws Exception {
		ExpenseItemMob eim;
		
		String remoteId = null;
		if(ei.localId != null)
			remoteId = getRemoteId(state, ei.localId);
		
		if(remoteId != null)
			eim = getExpenseItemById(driver, creds, remoteId);
		else 
			eim = new ExpenseItemMob();
		
		eim.setExpenseClaim(claimMob);
		eim.setExchangeRateConversionFactor(1);
		if(ei.incurredDate != null) eim.setIncurredDate(convertToMobileDateFormat(ei.incurredDate));
		if(ei.category != null) eim.setActivityExpenseCategory(getExpenseCategory(driver, creds, ei.category, claimMob.getResourcedActivityId()));
		if(ei.currency != null) eim.setCurrency(ei.currency);
		if(ei.amount != null) eim.setIncurredAmount(Double.parseDouble(ei.amount));
		if(ei.notes != null) eim.setNotes(ei.notes);
		if(ei.incurredGrossAmount != null) eim.setExpenseGrossAmount(Double.parseDouble(ei.incurredGrossAmount));
		if(ei.incurredCurrencyTaxAmount != null) eim.setIncurredTaxAmount(Double.parseDouble(ei.incurredCurrencyTaxAmount));
		else eim.setIncurredTaxAmount(0.0);
		if(ei.startLocation != null) eim.setStartLocation(ei.startLocation);
		if(ei.endLocation != null) eim.setEndLocation(ei.endLocation);
		
		return eim;
	}
	
	// Mobile data 'query'-methods
	
	public static TimeEntryMob getTimeEntryById(WebDriver driver, UserCredentials creds, String id) throws Exception {
		SessionInfo info = getSessionInfo(driver, creds);
		
		for(TimeEntryMob te : info.mobileData.TimeEntries)
			if(te.getId().equals(id) && !te.getDeleted())
				return te;
		return null;
	}
	
	public static TimeEntryMob[] getTimeEntries(WebDriver driver, UserCredentials creds, TestState ts, String[] localIds) throws Exception {
		String[] remoteIds = getRemoteIds(ts, localIds);
		TimeEntryMob[] entries = new TimeEntryMob[localIds.length];
		
		for(int i = 0; i < remoteIds.length; i++)
			entries[i] = getTimeEntryById(driver, creds, remoteIds[i]);
		return entries;
	}
	
	public static TimestampedIdentifier[] getTimeEntryIdentifiers(WebDriver driver, UserCredentials creds, TestState ts, String[] localIds) throws Exception {
		String[] remoteIds = getRemoteIds(ts, localIds);
		TimestampedIdentifier[] identifiers = new TimestampedIdentifier[localIds.length];
		
		for(int i = 0; i < remoteIds.length; i++)
			identifiers[i] = getTimeEntryById(driver, creds, remoteIds[i]).getIdentifier();
		return identifiers;
	}
	
	public static AssignmentMob getAssignment(WebDriver driver, UserCredentials creds, String name) throws Exception {
		SessionInfo info = getSessionInfo(driver, creds);
		
		for(AssignmentMob a : info.mobileData.Assignments)
			if(!a.getDeleted() && a.getName().equals(name))
				return a;
		return null;
	}
	
	public static AssignmentMob getAssignmentById(WebDriver driver, UserCredentials creds, String id) throws Exception {
		SessionInfo info = getSessionInfo(driver, creds);
		
		for(AssignmentMob a : info.mobileData.Assignments)
			if(a.getId().equals(id))
				return a;
		return null;
	}
	
	public static PlanTaskAssignmentMob getPlanTaskAssignment(WebDriver driver, UserCredentials creds, String name) throws Exception {
		SessionInfo info = getSessionInfo(driver, creds);
		
		for(PlanTaskAssignmentMob pta : info.mobileData.PlanTaskAssignments)
			if(pta.getName().equals(name))
				return pta;
		return null;
	}
	
	public static PlanTaskAssignmentMob getPlanTaskAssignmentById(WebDriver driver, UserCredentials creds, String id) throws Exception {
		SessionInfo info = getSessionInfo(driver, creds);
		
		for(PlanTaskAssignmentMob pta : info.mobileData.PlanTaskAssignments)
			if(pta.getId().equals(id))
				return pta;
		return null;
	}
	
	public static ActivityRateBandMob getActivityRateBand(WebDriver driver, UserCredentials creds, String name, String assignmentId) throws Exception {
		SessionInfo info = getSessionInfo(driver, creds);
		
		for(ActivityRateBandMob arb : info.mobileData.ActivityRateBands)
			if(!arb.getDeleted() && arb.getName().equals(name) && arb.getAssignmentId().equals(assignmentId))
				return arb;
		return null;
	}
	
	public static ActivityRateBandMob getActivityRateBandById(WebDriver driver, UserCredentials creds, String id) throws Exception {
		SessionInfo info = getSessionInfo(driver, creds);
		
		for(ActivityRateBandMob arb : info.mobileData.ActivityRateBands)
			if(arb.getId().equals(id)) {
				return arb;
			}
		return null;
	}
	
//	public static ExpenseClaimMob getExpenseClaim(WebDriver driver, UserCredentials creds, String name) throws Exception {
//		SessionInfo info = getSessionInfo(driver, creds);
//		
//		for(ExpenseClaimMob ecm : info.mobileData.ExpenseClaims)
//			if(ecm.getName().equals(name))
//				return ecm;
//		return null;
//	}
	
	public static ExpenseClaimMob getExpenseClaimById(WebDriver driver, UserCredentials creds, String id) throws Exception {
		SessionInfo info = getSessionInfo(driver, creds);
		
		for(ExpenseClaimMob ecm : info.mobileData.ExpenseClaims)
			if(ecm.getId().equals(id) && !ecm.getDeleted())
				return ecm;
		return null;
	}
	
	public static ExpenseCategoryMob getExpenseCategory(WebDriver driver, UserCredentials creds, String name, String activityId) throws Exception {
		SessionInfo info = getSessionInfo(driver, creds);
		
		for(ExpenseCategoryMob ecm : info.mobileData.ExpenseCategories)
			if(!ecm.getDeleted() && activityId.equals(ecm.getActivity().getId()) && ecm.getName().equals(name))
				return ecm;
		return null;
	}
	
	public static ExpenseCategoryMob getExpenseCategoryById(WebDriver driver, UserCredentials creds, String id) throws Exception {
		SessionInfo info = getSessionInfo(driver, creds);
		
		for(ExpenseCategoryMob ecm : info.mobileData.ExpenseCategories)
			if(id.equals(ecm.getId()))
				return ecm;
		return null;
	}
	
	public static TimeCategoryMob getTimeCategoryById(WebDriver driver, UserCredentials creds, String id) throws Exception {
		SessionInfo info = getSessionInfo(driver, creds);
		
		for(TimeCategoryMob tcm : info.mobileData.TimeCategories)
			if(id.equals(tcm.getId()))
				return tcm;
		return null;
	}
	
	public static TaxRateMob getTaxRateById(WebDriver driver, UserCredentials creds, String id) throws Exception {
		SessionInfo info = getSessionInfo(driver, creds);
		
		for(TaxRateMob trm : info.mobileData.TaxRates)
			if(id.equals(trm.getId()))
				return trm;
		return null;
	}
	
	public static TaxCodeMob getTaxCodeById(WebDriver driver, UserCredentials creds, String id) throws Exception {
		SessionInfo info = getSessionInfo(driver, creds);
		
		for(TaxCodeMob tcm : info.mobileData.TaxCodes)
			if(id.equals(tcm.getId()))
				return tcm;
		return null;
	}
	
	public static ActivityMob getActivityById(WebDriver driver, UserCredentials creds, String id) throws Exception {
		SessionInfo info = getSessionInfo(driver, creds);
		
		for(ActivityMob a : info.mobileData.Activities)
			if(a.getId().equals(id))
				return a;
		return null;
	}
	
	public static ExpenseItemMob getExpenseItemById(WebDriver driver, UserCredentials creds, String id) throws Exception {
		SessionInfo info = getSessionInfo(driver, creds);
		
		for(ExpenseClaimMob c : info.mobileData.ExpenseClaims)
			for(ExpenseItemMob ei : c.getExpenseItems())
				if(ei.getId().equals(id) && !ei.getDeleted())
					return ei;
		return null;
	}
	
	public static void mapIds(TestState ts, String localId, String remoteId) {
		ts.localToRemoteIdsMap.put(localId, remoteId);
		ts.remoteToLocalIdsMap.put(remoteId, localId);
	}
	
	public static String getLocalId(TestState ts, String remoteId) {
		return ts.remoteToLocalIdsMap.get(remoteId);
	}
	
	public static String getRemoteId(TestState ts, String localId) {
		return ts.localToRemoteIdsMap.get(localId);
	}
	
	public static String[] getLocalIds(TestState ts, String... remoteIds) {
		String[] localIds = new String[remoteIds.length];
		for(int i = 0; i < remoteIds.length; i++)
			localIds[i] = ts.remoteToLocalIdsMap.get(remoteIds[i]);
		return localIds;
	}
	
	public static String[] getRemoteIds(TestState ts, String... localIds) {
		String[] remoteIds = new String[localIds.length];
		for(int i = 0; i < localIds.length; i++)
			remoteIds[i] = ts.localToRemoteIdsMap.get(localIds[i]);
		return remoteIds;
	}
	
	public static void handleRequestError(Object body, HttpResponse res, SessionInfo info) throws Exception {
		int status = res.getStatusLine().getStatusCode();
		if(status < 200 || status > 299 )
			throw new Exception(buildResponseExceptionMessage(body, res, info));
	}
	
	public static String buildResponseExceptionMessage(Object body, HttpResponse res, SessionInfo info) throws Exception{
		StringWriter sw = new StringWriter();
		IOUtils.copy(new java.io.InputStreamReader(res.getEntity().getContent(), "UTF-8"), sw);
		String message = "\nBody:\n" + getMapper().writerWithDefaultPrettyPrinter().writeValueAsString(body) +
				"\n\nResponse:\n\t" + res.getStatusLine() +
				"\n\theaders: ";

		for(Header header : res.getAllHeaders())
			message += "\n\t[" + header.getName() + " : " + header.getValue() + "]";
			
		message += "\n\tentity: " + sw.toString();
		sw.close();
		
		message += "\n" + info.toString();
		return message;
	}
	
	public static void populateReferences(WebDriver driver, UserCredentials creds, PayLoadMob pl) {
		try {
			for(ActivityMob a : pl.Activities) {
				if(a.getActivityRateBands() != null)
					for(int i = 0; i < a.getActivityRateBands().size(); i++)
						a.getActivityRateBands().set(i, getActivityRateBandById(driver, creds, a.getActivityRateBands().get(i).getId()));
				if(a.getAssignments() != null)
					for(int i = 0; i < a.getAssignments().size(); i++)
						a.getAssignments().set(i, getAssignmentById(driver, creds, a.getAssignments().get(i).getId()));
				if(a.getExpenseCategories() != null)
					for(int i = 0; i < a.getExpenseCategories().size(); i++)
						a.getExpenseCategories().set(i, getExpenseCategoryById(driver, creds, a.getExpenseCategories().get(i).getId()));
				if(a.getTimeCategories() != null)
					for(int i = 0; i < a.getTimeCategories().size(); i++)
						a.getTimeCategories().set(i, getTimeCategoryById(driver, creds, a.getTimeCategories().get(i).getId()));
			}
			for(ActivityRateBandMob arb : pl.ActivityRateBands)
				if(arb.getActivity() != null)
					arb.setActivity(getActivityById(driver, creds, arb.getActivity().getId()));
			for(AssignmentMob a : pl.Assignments) {
				if(a.getActivity() != null)
					a.setActivity(getActivityById(driver, creds, a.getActivity().getId()));
				if(a.getPlanTaskAssignments() != null)
					for(int i = 0; i < a.getPlanTaskAssignments().size(); i++)
						a.getPlanTaskAssignments().set(i, getPlanTaskAssignmentById(driver, creds, a.getPlanTaskAssignments().get(i).getId()));
				if(a.getTimeEntries() != null)
					for(int i = 0; i < a.getTimeEntries().size(); i++)
						a.getTimeEntries().set(i, getTimeEntryById(driver, creds, a.getTimeEntries().get(i).getId()));
			}
			for(ExpenseCategoryMob ec : pl.ExpenseCategories)
				if(ec.getActivity() != null)
					ec.setActivity(getActivityById(driver, creds, ec.getActivity().getId()));
			for(ExpenseClaimMob ec : pl.ExpenseClaims)
				if(ec.getExpenseItems() != null)
					for(ExpenseItemMob ei : ec.getExpenseItems()) {
						ei.setTaxCode(getTaxCodeById(driver, creds, ei.getTaxCode().getId()));
						ei.setExpenseClaim(getExpenseClaimById(driver, creds, ei.getExpenseClaim().getId()));
						ei.setActivityExpenseCategory(getExpenseCategoryById(driver, creds, ei.getActivityExpenseCategory().getId()));
					}
			for(TimeEntryMob fte : pl.ForecastTimeEntries) {
				if(fte.getActivityRateBand() != null)
					fte.setActivityRateBand(getActivityRateBandById(driver, creds, fte.getActivityRateBand().getId()));
				if(fte.getAssignment() != null)
					fte.setAssignment(getAssignmentById(driver, creds, fte.getAssignment().getId()));
				if(fte.getPlanTaskAssignment() != null)
					fte.setPlanTaskAssignment(getPlanTaskAssignmentById(driver, creds, fte.getPlanTaskAssignment().getId()));
			}
			for(PlanTaskAssignmentMob ptam : pl.PlanTaskAssignments)
				if(ptam.getAssignment() != null)
					ptam.setAssignment(getAssignmentById(driver, creds, ptam.getAssignment().getId()));
			for(TaxCodeMob tcm : pl.TaxCodes)
				if(tcm.getTaxRates() != null)
					for(int i = 0; i < tcm.getTaxRates().size(); i++)
						tcm.getTaxRates().set(i, getTaxRateById(driver, creds, tcm.getTaxRates().get(i).getId()));
			for(TaxRateMob trm : pl.TaxRates)
				if(trm.getTaxCode() != null)
					trm.setTaxCode(getTaxCodeById(driver, creds, trm.getTaxCode().getId()));
			for(TimeCategoryMob tcm : pl.TimeCategories)
				if(tcm.getActivity() != null)
					tcm.setActivity(getActivityById(driver, creds, tcm.getActivity().getId()));
			for(TimeEntryMob fte : pl.TimeEntries) {
				if(fte.getActivityRateBand() != null)
					fte.setActivityRateBand(getActivityRateBandById(driver, creds, fte.getActivityRateBand().getId()));
				if(fte.getAssignment() != null)
					fte.setAssignment(getAssignmentById(driver, creds, fte.getAssignment().getId()));
				if(fte.getPlanTaskAssignment() != null)
					fte.setPlanTaskAssignment(getPlanTaskAssignmentById(driver, creds, fte.getPlanTaskAssignment().getId()));
			}
		} catch(Exception e) {
			throw new RuntimeException("Failed to populate payload references", e);
		}
	}
	
	public static String convertToMobileDateFormat(String automationFormat) {
		try {
			return mobileDateFormatter.format(automationDateFormatter.parse(automationFormat));
		} catch(Exception e) {
			throw new RuntimeException("Failed to convert the date string from automation format to mobile format", e);
		}
	}
	
	public static String convertToAutomationDateFormat(String mobileFormat) {
		try {
			return automationDateFormatter.format(mobileDateFormatter.parse(mobileFormat));
		} catch(Exception e) {
			throw new RuntimeException("Failed to convert the date string from mobile format to automation format", e);
		}
	}
}
