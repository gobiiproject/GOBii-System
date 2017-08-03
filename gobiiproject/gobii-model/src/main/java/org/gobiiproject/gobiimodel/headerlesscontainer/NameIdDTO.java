package org.gobiiproject.gobiimodel.headerlesscontainer;

/**
 * Created by Phil on 4/8/2016.
 */
public class NameIdDTO extends DTOBase {

    private Integer id = 0;
    private String name = null;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


}
