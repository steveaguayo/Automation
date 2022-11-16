package kimble.automation.helpers;

import java.util.Map;
import java.util.TreeMap;

import kimble.automation.domain.UserCredentials;

public class CredentialsPool {
	
	static final Map<String, UserCredentials> credentialsSet = new TreeMap();
	static final Map<String, UserCredentials> credentialsInUse = new TreeMap();

	public static synchronized void addCredentials(UserCredentials... credsList) {
		for(UserCredentials uc : credsList)
			credentialsSet.put(uc.environment, uc);
	}
	
	public static synchronized UserCredentials allocateCredentialsFor(String scenario) {
		if(credentialsSet.size() == 0)
			throw new RuntimeException("There are no more free credentials to allocate for scenario: " + scenario + " please check that you have at least as many credentilas defined as you have test threads");
		if(credentialsInUse.containsKey(scenario))
			throw new RuntimeException("Credentials have already been allocated for the scenario: " + scenario);
		String targetEnvironment = credentialsSet.keySet().iterator().next();
		UserCredentials creds = credentialsSet.remove(targetEnvironment);
		credentialsInUse.put(scenario, creds);
		return creds;
	}
	
	public static synchronized UserCredentials freeCredentialsFor(String scenario) {
		UserCredentials creds = credentialsInUse.remove(scenario);
		if(creds == null) {
			System.out.println("No credentials have been or are longer allocated for the scenario " + scenario + " so no credentials were freed");
			return null;
		}
		credentialsSet.put(creds.environment, creds);
		return creds;
	}
	
	public static synchronized UserCredentials getCredentials(String scenario) {
		return credentialsInUse.get(scenario);
	}
	
	public static synchronized String[] getEnvironments() {
		String[] environments = new String[credentialsSet.size() + credentialsInUse.size()];
		int i = 0;
		for(UserCredentials uc : credentialsSet.values()) {
			environments[i] = uc.environment;
			i++;
		}
		for(UserCredentials uc : credentialsInUse.values()) {
			environments[i] = uc.environment;
			i++;
		}
		return environments;
	}
}
