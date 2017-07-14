--liquibase formatted sql
/*
Issue #: GP1-687
Liquibase changeset: Fix functions- drop *_prop tables, use props (jsonb) column in main entity table
*/

--changeset venice.juanillas:fix_functions_dnasample context:general splitStatements:false
DROP FUNCTION IF EXISTS creatednasample(integer);

CREATE OR REPLACE FUNCTION creatednasample(dnasamplename text, dnasamplecode text, dnasampleplatename text, dnasamplenum text, wellrow text, wellcol text, projectid integer, germplasmid integer, createdby integer, createddate date, modifiedby integer, modifieddate date, dnasamplestatus integer, OUT id integer)
RETURNS integer AS $$
BEGIN
	insert into dnasample(name, code, platename, num, well_row, well_col, project_id, germplasm_id, created_by, created_date, modified_by, modified_date, status)
	values(dnasamplename, dnasamplecode, dnasampleplatename, dnasamplenum, wellrow, wellcol, projectid, germplasmid, createdby, createddate, modifiedby, modifieddate,dnasamplestatus);
END;
$$ LANGUAGE plpgsql;

--drop deletednasamplepropertybyid
DROP FUNCTION IF EXISTS deletednasamplepropertybyid(integer);

CREATE OR REPLACE FUNCTION deleteDnaSamplePropertyById(id integer, propertyId integer)
RETURNS integer AS $$
  BEGIN
    update dnasample
    set props = props - propertyId::text
    where dnasample_id=id;
    return propertyId;
  END;
$$ LANGUAGE plpgsql;


--delete property by name
DROP FUNCTION IF EXISTS deleteDnaSamplePropertyByName(text);

CREATE OR REPLACE FUNCTION deleteDnaSamplePropertyByName(id integer, propertyName text)
RETURNS text AS $$
  BEGIN
    with property as (select cv_id from cv where term=propertyName)
    update dnasample
      set props = props - property.cv_id::text
      from property
      where dnasample_id=id;
    return propertyName;
  END;
$$ LANGUAGE plpgsql;


--getallpropertiesofdnasample
DROP FUNCTION IF EXISTS getallpropertiesofdnasample(integer,text,text);

CREATE OR REPLACE FUNCTION getAllPropertiesOfDnaSample(id integer)
RETURNS table (property_id integer, property_name text, property_value text) AS $$
  BEGIN
    return query
    select p1.key::int as property_id, cv.term as property_name, p1.value as property_value
    from cv, (select (jsonb_each_text(props)).* from dnasample where dnasample_id=id) as p1
    where cv.cv_id = p1.key::int;
    END;
$$ LANGUAGE plpgsql;


--getDnaSamplePropertyById
DROP FUNCTION IF EXISTS getDnaSamplePropertyById(text);

CREATE OR REPLACE FUNCTION getDnaSamplePropertyById(id integer, propertyId integer)
RETURNS text AS $$
  DECLARE
    value text;
  BEGIN
    select props->propertyId::text into value from dnasample where dnasample_id=id;
    return value;
  END;
$$ LANGUAGE plpgsql;

--getdnasamplepropertybyname
DROP FUNCTION IF EXISTS getDnaSamplePropertyByName(integer,text);

CREATE OR REPLACE FUNCTION getDnaSamplePropertyByName(id integer, propertyName text)
RETURNS table (property_id integer, property_value text) AS $$
  BEGIN
    return query
    (with property as (select cv_id from cv where term=propertyName)
    select property.cv_id, (props->property.cv_id::text)::text as value
      from dnasample, property
      where dnasample_id=id);
  END;
$$ LANGUAGE plpgsql;

--updateDnaSamplePropertyById
DROP FUNCTION IF EXISTS updateDnaSamplePropertyById();

CREATE OR REPLACE FUNCTION updateDnaSamplePropertyById(id integer, propertyId integer, propertyValue text)
RETURNS void AS $$
  BEGIN
    update dnasample set props = jsonb_set(props, ('{'||propertyId::text||'}')::text[], ('"'||propertyValue||'"')::jsonb)
      where dnasample_id=id;
  END;
$$ LANGUAGE plpgsql;

--updatednasamplepropertybyname
DROP FUNCTION IF EXISTS updatednasamplepropertybyname();

CREATE OR REPLACE FUNCTION updateDnaSamplePropertyByName(id integer, propertyName text, propertyValue text)
RETURNS void AS $$
  BEGIN
    with property as (select cv_id from cv where term=propertyName)
    update dnasample
      set props = jsonb_set(props, ('{'||property.cv_id::text||'}')::text[], ('"'||propertyValue||'"')::jsonb)
      from property
      where dnasample_id=id;
  END;
$$ LANGUAGE plpgsql;

--upsertdnasamplepropertybyid
DROP FUNCTION IF EXISTS upsertDnaSamplePropertyById();

