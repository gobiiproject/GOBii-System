package edu.cornell.gobii.gdi.wizards.markers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiFile;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiFileColumn;
import org.gobiiproject.gobiimodel.types.GobiiColumnType;

public class DTOmarkers {
	
	private HashMap<String, GobiiFileColumn> markerFields = new HashMap<>();
	private HashMap<String, GobiiFileColumn> markerPropFields = new HashMap<>();
	private HashMap<String, GobiiFileColumn> dsMarkerFields = new HashMap<>();
	private HashMap<String, GobiiFileColumn> lgFields = new HashMap<>();
	private HashMap<String, GobiiFileColumn> lgMarkerFields = new HashMap<>();
	private List<String[]> header = new ArrayList<>();
	private String fileExtention;
	private GobiiFile file = new GobiiFile();
	private List<String> files = new ArrayList<String>();
	private GobiiColumnType columnType;
	private int rCoord = -1;
	private int cCoord = -1;
	private Integer projectID = null;
	private Integer platformID = null;
	private Integer experimentID = null;
	private Integer mapsetID =null;
	private Integer datasetID = null;
	private String template = null;
	private boolean isRemote = false;
	
	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public boolean isRemote() {
		return isRemote;
	}

	public void setRemote(boolean isRemote) {
		this.isRemote = isRemote;
	}

	public String getFileExtention() {
		return fileExtention;
	}

	public void setFileExtention(String fileExtention) {
		this.fileExtention = fileExtention;
	}

	public Integer getDatasetID() {
		return datasetID;
	}

	public void setDatasetID(Integer datasetID) {
		this.datasetID = datasetID;
	}

	public Integer getMapsetID() {
		return mapsetID;
	}

	public void setMapsetID(Integer mapsetID) {
		this.mapsetID = mapsetID;
	}

	public Integer getProjectID() {
		return projectID;
	}

	public void setProjectID(Integer projectID) {
		this.projectID = projectID;
	}

	public Integer getPlatformID() {
		return platformID;
	}

	public void setPlatformID(Integer platformID) {
		this.platformID = platformID;
	}

	public Integer getExperimentID() {
		return experimentID;
	}

	public void setExperimentID(Integer experiemtnID) {
		this.experimentID = experiemtnID;
	}

	public List<String[]> getHeader() {
		return header;
	}

	public void setHeader(List<String[]> header) {
		this.header = header;
	}

	public GobiiColumnType getColumnType() {
		return columnType;
	}

	public void setColumnType(GobiiColumnType columnType) {
		this.columnType = columnType;
	}

	public GobiiFile getFile() {
		return file;
	}

	public void setFile(GobiiFile file) {
		this.file = file;
	}

	public int getrCoord() {
		return rCoord;
	}

	public void setrCoord(int rCoord) {
		this.rCoord = rCoord;
	}

	public int getcCoord() {
		return cCoord;
	}

	public void setcCoord(int cCoord) {
		this.cCoord = cCoord;
	}

	public List<String> getFiles() {
		return files;
	}

	public void setFiles(List<String> files) {
		this.files = files;
	}

	public HashMap<String, GobiiFileColumn> getMarkerFields() {
		return markerFields;
	}

	public void setMarkerFields(HashMap<String, GobiiFileColumn> markerFields) {
		this.markerFields = markerFields;
	}

	public HashMap<String, GobiiFileColumn> getMarkerPropFields() {
		return markerPropFields;
	}

	public void setMarkerPropFields(HashMap<String, GobiiFileColumn> markerPropFields) {
		this.markerPropFields = markerPropFields;
	}

	public HashMap<String, GobiiFileColumn> getDsMarkerFields() {
		return dsMarkerFields;
	}

	public void setDsMarkerFields(HashMap<String, GobiiFileColumn> dsMarkerFields) {
		this.dsMarkerFields = dsMarkerFields;
	}

	public HashMap<String, GobiiFileColumn> getLgFields() {
		return lgFields;
	}

	public void setLgFields(HashMap<String, GobiiFileColumn> lgFields) {
		this.lgFields = lgFields;
	}

	public HashMap<String, GobiiFileColumn> getLgMarkerFields() {
		return lgMarkerFields;
	}

	public void setLgMarkerFields(HashMap<String, GobiiFileColumn> lgMarkerFields) {
		this.lgMarkerFields = lgMarkerFields;
	}

	public DTOmarkers(){
		
	}
}
