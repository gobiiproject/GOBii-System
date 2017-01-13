package org.gobiiproject.gobiibrapi.calls.studies.observationvariables;

import java.util.List;

/**
 * Created by Phil on 12/18/2016.
 */
public class BrapiResponseObservationVariablesDetail {


    public BrapiResponseObservationVariablesDetail() {
    }

    public BrapiResponseObservationVariablesDetail(ObservationScale scale,
                                                   String observationVariableDbId,
                                                   String name,
                                                   ObservationMethhod method,
                                                   ObservationTrait trait) {
        this.scale = scale;
        this.observationVariableDbId = observationVariableDbId;
        this.name = name;
        this.method = method;
        this.trait = trait;
    }

    private ObservationScale scale;

    private String status;

    private String ontologyName;

    private String ontologyDbId;

    private List<String> contextOfUse;

    private List<String> synonyms;

    private String scientist;

    private String date;

    private String crop;

    private String observationVariableDbId;

    private String growthStage;

    private String name;

    private String xref;

    private ObservationMethhod method;

    private String language;

    private String defaultValue;

    private ObservationTrait trait;

    private String institution;

    public ObservationScale getScale() {
        return scale;
    }

    public void setScale(ObservationScale scale) {
        this.scale = scale;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOntologyName() {
        return ontologyName;
    }

    public void setOntologyName(String ontologyName) {
        this.ontologyName = ontologyName;
    }

    public String getOntologyDbId() {
        return ontologyDbId;
    }

    public void setOntologyDbId(String ontologyDbId) {
        this.ontologyDbId = ontologyDbId;
    }

    public List<String> getContextOfUse() {
        return contextOfUse;
    }

    public void setContextOfUse(List<String> contextOfUse) {
        this.contextOfUse = contextOfUse;
    }

    public List<String> getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(List<String> synonyms) {
        this.synonyms = synonyms;
    }

    public String getScientist() {
        return scientist;
    }

    public void setScientist(String scientist) {
        this.scientist = scientist;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCrop() {
        return crop;
    }

    public void setCrop(String crop) {
        this.crop = crop;
    }

    public String getObservationVariableDbId() {
        return observationVariableDbId;
    }

    public void setObservationVariableDbId(String observationVariableDbId) {
        this.observationVariableDbId = observationVariableDbId;
    }

    public String getGrowthStage() {
        return growthStage;
    }

    public void setGrowthStage(String growthStage) {
        this.growthStage = growthStage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getXref() {
        return xref;
    }

    public void setXref(String xref) {
        this.xref = xref;
    }

    public ObservationMethhod getMethod() {
        return method;
    }

    public void setMethod(ObservationMethhod method) {
        this.method = method;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public ObservationTrait getTrait() {
        return trait;
    }

    public void setTrait(ObservationTrait trait) {
        this.trait = trait;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

}