CREATE OR REPLACE FUNCTION upsertDnaSamplePropertyById(id integer, propertyId integer, propertyValue text)
RETURNS void AS $$
  BEGIN
    update dnasample set props = props || ('{"'||propertyId::text||'": "'||propertyValue||'"}')::jsonb
      where dnasample_id=id;
  END;
$$ LANGUAGE plpgsql;

--upsertdnasamplepropertybyname
DROP FUNCTION IF EXISTS upsertDnaSamplePropertyByName(integer);

CREATE OR REPLACE FUNCTION upsertDnaSamplePropertyByName(id integer, propertyName text, propertyValue text)
RETURNS integer AS $$
  DECLARE
    propertyId integer;
  BEGIN
    select cv_id into propertyId from cv where term=propertyName;
    update dnasample set props = props || ('{"'||propertyId::text||'": "'||propertyValue||'"}')::jsonb
      where dnasample_id=id;
    return propertyId;
  END;
$$ LANGUAGE plpgsql;

--changeset venice.juanillas:fix_functions_platform context:general splitStatements:false
DROP FUNCTION IF EXISTS createPlatform(integer);

CREATE OR REPLACE FUNCTION createPlatform(platformName text, platformCode text, vendorId integer, platformDescription text, createdBy integer, createdDate date, modifiedBy integer, modifiedDate date, platformStatus integer, typeId integer, OUT id integer)
RETURNS integer AS $$
  BEGIN
    insert into platform (name, code, vendor_id, description, created_by, created_date, modified_by, modified_date, status, type_id)
      values (platformName, platformCode, vendorId, platformDescription, createdBy, createdDate, modifiedBy, modifiedDate, platformStatus, typeId);
  END;
$$ LANGUAGE plpgsql;

--deleteplatformpropertybyid
DROP FUNCTION IF EXISTS deletePlatformPropertyById(integer);

CREATE OR REPLACE FUNCTION deletePlatformPropertyById(id integer, propertyId integer)
RETURNS integer AS $$
  BEGIN
    update platform
    set props = props - propertyId::text
    where platform_id=id;
    return propertyId;
  END;
$$ LANGUAGE plpgsql;

--delete property by name
DROP FUNCTION IF EXISTS deletePlatformPropertyByName(text);

CREATE OR REPLACE FUNCTION deletePlatformPropertyByName(id integer, propertyName text)
RETURNS text AS $$
  BEGIN
    with property as (select cv_id from cv where term=propertyName)
    update platform
      set props = props - property.cv_id::text
      from property
      where platform_id=id;
    return propertyName;
  END;
$$ LANGUAGE plpgsql;

--getallpropertiesofplatform
DROP FUNCTION IF EXISTS getAllPropertiesOfPlatform(integer,text,text);

CREATE OR REPLACE FUNCTION getAllPropertiesOfPlatform(id integer)
RETURNS table (property_id integer, property_name text, property_value text) AS $$
  BEGIN
    return query
    select p1.key::int as property_id, cv.term as property_name, p1.value as property_value
    from cv, (select (jsonb_each_text(props)).* from platform where platform_id=id) as p1
    where cv.cv_id = p1.key::int;
    END;
$$ LANGUAGE plpgsql;

--getplatformpropertybyid
DROP FUNCTION IF EXISTS getPlatformPropertyById(text);

CREATE OR REPLACE FUNCTION getPlatformPropertyById(id integer, propertyId integer)
RETURNS text AS $$
  DECLARE
    value text;
  BEGIN
    select props->propertyId::text into value from platform where platform_id=id;
    return value;
  END;
$$ LANGUAGE plpgsql;

--getplatformpropertybyname
DROP FUNCTION IF EXISTS getPlatformPropertyByName(integer,text);

CREATE OR REPLACE FUNCTION getPlatformPropertyByName(id integer, propertyName text)
RETURNS table (property_id integer, property_value text) AS $$
  BEGIN
    return query
    (with property as (select cv_id from cv where term=propertyName)
    select property.cv_id, (props->property.cv_id::text)::text as value
      from platform, property
      where platform_id=id);
  END;
$$ LANGUAGE plpgsql;

--updateplatformpropertybyid
DROP FUNCTION IF EXISTS updatePlatformPropertyById();

CREATE OR REPLACE FUNCTION updatePlatformPropertyById(id integer, propertyId integer, propertyValue text)
RETURNS void AS $$
  BEGIN
    update platform set props = jsonb_set(props, ('{'||propertyId::text||'}')::text[], ('"'||propertyValue||'"')::jsonb)
      where platform_id=id;
  END;
$$ LANGUAGE plpgsql;

--updateplatformpropertybyid
DROP FUNCTION IF EXISTS updatePlatformPropertyByName();

