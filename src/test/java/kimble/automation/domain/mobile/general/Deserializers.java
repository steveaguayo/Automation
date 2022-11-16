package kimble.automation.domain.mobile.general;

import java.io.IOException;
import java.math.BigInteger;

import kimble.automation.domain.mobile.ActivityMob;
import kimble.automation.domain.mobile.ActivityRateBandMob;
import kimble.automation.domain.mobile.AssignmentMob;
import kimble.automation.domain.mobile.ExchangeRateMob;
import kimble.automation.domain.mobile.ExpenseCategoryMob;
import kimble.automation.domain.mobile.ExpenseClaimMob;
import kimble.automation.domain.mobile.ExpenseItemMob;
import kimble.automation.domain.mobile.PlanTaskAssignmentMob;
import kimble.automation.domain.mobile.TaxCodeMob;
import kimble.automation.domain.mobile.TaxRateMob;
import kimble.automation.domain.mobile.TimeCategoryMob;
import kimble.automation.domain.mobile.TimeEntryMob;
import kimble.automation.domain.mobile.TrackingPeriodMob;
import kimble.automation.domain.mobile.general.Entities.Entity;
import kimble.automation.domain.mobile.general.Identifiers.Identifier;
import kimble.automation.domain.mobile.general.Identifiers.StringIdentifier;
import kimble.automation.domain.mobile.general.Identifiers.StringIdentifierImpl;
import kimble.automation.domain.mobile.general.Identifiers.TimestampedIdentifier;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

public class Deserializers {
	
	public static final String temp_id_prefix = "new_";
	public static BigInteger runningId;

	public static synchronized String generateId() {
		if(runningId == null)
			runningId = new BigInteger("10000000000000");
		runningId = runningId.add(Functions.one);
		return temp_id_prefix + runningId.toString();
	}
	// General
	
	@SuppressWarnings("rawtypes")
	public static abstract class FromIdentifierDeserialiser<T extends Entity, I extends Identifier> extends JsonDeserializer<T>{
		
		abstract Class<T> getType();
		abstract Class<I> getIdType();
		
		@SuppressWarnings("unchecked")
		@Override
		public T deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
			I identifier = TnXContext.getMapper().readValue(p, getIdType());

			T te;
			try {
				te = getType().newInstance();
				te.setId(identifier != null &&  identifier.getId() != null ? identifier.getId() : generateId());
			} catch (Exception e) {
				throw new IOException("Failed create a new " + getType().getSimpleName(), e);
			}
			try {
				te.setIdentifier(identifier);
			} catch(Exception e) { throw new RuntimeException("Failed to set the Identifier of " + te.getClass().getName()); }
			return te;
		}

	}
	
	public static abstract class FromStringIdDeserializer<T extends Entity<T, StringIdentifier>> extends FromIdentifierDeserialiser<T, StringIdentifier> {
		public Class<StringIdentifier> getIdType() { return StringIdentifier.class; }
	}
	
	@SuppressWarnings("rawtypes")
	public static abstract class FromTimestampedIdDeserializer<T extends Entity> extends FromIdentifierDeserialiser<T, TimestampedIdentifier> {
		public Class<TimestampedIdentifier> getIdType() { return TimestampedIdentifier.class; }
	}

	public static class StringIdDeserializer extends JsonDeserializer<StringIdentifier> {
	
		@Override
		public StringIdentifier deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
			JsonNode node = p.getCodec().readTree(p);
			return new StringIdentifierImpl(node.asText());
		}

		@Override
		public StringIdentifier getNullValue() { return new StringIdentifierImpl(null); }
	}
	
	// Specific
	
	public static class ActivityFromIdDeserializer extends FromStringIdDeserializer<ActivityMob> { 
		@Override Class<ActivityMob> getType() { return ActivityMob.class; } 
	}
	
	public static class ActivityRateBandFromIdDeserializer extends FromStringIdDeserializer<ActivityRateBandMob> { 
		@Override Class<ActivityRateBandMob> getType() { return ActivityRateBandMob.class; } 
	}
	
	public static class AssignmentFromIdDeserializer extends FromStringIdDeserializer<AssignmentMob> { 
		@Override Class<AssignmentMob> getType() { return AssignmentMob.class; } 
	}
	
	public static class ExchangeRateFromIdDeserializer extends FromStringIdDeserializer<ExchangeRateMob> { 
		@Override Class<ExchangeRateMob> getType() { return ExchangeRateMob.class; } 
	}
	
	public static class ExpenseCategoryFromIdDeserializer extends FromStringIdDeserializer<ExpenseCategoryMob> { 
		@Override Class<ExpenseCategoryMob> getType() { return ExpenseCategoryMob.class; } 
	}
	
	public static class ExpenseClaimFromIdDeserializer extends FromTimestampedIdDeserializer<ExpenseClaimMob> { 
		@Override Class<ExpenseClaimMob> getType() { return ExpenseClaimMob.class; } 
	}
	
	public static class ExpenseItemFromIdDeserializer extends FromTimestampedIdDeserializer<ExpenseItemMob> { 
		@Override Class<ExpenseItemMob> getType() { return ExpenseItemMob.class; } 
	}
	
	public static class PlanTaskAssignmentFromIdDeserializer extends FromStringIdDeserializer<PlanTaskAssignmentMob> { 
		@Override Class<PlanTaskAssignmentMob> getType() { return PlanTaskAssignmentMob.class; } 
	}
	
//	public static class SynchroniseWindowFromIdDeserializer extends FromStringIdDeserializer<SynchroniseWindow> { 
//		@Override Class<SynchroniseWindow> getType() { return SynchroniseWindow.class; } 
//	}
	
	public static class TaxCodeFromIdDeserializer extends FromStringIdDeserializer<TaxCodeMob> { 
		@Override Class<TaxCodeMob> getType() { return TaxCodeMob.class; } 
	}
	
	public static class TaxRateFromIdDeserializer extends FromStringIdDeserializer<TaxRateMob> { 
		@Override Class<TaxRateMob> getType() { return TaxRateMob.class; } 
	}
	
	public static class TimeCategoryFromIdDeserializer extends FromStringIdDeserializer<TimeCategoryMob> { 
		@Override Class<TimeCategoryMob> getType() { return TimeCategoryMob.class; } 
	}
	
	public static class TimeEntryFromIdDeserializer extends FromTimestampedIdDeserializer<TimeEntryMob> { 
		@Override Class<TimeEntryMob> getType() { return TimeEntryMob.class; } 
	}
	
	public static class TrackingPeriodFromIdDeserializer extends FromStringIdDeserializer<TrackingPeriodMob> { 
		@Override Class<TrackingPeriodMob> getType() { return TrackingPeriodMob.class; } 
	}
	
	// Special cases
	
	public static class ForecastTimeEntryDeserializer extends JsonDeserializer<TimeEntryMob> {

		static class ForecastTimeEntry extends TimeEntryMob{
			public ForecastTimeEntry() throws Exception {
				this.setStatus(Status.Forecast);
				this.setId(generateId());
			}
			
			@JsonProperty("ForecastDate")
			public void setForecastDate(String aForecastDate) { this.setEntryDate(aForecastDate); }
			
			@JsonProperty("IsSuggested")
			public void setIsSuggested(String aSuggestion) { this.setEntryDate(aSuggestion); }
			
			@JsonProperty("DisplayLabel")
			public void setDisplayLabel(String aDisplayLabel) { this.setEntryDate(aDisplayLabel); }
		}

		@Override
		public TimeEntryMob deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
			return TnXContext.getMapper().readValue(p, ForecastTimeEntry.class);
		}
	}
	
}
