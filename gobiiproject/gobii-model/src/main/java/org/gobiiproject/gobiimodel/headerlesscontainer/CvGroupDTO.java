package org.gobiiproject.gobiimodel.headerlesscontainer;

import org.gobiiproject.gobiimodel.dto.annotations.GobiiEntityColumn;
import org.gobiiproject.gobiimodel.dto.annotations.GobiiEntityParam;

/**
 * Created by Angel on 4/29/2016.
 * Modified by Yanii on 1/25/2016
 */
public class CvGroupDTO extends DTOBase {

	public CvGroupDTO() {}

	Integer cvGroupId = 0;
	String name;
	String definition;
	Integer groupType;

	@Override
    public Integer getId() {
	    return this.cvGroupId;
    }

    @Override
    public void setId(Integer id) {
	    this.cvGroupId = id;
    }

	@GobiiEntityParam(paramName = "cvGroupId")
	public Integer getCvGroupId() {
		return cvGroupId;
	}

	@GobiiEntityColumn(columnName = "cvgroup_id")
	public void setCvGroupId(Integer cvGroupId) {
		this.cvGroupId = cvGroupId;
	}

	@GobiiEntityParam(paramName = "name")
	public String getName() {
		return name;
	}

	@GobiiEntityColumn(columnName = "name")
	public void setName(String name) {
		this.name = name;
	}

	@GobiiEntityParam(paramName = "definition")
	public String getDefinition() {
		return definition;
	}

	@GobiiEntityColumn(columnName = "definition")
	public void setDefinition(String definition) {
		this.definition = definition;
	}

	@GobiiEntityParam(paramName = "groupType")
	public Integer getGroupType() {
		return groupType;
	}

	@GobiiEntityColumn(columnName = "type")
	public void setGroupType(Integer groupType) {
		this.groupType = groupType;
	}
}
