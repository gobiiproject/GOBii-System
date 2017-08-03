package org.gobiiproject.gobiimodel.headerlesscontainer;

import org.gobiiproject.gobiimodel.dto.annotations.GobiiEntityColumn;
import org.gobiiproject.gobiimodel.dto.annotations.GobiiEntityParam;

/**
 * Created by Angel on 4/29/2016.
 * Modified by Yanii on 1/25/2016
 */
public class CvDTO extends DTOBase {

	public CvDTO() {}

	private Integer cv_id;
	private Integer groupId;
	private Integer xrefId;
	private Integer entityStatus; // renmae to status when base class changes
	private String term;
	private String abbreviation;
	private String definition;
	private Integer rank;
	private Integer groupType;

	@Override
    public Integer getId() {
	    return this.cv_id;
    }

    @Override
    public void setId(Integer id) {
	    this.cv_id = id;
    }

	@GobiiEntityParam(paramName = "cvId")
	public Integer getCvId() {
		return cv_id;
	}

	@GobiiEntityColumn(columnName = "cv_id")
	public void setCvId(Integer cv_id) {
		this.cv_id = cv_id;
	}

	@GobiiEntityParam(paramName = "group")
	public Integer getGroup() {
		return this.groupId;
	}

	@GobiiEntityColumn(columnName = "cvgroup_id")
	public void setGroup(Integer groupId) {
		this.groupId = groupId;
	}

	@GobiiEntityParam(paramName = "term")
	public String getTerm() {
		return term;
	}

	@GobiiEntityColumn(columnName = "term")
	public void setTerm(String term) {
		this.term = term;
	}

	@GobiiEntityParam(paramName = "definition")
	public String getDefinition() {
		return definition;
	}

	@GobiiEntityColumn(columnName = "definition")
	public void setDefinition(String definition) {
		this.definition = definition;
	}

	@GobiiEntityParam(paramName = "rank")
	public Integer getRank() {
		return rank;
	}

	@GobiiEntityColumn(columnName = "rank")
	public void setRank(Integer rank) {
		this.rank = rank;
	}


	@GobiiEntityParam(paramName = "groupId")
	public Integer getGroupId() {
		return groupId;
	}

	@GobiiEntityColumn(columnName = "cvgroup_id")
	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	@GobiiEntityParam(paramName = "xrefId")
	public Integer getXrefId() {
		return xrefId;
	}

	@GobiiEntityColumn(columnName = "dbxref_id")
	public void setXrefId(Integer xrefId) {
		this.xrefId = xrefId;
	}


	@GobiiEntityParam(paramName = "abbreviation")
	public String getAbbreviation() {
		return abbreviation;
	}

	@GobiiEntityColumn(columnName = "abbreviation")
	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}

	@GobiiEntityParam(paramName = "status")
	public Integer getEntityStatus() {
		return entityStatus;
	}

	@GobiiEntityColumn(columnName = "status")
	public void setEntityStatus(Integer entityStatus) {
		this.entityStatus = entityStatus;
	}

	@GobiiEntityParam(paramName = "groupType")
	public Integer getGroupType() { return groupType; }

	@GobiiEntityColumn(columnName = "group_type")
	public void setGroupType(Integer groupType) { this.groupType = groupType; }

}
