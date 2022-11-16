package kimble.automation.domain.mobile.builders;

import kimble.automation.domain.mobile.general.Deserializers;
import kimble.automation.domain.mobile.general.Entities.RemotelySaveableEntity;
import kimble.automation.domain.mobile.general.Identifiers.Identifier;
import kimble.automation.domain.mobile.general.Identifiers.StringIdentifierImpl;

/**
 * Created by Benjamin on 7/3/2015.
 */
public class RemotelySaveableEntityBuilder extends EntityBuilderA<RemotelySaveableEntityBuilder, RemotelySaveableEntity> {

    public RemotelySaveableEntityBuilder() {
        e = new RemotelySaveableEntity() {
            Identifier identifier;
            boolean hasChanged = false;
            boolean isDeletedLocally = false;
            boolean isDeletedOnServer = false;

            public Identifier getIdentifier() {
                if(identifier == null) {
                    try {
                        identifier = new StringIdentifierImpl(Deserializers.generateId());
                    } catch(Exception e) { throw new RuntimeException("Failed to generate an id", e); }
                }
                return identifier;
            }
            public void setIdentifier(Identifier aIdentifier) throws Exception { getIdentifier().setId(aIdentifier.getId()); }

            public String getId() { return getIdentifier().getId(); }
            public void setId(String aId) { getIdentifier().setId(aId); }

            @SuppressWarnings("unused")
            public boolean getDeleted() { return false; }

            public boolean hasChanged() { return hasChanged; }
            public void setHasChanged(boolean hasChanged) { this.hasChanged = hasChanged; }

            public boolean isDeletedLocally() { return isDeletedLocally; }
            public void setDeletedLocally(boolean isDeletedLocally) { this.isDeletedLocally = isDeletedLocally; }

            public boolean isDeletedOnServer() { return isDeletedOnServer; }
            public void setDeletedOnServer(boolean isDeletedOnServer) { this.isDeletedOnServer = isDeletedOnServer; }

            public RemotelySaveableEntity save() { System.out.println("Fake " + this.getClass().getSimpleName() + " save method called"); return this;}

            public RemotelySaveableEntity delete() { System.out.println("Fake " + this.getClass().getSimpleName() + " delete method called"); return this;}

            public RemotelySaveableEntity refresh() { System.out.println("Fake " + this.getClass().getSimpleName() + " refresh method called"); return this;}
        };
    }

    public RemotelySaveableEntityBuilder withHasChanged(boolean aHasChanged) {
        e.setHasChanged(aHasChanged);
        return this;
    }

    public RemotelySaveableEntityBuilder withIsDeletedLocally(boolean aIsDeletedLocally) {
        e.setDeletedLocally(aIsDeletedLocally);
        return this;
    }

    public RemotelySaveableEntityBuilder withIsDeletedOnServer(boolean aIsDeletedOnServer) {
        e.setDeletedOnServer(aIsDeletedOnServer);
        return this;
    }

    public RemotelySaveableEntity<RemotelySaveableEntity, Identifier> build() { return e; }
}
