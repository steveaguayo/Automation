package kimble.automation.domain.mobile.general;

import kimble.automation.domain.mobile.general.Identifiers.Identifier;
import kimble.automation.domain.mobile.general.Identifiers.StringIdentifier;
import kimble.automation.domain.mobile.general.Identifiers.StringIdentifierEmbedded;
import kimble.automation.domain.mobile.general.Identifiers.TimestampedIdentifier;
import kimble.automation.domain.mobile.general.Identifiers.TimestampedIdentifierEmbedded;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;


public class Entities {

	public static interface Entity<S extends Entity, I extends Identifier> {

		I getIdentifier();
		void setIdentifier(I aIdentifier) throws Exception;

		String getId();
		void setId(String aId);

		S save();

		S delete();

		S refresh();
	}

	public static interface RemotelySaveableEntity<S extends Entity, I extends Identifier>
	extends Entity<S, I>
	{
		// Client side only
		@JsonIgnore
		public boolean hasChanged();
		public void setHasChanged(boolean hasChanged);

		@JsonIgnore
		public boolean isDeletedLocally();
		public void setDeletedLocally(boolean isDeletedLocally);

		@JsonIgnore
		public boolean isDeletedOnServer();
		public void setDeletedOnServer(boolean isDeletedOnServer);
	}

	public static abstract class EntityA<S extends EntityA, I extends Identifier> implements Entity<S, I> {

		protected String id;
		@JsonIgnore
		public String getId() { return this.id; }
		public void setId(String aId) { this.id = aId; }

		protected I identifier;
		public I getIdentifier() { return identifier; };
		public S save() {
			try {
				return (S)this;
			} catch(Exception e) { throw new RuntimeException("Failed to save " + this.getClass().getSimpleName() + " : " + this.getId(), e); }
		}

		public S delete() {
			try {
				return (S)this;
			} catch(Exception e) { throw new RuntimeException("Failed to delete " + this.getClass().getSimpleName() + " : " + this.getId(), e); }
		}

		public S refresh() {
			try {
				return (S)this;
			} catch(Exception e) { throw new RuntimeException("Failed to refresh " + this.getClass().getSimpleName() + " : " + this.getId(), e); }
		}
	}

	public static abstract class StringIdEntityA<S extends StringIdEntityA, I extends StringIdentifier> extends EntityA<S, I> {
	}

	public static abstract class TimestampedEntityA<S extends TimestampedEntityA, I extends TimestampedIdentifier> extends StringIdEntityA<S, I> {
		String timestamp;
		@JsonIgnore
		public String getTimestamp() { return timestamp; }
		public void setTimestamp(String aTimestamp) { timestamp = aTimestamp; }
	}

	public static abstract class StringIdEntity<S extends StringIdEntity> extends StringIdEntityA<S, StringIdentifier> {

		@JsonProperty("Id")
		public StringIdentifier getIdentifier() {
			if(identifier == null) identifier = new StringIdentifierEmbedded(this);
			return identifier;
		}
		public void setIdentifier(StringIdentifier aIdentifier) {
			if(aIdentifier == null) return;
			getIdentifier().setId(aIdentifier.getId());
		}
	}
	
	public static abstract class TimestampedEntity<S extends TimestampedEntity> extends TimestampedEntityA<S, TimestampedIdentifier>
	{
		@JsonProperty("Identifier")
		public TimestampedIdentifier getIdentifier() {
			if(identifier == null) identifier = new TimestampedIdentifierEmbedded(this);
			return identifier;
		}
		public void setIdentifier(TimestampedIdentifier aIdentifier) {
			if(aIdentifier == null) return;
			getIdentifier().setId(aIdentifier.getId());
			getIdentifier().setTimestamp(aIdentifier.getTimestamp());
		}
	}
}
