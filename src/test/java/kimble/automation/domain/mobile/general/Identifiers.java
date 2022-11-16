package kimble.automation.domain.mobile.general;

import kimble.automation.domain.mobile.ExpenseItemMob;
import kimble.automation.domain.mobile.general.Deserializers.StringIdDeserializer;
import kimble.automation.domain.mobile.general.Entities.StringIdEntity;
import kimble.automation.domain.mobile.general.Entities.TimestampedEntity;
import kimble.automation.domain.mobile.general.Serializers.ExpenseItemIdentifierSerializer;
import kimble.automation.domain.mobile.general.Serializers.StringIdentifierSerializer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class Identifiers {

	public static interface Identifier {

		@JsonProperty("Id")
		String getId();
		void setId(String aId);

		@JsonIgnore
		Class getInterface();

		String toUrlParams();
	}

	@JsonSerialize(using = StringIdentifierSerializer.class)
	@JsonDeserialize(using = StringIdDeserializer.class)
	//@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, defaultImpl = StringIdentifierImpl.class)
	public interface StringIdentifier extends Identifier {}

	// Override StringIdentifier serialisation to use default serialisation
	@JsonSerialize(using = Serializers.TimestampedIdentifierSerializer.class)
	@JsonDeserialize
	@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, defaultImpl = TimestampedIdentifierImpl.class)
	public interface TimestampedIdentifier extends StringIdentifier {
		@JsonProperty("Timestamp")
		String getTimestamp();
		void setTimestamp(String aTimestamp);
	}

	@JsonSerialize(using = ExpenseItemIdentifierSerializer.class)
	@JsonDeserialize
	@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, defaultImpl = ExpenseItemIdentifierImpl.class)
	public interface ExpenseItemIdentifier extends TimestampedIdentifier {

		@JsonProperty("ExpenseClaim")
		TimestampedIdentifier getExpenseClaimIdentifier();
//		void setExpenseClaimIdentifier(TimestampedIdentifier aIdentifier) throws Exception;
	}

	public static abstract class StringIdentifierA implements StringIdentifier
	{
		@Override
		public Class getInterface() { return StringIdentifier.class; }

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if(!(obj instanceof Identifier))
				return false;
			Identifier id = (Identifier)obj;
			if (!getInterface().isAssignableFrom(id.getInterface())) {
				if(id.getInterface().isAssignableFrom(getInterface()))
					return obj.equals(this);
				return false;
			}
			StringIdentifier other = (StringIdentifier) obj;
			if (getId() == null) {
				if (other.getId() != null)
					return false;
			} else if (!getId().equals(other.getId()))
				return false;
			return true;
		}

		public String toUrlParams() { return "?id=" + getId(); }
	}

	public static class StringIdentifierImpl extends StringIdentifierA
	{
		public StringIdentifierImpl() {}
		public StringIdentifierImpl(String aId) { this.Id = aId; }

		String Id;
		public String getId() { return this.Id; }
		public void setId(String aId) { this.Id = aId; }
	}

	public static class StringIdentifierEmbedded extends StringIdentifierA
	{
		StringIdEntity entity;
		public StringIdentifierEmbedded(StringIdEntity aEntity) { this.entity = aEntity; }

		public String getId() { return entity.getId(); }
		public void setId(String aId) { entity.setId(aId); }
	}

	public static abstract class TimestampedIdentifierA extends StringIdentifierA implements TimestampedIdentifier
	{
		@Override
		public Class getInterface() { return TimestampedIdentifier.class; }

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if(!(obj instanceof Identifier))
				return false;
			Identifier id = (Identifier)obj;
			if (!getInterface().isAssignableFrom(id.getInterface())) {
				if(id.getInterface().isAssignableFrom(getInterface()))
					return obj.equals(this);
				return false;
			}
			TimestampedIdentifier other = (TimestampedIdentifier) obj;
			if (getId() == null) {
				if (other.getId() != null)
					return false;
			} else if (!getId().equals(other.getId()))
				return false;
			if (getTimestamp() == null) {
				if (other.getTimestamp() != null)
					return false;
			} else if (!getTimestamp().equals(other.getTimestamp()))
				return false;
			return true;
		}

		@Override
		public String toString() {
			try {
				return TnXContext.getMapper().writerWithDefaultPrettyPrinter().writeValueAsString(this);
			} catch (JsonProcessingException e) { return super.toString(); }
		}

		public String toUrlParams() { return super.toUrlParams() + "&timestamp=" + getTimestamp(); }
	}

	public static class TimestampedIdentifierImpl extends TimestampedIdentifierA implements TimestampedIdentifier
	{
		public TimestampedIdentifierImpl() {}
		public TimestampedIdentifierImpl(String aId) { this.Id = aId; }
		public TimestampedIdentifierImpl(String aId, String aTimestamp) { this(aId); this.setTimestamp(aTimestamp);}

		String Id;
		public String getId() { return this.Id; }
		public void setId(String aId) { this.Id = aId; }

		String Timestamp;
		public String getTimestamp() { return this.Timestamp; }
		public void setTimestamp(String aTimestamp) { this.Timestamp = aTimestamp; }

	}

	public static class TimestampedIdentifierEmbedded extends TimestampedIdentifierA
	{
		TimestampedEntity entity;
		public TimestampedIdentifierEmbedded(TimestampedEntity aEntity) { this.entity = aEntity; }

		public String getId() { return entity.getId(); }
		public void setId(String aId) { entity.setId(aId); }

		public String getTimestamp() { return entity.getTimestamp(); }
		public void setTimestamp(String aTimestamp) { entity.setTimestamp(aTimestamp); }
	}

	public static class ExpenseItemIdentifierImpl extends TimestampedIdentifierImpl implements ExpenseItemIdentifier {

		public ExpenseItemIdentifierImpl() {}
		public ExpenseItemIdentifierImpl(String aId) { super(aId); }
		public ExpenseItemIdentifierImpl(String aId, String aTimestamp) { super(aId, aTimestamp);}
		public ExpenseItemIdentifierImpl(String aId, String aTimestamp, TimestampedIdentifier aExpenseClaimId) {
			super(aId, aTimestamp);
			expenseClaimId = aExpenseClaimId;
		}

		TimestampedIdentifier expenseClaimId;

		public TimestampedIdentifier getExpenseClaimIdentifier() { return expenseClaimId; }
		public void setExpenseClaimIdentifier(TimestampedIdentifier aIdentifier) { expenseClaimId = aIdentifier; }
	}

	public static class ExpenseItemIdentifierEmbedded extends TimestampedIdentifierA implements ExpenseItemIdentifier
	{
		ExpenseItemMob entity;
		public ExpenseItemIdentifierEmbedded(ExpenseItemMob aEntity) { this.entity = aEntity; }

		public String getId() { return entity.getId(); }
		public void setId(String aId) { entity.setId(aId); }

		public String getTimestamp() { return entity.getTimestamp(); }
		public void setTimestamp(String aTimestamp) { entity.setTimestamp(aTimestamp); }

		public TimestampedIdentifier getExpenseClaimIdentifier() { return entity.getExpenseClaim() == null ? null : entity.getExpenseClaim().getIdentifier(); }
//		public void setExpenseClaimIdentifier(TimestampedIdentifier aIdentifier) throws Exception { if(aIdentifier != null) entity.setExpenseClaimById(aIdentifier); }
	}

}
