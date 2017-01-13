package org.gobiiproject.gobiimodel.dto.instructions;

/**
 * A singluar identifier used for unambiguously sepcifying a UI parameter used during this action.
 * Created by Phil on 9/1/2016.
 */
public class GobiiFilePropNameId {
    //Property ID - Internal unique identifier for this label
    Integer Id;
    //Property Name (User readable)
    String name;

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
