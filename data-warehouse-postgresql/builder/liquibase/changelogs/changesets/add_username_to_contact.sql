--liquibase formatted sql

--changeset kpalis:add_username_to_contact context:general splitStatements:false
ALTER TABLE contact ADD COLUMN username text;

DROP FUNCTION IF EXISTS createcontact(lastname text, firstname text, contactcode text, contactemail text, contactroles integer[], createdby integer, createddate date, modifiedby integer, modifieddate date, organizationid integer, OUT id integer);
CREATE OR REPLACE FUNCTION createcontact(lastname text, firstname text, contactcode text, contactemail text, contactroles integer[], createdby integer, createddate date, modifiedby integer, modifieddate date, organizationid integer, uname text, OUT id integer)
 RETURNS integer
 LANGUAGE plpgsql
AS $function$
  BEGIN
    insert into contact (lastname, firstname, code, email, roles, created_by, created_date, modified_by, modified_date, organization_id, username)
      values (lastName, firstName, contactCode, contactEmail, contactRoles, createdBy, createdDate, modifiedBy, modifiedDate, organizationId, uname);
    select lastval() into id;
  END;
$function$;

DROP FUNCTION IF EXISTS updatecontact(contactid integer, contactlastname text, contactfirstname text, contactcode text, contactemail text, contactroles integer[], createdby integer, createddate date, modifiedby integer, modifieddate date, organizationid integer);
CREATE OR REPLACE FUNCTION updatecontact(contactid integer, contactlastname text, contactfirstname text, contactcode text, contactemail text, contactroles integer[], createdby integer, createddate date, modifiedby integer, modifieddate date, organizationid integer, uname text)
 RETURNS void
 LANGUAGE plpgsql
AS $function$
    BEGIN
    update contact set lastname=contactLastName, firstname=contactFirstName, code=contactCode, email=contactEmail, roles=contactRoles, created_by=createdBy, created_date=createdDate,
      modified_by=modifiedBy, modified_date=modifiedDate, organization_id=organizationId, username=uname
     where contact_id = contactId;
    END;
$function$;