CREATE OR REPLACE FUNCTION updatePlatformPropertyByName(id integer, propertyName text, propertyValue text)
RETURNS void AS $$
  BEGIN
    with property as (select cv_id from cv where term=propertyName)
    update platform
      set props = jsonb_set(props, ('{'||property.cv_id::text||'}')::text[], ('"'||propertyValue||'"')::jsonb)
      from property
      where platform_id=id;
  END;
$$ LANGUAGE plpgsql;

--upsertplatformpropertybyid
DROP FUNCTION IF EXISTS updatePlatformPropertyById();

CREATE OR REPLACE FUNCTION upsertPlatformPropertyById(id integer, propertyId integer, propertyValue text)
RETURNS void AS $$
  BEGIN
    update platform set props = props || ('{"'||propertyId::text||'": "'||propertyValue||'"}')::jsonb
      where platform_id=id;
  END;
$$ LANGUAGE plpgsql;

--upsert by property name
DROP FUNCTION IF EXISTS updatePlatformPropertyByName(integer);

CREATE OR REPLACE FUNCTION upsertPlatformPropertyByName(id integer, propertyName text, propertyValue text)
RETURNS integer AS $$
  DECLARE
    propertyId integer;
  BEGIN
    select cv_id into propertyId from cv where term=propertyName;
    update platform set props = props || ('{"'||propertyId::text||'": "'||propertyValue||'"}')::jsonb
      where platform_id=id;
    return propertyId;
  END;
$$ LANGUAGE plpgsql;

--changeset venice.juanillas:fix_functions_dnarun context:general splitStatements:false
--createDnarun
DROP FUNCTION IF EXISTS createDnarun(integer);

CREATE OR REPLACE FUNCTION createDnarun(experimentId integer, dnasampleId integer, dnarunName text, dnarunCode text, OUT id integer)
RETURNS integer AS $$
  BEGIN
    insert into dnarun (experiment_id, dnasample_id, name, code)
      values (experimentId, dnasampleId, dnarunName, dnarunCode);
  END;
$$ LANGUAGE plpgsql;

--deletednarunpropertybyid
DROP FUNCTION IF EXISTS deletednarunpropertybyid(integer);

CREATE OR REPLACE FUNCTION deleteDnarunPropertyById(id integer, propertyId integer)
RETURNS integer AS $$
  BEGIN
    update dnarun
    set props = props - propertyId::text
    where dnarun_id=id;
    return propertyId;
  END;
$$ LANGUAGE plpgsql;

--delete property by name
DROP FUNCTION IF EXISTS deletednarunpropertybyname(text);

CREATE OR REPLACE FUNCTION deleteDnarunPropertyByName(id integer, propertyName text)
RETURNS text AS $$
  BEGIN
    with property as (select cv_id from cv where term=propertyName)
    update dnarun
      set props = props - property.cv_id::text
      from property
      where dnarun_id=id;
    return propertyName;
  END;
$$ LANGUAGE plpgsql;

--getallpropertiesofdnarun
DROP FUNCTION IF EXISTS getAllPropertiesOfDnarun(integer,text,text);

CREATE OR REPLACE FUNCTION getAllPropertiesOfDnarun(id integer)
RETURNS table (property_id integer, property_name text, property_value text) AS $$
  BEGIN
    return query
    select p1.key::int as property_id, cv.term as property_name, p1.value as property_value
    from cv, (select (jsonb_each_text(props)).* from dnarun where dnarun_id=id) as p1
    where cv.cv_id = p1.key::int;
    END;
$$ LANGUAGE plpgsql;

--getdnarunpropertybyid
DROP FUNCTION IF EXISTS getDnarunPropertyById(text);

CREATE OR REPLACE FUNCTION getDnarunPropertyById(id integer, propertyId integer)
RETURNS text AS $$
  DECLARE
    value text;
  BEGIN
    select props->propertyId::text into value from dnarun where dnarun_id=id;
    return value;
  END;
$$ LANGUAGE plpgsql;

--getdnarunpropertybyname
DROP FUNCTION IF EXISTS getDnarunPropertyByName(integer,text);

CREATE OR REPLACE FUNCTION getDnarunPropertyByName(id integer, propertyName text)
RETURNS table (property_id integer, property_value text) AS $$
  BEGIN
    return query
    (with property as (select cv_id from cv where term=propertyName)
    select property.cv_id, (props->property.cv_id::text)::text as value
      from dnarun, property
      where dnarun_id=id);
  END;
$$ LANGUAGE plpgsql;

--updatednarunpropertybyid
DROP FUNCTION IF EXISTS updateDnarunPropertyById();

CREATE OR REPLACE FUNCTION updateDnarunPropertyById(id integer, propertyId integer, propertyValue text)
RETURNS void AS $$
  BEGIN
    update dnarun set props = jsonb_set(props, ('{'||propertyId::text||'}')::text[], ('"'||propertyValue||'"')::jsonb)
      where dnarun_id=id;
  END;
$$ LANGUAGE plpgsql;

