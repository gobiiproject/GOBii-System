package org.gobiiproject.gobiimodel.dto.container;

import org.gobiiproject.gobiimodel.dto.DtoMetaData;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Phil on 4/8/2016.
 */
public class NameIdListDTO extends DtoMetaData {

    public enum EntityType {DBTABLE, CVTERM};
    private EntityType entityType = EntityType.DBTABLE;
    private String entityName = null;
    private Map<String, String> namesById = new HashMap<>();
    private String filter = null;

    public EntityType getEntityType() {
        return entityType;
    }

    public void setEntityType(EntityType entityType) {
        this.entityType = entityType;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public Map<String, String> getNamesById() {
        return namesById;
    }

    public void setNamesById(Map<String, String> namesById) {
        this.namesById = namesById;
    }
}
