package org.gobiiproject.gobiidao.entity.pojos;
// Generated Mar 31, 2016 1:44:38 PM by Hibernate Tools 3.2.2.GA


import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * DatasetMarker generated by hbm2java
 */
@Entity
@Table(name="dataset_marker"
    ,schema="public"
)
public class DatasetMarker  implements java.io.Serializable {


     private int datasetMarkerId;
     private int datasetId;
     private int markerId;
     private Float callRate;
     private Float maf;
     private Float reproducibility;
     private Serializable scores;

    public DatasetMarker() {
    }

	
    public DatasetMarker(int datasetMarkerId, int datasetId, int markerId) {
        this.datasetMarkerId = datasetMarkerId;
        this.datasetId = datasetId;
        this.markerId = markerId;
    }
    public DatasetMarker(int datasetMarkerId, int datasetId, int markerId, Float callRate, Float maf, Float reproducibility, Serializable scores) {
       this.datasetMarkerId = datasetMarkerId;
       this.datasetId = datasetId;
       this.markerId = markerId;
       this.callRate = callRate;
       this.maf = maf;
       this.reproducibility = reproducibility;
       this.scores = scores;
    }
   
     @Id 
    
    @Column(name="dataset_marker_id", unique=true, nullable=false)
    public int getDatasetMarkerId() {
        return this.datasetMarkerId;
    }
    
    public void setDatasetMarkerId(int datasetMarkerId) {
        this.datasetMarkerId = datasetMarkerId;
    }
    
    @Column(name="dataset_id", nullable=false)
    public int getDatasetId() {
        return this.datasetId;
    }
    
    public void setDatasetId(int datasetId) {
        this.datasetId = datasetId;
    }
    
    @Column(name="marker_id", nullable=false)
    public int getMarkerId() {
        return this.markerId;
    }
    
    public void setMarkerId(int markerId) {
        this.markerId = markerId;
    }
    
    @Column(name="call_rate", precision=8, scale=8)
    public Float getCallRate() {
        return this.callRate;
    }
    
    public void setCallRate(Float callRate) {
        this.callRate = callRate;
    }
    
    @Column(name="maf", precision=8, scale=8)
    public Float getMaf() {
        return this.maf;
    }
    
    public void setMaf(Float maf) {
        this.maf = maf;
    }
    
    @Column(name="reproducibility", precision=8, scale=8)
    public Float getReproducibility() {
        return this.reproducibility;
    }
    
    public void setReproducibility(Float reproducibility) {
        this.reproducibility = reproducibility;
    }
    
    @Column(name="scores")
    public Serializable getScores() {
        return this.scores;
    }
    
    public void setScores(Serializable scores) {
        this.scores = scores;
    }




}