--updatednarunpropertybyname
DROP FUNCTION IF EXISTS updateDnarunPropertyByName();

CREATE OR REPLACE FUNCTION updateDnarunPropertyByName(id integer, propertyName text, propertyValue text)
RETURNS void AS $$
  BEGIN
    with property as (select cv_id from cv where term=propertyName)
    update dnarun
      set props = jsonb_set(props, ('{'||property.cv_id::text||'}')::text[], ('"'||propertyValue||'"')::jsonb)
      from property
      where dnarun_id=id;
  END;
$$ LANGUAGE plpgsql;


--upsertdnarunpropertybyid
DROP FUNCTION IF EXISTS upsertDnarunPropertyById();

CREATE OR REPLACE FUNCTION upsertDnarunPropertyById(id integer, propertyId integer, propertyValue text)
RETURNS void AS $$
  BEGIN
    update dnarun set props = props || ('{"'||propertyId::text||'": "'||propertyValue||'"}')::jsonb
      where dnarun_id=id;
  END;
$$ LANGUAGE plpgsql;

--upsertdnarunpropertybyname
DROP FUNCTION IF EXISTS upsertDnarunPropertyByName(integer);

CREATE OR REPLACE FUNCTION upsertDnarunPropertyByName(id integer, propertyName text, propertyValue text)
RETURNS integer AS $$
  DECLARE
    propertyId integer;
  BEGIN
    select cv_id into propertyId from cv where term=propertyName;
    update dnarun set props = props || ('{"'||propertyId::text||'": "'||propertyValue||'"}')::jsonb
      where dnarun_id=id;
    return propertyId;
  END;
$$ LANGUAGE plpgsql;


--changeset venice.juanillas:fix_functions_project context:general splitStatements:false
--createproject
DROP FUNCTION IF EXISTS createProject(integer);

CREATE OR REPLACE FUNCTION createProject(projectName text, projectCode text, projectDescription text, piContact integer, createdBy integer, createdDate date,
  modifiedBy integer, modifiedDate date, projectStatus integer, OUT projectId integer)
RETURNS integer AS $$
    BEGIN
    insert into project (name, code, description, pi_contact, created_by, created_date, modified_by, modified_date, status)
      values (projectName, projectCode, projectDescription, piContact, createdBy, createdDate, modifiedBy, modifiedDate, projectStatus);
    END;
$$ LANGUAGE plpgsql;

--deleteprojectpropertybyid
DROP FUNCTION IF EXISTS deleteProjectPropertyById(integer);

CREATE OR REPLACE FUNCTION deleteProjectPropertyById(projectId integer, propertyId integer)
RETURNS integer AS $$
  BEGIN
    update project
    set props = props - propertyId::text
    where project_id=projectId;
    return propertyId;
  END;
$$ LANGUAGE plpgsql;

--deletePropertyByName
DROP FUNCTION IF EXISTS deleteProjectPropertyByName(text);

CREATE OR REPLACE FUNCTION deleteProjectPropertyByName(projectId integer, propertyName text)
RETURNS text AS $$
  BEGIN
    with property as (select cv_id from cv where term=propertyName)
    update project
      set props = props - property.cv_id::text
      from property
      where project_id=projectId;
    return propertyName;
  END;
$$ LANGUAGE plpgsql;

--getallpropertiesofproject
DROP FUNCTION IF EXISTS getAllPropertiesOfProject(integer,text,text);

CREATE OR REPLACE FUNCTION getAllPropertiesOfProject(projectId integer)
RETURNS table (property_id integer, property_name text, property_value text) AS $$
  BEGIN
    return query
    select p1.key::int as property_id, cv.term as property_name, p1.value as property_value
    from cv, (select (jsonb_each_text(props)).* from project where project_id=projectId) as p1
    where cv.cv_id = p1.key::int;
    END;
$$ LANGUAGE plpgsql;

--getprojectpropertybyid
DROP FUNCTION IF EXISTS getProjectPropertyById(text);

CREATE OR REPLACE FUNCTION getProjectPropertyById(projectId integer, propertyId integer)
RETURNS text AS $$
  DECLARE
    value text;
  BEGIN
    select props->propertyId::text into value from project where project_id=projectId;
    return value;
  END;
$$ LANGUAGE plpgsql;

--getprojectpropertybyname
DROP FUNCTION IF EXISTS getProjectPropertyByName(integer,text);

CREATE OR REPLACE FUNCTION getProjectPropertyByName(projectId integer, propertyName text)
RETURNS table (property_id integer, property_value text) AS $$
  BEGIN
    return query
    (with property as (select cv_id from cv where term=propertyName)
    select property.cv_id, (props->property.cv_id::text)::text as value
      from project, property
      where project_id=projectId);
  END;
$$ LANGUAGE plpgsql;

--updateprojectpropertybyid
DROP FUNCTION IF EXISTS updateProjectPropertyById();

