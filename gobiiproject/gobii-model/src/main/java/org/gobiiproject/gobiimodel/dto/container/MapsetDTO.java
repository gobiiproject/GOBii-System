
package org.gobiiproject.gobiimodel.dto.container;

import org.gobiiproject.gobiimodel.dto.DtoMetaData;
import org.gobiiproject.gobiimodel.dto.annotations.GobiiEntityColumn;
import org.gobiiproject.gobiimodel.dto.annotations.GobiiEntityParam;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Angel on 4/13/2016.
 */
public class MapsetDTO extends DtoMetaData {

	public MapsetDTO() {}

	public MapsetDTO(ProcessType processType) {
		super(processType);
	}

	private Integer mapsetId;
	private String name;
	private String code;
	private String description;
	private Integer referenceId;
	private Integer mapType;
	private Integer createdBy;
	private Date createdDate;
	private Integer modifiedBy;
	private Date modifiedDate;
	private Integer status;
	private List<EntityPropertyDTO> properties = new ArrayList<>();

	@GobiiEntityParam(paramName = "mapsetId")
	public Integer getMapsetId() {
		return mapsetId;
	}

	@GobiiEntityColumn(columnName ="mapset_id")
	public void setMapsetId(Integer mapsetId) {
		this.mapsetId = mapsetId;
	}


	@GobiiEntityParam(paramName = "name")
	public String getName() {
		return name;
	}

	@GobiiEntityColumn(columnName ="name")
	public void setName(String name) {
		this.name = name;
	}

	@GobiiEntityParam(paramName = "code")
	public String getCode() {
		return code;
	}

	@GobiiEntityColumn(columnName ="code")
	public void setCode(String code) {
		this.code = code;
	}

	@GobiiEntityParam(paramName = "description")
	public String getDescription() {
		return description;
	}

	@GobiiEntityColumn(columnName ="description")
	public void setDescription(String description) {
		this.description = description;
	}

	@GobiiEntityParam(paramName = "referenceId")
	public Integer getReferenceId() {
		return referenceId;
	}

	@GobiiEntityColumn(columnName ="reference_id")
	public void setReferenceId(Integer referenceId) {
		this.referenceId = referenceId;
	}

	@GobiiEntityParam(paramName = "mapType")
	public Integer getMapType() {
		return mapType;
	}

	@GobiiEntityColumn(columnName ="type_id")
	public void setMapType(Integer mapType) {
		this.mapType = mapType;
	}

	@GobiiEntityParam(paramName = "createdBy")
	public Integer getCreatedBy() {
		return createdBy;
	}

	@GobiiEntityColumn(columnName ="created_by")
	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	@GobiiEntityParam(paramName = "createdDate")
	public Date getCreatedDate() {
		return createdDate;
	}

	@GobiiEntityColumn(columnName ="created_date")
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@GobiiEntityParam(paramName = "modifiedBy")
	public Integer getModifiedBy() {
		return modifiedBy;
	}

	@GobiiEntityColumn(columnName ="modified_by")
	public void setModifiedBy(Integer modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	@GobiiEntityParam(paramName = "modifiedDate")
	public Date getModifiedDate() {
		return modifiedDate;
	}

	@GobiiEntityColumn(columnName ="modified_date")
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	@GobiiEntityParam(paramName = "status")
	public Integer getStatus() {
		return status;
	}

	@GobiiEntityColumn(columnName ="status")
	public void setStatus(Integer status) {
		this.status = status;
	}


	public List<EntityPropertyDTO> getProperties() {
		return properties;
	}

	public void setProperties(List<EntityPropertyDTO> properties) {
		this.properties = properties;
	}
}
