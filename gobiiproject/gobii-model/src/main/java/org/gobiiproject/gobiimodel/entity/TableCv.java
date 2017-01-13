// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-24
// ************************************************************************
package org.gobiiproject.gobiimodel.entity;

import java.util.Objects;

public class TableCv {

	private int cv_id;
	private String group;
	private String term;
	private String definition;
	private int rank;
	public int getCv_id() {
		return cv_id;
	}
	public void setCv_id(int cv_id) {
		this.cv_id = cv_id;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
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
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	
}
