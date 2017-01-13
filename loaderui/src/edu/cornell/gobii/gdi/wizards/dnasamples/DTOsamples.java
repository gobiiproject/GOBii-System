package edu.cornell.gobii.gdi.wizards.dnasamples;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiFile;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiFileColumn;
import org.gobiiproject.gobiimodel.headerlesscontainer.LoaderFilePreviewDTO;
import org.gobiiproject.gobiimodel.types.GobiiColumnType;

import edu.cornell.gobii.gdi.utils.WizardDTO;

public class DTOsamples implements WizardDTO{

	private GobiiFile file = new GobiiFile();
//	private List<String> files = new ArrayList<String>();
	private List<String[]> header = new ArrayList<>();
	private GobiiColumnType columnType;
	private int rCoord = -1;
	private int cCoord = -1;
	private HashMap<String, GobiiFileColumn> germplasmFields = new HashMap<>();
	private HashMap<String, GobiiFileColumn> subGermplasmFields = new HashMap<>();
	private HashMap<String, GobiiFileColumn> germplasmPropFields = new HashMap<>();
	private HashMap<String, GobiiFileColumn> sampleFields = new HashMap<>();
	private HashMap<String, GobiiFileColumn> subSampleFields = new HashMap<>();
	private HashMap<String, GobiiFileColumn> samplePropFields = new HashMap<>();
	private HashMap<String, GobiiFileColumn> runFields = new HashMap<>();
	private HashMap<String, GobiiFileColumn> subRunFields = new HashMap<>();
	private HashMap<String, GobiiFileColumn> runPropFields = new HashMap<>();
	private Integer projectID;
	private Integer experimentID;
	private Integer datasetID;
	private String projectName;
	private String platformName;
	private String datasetName;
	private String experimentName;
	private String fileExtention;
	private String template = null;
	private boolean isRemote = false;
	private LoaderFilePreviewDTO previewDTO = new LoaderFilePreviewDTO();
	
	public HashMap<String, GobiiFileColumn> getSubGermplasmFields() {
		return subGermplasmFields;
	}
	public void setSubGermplasmFields(HashMap<String, GobiiFileColumn> subGermplasmFields) {
		this.subGermplasmFields = subGermplasmFields;
	}
	public HashMap<String, GobiiFileColumn> getSubSampleFields() {
		return subSampleFields;
	}
	public void setSubSampleFields(HashMap<String, GobiiFileColumn> subSampleFields) {
		this.subSampleFields = subSampleFields;
	}
	public HashMap<String, GobiiFileColumn> getSubRunFields() {
		return subRunFields;
	}
	public void setSubRunFields(HashMap<String, GobiiFileColumn> subRunFields) {
		this.subRunFields = subRunFields;
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
	public Integer getProjectID() {
		return projectID;
	}
	public void setProjectID(Integer projectID) {
		this.projectID = projectID;
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
	public GobiiFile getFile() {
		return file;
	}
	public void setFile(GobiiFile file) {
		this.file = file;
	}
	public List<String> getFiles() {
		return previewDTO.getFileList();
	}
	public void setFiles(List<String> files) {
		previewDTO.setFileList(files);
	}
	public GobiiColumnType getColumnType() {
		return columnType;
	}
	public void setColumnType(GobiiColumnType columnType) {
		this.columnType = columnType;
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
	public HashMap<String, GobiiFileColumn> getGermplasmFields() {
		return germplasmFields;
	}
	public void setGermplasmFields(HashMap<String, GobiiFileColumn> germplasmFields) {
		this.germplasmFields = germplasmFields;
	}
	public HashMap<String, GobiiFileColumn> getGermplasmPropFields() {
		return germplasmPropFields;
	}
	public void setGermplasmPropFields(HashMap<String, GobiiFileColumn> germplasmPropFields) {
		this.germplasmPropFields = germplasmPropFields;
	}
	public HashMap<String, GobiiFileColumn> getSampleFields() {
		return sampleFields;
	}
	public void setSampleFields(HashMap<String, GobiiFileColumn> sampleFields) {
		this.sampleFields = sampleFields;
	}
	public HashMap<String, GobiiFileColumn> getSamplePropFields() {
		return samplePropFields;
	}
	public void setSamplePropFields(HashMap<String, GobiiFileColumn> samplePropFields) {
		this.samplePropFields = samplePropFields;
	}
	public HashMap<String, GobiiFileColumn> getRunFields() {
		return runFields;
	}
	public void setRunFields(HashMap<String, GobiiFileColumn> runFields) {
		this.runFields = runFields;
	}
	public HashMap<String, GobiiFileColumn> getRunPropFields() {
		return runPropFields;
	}
	public void setRunPropFields(HashMap<String, GobiiFileColumn> runPropFields) {
		this.runPropFields = runPropFields;
	}
	
	public DTOsamples(){
		
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
	public String getExperimentName() {
		return experimentName;
	}
	public void setExperimentName(String experimentName) {
		this.experimentName = experimentName;
	}
	public String getDatasetName() {
		return datasetName;
	}
	public void setDatasetName(String datasetName) {
		this.datasetName = datasetName;
	}
}
