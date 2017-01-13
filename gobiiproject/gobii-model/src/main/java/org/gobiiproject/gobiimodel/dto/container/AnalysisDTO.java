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
public class AnalysisDTO extends DtoMetaData {

    public AnalysisDTO() {
    }

    public AnalysisDTO(ProcessType processType) {
        super(processType);
    }

    private Integer analysisId;
    private String analysisName;
    private String analysisDescription;
    private Integer anlaysisTypeId;
    private String program;
    private String programVersion;
    private String algorithm;
    private String sourceName;
    private String sourceVersion;
    private String sourceUri;
    private Integer referenceId;
    private Date timeExecuted;
    private Integer status;
    private List<EntityPropertyDTO> parameters = new ArrayList<>();

    @GobiiEntityParam(paramName = "analysisId")
    public Integer getAnalysisId() {
        return analysisId;
    }

    @GobiiEntityColumn(columnName = "analysis_id")
    public void setAnalysisId(Integer analysisId) {
        this.analysisId = analysisId;
    }

    @GobiiEntityParam(paramName = "analysisName")
    public String getAnalysisName() {
        return analysisName;
    }

    @GobiiEntityColumn(columnName = "name")
    public void setAnalysisName(String analysisName) {
        this.analysisName = analysisName;
    }

    @GobiiEntityParam(paramName = "analysisDescription")
    public String getAnalysisDescription() {
        return analysisDescription;
    }

    @GobiiEntityColumn(columnName = "description")
    public void setAnalysisDescription(String analysisDescription) {
        this.analysisDescription = analysisDescription;
    }

    @GobiiEntityParam(paramName = "analysisTypeId")
    public Integer getAnlaysisTypeId() {
        return anlaysisTypeId;
    }

    @GobiiEntityColumn(columnName = "type_id")
    public void setAnlaysisTypeId(Integer anlaysisTypeId) {
        this.anlaysisTypeId = anlaysisTypeId;
    }

    @GobiiEntityParam(paramName = "program")
    public String getProgram() {
        return program;
    }

    @GobiiEntityColumn(columnName = "program")
    public void setProgram(String program) {
        this.program = program;
    }

    @GobiiEntityParam(paramName = "programVersion")
    public String getProgramVersion() {
        return programVersion;
    }

    @GobiiEntityColumn(columnName = "programversion")
    public void setProgramVersion(String programVersion) {
        this.programVersion = programVersion;
    }

    @GobiiEntityParam(paramName = "algorithm")
    public String getAlgorithm() {
        return algorithm;
    }

    @GobiiEntityColumn(columnName = "algorithm")
    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    @GobiiEntityParam(paramName = "sourceName")
    public String getSourceName() {
        return sourceName;
    }

    @GobiiEntityColumn(columnName = "sourcename")
    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    @GobiiEntityParam(paramName = "sourceVersion")
    public String getSourceVersion() {
        return sourceVersion;
    }

    @GobiiEntityColumn(columnName = "sourceversion")
    public void setSourceVersion(String sourceVersion) {
        this.sourceVersion = sourceVersion;
    }

    @GobiiEntityParam(paramName = "sourceUri")
    public String getSourceUri() {
        return sourceUri;
    }

    @GobiiEntityColumn(columnName = "sourceuri")
    public void setSourceUri(String sourceUri) {
        this.sourceUri = sourceUri;
    }

    @GobiiEntityParam(paramName = "referenceId")
    public Integer getReferenceId() {
        return referenceId;
    }

    @GobiiEntityColumn(columnName = "reference_id")
    public void setReferenceId(Integer referenceId) {
        this.referenceId = referenceId;
    }

    @GobiiEntityParam(paramName = "timeExecuted")
    public Date getTimeExecuted() {
        return timeExecuted;
    }

    @GobiiEntityColumn(columnName = "timeexecuted")
    public void setTimeExecuted(Date timeExecuted) {
        this.timeExecuted = timeExecuted;
    }

    @GobiiEntityParam(paramName = "status")
    public Integer getStatus() {
        return status;
    }

    @GobiiEntityColumn(columnName = "status")
    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<EntityPropertyDTO> getParameters() {
        return parameters;
    }

    public void setParameters(List<EntityPropertyDTO> parameters) {
        this.parameters = parameters;
    }
}