CREATE OR REPLACE FUNCTION updateProjectPropertyById(projectId integer, propertyId integer, propertyValue text)
RETURNS void AS $$
  BEGIN
    update project set props = jsonb_set(props, ('{'||propertyId::text||'}')::text[], ('"'||propertyValue||'"')::jsonb)
      where project_id=projectId;
  END;
$$ LANGUAGE plpgsql;

--updateprojectpropertybyname
DROP FUNCTION IF EXISTS updateProjectPropertyByName();

CREATE OR REPLACE FUNCTION updateProjectPropertyByName(projectId integer, propertyName text, propertyValue text)
RETURNS void AS $$
  BEGIN
    with property as (select cv_id from cv where term=propertyName)
    update project
      set props = jsonb_set(props, ('{'||property.cv_id::text||'}')::text[], ('"'||propertyValue||'"')::jsonb)
      from property
      where project_id=projectId;
  END;
$$ LANGUAGE plpgsql;

--upsertprojectpropertybyid
DROP FUNCTION IF EXISTS upsertProjectPropertyById();

CREATE OR REPLACE FUNCTION upsertProjectPropertyById(id integer, propertyId integer, propertyValue text)
RETURNS void AS $$
  BEGIN
    update project set props = props || ('{"'||propertyId::text||'": "'||propertyValue||'"}')::jsonb
      where project_id=id;
  END;
$$ LANGUAGE plpgsql;

--upsert by name
DROP FUNCTION IF EXISTS upsertProjectPropertyByName(integer);

CREATE OR REPLACE FUNCTION upsertProjectPropertyByName(id integer, propertyName text, propertyValue text)
RETURNS integer AS $$
  DECLARE
    propertyId integer;
  BEGIN
    select cv_id into propertyId from cv where term=propertyName;
    update project set props = props || ('{"'||propertyId::text||'": "'||propertyValue||'"}')::jsonb
      where project_id=id;
    return propertyId;
  END;
$$ LANGUAGE plpgsql;

--changeset venice.juanillas:fix_functions_germplasm context:general splitStatements:false
--creategermplasm
DROP FUNCTION IF EXISTS createGermplasm(integer);

CREATE OR REPLACE FUNCTION createGermplasm(germplasmName text, externalCode text, speciesId integer, typeId integer, createdBy integer, createdDate date, modifiedBy integer, modifiedDate date, germplasmStatus integer, germplasmCode text, OUT id integer)
RETURNS integer AS $$
  BEGIN
    insert into germplasm (name, external_code, species_id, type_id, created_by, created_date, modified_by, modified_date, status, code)
      values (germplasmName, externalCode, speciesId, typeId, createdBy, createdDate, modifiedBy, modifiedDate, germplasmStatus, germplasmCode);
  END;
$$ LANGUAGE plpgsql;

--deletegermplasmpropertybyid
DROP FUNCTION IF EXISTS deleteGermplasmPropertyById(integer);

CREATE OR REPLACE FUNCTION deleteGermplasmPropertyById(id integer, propertyId integer)
RETURNS integer AS $$
  BEGIN
    update germplasm
    set props = props - propertyId::text
    where germplasm_id=id;
    return propertyId;
  END;
$$ LANGUAGE plpgsql;

--deletePropertyByName
DROP FUNCTION IF EXISTS deleteGermplasmPropertyByName(text);

CREATE OR REPLACE FUNCTION deleteGermplasmPropertyByName(id integer, propertyName text)
RETURNS text AS $$
  BEGIN
    with property as (select cv_id from cv where term=propertyName)
    update germplasm
      set props = props - property.cv_id::text
      from property
      where germplasm_id=id;
    return propertyName;
  END;
$$ LANGUAGE plpgsql;

--getallpropertiesofgermplasm
DROP FUNCTION IF EXISTS getAllPropertiesOfGermplasm(integer, text, text);

CREATE OR REPLACE FUNCTION getAllPropertiesOfGermplasm(id integer)
RETURNS table (property_id integer, property_name text, property_value text) AS $$
  BEGIN
    return query
    select p1.key::int as property_id, cv.term as property_name, p1.value as property_value
    from cv, (select (jsonb_each_text(props)).* from germplasm where germplasm_id=id) as p1
    where cv.cv_id = p1.key::int;
    END;
$$ LANGUAGE plpgsql;

--getgermplasmpropertybyid
DROP FUNCTION IF EXISTS getGermplasmPropertyById(text);

CREATE OR REPLACE FUNCTION getGermplasmPropertyById(id integer, propertyId integer)
RETURNS text AS $$
  DECLARE
    value text;
  BEGIN
    select props->propertyId::text into value from germplasm where germplasm_id=id;
    return value;
  END;
$$ LANGUAGE plpgsql;

--getgermplasmpropertybyname
DROP FUNCTION IF EXISTS getGermplasmPropertyByName(integer, text);

