package edu.cornell.gobii.gdi.utils;

import java.util.HashMap;
import java.util.List;

import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiFile;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiFileColumn;
import org.gobiiproject.gobiimodel.headerlesscontainer.LoaderFilePreviewDTO;
import org.gobiiproject.gobiimodel.types.GobiiColumnType;

public interface WizardDTO {

	public LoaderFilePreviewDTO getPreviewDTO();
	public void setPreviewDTO(LoaderFilePreviewDTO previewDTO);
	public String getTemplate();
	public void setTemplate(String template);
	public boolean isRemote();
	public void setRemote(boolean isRemote);
	public String getFileExtention();
	public void setFileExtention(String fileExtention);
	public Integer getDatasetID();
	public void setDatasetID(Integer datasetID);
	public Integer getProjectID();
	public void setProjectID(Integer projectID);
	public Integer getExperimentID();
	public void setExperimentID(Integer experiemtnID);
	public List<String[]> getHeader();
	public void setHeader(List<String[]> header);
	public GobiiFile getFile();
	public void setFile(GobiiFile file);
	public List<String> getFiles();
	public void setFiles(List<String> files);
	public GobiiColumnType getColumnType();
	public void setColumnType(GobiiColumnType columnType);
	public int getrCoord();
	public void setrCoord(int rCoord);
	public int getcCoord();
	public void setcCoord(int cCoord);
	public String getProjectName();
	public void setProjectName(String projectName);
	public String getPlatformName();
	public void setPlatformName(String platformName);
	public String getExperimentName();
	public void setExperimentName(String experimentName);
	public String getDatasetName();
	public void setDatasetName(String datasetName);
}
