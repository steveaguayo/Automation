package kimble.automation.domain.mobile.general;

import java.io.IOException;

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
import kimble.automation.domain.mobile.general.Identifiers.*;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

public class Serializers {

	// Static methods

	public static void serialiseIdString(JsonGenerator aJsonGenerator, String aId) throws IOException {
		if(aId.startsWith(Deserializers.temp_id_prefix))
			aJsonGenerator.writeNull();
		else
			aJsonGenerator.writeString(aId);
	}

	// General

	/**
	 * This method handles the special case of serialising a local temporary id as null
	 */
	public static class IdStringSerializer extends JsonSerializer<String> {

		@Override
		public void serialize(String aIdString, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
			serialiseIdString(jsonGenerator, aIdString);
		}
	}

	/**
	 * This method handles the special case of serialising a local temporary id as null
	 */
	public static class StringIdentifierSerializer extends JsonSerializer<StringIdentifier> {

		@Override
		public void serialize(StringIdentifier aIdentifier, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
			if(aIdentifier.getId() != null && aIdentifier.getId().startsWith(Deserializers.temp_id_prefix))
				jsonGenerator.writeNull();
			else
				jsonGenerator.writeObject(aIdentifier.getId());
		}

		@Override
		public void serializeWithType(StringIdentifier aIdentifier, JsonGenerator jsonGenerator, SerializerProvider serializerProvider, TypeSerializer typeSer) throws IOException, JsonProcessingException {
			serialize(aIdentifier, jsonGenerator, serializerProvider);
		}
	}

	/**
	 * This method handles the special case of serialising a local temporary id as null
	 */
	public static class TimestampedIdentifierSerializer extends JsonSerializer<TimestampedIdentifier> {

		@Override
		public void serialize(TimestampedIdentifier aIdentifier, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
			jsonGenerator.writeStartObject();
			jsonGenerator.writeFieldName("Id");
			if(aIdentifier.getId() != null && aIdentifier.getId().startsWith(Deserializers.temp_id_prefix))
				jsonGenerator.writeNull();
			else
				jsonGenerator.writeObject(aIdentifier.getId());
			jsonGenerator.writeFieldName("Timestamp");
			jsonGenerator.writeObject(aIdentifier.getTimestamp());
			jsonGenerator.writeEndObject();
		}

		@Override
		public void serializeWithType(TimestampedIdentifier aIdentifier, JsonGenerator jsonGenerator, SerializerProvider serializerProvider, TypeSerializer typeSer) throws IOException, JsonProcessingException {
			serialize(aIdentifier, jsonGenerator, serializerProvider);
		}
	}

	/**
	 * This method handles the special case of serialising a local temporary id as null
	 */
	public static class ExpenseItemIdentifierSerializer extends JsonSerializer<ExpenseItemIdentifier> {

		@Override
		public void serialize(ExpenseItemIdentifier aIdentifier, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
			jsonGenerator.writeStartObject();
			jsonGenerator.writeFieldName("Id");
			if(aIdentifier.getId() != null && aIdentifier.getId().startsWith(Deserializers.temp_id_prefix))
				jsonGenerator.writeNull();
			else
				jsonGenerator.writeObject(aIdentifier.getId());
			jsonGenerator.writeFieldName("Timestamp");
			jsonGenerator.writeObject(aIdentifier.getTimestamp());
			jsonGenerator.writeFieldName("ExpenseClaim");
			jsonGenerator.writeObject(aIdentifier.getExpenseClaimIdentifier());
			jsonGenerator.writeEndObject();
		}

		@Override
		public void serializeWithType(ExpenseItemIdentifier aIdentifier, JsonGenerator jsonGenerator, SerializerProvider serializerProvider, TypeSerializer typeSer) throws IOException, JsonProcessingException {
			serialize(aIdentifier, jsonGenerator, serializerProvider);
		}
	}

	/**
	 * This method is handling object serialisation to an id only - this would be used when the actual object has already been side loaded and another object is referencing it within the same payload
	 * @param <T>
	 */
	@SuppressWarnings("rawtypes")
	public static class ToIdentifierSerializer<T extends Entity> extends JsonSerializer<T> {

		@Override
		public void serialize(T aEntity, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
			jsonGenerator.writeObject(aEntity.getIdentifier());
		}

		@Override
		public void serializeWithType(T aEntity, JsonGenerator jsonGenerator, SerializerProvider serializerProvider, TypeSerializer typeSer) throws IOException, JsonProcessingException {
			jsonGenerator.writeObject(aEntity.getIdentifier());
		}
	}

	/**
	 * This method is handling object serialisation to an id only - this would be used when the actual object has already been side loaded and another object is referencing it within the same payload
	 * @param <T>
	 */
	public static class ToStringIdentifierSerializer<T extends Entity<T, ? extends StringIdentifier>> extends JsonSerializer<T> {

		@Override
		public void serialize(T aEntity, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
			serialiseIdString(jsonGenerator, aEntity.getIdentifier().getId());
		}
	}

	// Specific

	public static class ActivityToIdSerializer extends ToIdentifierSerializer<ActivityMob> {}
	
	public static class ActivityRateBandToIdSerializer extends ToIdentifierSerializer<ActivityRateBandMob> {}
	
	public static class AssignmentToIdSerializer extends ToIdentifierSerializer<AssignmentMob> {}
	
	public static class ExchangeRateToIdSerializer extends ToIdentifierSerializer<ExchangeRateMob> {}
	
	public static class ExpenseCategoryToIdSerializer extends ToIdentifierSerializer<ExpenseCategoryMob> {}
	
	public static class ExpenseClaimToIdSerializer extends ToIdentifierSerializer<ExpenseClaimMob> {}
	
	public static class ExpenseClaimToStringIdSerializer extends ToStringIdentifierSerializer<ExpenseClaimMob> {}
	
	public static class ExpenseItemToIdSerializer extends ToIdentifierSerializer<ExpenseItemMob> {}
	
	public static class PlanTaskAssignmentToIdSerializer extends ToIdentifierSerializer<PlanTaskAssignmentMob> {}
	
//	public static class SynchroniseWindowToIdSerializer extends ToIdentifierSerializer<SynchroniseWindow> {}
	
	public static class TaxCodeToIdSerializer extends ToIdentifierSerializer<TaxCodeMob> {}
	
	public static class TaxRateToIdSerializer extends ToIdentifierSerializer<TaxRateMob> {}
	
	public static class TimeCategoryToIdSerializer extends ToIdentifierSerializer<TimeCategoryMob> {}
	
	public static class TimeEntryToIdSerializer extends ToIdentifierSerializer<TimeEntryMob> {}
	
	public static class TrackingPeriodToIdSerializer extends ToIdentifierSerializer<TrackingPeriodMob> {}
	
}
