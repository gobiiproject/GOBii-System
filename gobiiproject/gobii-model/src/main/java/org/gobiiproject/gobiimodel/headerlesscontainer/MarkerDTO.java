package org.gobiiproject.gobiimodel.headerlesscontainer;
// Generated Mar 31, 2016 1:44:38 PM by Hibernate Tools 3.2.2.GA



import org.gobiiproject.gobiimodel.dto.annotations.GobiiEntityColumn;
import org.gobiiproject.gobiimodel.dto.annotations.GobiiEntityParam;

import java.util.List;

public class MarkerDTO extends  DTOBase {


    private Integer markerId = 0;
    private Integer platformId;
    private Integer variantId;
    private String markerName;
    private String platformName;
    private String code;
    private String ref;
    private List<Integer> alts;
    private String sequence;
    private Integer referenceId;
    private Integer strandId;
    private Integer status;



    public MarkerDTO() {
    }

    @Override
    public Integer getId() {
        return this.markerId;
    }

    @Override
    public void setId(Integer id) {
        this.markerId = id;
    }

    @GobiiEntityParam(paramName = "platformId")
    public Integer getPlatformId() {
        return(this.platformId);
    }

    @GobiiEntityColumn(columnName = "platform_id")
    public void setPlatformId(Integer platformId) {
        this.platformId = platformId;
    }

    @GobiiEntityParam(paramName = "alts")
    public List<Integer> getAlts() {
        return alts;
    }

    @GobiiEntityColumn(columnName = "alts")
    public void setAlts(List<Integer> alts) {
        this.alts = alts;
    }

    @GobiiEntityParam(paramName = "strandId")
    public Integer getStrandId() {
        return strandId;
    }

    @GobiiEntityColumn(columnName = "strand_id")
    public void setStrandId(Integer strandId) {
        this.strandId = strandId;
    }

    @GobiiEntityParam(paramName = "markerId")
    public Integer getMarkerId() {
        return this.markerId;
    }

    @GobiiEntityColumn(columnName = "marker_id")
    public void setMarkerId(Integer markerId) {
        this.markerId = markerId;
    }

    @GobiiEntityParam(paramName = "variantId")
    public Integer getVariantId() {
        return this.variantId;
    }

    @GobiiEntityColumn(columnName = "variant_id")
    public void setVariantId(Integer variantId) {
        this.variantId = variantId;
    }

    @GobiiEntityParam(paramName = "markerName")
    public String getMarkerName() {
        return this.markerName;
    }

    @GobiiEntityColumn(columnName = "marker_name")
    public void setMarkerName(String markerName) {
        this.markerName = markerName;
    }

    @GobiiEntityParam(paramName = "platformName")
    public String getPlatformName() {
        return platformName;
    }

    @GobiiEntityColumn(columnName = "platform_name")
    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    @GobiiEntityParam(paramName = "code")
    public String getCode() {
        return this.code;
    }

    @GobiiEntityColumn(columnName = "code")
    public void setCode(String code) {
        this.code = code;
    }

    @GobiiEntityParam(paramName = "ref")
    public String getRef() {
        return this.ref;
    }

    @GobiiEntityColumn(columnName = "ref")
    public void setRef(String ref) {
        this.ref = ref;
    }

    @GobiiEntityParam(paramName = "sequence")
    public String getSequence() {
        return this.sequence;
    }

    @GobiiEntityColumn(columnName = "sequence")
    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    @GobiiEntityParam(paramName = "referenceId")
    public Integer getReferenceId() {
        return this.referenceId;
    }

    @GobiiEntityColumn(columnName = "reference_id")
    public void setReferenceId(Integer referenceId) {
        this.referenceId = referenceId;
    }

    @GobiiEntityParam(paramName = "strandId")
    public Integer getStrand() {
        return this.strandId;
    }

    @GobiiEntityColumn(columnName = "strand_id")
    public void setStrand(Integer strandId) {
        this.strandId = strandId;
    }

    @GobiiEntityParam(paramName = "status")
    public Integer getStatus() {
        return this.status;
    }

    @GobiiEntityColumn(columnName = "status")
    public void setStatus(Integer status) {
        this.status = status;
    }

}