CREATE OR REPLACE FUNCTION getGermplasmPropertyByName(id integer, propertyName text)
RETURNS table (property_id integer, property_value text) AS $$
  BEGIN
    return query
    (with property as (select cv_id from cv where term=propertyName)
    select property.cv_id, (props->property.cv_id::text)::text as value
      from germplasm, property
      where germplasm_id=id);
  END;
$$ LANGUAGE plpgsql;

--updategermplasmpropertybyid
DROP FUNCTION IF EXISTS updateGermplasmPropertyById();

CREATE OR REPLACE FUNCTION updateGermplasmPropertyById(id integer, propertyId integer, propertyValue text)
RETURNS void AS $$
  BEGIN
    update germplasm set props = jsonb_set(props, ('{'||propertyId::text||'}')::text[], ('"'||propertyValue||'"')::jsonb)
      where germplasm_id=id;
  END;
$$ LANGUAGE plpgsql;

--updategermplasmpropertybyname
DROP FUNCTION IF EXISTS updateGermplasmPropertyByName();

CREATE OR REPLACE FUNCTION updateGermplasmPropertyByName(id integer, propertyName text, propertyValue text)
RETURNS void AS $$
  BEGIN
    with property as (select cv_id from cv where term=propertyName)
    update germplasm
      set props = jsonb_set(props, ('{'||property.cv_id::text||'}')::text[], ('"'||propertyValue||'"')::jsonb)
      from property
      where germplasm_id=id;
  END;
$$ LANGUAGE plpgsql;

--upsertgermplasmpropertybyid
DROP FUNCTION IF EXISTS upsertGermplasmPropertyById();

CREATE OR REPLACE FUNCTION upsertGermplasmPropertyById(id integer, propertyId integer, propertyValue text)
RETURNS void AS $$
  BEGIN
    update germplasm set props = props || ('{"'||propertyId::text||'": "'||propertyValue||'"}')::jsonb
      where germplasm_id=id;
  END;
$$ LANGUAGE plpgsql;

--upsertByPropertyName
DROP FUNCTION IF EXISTS upsertGermplasmPropertyByName(integer);

CREATE OR REPLACE FUNCTION upsertGermplasmPropertyByName(id integer, propertyName text, propertyValue text)
RETURNS integer AS $$
  DECLARE
    propertyId integer;
  BEGIN
    select cv_id into propertyId from cv where term=propertyName;
    update germplasm set props = props || ('{"'||propertyId::text||'": "'||propertyValue||'"}')::jsonb
      where germplasm_id=id;
    return propertyId;
  END;
$$ LANGUAGE plpgsql;


--changeset venice.juanillas:fix_functions_marker context:general splitStatements:false
DROP FUNCTION IF EXISTS createMarker(integer);

CREATE OR REPLACE FUNCTION createMarker(platformId integer, variantId integer, markerName text, markerCode text, markerRef text, markerAlts text[], markerSequence text, referenceId integer, strandId integer, markerStatus integer, OUT id integer)
RETURNS integer AS $$
  BEGIN
    insert into marker (marker_id, platform_id, variant_id, name, code, ref, alts, sequence, reference_id, primers, probsets, strand_id, status)
      values (markerId, platformId, variantId, markerName, markerCode, markerRef, markerAlts, markerSequence, referenceId, '{}'::jsonb, '{}'::jsonb, strandId, markerStatus);
  END;
$$ LANGUAGE plpgsql;

--changeset venice.juanillas:fix_functions_marker_deletemarkerpropertybyname context:general splitStatements:false
DROP FUNCTION IF EXISTS deleteMarkerPropertyByName(text);

CREATE OR REPLACE FUNCTION deleteMarkerPropertyByName(id integer, propertyName text)
RETURNS text AS $$
  BEGIN
    with property as (select cv_id from cv where term=propertyName)
    update marker
      set props = props - property.cv_id::text
      from property
      where marker_id=id;
    return propertyName;
  END;
$$ LANGUAGE plpgsql;

--changeset venice.juanillas:fix_functions_marker_deletemarkerpropertybyid context:general splitStatements:false
DROP FUNCTION IF EXISTS deleteMarkerPropertyById(integer);

CREATE OR REPLACE FUNCTION deleteMarkerPropertyById(id integer, propertyId integer)
RETURNS integer AS $$
  BEGIN
    update marker
    set props = props - propertyId::text
    where marker_id=id;
    return propertyId;
  END;
$$ LANGUAGE plpgsql;

--changeset venice.juanillas:fix_functions_marker_getallpropertiesofmarker context:general splitStatements:false
DROP FUNCTION IF EXISTS getAllPropertiesOfMarker(integer, text, text);

CREATE OR REPLACE FUNCTION getAllPropertiesOfMarker(id integer)
RETURNS table (property_id integer, property_name text, property_value text) AS $$
  BEGIN
    return query
    select p1.key::int as property_id, cv.term as property_name, p1.value as property_value
    from cv, (select (jsonb_each_text(props)).* from marker where marker_id=id) as p1
    where cv.cv_id = p1.key::int;
    END;
