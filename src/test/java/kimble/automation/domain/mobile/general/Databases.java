package kimble.automation.domain.mobile.general;

import java.math.BigInteger;

import kimble.automation.domain.mobile.general.Entities.Entity;
import kimble.automation.domain.mobile.general.Identifiers.Identifier;

public class Databases {

	public interface Db {
		
		<T extends Entity<T, I>, I extends Identifier> T readOrCreate(Class<T> aType, I aId) throws Exception;
		
		String generateId();
		
	}
	
	public static abstract class KimbleDbHelper implements Db{

		BigInteger runningId;

		public KimbleDbHelper(BigInteger aRunningId) {
			runningId = aRunningId != null ? aRunningId : new BigInteger("10000000000000");
		}


		public <T extends Entity<T, I>, I extends Identifier> T readOrCreate(Class<T> aType, I aId) throws Exception {
			T aObj = aType.newInstance();
			aObj.setId(aId != null &&  aId.getId() != null ? aId.getId() : generateId());
			return aObj;
		}

	}
}
