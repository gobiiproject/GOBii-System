package edu.cornell.gobii.gdi.wizards.markers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiFile;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiFileColumn;
import org.gobiiproject.gobiimodel.headerlesscontainer.LoaderFilePreviewDTO;
import org.gobiiproject.gobiimodel.types.GobiiColumnType;

import edu.cornell.gobii.gdi.utils.WizardDTO;

public class DTOmarkers implements WizardDTO{
	
	private HashMap<String, GobiiFileColumn> markerFields = new HashMap<>();
	private HashMap<String, GobiiFileColumn> subMarkerFields = new HashMap<>();
	private HashMap<String, GobiiFileColumn> markerPropFields = new HashMap<>();
	private HashMap<String, GobiiFileColumn> dsMarkerFields = new HashMap<>();
	private HashMap<String, GobiiFileColumn> subDsMarkerFields = new HashMap<>();
	private HashMap<String, GobiiFileColumn> lgFields = new HashMap<>();
	private HashMap<String, GobiiFileColumn> subLgFields = new HashMap<>();
	private HashMap<String, GobiiFileColumn> lgMarkerFields = new HashMap<>();
	private HashMap<String, GobiiFileColumn> subLgMarkerFields = new HashMap<>();
	private List<String[]> header = new ArrayList<>();
	private String fileExtention;
	private GobiiFile file = new GobiiFile();
	private List<String> files = new ArrayList<String>();
	private GobiiColumnType columnType;
	private int rCoord = -1;
	private int cCoord = -1;
	private Integer projectID;
	private Integer experimentID;
	private Integer datasetID;
	private Integer platformID;
	private Integer mapsetID;
	private String projectName;
	private String platformName;
	private String datasetName;
	private String experimentName;
	private String mapsetName;
	private String template = null;
	private boolean isRemote = false;
	private LoaderFilePreviewDTO previewDTO = new LoaderFilePreviewDTO();
	
	public HashMap<String, GobiiFileColumn> getSubDsMarkerFields() {
		return subDsMarkerFields;
	}

	public void setSubDsMarkerFields(HashMap<String, GobiiFileColumn> subDsMarkerFields) {
		this.subDsMarkerFields = subDsMarkerFields;
	}

	public HashMap<String, GobiiFileColumn> getSubLgFields() {
		return subLgFields;
	}

	public void setSubLgFields(HashMap<String, GobiiFileColumn> subLgFields) {
		this.subLgFields = subLgFields;
	}

	public HashMap<String, GobiiFileColumn> getSubLgMarkerFields() {
		return subLgMarkerFields;
	}

	public void setSubLgMarkerFields(HashMap<String, GobiiFileColumn> subLgMarkerFields) {
		this.subLgMarkerFields = subLgMarkerFields;
	}

	public HashMap<String, GobiiFileColumn> getSubMarkerFields() {
		return subMarkerFields;
	}

	public void setSubMarkerFields(HashMap<String, GobiiFileColumn> subMarkerFields) {
		this.subMarkerFields = subMarkerFields;
	}

	public LoaderFilePreviewDTO getPreviewDTO() {
		return previewDTO;
	}

	public void setPreviewDTO(LoaderFilePreviewDTO previewDTO) {
		this.previewDTO = previewDTO;
	}

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
		return previewDTO.getFileList();
	}
	public void setFiles(List<String> files) {
		previewDTO.setFileList(files);
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

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getPlatformName() {
		return platformName;
	}

	public void setPlatformName(String platformName) {
		this.platformName = platformName;
	}

	public String getDatasetName() {
		return datasetName;
	}

	public void setDatasetName(String datasetName) {
		this.datasetName = datasetName;
	}

	public String getExperimentName() {
		return experimentName;
	}

	public void setExperimentName(String experimentName) {
		this.experimentName = experimentName;
	}

	public String getMapsetName() {
		return mapsetName;
	}

	public void setMapsetName(String mapsetName) {
		this.mapsetName = mapsetName;
	}
}
