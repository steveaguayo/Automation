package kimble.automation.domain;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;


public class TestState implements Cloneable {
	
	public String date;
	public String environment;
    public int testIndex = 0; 
	
	public String stage;
	public String url;

	public KimbleData data; 
	
	public Map<String, String> localToRemoteIdsMap;
	public Map<String, String> remoteToLocalIdsMap;

	
	public TestState() {}
	
	public TestState(KimbleData aData, String aDate) {
		date = aDate;
		stage = null;
		url = null;
		data = aData;
		localToRemoteIdsMap = new HashMap();
		remoteToLocalIdsMap = new HashMap();
		this.clone();
	}
	
	public TestState clone() {
		Class cls = TestState.class;
		TestState copy = new TestState();
		for(Field f : cls.getDeclaredFields())
			if(Modifier.isPublic(f.getModifiers()))
				try { f.set(copy, f.get(this)); } catch (Exception e) { throw new RuntimeException("Failed to clone " + cls.getName()); }
		return copy;
	}
}