$$ LANGUAGE plpgsql;

--changeset venice.juanillas:fix_functions_marker_getmarkerpropertybyid context:general splitStatements:false
DROP FUNCTION IF EXISTS getMarkerPropertyById(text);

CREATE OR REPLACE FUNCTION getMarkerPropertyById(id integer, propertyId integer)
RETURNS text AS $$
  DECLARE
    value text;
  BEGIN
    select props->propertyId::text into value from marker where marker_id=id;
    return value;
  END;
$$ LANGUAGE plpgsql;

--changeset venice.juanillas:fix_functions_marker_getmarkerpropertybyname context:general splitStatements:false
DROP FUNCTION IF EXISTS getMarkerPropertyByName(integer,text);

CREATE OR REPLACE FUNCTION getMarkerPropertyByName(id integer, propertyName text)
RETURNS table (property_id integer, property_value text) AS $$
  BEGIN
    return query
    (with property as (select cv_id from cv where term=propertyName)
    select property.cv_id, (props->property.cv_id::text)::text as value
      from marker, property
      where marker_id=id);
  END;
$$ LANGUAGE plpgsql;

--changeset venice.juanillas:fix_functions_marker_updatemarkerpropertybyid context:general splitStatements:false
DROP FUNCTION IF EXISTS updateMarkerPropertyById();

CREATE OR REPLACE FUNCTION updateMarkerPropertyById(id integer, propertyId integer, propertyValue text)
RETURNS void AS $$
  BEGIN
    update marker set props = jsonb_set(props, ('{'||propertyId::text||'}')::text[], ('"'||propertyValue||'"')::jsonb)
      where marker_id=id;
  END;
$$ LANGUAGE plpgsql;

--changeset venice.juanillas:fix_functions_marker_updatemarkerpropertybyname context:general splitStatements:false
DROP FUNCTION IF EXISTS updateMarkerPropertyByName();

CREATE OR REPLACE FUNCTION updateMarkerPropertyByName(id integer, propertyName text, propertyValue text)
RETURNS void AS $$
  BEGIN
    with property as (select cv_id from cv where term=propertyName)
    update marker
      set props = jsonb_set(props, ('{'||property.cv_id::text||'}')::text[], ('"'||propertyValue||'"')::jsonb)
      from property
      where marker_id=id;
  END;
$$ LANGUAGE plpgsql;

--changeset venice.juanillas:fix_functions_marker_upsertmarkerpropertybyid context:general splitStatements:false
DROP FUNCTION IF EXISTS upsertMarkerPropertyById();

CREATE OR REPLACE FUNCTION upsertMarkerPropertyById(id integer, propertyId integer, propertyValue text)
RETURNS void AS $$
  BEGIN
    update marker set props = props || ('{"'||propertyId::text||'": "'||propertyValue||'"}')::jsonb
      where marker_id=id;
  END;
$$ LANGUAGE plpgsql;

--changeset venice.juanillas:fix_functions_marker_upsertmarkerpropertybyname context:general splitStatements:false
DROP FUNCTION IF EXISTS upsertMarkerPropertyByName(integer);

CREATE OR REPLACE FUNCTION upsertMarkerPropertyByName(id integer, propertyName text, propertyValue text)
RETURNS integer AS $$
  DECLARE
    propertyId integer;
  BEGIN
    select cv_id into propertyId from cv where term=propertyName;
    update marker set props = props || ('{"'||propertyId::text||'": "'||propertyValue||'"}')::jsonb
      where marker_id=id;
    return propertyId;
  END;
$$ LANGUAGE plpgsql;

--changeset venice.juanillas:fix_functions_mapset_createmapset context:general splitStatements:false
DROP FUNCTION IF EXISTS createMapset(integer);

CREATE OR REPLACE FUNCTION createMapset(mapsetName text, mapsetCode text, mapsetDescription text, referenceId integer, typeId integer, createdBy integer, createdDate date, modifiedBy integer, modifiedDate date, mapsetStatus integer, OUT id integer)
RETURNS integer AS $$
  BEGIN
    insert into mapset (name, code, description, reference_id, type_id,
created_by, created_date, modified_by, modified_date, status)
      values (mapsetName, mapsetCode, mapsetDescription, referenceId, typeId, createdBy, createdDate, modifiedBy, modifiedDate, mapsetStatus);
  END;
$$ LANGUAGE plpgsql;

--changeset venice.juanillas:fix_functions_mapset_deletemapsetpropertybyid context:general splitStatements:false
DROP FUNCTION IF EXISTS deleteMapsetPropertyById(integer);

CREATE OR REPLACE FUNCTION deleteMapsetPropertyById(id integer, propertyId integer)
RETURNS integer AS $$
  BEGIN
    update mapset
    set props = props - propertyId::text
    where map_id=id;
    return propertyId;
  END;
