package org.gobiiproject.gobiimodel.dto.container;

import org.gobiiproject.gobiimodel.dto.DtoMetaData;
import org.gobiiproject.gobiimodel.dto.annotations.GobiiEntityColumn;
import org.gobiiproject.gobiimodel.dto.annotations.GobiiEntityParam;
import org.gobiiproject.gobiimodel.entity.CvItem;
import org.gobiiproject.gobiimodel.entity.TableColDisplay;
import org.gobiiproject.gobiimodel.entity.TableCv;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Angel on 4/29/2016.
 */
public class CvDTO extends DtoMetaData {

	public CvDTO() {}

	public CvDTO(ProcessType processType) {
		super(processType);
	}

	boolean includeDetailsList = false;

	private Integer cv_id;
	private String group;
	private String term;
	private String definition;
	private Integer rank;

	public boolean isIncludeDetailsList() {
		return includeDetailsList;
	}

	public void setIncludeDetailsList(boolean includeDetailsList) {
		this.includeDetailsList = includeDetailsList;
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
	public String getGroup() {
		return group;
	}

	@GobiiEntityColumn(columnName = "group")
	public void setGroup(String group) {
		this.group = group;
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

	Map<String,List<CvItem>> groupCvItems = new HashMap<>();

	public Map<String, List<CvItem>> getGroupCvItems() {
		return groupCvItems;
	}

	public void setGroupCvItems(Map<String, List<CvItem>> groupCvItems) {
		this.groupCvItems = groupCvItems;
	}
}
