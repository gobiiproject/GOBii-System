package org.gobiiproject.gobiiclient.generic.model;

/**
 * Created by Phil on 5/19/2017.
 */
public class Person {

    public Person() {}

    public Person(String personId, String nameFirst, String nameLast, String description) {
        this.personId = personId;
        this.nameFirst = nameFirst;
        this.nameLast = nameLast;
        this.description = description;
    }

    private String personId;
    private String nameFirst;
    private String nameLast;
    private String description;

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getNameFirst() {
        return nameFirst;
    }

    public void setNameFirst(String nameFirst) {
        this.nameFirst = nameFirst;
    }

    public String getNameLast() {
        return nameLast;
    }

    public void setNameLast(String nameLast) {
        this.nameLast = nameLast;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
