package org.gobiiproject.gobiimodel.dto.container;

import org.gobiiproject.gobiimodel.dto.DtoMetaData;
import org.gobiiproject.gobiimodel.dto.annotations.GobiiEntityColumn;
import org.gobiiproject.gobiimodel.dto.annotations.GobiiEntityParam;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Phil on 4/21/2016.
 */
public class DataSetDTO extends DtoMetaData {

    public DataSetDTO() {
    }

    public DataSetDTO(ProcessType processType) {
        super(processType);
    }

    private Integer dataSetId;
    private String name;
    private Integer experimentId;
    private Integer callingAnalysisId;
//    private AnalysisDTO callingAnalysis;
    private String dataTable;
    private String dataFile;
    private String qualityTable;
    private String qualityFile;
    private Integer createdBy;
    private Date createdDate;
    private Integer modifiedBy;
    private Date modifiedDate;
    private Integer status;
    private Integer typeId;
    private List<Integer> analysesIds = new ArrayList<>();
  //  private List<AnalysisDTO> analyses = new ArrayList<>();
    private List<Integer> scores = new ArrayList<>();

    @GobiiEntityParam(paramName = "dataSetId")
    public Integer getDataSetId() {
        return dataSetId;
    }

    @GobiiEntityColumn(columnName = "dataset_id")
    public void setDataSetId(Integer dataSetId) {
        this.dataSetId = dataSetId;
    }

    @GobiiEntityParam(paramName = "name")
    public String getName() {
        return name;
    }

    @GobiiEntityColumn(columnName = "name")
    public void setName(String name) {
        this.name = name;
    }

    @GobiiEntityParam(paramName = "experimentId")
    public Integer getExperimentId() {
        return experimentId;
    }

    @GobiiEntityColumn(columnName = "experiment_id")
    public void setExperimentId(Integer experimentId) {
        this.experimentId = experimentId;
    }

//    public AnalysisDTO getCallingAnalysis() {
//        return callingAnalysis;
//    }
//    public void setCallingAnalysis(AnalysisDTO callingAnalysis) {
//        this.callingAnalysis = callingAnalysis;
//    }

    @GobiiEntityParam(paramName = "callingAnalysisId")
    public Integer getCallingAnalysisId() {
        return callingAnalysisId;
    }

    @GobiiEntityColumn(columnName = "callinganalysis_id")
    public void setCallingAnalysisId(Integer callingAnalysisId) {
        this.callingAnalysisId = callingAnalysisId;
    }


    @GobiiEntityParam(paramName = "dataTable")
    public String getDataTable() {
        return dataTable;
    }

    @GobiiEntityColumn(columnName = "data_table")
    public void setDataTable(String dataTable) {
        this.dataTable = dataTable;
    }

    @GobiiEntityParam(paramName = "dataFile")
    public String getDataFile() {
        return dataFile;
    }

    @GobiiEntityColumn(columnName = "data_file")
    public void setDataFile(String dataFile) {
        this.dataFile = dataFile;
    }

    @GobiiEntityParam(paramName = "qualityTable")
    public String getQualityTable() {
        return qualityTable;
    }

    @GobiiEntityColumn(columnName = "quality_table")
    public void setQualityTable(String qualityTable) {
        this.qualityTable = qualityTable;
    }

    @GobiiEntityParam(paramName = "qualityFile")
    public String getQualityFile() {
        return qualityFile;
    }

    @GobiiEntityColumn(columnName = "quality_file")
    public void setQualityFile(String qualityFile) {
        this.qualityFile = qualityFile;
    }

    @GobiiEntityParam(paramName = "createdBy")
    public Integer getCreatedBy() {
        return createdBy;
    }

    @GobiiEntityColumn(columnName = "created_by")
    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    @GobiiEntityParam(paramName = "createdDate")
    public Date getCreatedDate() {
        return createdDate;
    }

    @GobiiEntityColumn(columnName = "created_date")
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    @GobiiEntityParam(paramName = "modifiedBy")
    public Integer getModifiedBy() {
        return modifiedBy;
    }

     @GobiiEntityColumn(columnName = "modified_by")
    public void setModifiedBy(Integer modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    @GobiiEntityParam(paramName = "modifiedDate")
    public Date getModifiedDate() {
        return modifiedDate;
    }

    @GobiiEntityColumn(columnName = "modified_date")
    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    @GobiiEntityParam(paramName = "status")
    public Integer getStatus() {
        return status;
    }

    @GobiiEntityColumn(columnName = "status")
    public void setStatus(Integer status) {
        this.status = status;
    }


    @GobiiEntityParam(paramName = "datasetAnalysIds")
    public List<Integer> getAnalysesIds() {
        return analysesIds;
    }

    @GobiiEntityColumn(columnName = "analyses")
    public void setAnalysesIds(List<Integer> analysesIds) {
        this.analysesIds = analysesIds;
    }


//    public List<AnalysisDTO> getAnalyses() {
//        return analyses;
//    }
//    public void setAnalyses(List<AnalysisDTO> analyses) {
//        this.analyses = analyses;
//    }

    public List<Integer> getScores() {
        return scores;
    }

    public void setScores(List<Integer> scores) {
        this.scores = scores;
    }

    @GobiiEntityParam(paramName = "typeId")
    public Integer getTypeId() {
        return typeId;
    }

    @GobiiEntityColumn(columnName = "type_id")
    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }
}
