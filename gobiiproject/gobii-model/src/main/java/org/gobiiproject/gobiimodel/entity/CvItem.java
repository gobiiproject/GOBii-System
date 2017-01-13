package org.gobiiproject.gobiimodel.entity;

/**
 * Created by araquel on 5/16/2016.
 */
public class CvItem {

    private Integer cvId;
    private String term;
    private String definition;
    private Integer rank;


    public Integer getCvId() {
        return cvId;
    }

    public void setCvId(Integer cvId) {
        this.cvId = cvId;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }
}
