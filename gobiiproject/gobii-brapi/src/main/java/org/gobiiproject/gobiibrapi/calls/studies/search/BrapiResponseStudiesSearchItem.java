package org.gobiiproject.gobiibrapi.calls.studies.search;

import java.util.List;
import java.util.Map;

/**
 * Created by Phil on 12/15/2016.
 */
public class BrapiResponseStudiesSearchItem {

    private String studyDbId;
    private String name;
    private String trialDbId1;
    private String trialName;
    private String studyType;
    private List<String> seasons;
    private String locationDbId;
    private String locationName;
    private String programDbId;
    private String programName;
    private String startDate;
    private String endDate;
    boolean active;
    private Map<String, String> additionalInfo;

    public String getStudyDbId() {
        return studyDbId;
    }

    public void setStudyDbId(String studyDbId) {
        this.studyDbId = studyDbId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTrialDbId1() {
        return trialDbId1;
    }

    public void setTrialDbId1(String trialDbId1) {
        this.trialDbId1 = trialDbId1;
    }

    public String getTrialName() {
        return trialName;
    }

    public void setTrialName(String trialName) {
        this.trialName = trialName;
    }

    public String getStudyType() {
        return studyType;
    }

    public void setStudyType(String studyType) {
        this.studyType = studyType;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Map<String, String> getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(Map<String, String> additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public List<String> getSeasons() {
        return seasons;
    }

    public void setSeasons(List<String> seasons) {
        this.seasons = seasons;
    }

    public String getLocationDbId() {
        return locationDbId;
    }

    public void setLocationDbId(String locationDbId) {
        this.locationDbId = locationDbId;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getProgramDbId() {
        return programDbId;
    }

    public void setProgramDbId(String programDbId) {
        this.programDbId = programDbId;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
