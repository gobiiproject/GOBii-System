package org.gobiiproject.gobiimodel.headerlesscontainer;

import org.gobiiproject.gobiimodel.dto.annotations.GobiiEntityColumn;
import org.gobiiproject.gobiimodel.dto.annotations.GobiiEntityParam;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by Angel on 5/6/2016.
 */
public class ContactDTO extends DTOBase {

    public ContactDTO() {
    }


    // we are waiting until we a have a view to retirn
    // properties for that property: we don't know how to represent them yet
    private Integer contactId = 0;

    private String lastName;
    private String firstName;
    private String code;
    private String email;
    private List<Integer> roles = new ArrayList<>();
    private Integer createdBy;
    private Date createdDate;
    private Integer modifiedBy;
    private Date modifiedDate;
    private Integer organizationId;
    private String userName;

    @Override
    public Integer getId() {
        return this.contactId;
    }

    @Override
    public void setId(Integer id) {
        this.contactId = id;
    }

    @GobiiEntityParam(paramName = "contactId")
    public Integer getContactId() {
        return contactId;
    }

    @GobiiEntityColumn(columnName = "contact_id")
    public void setContactId(Integer contactId) {
        this.contactId = contactId;
    }

    @GobiiEntityParam(paramName = "lastName")
    public String getLastName() {
        return lastName;
    }

    @GobiiEntityColumn(columnName = "lastname")
    public void setLastName(String projectName) {
        this.lastName = projectName;
    }

    @GobiiEntityParam(paramName = "firstName")
    public String getFirstName() {
        return firstName;
    }

    @GobiiEntityColumn(columnName = "firstname")
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @GobiiEntityParam(paramName = "code")
    public String getCode() {
        return code;
    }

    @GobiiEntityColumn(columnName = "code")
    public void setCode(String code) {
        this.code = code;
    }

    @GobiiEntityParam(paramName = "email")
    public String getEmail() {
        return email;
    }

    @GobiiEntityColumn(columnName = "email")
    public void setEmail(String email) {
        this.email = email;
    }

    @GobiiEntityParam(paramName = "roles")
    public List<Integer> getRoles() {
        return roles;
    }

    @GobiiEntityColumn(columnName = "roles")
    public void setRoles(List<Integer> roles) {
        this.roles = roles;
    }

    @GobiiEntityParam(paramName = "organizationId")
    public Integer getOrganizationId() {
        return organizationId;
    }

    @GobiiEntityColumn(columnName = "organization_id")
    public void setOrganizationId(Integer organizationId) {
        this.organizationId = organizationId;
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

    @GobiiEntityParam(paramName = "username")
    public String getUserName() { return userName; }

    @GobiiEntityColumn(columnName = "username")
    public void setUserName(String userName) {this.userName = userName; }


}