$$ LANGUAGE plpgsql;

--changeset venice.juanillas:fix_functions_mapset_deletemapsetpropertybyname context:general splitStatements:false
DROP FUNCTION IF EXISTS deleteMapsetPropertyByName(text);

CREATE OR REPLACE FUNCTION deleteMapsetPropertyByName(id integer, propertyName text)
RETURNS text AS $$
  BEGIN
    with property as (select cv_id from cv where term=propertyName)
    update mapset
      set props = props - property.cv_id::text
      from property
      where map_id=id;
    return propertyName;
  END;
$$ LANGUAGE plpgsql;

--changeset venice.juanillas:fix_functions_mapset_getallpropertiesofmapset context:general splitStatements:false
DROP FUNCTION IF EXISTS getAllPropertiesOfMapset(integer,text,text);

CREATE OR REPLACE FUNCTION getAllPropertiesOfMapset(id integer)
RETURNS table (property_id integer, property_name text, property_value text) AS $$
  BEGIN
    return query
    select p1.key::int as property_id, cv.term as property_name, p1.value as property_value
    from cv, (select (jsonb_each_text(props)).* from mapset where map_id=id) as p1
    where cv.cv_id = p1.key::int;
    END;
$$ LANGUAGE plpgsql;

--changeset venice.juanillas:fix_functions_mapset_getmapsetpropertybyid context:general splitStatements:false
DROP FUNCTION IF EXISTS getMapsetPropertyById(text);

CREATE OR REPLACE FUNCTION getMapsetPropertyById(id integer, propertyId integer)
RETURNS text AS $$
  DECLARE
    value text;
  BEGIN
    select props->propertyId::text into value from mapset where map_id=id;
    return value;
  END;
$$ LANGUAGE plpgsql;

--changeset venice.juanillas:fix_functions_mapset_getmapsetpropertybyname context:general splitStatements:false
DROP FUNCTION IF EXISTS getMapsetPropertyByName(integer,text);

CREATE OR REPLACE FUNCTION getMapsetPropertyByName(id integer, propertyName text)
RETURNS table (property_id integer, property_value text) AS $$
  BEGIN
    return query
    (with property as (select cv_id from cv where term=propertyName)
    select property.cv_id, (props->property.cv_id::text)::text as value
      from mapset, property
      where map_id=id);
  END;
$$ LANGUAGE plpgsql;

--changeset venice.juanillas:fix_functions_mapset_updatemapsetpropertybyid context:general splitStatements:false
DROP FUNCTION IF EXISTS updateMapsetPropertyById();

CREATE OR REPLACE FUNCTION updateMapsetPropertyById(id integer, propertyId integer, propertyValue text)
RETURNS void AS $$
  BEGIN
    update mapset set props = jsonb_set(props, ('{'||propertyId::text||'}')::text[], ('"'||propertyValue||'"')::jsonb)
      where map_id=id;
  END;
$$ LANGUAGE plpgsql;

--changeset venice.juanillas:fix_functions_mapset_updatemapsetpropertybyname context:general splitStatements:false
DROP FUNCTION IF EXISTS updateMapsetPropertyByName();

CREATE OR REPLACE FUNCTION updateMapsetPropertyByName(id integer, propertyName text, propertyValue text)
RETURNS void AS $$
  BEGIN
    with property as (select cv_id from cv where term=propertyName)
    update mapset
      set props = jsonb_set(props, ('{'||property.cv_id::text||'}')::text[], ('"'||propertyValue||'"')::jsonb)
      from property
      where map_id=id;
  END;
$$ LANGUAGE plpgsql;

--changeset venice.juanillas:fix_functions_mapset_upsertmapsetpropertybyid context:general splitStatements:false
DROP FUNCTION IF EXISTS upsertMapsetPropertyById();

CREATE OR REPLACE FUNCTION upsertMapsetPropertyById(id integer, propertyId integer, propertyValue text)
RETURNS void AS $$
  BEGIN
    update mapset set props = props || ('{"'||propertyId::text||'": "'||propertyValue||'"}')::jsonb
      where map_id=id;
  END;
$$ LANGUAGE plpgsql;

--changeset venice.juanillas:fix_functions_mapset_upsertmapsetpropertybyname context:general splitStatements:false
DROP FUNCTION IF EXISTS upsertMapsetPropertyByName(integer);

CREATE OR REPLACE FUNCTION upsertMapsetPropertyByName(id integer, propertyName text, propertyValue text)
RETURNS integer AS $$
  DECLARE
    propertyId integer;
  BEGIN
    select cv_id into propertyId from cv where term=propertyName;
    update mapset set props = props || ('{"'||propertyId::text||'": "'||propertyValue||'"}')::jsonb
      where map_id=id;
    return propertyId;
  END;
$$ LANGUAGE plpgsql;

