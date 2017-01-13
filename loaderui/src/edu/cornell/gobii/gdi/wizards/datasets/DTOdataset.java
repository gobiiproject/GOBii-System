package edu.cornell.gobii.gdi.wizards.datasets;

import java.util.ArrayList;
import java.util.List;

import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiFile;
import org.gobiiproject.gobiimodel.types.DataSetOrientationType;
import org.gobiiproject.gobiimodel.types.GobiiColumnType;

import edu.cornell.gobii.gdi.wizards.dnasamples.DTOsamples;
import edu.cornell.gobii.gdi.wizards.markers.DTOmarkers;

public class DTOdataset {

	private DTOsamples dtoSamples = new DTOsamples();
	private DTOmarkers dtoMarkers = new DTOmarkers();
	private List<String> headerSamples = new ArrayList<>();
	private List<String> headerMarkers = new ArrayList<>();
	private GobiiFile file = new GobiiFile();
	private GobiiColumnType columnType = GobiiColumnType.CSV_BOTH;
//	private GobiiColumnType markerColumnType;
//	private GobiiColumnType sampleColumnType;
	private List<String> files = new ArrayList<String>();
	private int rCoord = -1;
	private int cCoord = -1;
	private Integer projectID;
	private Integer experimentID;
	private Integer datasetID;
	private Integer datasetTypeID;
	private Integer platformID;
	private Integer mapsetID;
	private String datasetType;
	private String fileExtention;
	private String template = null;
	private boolean isRemote = false;
	private DataSetOrientationType orientation = DataSetOrientationType.MARKER_FAST;
	
	public DataSetOrientationType getOrientation() {
		return orientation;
	}
	public void setOrientation(DataSetOrientationType orientation) {
		this.orientation = orientation;
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
	public String getDatasetType() {
		return datasetType;
	}
	public void setDatasetType(String datasetType) {
		this.datasetType = datasetType;
	}
	public Integer getProjectID() {
		return projectID;
	}
	public void setProjectID(Integer projectID) {
		this.projectID = projectID;
		dtoSamples.setProjectID(projectID);
		dtoMarkers.setProjectID(projectID);
	}
	public Integer getExperimentID() {
		return experimentID;
	}
	public void setExperimentID(Integer experimentID) {
		this.experimentID = experimentID;
		dtoSamples.setExperimentID(experimentID);
		dtoMarkers.setExperimentID(experimentID);
	}
	public Integer getDatasetID() {
		return datasetID;
	}
	public void setDatasetID(Integer datasetID) {
		this.datasetID = datasetID;
		dtoSamples.setDatasetID(datasetID);
		dtoMarkers.setDatasetID(datasetID);
	}
	public Integer getDatasetTypeID() {
		return datasetTypeID;
	}
	public void setDatasetTypeID(Integer datasetTypeID) {
		this.datasetTypeID = datasetTypeID;
	}
	public Integer getPlatformID() {
		return platformID;
	}
	public void setPlatformID(Integer platformID) {
		this.platformID = platformID;
		dtoMarkers.setPlatformID(platformID);
	}
	public Integer getMapsetID() {
		return mapsetID;
	}
	public void setMapsetID(Integer mapsetID) {
		this.mapsetID = mapsetID;
		dtoMarkers.setMapsetID(mapsetID);
	}
//	public GobiiColumnType getMarkerColumnType() {
//		return markerColumnType;
//	}
//	public void setMarkerColumnType(GobiiColumnType markerColumnType) {
//		this.markerColumnType = markerColumnType;
//	}
//	public GobiiColumnType getSampleColumnType() {
//		return sampleColumnType;
//	}
//	public void setSampleColumnType(GobiiColumnType sampleColumnType) {
//		this.sampleColumnType = sampleColumnType;
//	}
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
		dtoSamples.setFiles(files);
		dtoMarkers.setFiles(files);
	}
	public GobiiFile getFile() {
		return file;
	}
	public void setFile(GobiiFile file) {
		this.file = file;
		dtoSamples.setFile(file);
		dtoMarkers.setFile(file);
	}
	public GobiiColumnType getColumnType() {
		return columnType;
	}
	public void setColumnType(GobiiColumnType columnType) {
		this.columnType = columnType;
	}
	public DTOsamples getDtoSamples() {
		return dtoSamples;
	}
	public void setDtoSamples(DTOsamples dtoSamples) {
		this.dtoSamples = dtoSamples;
	}
	public DTOmarkers getDtoMarkers() {
		return dtoMarkers;
	}
	public void setDtoMarkers(DTOmarkers dtoMarkers) {
		this.dtoMarkers = dtoMarkers;
	}
	public List<String> getHeaderSamples() {
		return headerSamples;
	}
	public void setHeaderSamples(List<String> headerSamples) {
		this.headerSamples = headerSamples;
	}
	public List<String> getHeaderMarkers() {
		return headerMarkers;
	}
	public void setHeaderMarkers(List<String> headerMarkers) {
		this.headerMarkers = headerMarkers;
	}
	
	public DTOdataset(){
	}
}
