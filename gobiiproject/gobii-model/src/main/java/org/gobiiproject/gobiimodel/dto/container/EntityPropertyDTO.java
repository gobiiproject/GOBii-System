package org.gobiiproject.gobiimodel.dto.container;

/**
 * Created by Phil on 4/14/2016.
 */
public class EntityPropertyDTO {

    private Integer entityIdId = null;
    private Integer propertyId = null;
    private String propertyName = null;
    private String propertyValue = null;

    public EntityPropertyDTO() {

    }

    public EntityPropertyDTO(Integer entityIdId,
                             Integer propertyId,
                             String propertyName,
                             String propertyValue) {
        this.entityIdId = entityIdId;
        this.propertyId = propertyId;
        this.propertyName = propertyName;
        this.propertyValue = propertyValue;
    }

    public Integer getEntityIdId() {
        return entityIdId;
    }

    public void setEntityIdId(Integer entityIdId) {
        this.entityIdId = entityIdId;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getPropertyValue() {
        return propertyValue;
    }

    public void setPropertyValue(String propertyValue) {
        this.propertyValue = propertyValue;
    }

    public Integer getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(Integer propertyId) {
        this.propertyId = propertyId;
    }


}
