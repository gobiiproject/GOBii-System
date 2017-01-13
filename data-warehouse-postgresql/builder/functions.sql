/* GOBII FlatMeta Database Functions 

  This file contains the DDL to create the required functions for GOBII applications to transact with the database.

  @author Kevin Palis <kdp44@cornell.edu>
  @date 04/14/2016
*/


--### Project ###--
--create a new project, you may supply null for columns that are nullable
CREATE OR REPLACE FUNCTION createProject(projectName text, projectCode text, projectDescription text, piContact integer, createdBy integer, createdDate date, 
  modifiedBy integer, modifiedDate date, projectStatus integer, OUT projectId integer)
RETURNS integer AS $$
    BEGIN
    insert into project (name, code, description, pi_contact, created_by, created_date, modified_by, modified_date, status)
      values (projectName, projectCode, projectDescription, piContact, createdBy, createdDate, modifiedBy, modifiedDate, projectStatus); 
          
    select lastval() into projectId;
    insert into project_prop (project_id, props) values (projectId, '{}'::jsonb);
    END;
$$ LANGUAGE plpgsql;

--update all project columns
--You can "avoid" updating certain columns by passing the same value as what's currently in that column
--OR I can create update functions that updates only certain columns, just let me know.
CREATE OR REPLACE FUNCTION updateProject(pId integer, projectName text, projectCode text, projectDescription text, piContact integer, createdBy integer, createdDate date, 
  modifiedBy integer, modifiedDate date, projectStatus integer)
RETURNS void AS $$
    BEGIN
    update project set name = projectName, code = projectCode, description = projectDescription, pi_contact = piContact, created_by = createdBy, created_date = createdDate, 
      modified_by = modifiedBy, modified_date = modifiedDate, status = projectStatus where project_id = pId;
    END;
$$ LANGUAGE plpgsql;

--delete
CREATE OR REPLACE FUNCTION deleteProject(pId integer)
RETURNS integer AS $$
    BEGIN
    delete from project where project_id = pId;
    return pId;
    END;
$$ LANGUAGE plpgsql;

--count
CREATE OR REPLACE FUNCTION getTotalProjects()
RETURNS integer AS $$
  DECLARE
    total integer; 
  BEGIN
    select count(*) into total from projects;
    return total;
  END;
$$ LANGUAGE plpgsql;

--Project Properties
--upsert by id
CREATE OR REPLACE FUNCTION upsertProjectPropertyById(id integer, propertyId integer, propertyValue text)
RETURNS void AS $$
  BEGIN
    update project_prop set props = props || ('{"'||propertyId::text||'": "'||propertyValue||'"}')::jsonb
      where project_id=id;
  END;
$$ LANGUAGE plpgsql;

--upsert by name
CREATE OR REPLACE FUNCTION upsertProjectPropertyByName(id integer, propertyName text, propertyValue text)
RETURNS integer AS $$
  DECLARE
    propertyId integer;
  BEGIN
    select cv_id into propertyId from cv where term=propertyName;
    update project_prop set props = props || ('{"'||propertyId::text||'": "'||propertyValue||'"}')::jsonb
      where project_id=id;
    return propertyId;
  END;
$$ LANGUAGE plpgsql;


--read/select all
CREATE OR REPLACE FUNCTION getAllPropertiesOfProject(projectId integer)
RETURNS table (property_id integer, property_name text, property_value text) AS $$
  BEGIN
    return query
    select p1.key::int as property_id, cv.term as property_name, p1.value as property_value
    from cv, (select (jsonb_each_text(props)).* from project_prop where project_id=projectId) as p1
    where cv.cv_id = p1.key::int;
    END;
$$ LANGUAGE plpgsql;

--read/select a property of a single project given the project id and property id (cv_id of that property)
CREATE OR REPLACE FUNCTION getProjectPropertyById(projectId integer, propertyId integer)
RETURNS text AS $$
  DECLARE
    value text;
  BEGIN
    select props->propertyId::text into value from project_prop where project_id=projectId;
    return value;
  END;
$$ LANGUAGE plpgsql;

--read/select a property of a single project given the project id and property name --> should match with the property name in the CV table
CREATE OR REPLACE FUNCTION getProjectPropertyByName(projectId integer, propertyName text)
RETURNS table (property_id integer, property_value text) AS $$
  BEGIN
    return query
    (with property as (select cv_id from cv where term=propertyName)
    select property.cv_id, (props->property.cv_id::text)::text as value
      from project_prop, property
      where project_id=projectId);
  END;
$$ LANGUAGE plpgsql;

--update property by key id
CREATE OR REPLACE FUNCTION updateProjectPropertyById(projectId integer, propertyId integer, propertyValue text)
RETURNS void AS $$
  BEGIN
    update project_prop set props = jsonb_set(props, ('{'||propertyId::text||'}')::text[], ('"'||propertyValue||'"')::jsonb)
      where project_id=projectId;
  END;
$$ LANGUAGE plpgsql;

--update property by property name --> should match with the property name in the CV table
CREATE OR REPLACE FUNCTION updateProjectPropertyByName(projectId integer, propertyName text, propertyValue text)
RETURNS void AS $$
  BEGIN
    with property as (select cv_id from cv where term=propertyName)
    update project_prop 
      set props = jsonb_set(props, ('{'||property.cv_id::text||'}')::text[], ('"'||propertyValue||'"')::jsonb)
      from property
      where project_id=projectId;
  END;
$$ LANGUAGE plpgsql;

--delete property by ID
CREATE OR REPLACE FUNCTION deleteProjectPropertyById(projectId integer, propertyId integer)
RETURNS integer AS $$
  BEGIN
    update project_prop 
    set props = props - propertyId::text
    where project_id=projectId;
    return propertyId;
  END;
$$ LANGUAGE plpgsql;

--delete property by name
CREATE OR REPLACE FUNCTION deleteProjectPropertyByName(projectId integer, propertyName text)
RETURNS text AS $$
  BEGIN
    with property as (select cv_id from cv where term=propertyName)
    update project_prop 
      set props = props - property.cv_id::text
      from property
      where project_id=projectId;
    return propertyName;
  END;
$$ LANGUAGE plpgsql;


--### Experiment ###--
--create a new experiment, you may supply null for columns that are nullable
CREATE OR REPLACE FUNCTION createExperiment(expName text, expCode text, projectId integer, platformId integer, manifestId integer, dataFile text, 
    createdBy integer, createdDate date, modifiedBy integer, modifiedDate date, expStatus integer, OUT expId integer)
RETURNS integer AS $$
    BEGIN
    insert into experiment (name, code, project_id, platform_id, manifest_id, data_file, created_by, created_date, modified_by, modified_date, status)
      values (expName, expCode, projectId, platformId, manifestId, dataFile, createdBy, createdDate, modifiedBy, modifiedDate, expStatus); 
    select lastval() into expId;
    END;
$$ LANGUAGE plpgsql;

--update all experiment columns
--You can "avoid" updating certain columns by passing the same value as what's currently in that column
--OR I can create update functions that updates only certain columns, just let me know.
CREATE OR REPLACE FUNCTION updateExperiment(eId integer, expName text, expCode text, projectId integer, platformId integer, manifestId integer, dataFile text, 
    createdBy integer, createdDate date, modifiedBy integer, modifiedDate date, expStatus integer)
RETURNS void AS $$
    BEGIN
    update experiment set name=expName, code=expCode, project_id=projectId, platform_id=platformId, manifest_id=manifestId, data_file=dataFile, 
      created_by=createdBy, created_date=createdDate, modified_by=modifiedBy, modified_date=modifiedDate, status=expStatus where experiment_id = eId;
    END;
$$ LANGUAGE plpgsql;

--just some additional querying functions -> these can easily be done using the table directly
CREATE OR REPLACE FUNCTION getExperimentNamesByProjectId(projectId integer)
RETURNS table (id integer, experiment_name text) AS $$
  BEGIN
    return query
    select experiment_id, name from experiment where project_id = projectId;
  END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION getExperimentsByProjectId(projectId integer)
RETURNS setof experiment AS $$
  BEGIN
    return query
    select * from experiment where project_id = projectId;
  END;
$$ LANGUAGE plpgsql;


--delete
CREATE OR REPLACE FUNCTION deleteExperiment(eId integer)
RETURNS integer AS $$
    BEGIN
    delete from experiment where experiment_id = eId;
    return eId;
    END;
$$ LANGUAGE plpgsql;

--### Manifest ###--

--create a new manifest, you may supply null for columns that are nullable
CREATE OR REPLACE FUNCTION createManifest(manifestName text, manifestCode text, filePath text, createdBy integer, createdDate date, modifiedBy integer, modifiedDate date, OUT mId integer)
RETURNS integer AS $$
  BEGIN
    insert into manifest (name, code, file_path, created_by, created_date, modified_by, modified_date)
      values (manifestName, manifestCode, filePath, createdBy, createdDate, modifiedBy, modifiedDate); 
    select lastval() into mId;
  END;
$$ LANGUAGE plpgsql;

--update all manifest columns
--You can "avoid" updating certain columns by passing the same value as what's currently in that column
--OR I can create update functions that updates only certain columns, just let me know.
CREATE OR REPLACE FUNCTION updateManifest(manifestId integer, manifestName text, manifestCode text, filePath text, createdBy integer, createdDate date, modifiedBy integer, modifiedDate date)
RETURNS void AS $$
    BEGIN
    update manifest set name=manifestName, code=manifestCode, file_path=filePath, created_by=createdBy, 
      created_date=createdDate, modified_by=modifiedBy, modified_date=modifiedDate
     where manifest_id = manifestId;
    END;
$$ LANGUAGE plpgsql;

--get manifest(s) for a given experiment
CREATE OR REPLACE FUNCTION getManifestByExperimentId(experimentId integer)
RETURNS setof manifest AS $$
  BEGIN
    return query
    select * from manifest where manifest_id in (select manifest_id from experiment where experiment_id = experimentId);
  END;
$$ LANGUAGE plpgsql;


--delete
CREATE OR REPLACE FUNCTION deleteManifest(id integer)
RETURNS integer AS $$
    BEGIN
    delete from manifest where manifest_id = id;
    return id;
    END;
$$ LANGUAGE plpgsql;

--### Contact ###--

--create a new contact, you may supply null for columns that are nullable
CREATE OR REPLACE FUNCTION createContact(lastName text, firstName text, contactCode text, contactEmail text, contactRoles integer[], createdBy integer, createdDate date, modifiedBy integer, modifiedDate date, organizationId integer, OUT id integer)
RETURNS integer AS $$
  BEGIN
    insert into contact (lastname, firstname, code, email, roles, created_by, created_date, modified_by, modified_date, organization_id)
      values (lastName, firstName, contactCode, contactEmail, contactRoles, createdBy, createdDate, modifiedBy, modifiedDate, organizationId); 
    select lastval() into id;
  END;
$$ LANGUAGE plpgsql;

--update all contact columns
--You can "avoid" updating certain columns by passing the same value as what's currently in that column
--OR I can create update functions that updates only certain columns, just let me know.
CREATE OR REPLACE FUNCTION updateContact(contactId integer,contactLastName text, contactFirstName text, contactCode text, contactEmail text, contactRoles integer[], createdBy integer, createdDate date, modifiedBy integer, modifiedDate date, organizationId integer)
RETURNS void AS $$
    BEGIN
    update contact set lastname=contactLastName, firstname=contactFirstName, code=contactCode, email=contactEmail, roles=contactRoles, created_by=createdBy, created_date=createdDate, 
      modified_by=modifiedBy, modified_date=modifiedDate, organization_id=organizationId
     where contact_id = contactId;
    END;
$$ LANGUAGE plpgsql;

--get roles for a given contact
CREATE OR REPLACE FUNCTION getRolesOfContact(contactId integer)
RETURNS setof role AS $$
  BEGIN
    return query
    select r.* from contact c, role r where c.contact_id = contactId and r.role_id = any(c.roles);
  END;
$$ LANGUAGE plpgsql;

--append array values
CREATE OR REPLACE FUNCTION appendRoleToContact(contactId integer, roleId integer)
RETURNS void AS $$
    BEGIN
    update contact set roles=array_append(roles, roleId)
     where contact_id = contactId;
    END;
$$ LANGUAGE plpgsql;

--remove array values
CREATE OR REPLACE FUNCTION removeRoleFromContact(contactId integer, roleId integer)
RETURNS void AS $$
    BEGIN
    update contact set roles=array_remove(roles, roleId)
     where contact_id = contactId;
    END;
$$ LANGUAGE plpgsql;


--delete
CREATE OR REPLACE FUNCTION deleteContact(id integer)
RETURNS integer AS $$
    BEGIN
    delete from contact where contact_id = id;
    return id;
    END;
$$ LANGUAGE plpgsql;

--### Role ###--

--create a new row, you may supply null for columns that are nullable
CREATE OR REPLACE FUNCTION createRole(roleName text, roleCode text, readTables integer[], writeTables integer[], OUT id integer)
RETURNS integer AS $$
  BEGIN
    insert into role (role_name, role_code, read_tables, write_tables)
      values (roleName, roleCode, readTables, writeTables); 
    select lastval() into id;
  END;
$$ LANGUAGE plpgsql;

--get all contacts given the role
CREATE OR REPLACE FUNCTION getAllContactsByRole(roleId integer)
RETURNS setof contact AS $$
  BEGIN
    return query
    select c.* from contact c, role r where r.role_id = roleId and r.role_id = any(c.roles);
  END;
$$ LANGUAGE plpgsql;

--update all columns
--You can "avoid" updating certain columns by passing the same value as what's currently in that column
--OR I can create update functions that updates only certain columns, just let me know.
CREATE OR REPLACE FUNCTION updateRole(roleId integer, roleName text, roleCode text, readTables integer[], writeTables integer[])
RETURNS void AS $$
    BEGIN
    update role set role_name=roleName, role_code=roleCode, read_tables=readTables, write_tables=writeTables
     where role_id = roleId;
    END;
$$ LANGUAGE plpgsql;

--delete
CREATE OR REPLACE FUNCTION deleteRole(id integer)
RETURNS integer AS $$
    BEGIN
    delete from role where role_id = id;
    return id;
    END;
$$ LANGUAGE plpgsql;

--append array values
CREATE OR REPLACE FUNCTION appendReadTableToRole(roleId integer, tableId integer)
RETURNS void AS $$
    BEGIN
    update role set read_tables=array_append(read_tables, tableId)
     where role_id = roleId;
    END;
$$ LANGUAGE plpgsql;

--remove array values
CREATE OR REPLACE FUNCTION removeReadTableFromRole(roleId integer, tableId integer)
RETURNS void AS $$
    BEGIN
    update role set read_tables=array_remove(read_tables, tableId)
     where role_id = roleId;
    END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION appendWriteTableToRole(roleId integer, tableId integer)
RETURNS void AS $$
    BEGIN
    update role set write_tables=array_append(write_tables, tableId)
     where role_id = roleId;
    END;
$$ LANGUAGE plpgsql;

--remove array values
CREATE OR REPLACE FUNCTION removeWriteTableFromRole(roleId integer, tableId integer)
RETURNS void AS $$
    BEGIN
    update role set write_tables=array_remove(write_tables, tableId)
     where role_id = roleId;
    END;
$$ LANGUAGE plpgsql;
--### DNASample and Properties ###--

--create a new row, you may supply null for columns that are nullable
CREATE OR REPLACE FUNCTION createDnaSample(dnaSampleName text, dnaSampleCode text, dnaSamplePlateName text, dnaSampleNum text, wellRow text, wellCol text, projectId integer, germplasmId integer, createdBy integer, createdDate date, modifiedBy integer, modifiedDate date, dnaSampleStatus integer, OUT id integer)
RETURNS integer AS $$
  BEGIN
    insert into dnasample (name, code, platename, num, well_row, well_col, project_id, germplasm_id, created_by, created_date, modified_by, modified_date, status)
      values (dnaSampleName, dnaSampleCode, dnaSamplePlateName, dnaSampleNum, wellRow, wellCol, projectId, germplasmId, createdBy, createdDate, modifiedBy, modifiedDate, dnaSampleStatus); 
    select lastval() into id;
    insert into dnasample_prop (dnasample_id, props) values (id, '{}'::jsonb);
  END;
$$ LANGUAGE plpgsql;

--update all columns
--You can "avoid" updating certain columns by passing the same value as what's currently in that column
--OR I can create update functions that updates only certain columns, just let me know.
CREATE OR REPLACE FUNCTION updateDnaSample(id integer, dnaSampleName text, dnaSampleCode text, dnaSamplePlateName text, dnaSampleNum text, wellRow text, wellCol text, projectId integer, germplasmId integer, createdBy integer, createdDate date, modifiedBy integer, modifiedDate date, dnaSampleStatus integer)
RETURNS void AS $$
    BEGIN
    update dnasample set name=dnaSampleName, code=dnaSampleCode, platename=dnaSamplePlateName, num=dnaSampleNum, well_row=wellRow, well_col=wellCol, project_id=projectId, germplasm_id=germplasmId, created_by=createdBy, created_date=createdDate, modified_by=modifiedBy, modified_date=modifiedDate, status=dnaSampleStatus
     where dnasample_id = id;
    END;
$$ LANGUAGE plpgsql;

--delete
CREATE OR REPLACE FUNCTION deleteDnaSample(id integer)
RETURNS integer AS $$
    BEGIN
    delete from dnasample where dnasample_id = id;
    return id;
    END;
$$ LANGUAGE plpgsql;

--jsonb/properties
--create or update property
--upsert (update or insert) property by key id
CREATE OR REPLACE FUNCTION upsertDnaSamplePropertyById(id integer, propertyId integer, propertyValue text)
RETURNS void AS $$
  BEGIN
    update dnasample_prop set props = props || ('{"'||propertyId::text||'": "'||propertyValue||'"}')::jsonb
      where dnasample_id=id;
  END;
$$ LANGUAGE plpgsql;

--upsert by property name
CREATE OR REPLACE FUNCTION upsertDnaSamplePropertyByName(id integer, propertyName text, propertyValue text)
RETURNS integer AS $$
  DECLARE
    propertyId integer;
  BEGIN
    select cv_id into propertyId from cv where term=propertyName;
    update dnasample_prop set props = props || ('{"'||propertyId::text||'": "'||propertyValue||'"}')::jsonb
      where dnasample_id=id;
    return propertyId;
  END;
$$ LANGUAGE plpgsql;

--read/select all
CREATE OR REPLACE FUNCTION getAllPropertiesOfDnaSample(id integer)
RETURNS table (property_id integer, property_name text, property_value text) AS $$
  BEGIN
    return query
    select p1.key::int as property_id, cv.term as property_name, p1.value as property_value
    from cv, (select (jsonb_each_text(props)).* from dnasample_prop where dnasample_id=id) as p1
    where cv.cv_id = p1.key::int;
    END;
$$ LANGUAGE plpgsql;

--read/select a property of a entity given its id and property id (cv_id of that property)
CREATE OR REPLACE FUNCTION getDnaSamplePropertyById(id integer, propertyId integer)
RETURNS text AS $$
  DECLARE
    value text;
  BEGIN
    select props->propertyId::text into value from dnasample_prop where dnasample_id=id;
    return value;
  END;
$$ LANGUAGE plpgsql;

--read/select a property of a single entity given its id and property name --> should match with the property name in the CV table
CREATE OR REPLACE FUNCTION getDnaSamplePropertyByName(id integer, propertyName text)
RETURNS table (property_id integer, property_value text) AS $$
  BEGIN
    return query
    (with property as (select cv_id from cv where term=propertyName)
    select property.cv_id, (props->property.cv_id::text)::text as value
      from dnasample_prop, property
      where dnasample_id=id);
  END;
$$ LANGUAGE plpgsql;

--update property by key id
CREATE OR REPLACE FUNCTION updateDnaSamplePropertyById(id integer, propertyId integer, propertyValue text)
RETURNS void AS $$
  BEGIN
    update dnasample_prop set props = jsonb_set(props, ('{'||propertyId::text||'}')::text[], ('"'||propertyValue||'"')::jsonb)
      where dnasample_id=id;
  END;
$$ LANGUAGE plpgsql;

--update property by property name --> should match with the property name in the CV table
CREATE OR REPLACE FUNCTION updateDnaSamplePropertyByName(id integer, propertyName text, propertyValue text)
RETURNS void AS $$
  BEGIN
    with property as (select cv_id from cv where term=propertyName)
    update dnasample_prop 
      set props = jsonb_set(props, ('{'||property.cv_id::text||'}')::text[], ('"'||propertyValue||'"')::jsonb)
      from property
      where dnasample_id=id;
  END;
$$ LANGUAGE plpgsql;

--delete property by ID
CREATE OR REPLACE FUNCTION deleteDnaSamplePropertyById(id integer, propertyId integer)
RETURNS integer AS $$
  BEGIN
    update dnasample_prop 
    set props = props - propertyId::text
    where dnasample_id=id;
    return propertyId;
  END;
$$ LANGUAGE plpgsql;

--delete property by name
CREATE OR REPLACE FUNCTION deleteDnaSamplePropertyByName(id integer, propertyName text)
RETURNS text AS $$
  BEGIN
    with property as (select cv_id from cv where term=propertyName)
    update dnasample_prop 
      set props = props - property.cv_id::text
      from property
      where dnasample_id=id;
    return propertyName;
  END;
$$ LANGUAGE plpgsql;

--### Dataset ###--

--create a new row, you may supply null for columns that are nullable
--DROP FUNCTION createDataset(experimentId integer, callinganalysisId integer, datasetAnalyses integer[], dataTable text, dataFile text, qualityTable text, qualityFile text, createdBy int, createdDate date, modifiedBy int, modifiedDate date, datasetStatus integer, OUT id integer);
CREATE OR REPLACE FUNCTION createDataset(datasetName text, experimentId integer, callinganalysisId integer, datasetAnalyses integer[], dataTable text, dataFile text, qualityTable text, qualityFile text, createdBy int, createdDate date, modifiedBy int, modifiedDate date, datasetStatus integer, typeId integer, OUT id integer)
RETURNS integer AS $$
  BEGIN
    insert into dataset (experiment_id, callinganalysis_id, analyses, data_table, data_file, quality_table, quality_file, scores, created_by, created_date, modified_by, modified_date, status, type_id, name)
      values (experimentId, callinganalysisId, datasetAnalyses, dataTable, dataFile, qualityTable, qualityFile, '{}'::jsonb, createdBy, createdDate, modifiedBy, modifiedDate, datasetStatus, typeId, datasetName); 
    select lastval() into id;
  END;
$$ LANGUAGE plpgsql;

--update all columns
--DROP FUNCTION updateDataset(id integer, experimentId integer, callinganalysisId integer, datasetAnalyses integer[], dataTable text, dataFile text, qualityTable text, qualityFile text, createdBy int, createdDate date, modifiedBy int, modifiedDate date, datasetStatus integer);
CREATE OR REPLACE FUNCTION updateDataset(id integer, datasetName text, experimentId integer, callinganalysisId integer, datasetAnalyses integer[], dataTable text, dataFile text, qualityTable text, qualityFile text, createdBy int, createdDate date, modifiedBy int, modifiedDate date, datasetStatus integer, typeId integer)
RETURNS void AS $$
    BEGIN
    update dataset set experiment_id=experimentId, callinganalysis_id=callinganalysisId, analyses=datasetAnalyses, data_table=dataTable, data_file=dataFile, quality_table=qualityTable, quality_file=qualityFile, scores='{}'::jsonb, created_by=createdBy, created_date=createdDate, modified_by=modifiedBy, modified_date=modifiedDate, status=datasetStatus, type_id=typeId, name=datasetName
     where dataset_id = id;
    END;
$$ LANGUAGE plpgsql;

--delete
CREATE OR REPLACE FUNCTION deleteDataset(id integer)
RETURNS integer AS $$
    BEGIN
    delete from dataset where dataset_id = id;
    return id;
    END;
$$ LANGUAGE plpgsql;

--append array values
CREATE OR REPLACE FUNCTION addAnalysisToDataset(datasetId integer, analysisId integer)
RETURNS void AS $$
    BEGIN
    update dataset set analyses=array_append(analyses, analysisId)
     where dataset_id = id;
    END;
$$ LANGUAGE plpgsql;

--append array values
CREATE OR REPLACE FUNCTION appendAnalysisToDataset(datasetId integer, analysisId integer)
RETURNS void AS $$
    BEGIN
    update dataset set analyses=array_append(analyses, analysisId)
     where dataset_id = datasetId;
    END;
$$ LANGUAGE plpgsql;

--remove array values
CREATE OR REPLACE FUNCTION removeAnalysisFromDataset(datasetId integer, analysisId integer)
RETURNS void AS $$
    BEGIN
    update dataset set analyses=array_remove(analyses, analysisId)
     where dataset_id = datasetId;
    END;
$$ LANGUAGE plpgsql;
--### DNARun and Properties ###--

--create a new row, you may supply null for columns that are nullable
CREATE OR REPLACE FUNCTION createDnarun(experimentId integer, dnasampleId integer, dnarunName text, dnarunCode text, OUT id integer)
RETURNS integer AS $$
  BEGIN
    insert into dnarun (experiment_id, dnasample_id, name, code)
      values (experimentId, dnasampleId, dnarunName, dnarunCode); 
    select lastval() into id;
    insert into dnarun_prop (dnarun_id, props) values (id, '{}'::jsonb);
  END;
$$ LANGUAGE plpgsql;

--update all columns
--You can "avoid" updating certain columns by passing the same value as what's currently in that column
--OR I can create update functions that updates only certain columns, just let me know.
CREATE OR REPLACE FUNCTION updateDnarun(id integer, experimentId integer, dnasampleId integer, dnarunName text, dnarunCode text)
RETURNS void AS $$
    BEGIN
    update dnarun set experiment_id=experimentId, dnasample_id=dnasampleId, name=dnarunName, code=dnarunCode
     where dnarun_id = id;
    END;
$$ LANGUAGE plpgsql;

--delete
CREATE OR REPLACE FUNCTION deleteDnarun(id integer)
RETURNS integer AS $$
    BEGIN
    delete from dnarun where dnarun_id = id;
    return id;
    END;
$$ LANGUAGE plpgsql;

--jsonb/properties
--create or update property
--upsert (update or insert) property by key id
CREATE OR REPLACE FUNCTION upsertDnarunPropertyById(id integer, propertyId integer, propertyValue text)
RETURNS void AS $$
  BEGIN
    update dnarun_prop set props = props || ('{"'||propertyId::text||'": "'||propertyValue||'"}')::jsonb
      where dnarun_id=id;
  END;
$$ LANGUAGE plpgsql;

--upsert by property name
CREATE OR REPLACE FUNCTION upsertDnarunPropertyByName(id integer, propertyName text, propertyValue text)
RETURNS integer AS $$
  DECLARE
    propertyId integer;
  BEGIN
    select cv_id into propertyId from cv where term=propertyName;
    update dnarun_prop set props = props || ('{"'||propertyId::text||'": "'||propertyValue||'"}')::jsonb
      where dnarun_id=id;
    return propertyId;
  END;
$$ LANGUAGE plpgsql;

--read/select all
CREATE OR REPLACE FUNCTION getAllPropertiesOfDnarun(id integer)
RETURNS table (property_id integer, property_name text, property_value text) AS $$
  BEGIN
    return query
    select p1.key::int as property_id, cv.term as property_name, p1.value as property_value
    from cv, (select (jsonb_each_text(props)).* from dnarun_prop where dnarun_id=id) as p1
    where cv.cv_id = p1.key::int;
    END;
$$ LANGUAGE plpgsql;

--read/select a property of a entity given its id and property id (cv_id of that property)
CREATE OR REPLACE FUNCTION getDnarunPropertyById(id integer, propertyId integer)
RETURNS text AS $$
  DECLARE
    value text;
  BEGIN
    select props->propertyId::text into value from dnarun_prop where dnarun_id=id;
    return value;
  END;
$$ LANGUAGE plpgsql;

--read/select a property of a single entity given its id and property name --> should match with the property name in the CV table
CREATE OR REPLACE FUNCTION getDnarunPropertyByName(id integer, propertyName text)
RETURNS table (property_id integer, property_value text) AS $$
  BEGIN
    return query
    (with property as (select cv_id from cv where term=propertyName)
    select property.cv_id, (props->property.cv_id::text)::text as value
      from dnarun_prop, property
      where dnarun_id=id);
  END;
$$ LANGUAGE plpgsql;

--update property by key id
CREATE OR REPLACE FUNCTION updateDnarunPropertyById(id integer, propertyId integer, propertyValue text)
RETURNS void AS $$
  BEGIN
    update dnarun_prop set props = jsonb_set(props, ('{'||propertyId::text||'}')::text[], ('"'||propertyValue||'"')::jsonb)
      where dnarun_id=id;
  END;
$$ LANGUAGE plpgsql;

--update property by property name --> should match with the property name in the CV table
CREATE OR REPLACE FUNCTION updateDnarunPropertyByName(id integer, propertyName text, propertyValue text)
RETURNS void AS $$
  BEGIN
    with property as (select cv_id from cv where term=propertyName)
    update dnarun_prop 
      set props = jsonb_set(props, ('{'||property.cv_id::text||'}')::text[], ('"'||propertyValue||'"')::jsonb)
      from property
      where dnarun_id=id;
  END;
$$ LANGUAGE plpgsql;

--delete property by ID
CREATE OR REPLACE FUNCTION deleteDnarunPropertyById(id integer, propertyId integer)
RETURNS integer AS $$
  BEGIN
    update dnarun_prop 
    set props = props - propertyId::text
    where dnarun_id=id;
    return propertyId;
  END;
$$ LANGUAGE plpgsql;

--delete property by name
CREATE OR REPLACE FUNCTION deleteDnarunPropertyByName(id integer, propertyName text)
RETURNS text AS $$
  BEGIN
    with property as (select cv_id from cv where term=propertyName)
    update dnarun_prop 
      set props = props - property.cv_id::text
      from property
      where dnarun_id=id;
    return propertyName;
  END;
$$ LANGUAGE plpgsql;


--### Dataset_DNARun ###--

--create a new row, you may supply null for columns that are nullable
CREATE OR REPLACE FUNCTION createDatasetDnaRun(datasetId integer, dnarunId integer, dnarunIdx integer, OUT id integer)
RETURNS integer AS $$
  BEGIN
    insert into dataset_dnarun (dataset_id, dnarun_id, dnarun_idx)
      values (datasetId, dnarunId, dnarunIdx); 
    select lastval() into id;
  END;
$$ LANGUAGE plpgsql;

--update all columns
--You can "avoid" updating certain columns by passing the same value as what's currently in that column
--OR I can create update functions that updates only certain columns, just let me know.
CREATE OR REPLACE FUNCTION updateDatasetDnaRun(id integer, datasetId integer, dnarunId integer, dnarunIdx integer)
RETURNS void AS $$
    BEGIN
    update dataset_dnarun set dataset_id=datasetId, dnarun_id=dnarunId, dnarun_idx=dnarunIdx
     where dataset_dnarun_id = id;
    END;
$$ LANGUAGE plpgsql;

--delete
CREATE OR REPLACE FUNCTION deleteDatasetDnaRun(id integer)
RETURNS integer AS $$
    BEGIN
    delete from dataset where dataset_id = id;
    return id;
    END;
$$ LANGUAGE plpgsql;

--### Display ###--

--create a new row, you may supply null for columns that are nullable
--DROP FUNCTION createDisplay(tableName text, columnName text, displayName text, createdBy integer, createdDate date, modifiedBy integer, modifiedDate date, OUT id integer);
CREATE OR REPLACE FUNCTION createDisplay(tableName text, columnName text, displayName text, createdBy integer, createdDate date, modifiedBy integer, modifiedDate date, displayRank integer, OUT id integer)
RETURNS integer AS $$
  BEGIN
    insert into display (table_name, column_name, display_name, created_by, created_date, modified_by, modified_date, rank)
      values (tableName, columnName, displayName, createdBy, createdDate, modifiedBy, modifiedDate, displayRank); 
    select lastval() into id;
  END;
$$ LANGUAGE plpgsql;

--update all columns
--You can "avoid" updating certain columns by passing the same value as what's currently in that column
--OR I can create update functions that updates only certain columns, just let me know.
--DROP FUNCTION updateDisplay(id integer, tableName text, columnName text, displayName text, createdBy integer, createdDate date, modifiedBy integer, modifiedDate date);
CREATE OR REPLACE FUNCTION updateDisplay(id integer, tableName text, columnName text, displayName text, createdBy integer, createdDate date, modifiedBy integer, modifiedDate date, displayRank integer)
RETURNS void AS $$
    BEGIN
    update display set table_name=tableName, column_name=columnName, display_name=displayName, created_by=createdBy, created_date=createdDate, 
      modified_by=modifiedBy, modified_date=modifiedDate, rank=displayRank
     where display_id = id;
    END;
$$ LANGUAGE plpgsql;

--delete
CREATE OR REPLACE FUNCTION deleteDisplay(id integer)
RETURNS integer AS $$
    BEGIN
    delete from display where display_id = id;
    return id;
    END;
$$ LANGUAGE plpgsql;


--### Germplasm and Properties ###--

--create a new row, you may supply null for columns that are nullable
CREATE OR REPLACE FUNCTION createGermplasm(germplasmName text, externalCode text, speciesId integer, typeId integer, createdBy integer, createdDate date, modifiedBy integer, modifiedDate date, germplasmStatus integer, germplasmCode text, OUT id integer)
RETURNS integer AS $$
  BEGIN
    insert into germplasm (name, external_code, species_id, type_id, created_by, created_date, modified_by, modified_date, status, code)
      values (germplasmName, externalCode, speciesId, typeId, createdBy, createdDate, modifiedBy, modifiedDate, germplasmStatus, germplasmCode); 
    select lastval() into id;
    insert into germplasm_prop (germplasm_id, props) values (id, '{}'::jsonb);
  END;
$$ LANGUAGE plpgsql;

--update all columns
--You can "avoid" updating certain columns by passing the same value as what's currently in that column
--OR I can create update functions that updates only certain columns, just let me know.
CREATE OR REPLACE FUNCTION updateGermplasm(id integer, germplasmName text, externalCode text, speciesId integer, typeId integer, createdBy integer, createdDate date, modifiedBy integer, modifiedDate date, germplasmStatus integer, germplasmCode text)
RETURNS void AS $$
    BEGIN
    update germplasm set name=germplasmName, external_code=externalCode, species_id=speciesId, type_id=typeId, created_by=createdBy, created_date=createdDate, modified_by=modifiedBy, modified_date=modifiedDate, status=germplasmStatus, code=germplasmCode
     where germplasm_id = id;
    END;
$$ LANGUAGE plpgsql;

--delete
CREATE OR REPLACE FUNCTION deleteGermplasm(id integer)
RETURNS integer AS $$
    BEGIN
    delete from germplasm where germplasm_id = id;
    return id;
    END;
$$ LANGUAGE plpgsql;

--jsonb/properties
--create or update property
--upsert (update or insert) property by key id
CREATE OR REPLACE FUNCTION upsertGermplasmPropertyById(id integer, propertyId integer, propertyValue text)
RETURNS void AS $$
  BEGIN
    update germplasm_prop set props = props || ('{"'||propertyId::text||'": "'||propertyValue||'"}')::jsonb
      where germplasm_id=id;
  END;
$$ LANGUAGE plpgsql;

--upsert by property name
CREATE OR REPLACE FUNCTION upsertGermplasmPropertyByName(id integer, propertyName text, propertyValue text)
RETURNS integer AS $$
  DECLARE
    propertyId integer;
  BEGIN
    select cv_id into propertyId from cv where term=propertyName;
    update germplasm_prop set props = props || ('{"'||propertyId::text||'": "'||propertyValue||'"}')::jsonb
      where germplasm_id=id;
    return propertyId;
  END;
$$ LANGUAGE plpgsql;

--read/select all
CREATE OR REPLACE FUNCTION getAllPropertiesOfGermplasm(id integer)
RETURNS table (property_id integer, property_name text, property_value text) AS $$
  BEGIN
    return query
    select p1.key::int as property_id, cv.term as property_name, p1.value as property_value
    from cv, (select (jsonb_each_text(props)).* from germplasm_prop where germplasm_id=id) as p1
    where cv.cv_id = p1.key::int;
    END;
$$ LANGUAGE plpgsql;

--read/select a property of a entity given its id and property id (cv_id of that property)
CREATE OR REPLACE FUNCTION getGermplasmPropertyById(id integer, propertyId integer)
RETURNS text AS $$
  DECLARE
    value text;
  BEGIN
    select props->propertyId::text into value from germplasm_prop where germplasm_id=id;
    return value;
  END;
$$ LANGUAGE plpgsql;

--read/select a property of a single entity given its id and property name --> should match with the property name in the CV table
CREATE OR REPLACE FUNCTION getGermplasmPropertyByName(id integer, propertyName text)
RETURNS table (property_id integer, property_value text) AS $$
  BEGIN
    return query
    (with property as (select cv_id from cv where term=propertyName)
    select property.cv_id, (props->property.cv_id::text)::text as value
      from germplasm_prop, property
      where germplasm_id=id);
  END;
$$ LANGUAGE plpgsql;

--update property by key id
CREATE OR REPLACE FUNCTION updateGermplasmPropertyById(id integer, propertyId integer, propertyValue text)
RETURNS void AS $$
  BEGIN
    update germplasm_prop set props = jsonb_set(props, ('{'||propertyId::text||'}')::text[], ('"'||propertyValue||'"')::jsonb)
      where germplasm_id=id;
  END;
$$ LANGUAGE plpgsql;

--update property by property name --> should match with the property name in the CV table
CREATE OR REPLACE FUNCTION updateGermplasmPropertyByName(id integer, propertyName text, propertyValue text)
RETURNS void AS $$
  BEGIN
    with property as (select cv_id from cv where term=propertyName)
    update germplasm_prop 
      set props = jsonb_set(props, ('{'||property.cv_id::text||'}')::text[], ('"'||propertyValue||'"')::jsonb)
      from property
      where germplasm_id=id;
  END;
$$ LANGUAGE plpgsql;

--delete property by ID
CREATE OR REPLACE FUNCTION deleteGermplasmPropertyById(id integer, propertyId integer)
RETURNS integer AS $$
  BEGIN
    update germplasm_prop 
    set props = props - propertyId::text
    where germplasm_id=id;
    return propertyId;
  END;
$$ LANGUAGE plpgsql;

--delete property by name
CREATE OR REPLACE FUNCTION deleteGermplasmPropertyByName(id integer, propertyName text)
RETURNS text AS $$
  BEGIN
    with property as (select cv_id from cv where term=propertyName)
    update germplasm_prop 
      set props = props - property.cv_id::text
      from property
      where germplasm_id=id;
    return propertyName;
  END;
$$ LANGUAGE plpgsql;

--### Analysis ###--

--create a new row, you may supply null for columns that are nullable
CREATE OR REPLACE FUNCTION createAnalysis(analysisName text, analysisDescription text, typeId integer, analysisProgram text, analysisProgramversion text, aanalysisAlgorithm text, analysisSourcename text, analysisSourceversion text, analysisSourceuri text, referenceId integer, analysisTimeexecuted timestamp, analysisStatus integer, OUT id integer)
RETURNS integer AS $$
  BEGIN
    insert into analysis (name, description, type_id, program, programversion, algorithm, sourcename, sourceversion, sourceuri, reference_id, parameters, timeexecuted, status)
      values (analysisName, analysisDescription, typeId, analysisProgram, analysisProgramversion, aanalysisAlgorithm, analysisSourcename, analysisSourceversion, analysisSourceuri, referenceId, '{}'::jsonb, analysisTimeexecuted, analysisStatus); 
    select lastval() into id;
  END;
$$ LANGUAGE plpgsql;

--update all columns
CREATE OR REPLACE FUNCTION updateAnalysis(id integer, analysisName text, analysisDescription text, typeId integer, analysisProgram text, analysisProgramversion text, aanalysisAlgorithm text, analysisSourcename text, analysisSourceversion text, analysisSourceuri text, referenceId integer, analysisTimeexecuted timestamp, analysisStatus integer)
RETURNS void AS $$
    BEGIN
    update analysis set name=analysisName, description=analysisDescription, type_id=typeId, program=analysisProgram, programversion=analysisProgramversion, algorithm=aanalysisAlgorithm, sourcename=analysisSourcename, sourceversion=analysisSourceversion, sourceuri=analysisSourceuri, reference_id=referenceId, parameters='{}'::jsonb, timeexecuted=analysisTimeexecuted, status=analysisStatus
     where analysis_id = id;
    END;
$$ LANGUAGE plpgsql;

--delete
CREATE OR REPLACE FUNCTION deleteAnalysis(id integer)
RETURNS integer AS $$
    BEGIN
    delete from analysis where analysis_id = id;
    return id;
    END;
$$ LANGUAGE plpgsql;

--jsonb column
--upsert
CREATE OR REPLACE FUNCTION upsertAnalysisParameter(id integer, parameterName text, parameterValue text)
RETURNS void AS $$
  DECLARE
    paramCol jsonb;
  BEGIN
    select parameters into paramCol from analysis where analysis_id=id;
    if paramCol is null then
      update analysis set parameters = ('{"'||parameterName||'": "'||parameterValue||'"}')::jsonb
        where analysis_id=id;
    else
      update analysis set parameters = parameters || ('{"'||parameterName||'": "'||parameterValue||'"}')::jsonb
        where analysis_id=id;
    end if;
  END;
$$ LANGUAGE plpgsql;

--
--read/select all
CREATE OR REPLACE FUNCTION getAllAnalysisParameters(id integer)
RETURNS table (property_name text, property_value text) AS $$
  BEGIN
    return query
    select (jsonb_each_text(parameters)).* from analysis where analysis_id=id;
    END;
$$ LANGUAGE plpgsql;



--delete parameter
CREATE OR REPLACE FUNCTION deleteAnalysisParameter(id integer, parameterName text)
RETURNS text AS $$
  BEGIN
    update analysis set parameters = parameters - parameterName
      where analysis_id=id;
    return parameterName;
  END;
$$ LANGUAGE plpgsql;

--### Reference ###--

--create a new row, you may supply null for columns that are nullable
CREATE OR REPLACE FUNCTION createReference(referenceName text, referenceVersion text, referenceLink text, filePath text, OUT id integer)
RETURNS integer AS $$
  BEGIN
    insert into reference (name, version, link, file_path)
      values (referenceName, referenceVersion, referenceLink, filePath); 
    select lastval() into id;
  END;
$$ LANGUAGE plpgsql;

--update all columns
CREATE OR REPLACE FUNCTION updateReference(id integer, referenceName text, referenceVersion text, referenceLink text, filePath text)
RETURNS void AS $$
    BEGIN
    update reference set name=referenceName, version=referenceVersion, link=referenceLink, file_path=filePath
     where reference_id = id;
    END;
$$ LANGUAGE plpgsql;

--delete
CREATE OR REPLACE FUNCTION deleteReference(id integer)
RETURNS integer AS $$
    BEGIN
    delete from reference where reference_id = id;
    return id;
    END;
$$ LANGUAGE plpgsql;

--### CV ###--

--create a new row, you may supply null for columns that are nullable
CREATE OR REPLACE FUNCTION createCV(cvGroup text, cvTerm text, cvDefinition text, cvRank integer, OUT id integer)
RETURNS integer AS $$
  BEGIN
    insert into cv ("group", term, definition, rank)
      values (cvGroup, cvTerm, cvDefinition, cvRank); 
    select lastval() into id;
  END;
$$ LANGUAGE plpgsql;

--update all columns
CREATE OR REPLACE FUNCTION updateCV(id integer, cvGroup text, cvTerm text, cvDefinition text, cvRank integer)
RETURNS void AS $$
    BEGIN
    update cv set "group"=cvGroup, term=cvTerm, definition=cvDefinition, rank=cvRank
     where cv_id = id;
    END;
$$ LANGUAGE plpgsql;

--delete
CREATE OR REPLACE FUNCTION deleteCV(id integer)
RETURNS integer AS $$
    BEGIN
    delete from cv where cv_id = id;
    return id;
    END;
$$ LANGUAGE plpgsql;

--### Mapset and Properties ###--

--create a new row, you may supply null for columns that are nullable
CREATE OR REPLACE FUNCTION createMapset(mapsetName text, mapsetCode text, mapsetDescription text, referenceId integer, typeId integer, createdBy integer, createdDate date, modifiedBy integer, modifiedDate date, mapsetStatus integer, OUT id integer)
RETURNS integer AS $$
  BEGIN
    insert into mapset (name, code, description, reference_id, type_id, created_by, created_date, modified_by, modified_date, status)
      values (mapsetName, mapsetCode, mapsetDescription, referenceId, typeId, createdBy, createdDate, modifiedBy, modifiedDate, mapsetStatus); 
    select lastval() into id;
    insert into map_prop (map_id, props) values (id, '{}'::jsonb);
  END;
$$ LANGUAGE plpgsql;

--update all columns
CREATE OR REPLACE FUNCTION updateMapset(id integer, mapsetName text, mapsetCode text, mapsetDescription text, referenceId integer, typeId integer, createdBy integer, createdDate date, modifiedBy integer, modifiedDate date, mapsetStatus integer)
RETURNS void AS $$
    BEGIN
    update mapset set name=mapsetName, code=mapsetCode, description=mapsetDescription, reference_id=referenceId, type_id=typeId, created_by=createdBy, created_date=createdDate, modified_by=modifiedBy, modified_date=modifiedDate, status=mapsetStatus
     where mapset_id = id;
    END;
$$ LANGUAGE plpgsql;

--delete
CREATE OR REPLACE FUNCTION deleteMapset(id integer)
RETURNS integer AS $$
    BEGIN
    delete from mapset where mapset_id = id;
    return id;
    END;
$$ LANGUAGE plpgsql;


--jsonb/properties
--create or update property
--upsert (update or insert) property by key id
CREATE OR REPLACE FUNCTION upsertMapsetPropertyById(id integer, propertyId integer, propertyValue text)
RETURNS void AS $$
  BEGIN
    update map_prop set props = props || ('{"'||propertyId::text||'": "'||propertyValue||'"}')::jsonb
      where map_id=id;
  END;
$$ LANGUAGE plpgsql;

--upsert by property name
CREATE OR REPLACE FUNCTION upsertMapsetPropertyByName(id integer, propertyName text, propertyValue text)
RETURNS integer AS $$
  DECLARE
    propertyId integer;
  BEGIN
    select cv_id into propertyId from cv where term=propertyName;
    update map_prop set props = props || ('{"'||propertyId::text||'": "'||propertyValue||'"}')::jsonb
      where map_id=id;
    return propertyId;
  END;
$$ LANGUAGE plpgsql;

--read/select all
CREATE OR REPLACE FUNCTION getAllPropertiesOfMapset(id integer)
RETURNS table (property_id integer, property_name text, property_value text) AS $$
  BEGIN
    return query
    select p1.key::int as property_id, cv.term as property_name, p1.value as property_value
    from cv, (select (jsonb_each_text(props)).* from map_prop where map_id=id) as p1
    where cv.cv_id = p1.key::int;
    END;
$$ LANGUAGE plpgsql;

--read/select a property of a entity given its id and property id (cv_id of that property)
CREATE OR REPLACE FUNCTION getMapsetPropertyById(id integer, propertyId integer)
RETURNS text AS $$
  DECLARE
    value text;
  BEGIN
    select props->propertyId::text into value from map_prop where map_id=id;
    return value;
  END;
$$ LANGUAGE plpgsql;

--read/select a property of a single entity given its id and property name --> should match with the property name in the CV table
CREATE OR REPLACE FUNCTION getMapsetPropertyByName(id integer, propertyName text)
RETURNS table (property_id integer, property_value text) AS $$
  BEGIN
    return query
    (with property as (select cv_id from cv where term=propertyName)
    select property.cv_id, (props->property.cv_id::text)::text as value
      from map_prop, property
      where map_id=id);
  END;
$$ LANGUAGE plpgsql;

--update property by key id
CREATE OR REPLACE FUNCTION updateMapsetPropertyById(id integer, propertyId integer, propertyValue text)
RETURNS void AS $$
  BEGIN
    update map_prop set props = jsonb_set(props, ('{'||propertyId::text||'}')::text[], ('"'||propertyValue||'"')::jsonb)
      where map_id=id;
  END;
$$ LANGUAGE plpgsql;

--update property by property name --> should match with the property name in the CV table
CREATE OR REPLACE FUNCTION updateMapsetPropertyByName(id integer, propertyName text, propertyValue text)
RETURNS void AS $$
  BEGIN
    with property as (select cv_id from cv where term=propertyName)
    update map_prop 
      set props = jsonb_set(props, ('{'||property.cv_id::text||'}')::text[], ('"'||propertyValue||'"')::jsonb)
      from property
      where map_id=id;
  END;
$$ LANGUAGE plpgsql;

--delete property by ID
CREATE OR REPLACE FUNCTION deleteMapsetPropertyById(id integer, propertyId integer)
RETURNS integer AS $$
  BEGIN
    update map_prop 
    set props = props - propertyId::text
    where map_id=id;
    return propertyId;
  END;
$$ LANGUAGE plpgsql;

--delete property by name
CREATE OR REPLACE FUNCTION deleteMapsetPropertyByName(id integer, propertyName text)
RETURNS text AS $$
  BEGIN
    with property as (select cv_id from cv where term=propertyName)
    update map_prop 
      set props = props - property.cv_id::text
      from property
      where map_id=id;
    return propertyName;
  END;
$$ LANGUAGE plpgsql;

--### MarkerLinkageGroup ###--

--create a new row, you may supply null for columns that are nullable
CREATE OR REPLACE FUNCTION createMarkerLinkageGroup(markerId integer, markerLinkageGroupStart decimal, markerLinkageGroupStop decimal, linkageGroupId integer, OUT id integer)
RETURNS integer AS $$
  BEGIN
    insert into marker_linkage_group (marker_id, start, stop, linkage_group_id)
      values (markerId, markerLinkageGroupStart, markerLinkageGroupStop, linkageGroupId); 
    select lastval() into id;
  END;
$$ LANGUAGE plpgsql;

--update all columns
CREATE OR REPLACE FUNCTION updateMarkerLinkageGroup(id integer, markerId integer, markerLinkageGroupStart decimal, markerLinkageGroupStop decimal, linkageGroupId integer)
RETURNS void AS $$
    BEGIN
    update marker_linkage_group set marker_id=markerId, start=markerLinkageGroupStart, stop=markerLinkageGroupStop, linkage_group_id=linkageGroupId
     where marker_linkage_group_id = id;
    END;
$$ LANGUAGE plpgsql;

--delete
CREATE OR REPLACE FUNCTION deleteMarkerLinkageGroup(id integer)
RETURNS integer AS $$
    BEGIN
    delete from marker_linkage_group where marker_linkage_group_id = id;
    return id;
    END;
$$ LANGUAGE plpgsql;

--### LinkageGroup ###--

--create a new row, you may supply null for columns that are nullable
CREATE OR REPLACE FUNCTION createLinkageGroup(linkageGroupName text, linkageGroupStart integer, linkageGroupStop integer, mapId integer, OUT id integer)
RETURNS integer AS $$
  BEGIN
    insert into linkage_group (name, start, stop, map_id)
      values (linkageGroupName, linkageGroupStart, linkageGroupStop, mapId); 
    select lastval() into id;
  END;
$$ LANGUAGE plpgsql;

--update all columns
CREATE OR REPLACE FUNCTION updateLinkageGroup(id integer, linkageGroupName text, linkageGroupStart integer, linkageGroupStop integer, mapId integer)
RETURNS void AS $$
    BEGIN
    update linkage_group set name=linkageGroupName, start=linkageGroupStart, stop=linkageGroupStop, map_id=mapId
     where linkage_group_id = id;
    END;
$$ LANGUAGE plpgsql;

--delete
CREATE OR REPLACE FUNCTION deleteLinkageGroup(id integer)
RETURNS integer AS $$
    BEGIN
    delete from linkage_group where linkage_group_id = id;
    return id;
    END;
$$ LANGUAGE plpgsql;

--### Marker and Properties ###--

--create a new row, you may supply null for columns that are nullable
CREATE OR REPLACE FUNCTION createMarker(platformId integer, variantId integer, markerName text, markerCode text, markerRef text, markerAlts text[], markerSequence text, referenceId integer, strandId integer, markerStatus integer, OUT id integer)
RETURNS integer AS $$
  BEGIN
    insert into marker (marker_id, platform_id, variant_id, name, code, ref, alts, sequence, reference_id, primers, probsets, strand_id, status)
      values (markerId, platformId, variantId, markerName, markerCode, markerRef, markerAlts, markerSequence, referenceId, '{}'::jsonb, '{}'::jsonb, strandId, markerStatus); 
    select lastval() into id;
  END;
$$ LANGUAGE plpgsql;

--update all columns
CREATE OR REPLACE FUNCTION updateMarker(id integer, platformId integer, variantId integer, markerName text, markerCode text, markerRef text, markerAlts text[], markerSequence text, referenceId integer, strandId integer, markerStatus integer)
RETURNS void AS $$
    BEGIN
    update marker set  platform_id=platformId, variant_id=variantId, name=markerName, code=markerCode, ref=markerRef, alts=markerAlts, sequence=markerSequence, reference_id=referenceId, primers='{}'::jsonb, probsets='{}'::jsonb, strand_id=strandId, status=markerStatus
     where marker_id = id;
    END;
$$ LANGUAGE plpgsql;

--delete
CREATE OR REPLACE FUNCTION deleteMarker(id integer)
RETURNS integer AS $$
    BEGIN
    delete from marker where marker_id = id;
    return id;
    END;
$$ LANGUAGE plpgsql;

--jsonb/properties
--create or update property
--upsert (update or insert) property by key id
CREATE OR REPLACE FUNCTION upsertMarkerPropertyById(id integer, propertyId integer, propertyValue text)
RETURNS void AS $$
  BEGIN
    update marker_prop set props = props || ('{"'||propertyId::text||'": "'||propertyValue||'"}')::jsonb
      where marker_id=id;
  END;
$$ LANGUAGE plpgsql;

--upsert by property name
CREATE OR REPLACE FUNCTION upsertMarkerPropertyByName(id integer, propertyName text, propertyValue text)
RETURNS integer AS $$
  DECLARE
    propertyId integer;
  BEGIN
    select cv_id into propertyId from cv where term=propertyName;
    update marker_prop set props = props || ('{"'||propertyId::text||'": "'||propertyValue||'"}')::jsonb
      where marker_id=id;
    return propertyId;
  END;
$$ LANGUAGE plpgsql;

--read/select all
CREATE OR REPLACE FUNCTION getAllPropertiesOfMarker(id integer)
RETURNS table (property_id integer, property_name text, property_value text) AS $$
  BEGIN
    return query
    select p1.key::int as property_id, cv.term as property_name, p1.value as property_value
    from cv, (select (jsonb_each_text(props)).* from marker_prop where marker_id=id) as p1
    where cv.cv_id = p1.key::int;
    END;
$$ LANGUAGE plpgsql;

--read/select a property of a entity given its id and property id (cv_id of that property)
CREATE OR REPLACE FUNCTION getMarkerPropertyById(id integer, propertyId integer)
RETURNS text AS $$
  DECLARE
    value text;
  BEGIN
    select props->propertyId::text into value from marker_prop where marker_id=id;
    return value;
  END;
$$ LANGUAGE plpgsql;

--read/select a property of a single entity given its id and property name --> should match with the property name in the CV table
CREATE OR REPLACE FUNCTION getMarkerPropertyByName(id integer, propertyName text)
RETURNS table (property_id integer, property_value text) AS $$
  BEGIN
    return query
    (with property as (select cv_id from cv where term=propertyName)
    select property.cv_id, (props->property.cv_id::text)::text as value
      from marker_prop, property
      where marker_id=id);
  END;
$$ LANGUAGE plpgsql;

--update property by key id
CREATE OR REPLACE FUNCTION updateMarkerPropertyById(id integer, propertyId integer, propertyValue text)
RETURNS void AS $$
  BEGIN
    update marker_prop set props = jsonb_set(props, ('{'||propertyId::text||'}')::text[], ('"'||propertyValue||'"')::jsonb)
      where marker_id=id;
  END;
$$ LANGUAGE plpgsql;

--update property by property name --> should match with the property name in the CV table
CREATE OR REPLACE FUNCTION updateMarkerPropertyByName(id integer, propertyName text, propertyValue text)
RETURNS void AS $$
  BEGIN
    with property as (select cv_id from cv where term=propertyName)
    update marker_prop 
      set props = jsonb_set(props, ('{'||property.cv_id::text||'}')::text[], ('"'||propertyValue||'"')::jsonb)
      from property
      where marker_id=id;
  END;
$$ LANGUAGE plpgsql;

--delete property by ID
CREATE OR REPLACE FUNCTION deleteMarkerPropertyById(id integer, propertyId integer)
RETURNS integer AS $$
  BEGIN
    update marker_prop 
    set props = props - propertyId::text
    where marker_id=id;
    return propertyId;
  END;
$$ LANGUAGE plpgsql;

--delete property by name
CREATE OR REPLACE FUNCTION deleteMarkerPropertyByName(id integer, propertyName text)
RETURNS text AS $$
  BEGIN
    with property as (select cv_id from cv where term=propertyName)
    update marker_prop 
      set props = props - property.cv_id::text
      from property
      where marker_id=id;
    return propertyName;
  END;
$$ LANGUAGE plpgsql;

--### Platform and Properties ###--

--create a new row, you may supply null for columns that are nullable
CREATE OR REPLACE FUNCTION createPlatform(platformName text, platformCode text, vendorId integer, platformDescription text, createdBy integer, createdDate date, modifiedBy integer, modifiedDate date, platformStatus integer, typeId integer, OUT id integer)
RETURNS integer AS $$
  BEGIN
    insert into platform (name, code, vendor_id, description, created_by, created_date, modified_by, modified_date, status, type_id)
      values (platformName, platformCode, vendorId, platformDescription, createdBy, createdDate, modifiedBy, modifiedDate, platformStatus, typeId); 
    select lastval() into id;
    insert into platform_prop (platform_id, props) values (id, '{}'::jsonb);
  END;
$$ LANGUAGE plpgsql;

--update all columns
CREATE OR REPLACE FUNCTION updatePlatform(id integer, platformName text, platformCode text, vendorId integer, platformDescription text, createdBy integer, createdDate date, modifiedBy integer, modifiedDate date, platformStatus integer, typeId integer)
RETURNS void AS $$
    BEGIN
    update platform set name=platformName, code=platformCode, vendor_id=vendorId, description=platformDescription, created_by=createdBy, created_date=createdDate, modified_by=modifiedBy, modified_date=modifiedDate, status=platformStatus, type_id=typeId
     where platform_id = id;
    END;
$$ LANGUAGE plpgsql;

--delete
CREATE OR REPLACE FUNCTION deletePlatform(id integer)
RETURNS integer AS $$
    BEGIN
    delete from platform where platform_id = id;
    return id;
    END;
$$ LANGUAGE plpgsql;

--jsonb/properties
--create or update property
--upsert (update or insert) property by key id
CREATE OR REPLACE FUNCTION upsertPlatformPropertyById(id integer, propertyId integer, propertyValue text)
RETURNS void AS $$
  BEGIN
    update platform_prop set props = props || ('{"'||propertyId::text||'": "'||propertyValue||'"}')::jsonb
      where platform_id=id;
  END;
$$ LANGUAGE plpgsql;

--upsert by property name
CREATE OR REPLACE FUNCTION upsertPlatformPropertyByName(id integer, propertyName text, propertyValue text)
RETURNS integer AS $$
  DECLARE
    propertyId integer;
  BEGIN
    select cv_id into propertyId from cv where term=propertyName;
    update platform_prop set props = props || ('{"'||propertyId::text||'": "'||propertyValue||'"}')::jsonb
      where platform_id=id;
    return propertyId;
  END;
$$ LANGUAGE plpgsql;

--read/select all
CREATE OR REPLACE FUNCTION getAllPropertiesOfPlatform(id integer)
RETURNS table (property_id integer, property_name text, property_value text) AS $$
  BEGIN
    return query
    select p1.key::int as property_id, cv.term as property_name, p1.value as property_value
    from cv, (select (jsonb_each_text(props)).* from platform_prop where platform_id=id) as p1
    where cv.cv_id = p1.key::int;
    END;
$$ LANGUAGE plpgsql;

--read/select a property of a entity given its id and property id (cv_id of that property)
CREATE OR REPLACE FUNCTION getPlatformPropertyById(id integer, propertyId integer)
RETURNS text AS $$
  DECLARE
    value text;
  BEGIN
    select props->propertyId::text into value from platform_prop where platform_id=id;
    return value;
  END;
$$ LANGUAGE plpgsql;

--read/select a property of a single entity given its id and property name --> should match with the property name in the CV table
CREATE OR REPLACE FUNCTION getPlatformPropertyByName(id integer, propertyName text)
RETURNS table (property_id integer, property_value text) AS $$
  BEGIN
    return query
    (with property as (select cv_id from cv where term=propertyName)
    select property.cv_id, (props->property.cv_id::text)::text as value
      from platform_prop, property
      where platform_id=id);
  END;
$$ LANGUAGE plpgsql;

--update property by key id
CREATE OR REPLACE FUNCTION updatePlatformPropertyById(id integer, propertyId integer, propertyValue text)
RETURNS void AS $$
  BEGIN
    update platform_prop set props = jsonb_set(props, ('{'||propertyId::text||'}')::text[], ('"'||propertyValue||'"')::jsonb)
      where platform_id=id;
  END;
$$ LANGUAGE plpgsql;

--update property by property name --> should match with the property name in the CV table
CREATE OR REPLACE FUNCTION updatePlatformPropertyByName(id integer, propertyName text, propertyValue text)
RETURNS void AS $$
  BEGIN
    with property as (select cv_id from cv where term=propertyName)
    update platform_prop 
      set props = jsonb_set(props, ('{'||property.cv_id::text||'}')::text[], ('"'||propertyValue||'"')::jsonb)
      from property
      where platform_id=id;
  END;
$$ LANGUAGE plpgsql;

--delete property by ID
CREATE OR REPLACE FUNCTION deletePlatformPropertyById(id integer, propertyId integer)
RETURNS integer AS $$
  BEGIN
    update platform_prop 
    set props = props - propertyId::text
    where platform_id=id;
    return propertyId;
  END;
$$ LANGUAGE plpgsql;

--delete property by name
CREATE OR REPLACE FUNCTION deletePlatformPropertyByName(id integer, propertyName text)
RETURNS text AS $$
  BEGIN
    with property as (select cv_id from cv where term=propertyName)
    update platform_prop 
      set props = props - property.cv_id::text
      from property
      where platform_id=id;
    return propertyName;
  END;
$$ LANGUAGE plpgsql;

--### MarkerGroup ###--

--create a new row, you may supply null for columns that are nullable
CREATE OR REPLACE FUNCTION createMarkerGroup(markerGroupName text, markerGroupCode text, germplasmGroup text, createdBy integer, createdDate date, modifiedBy integer, modifiedDate date, markerGroupStatus integer, OUT id integer)
RETURNS integer AS $$
  BEGIN
    insert into marker_group (name, code, markers, germplasm_group, created_by, created_date, modified_by, modified_date, status)
      values (markerGroupName, markerGroupCode, '{}'::jsonb, germplasmGroup, createdBy, createdDate, modifiedBy, modifiedDate, markerGroupStatus); 
    select lastval() into id;
  END;
$$ LANGUAGE plpgsql;

--update all columns
DROP FUNCTION updatemarkergroup(integer,text,text,text,integer,date,integer,date,integer);
CREATE OR REPLACE FUNCTION updateMarkerGroup(id integer, markerGroupName text, markerGroupCode text, germplasmGroup text, createdBy integer, createdDate date, modifiedBy integer, modifiedDate date, markerGroupStatus integer)
RETURNS void AS $$
    BEGIN
    update marker_group set name=markerGroupName, code=markerGroupCode, markers='{}'::jsonb, germplasm_group=germplasmGroup, created_by=createdBy, created_date=createdDate, modified_by=modifiedBy, modified_date=modifiedDate, status=markerGroupStatus
     where marker_group_id = id;
    END;
$$ LANGUAGE plpgsql;

--delete
CREATE OR REPLACE FUNCTION deleteMarkerGroup(id integer)
RETURNS integer AS $$
    BEGIN
    delete from marker_group where marker_group_id = id;
    return id;
    END;
$$ LANGUAGE plpgsql;

--jsonb column - markers
--create or update property
--upsert (update or insert) property by key id
CREATE OR REPLACE FUNCTION upsertMarkerToMarkerGroupById(id integer, markerId integer, favAllele text)
RETURNS void AS $$
  BEGIN
    update marker_group set markers = markers || ('{"'||markerId::text||'": "'||favAllele||'"}')::jsonb
      where marker_group_id=id;
  END;
$$ LANGUAGE plpgsql;

--upsert by property name
CREATE OR REPLACE FUNCTION upsertMarkerToMarkerGroupByName(id integer, markerName text, favAllele text)
RETURNS integer AS $$
  DECLARE
    markerId integer;
  BEGIN
    select marker_id into markerId from marker where name=markerName;
    update marker_group set markers = markers || ('{"'||markerId::text||'": "'||favAllele||'"}')::jsonb
      where marker_group_id=id;
    return propertyId;
  END;
$$ LANGUAGE plpgsql;

--read/select all
CREATE OR REPLACE FUNCTION getAllMarkersInMarkerGroup(id integer)
RETURNS table (marker_id integer, marker_name text, favorable_allele text) AS $$
  BEGIN
    return query
    select p1.key::int as marker_id, marker.name as marker_name, p1.value as favorable_allele
    from marker, (select (jsonb_each_text(markers)).* from marker_group where marker_group_id=id) as p1
    where marker.marker_id = p1.key::int;
    END;
$$ LANGUAGE plpgsql;

--read/select a single marker in group
CREATE OR REPLACE FUNCTION getMarkerInMarkerGroupById(id integer, markerId integer)
RETURNS text AS $$
  DECLARE
    value text;
  BEGIN
    select markers->markerId::text into value from marker_group where marker_group_id=id;
    return value;
  END;
$$ LANGUAGE plpgsql;

--read/select a single marker by name
CREATE OR REPLACE FUNCTION getMarkerInMarkerGroupByName(id integer, markerName text)
RETURNS table (marker_id integer, favorable_allele text) AS $$
  BEGIN
    return query
    with markerInfo as (select marker_id from marker where name=markerName)
    select markerInfo.marker_id, (props->markerInfo.marker_id::text)::text as favAllele
      from marker_group, markerInfo
      where marker_group_id=id;
  END;
$$ LANGUAGE plpgsql;

--delete property by ID
DROP FUNCTION deletemarkerinmarkergroupbyid(integer,integer);
CREATE OR REPLACE FUNCTION deleteMarkerInMarkerGroupById(id integer, markerId integer)
RETURNS void AS $$
  BEGIN
    update marker_group 
    set markers = markers - markerId::text
    where marker_group_id=id;
  END;
$$ LANGUAGE plpgsql;

--delete property by name
DROP FUNCTION deletemarkerinmarkergroupbyname(integer,text);
CREATE OR REPLACE FUNCTION deleteMarkerInMarkerGroupByName(id integer, markerName text)
RETURNS text AS $$
  BEGIN
    with markerInfo as (select marker_id from marker where name=markerName)
    update marker_group 
      set markers = markers - markerInfo.marker_id::text
      from markerInfo
      where marker_group_id=id;
    return markerName;
  END;
$$ LANGUAGE plpgsql;

--### DatasetMarker ###--

--create a new row, you may supply null for columns that are nullable
CREATE OR REPLACE FUNCTION createDatasetMarker(datasetId integer, markerId integer, callRate real, datasetMarkerMaf real, datasetMarkerReproducibility real, datasetMarkerIdx integer, OUT id integer)
RETURNS integer AS $$
  BEGIN
    insert into dataset_marker (dataset_id, marker_id, call_rate, maf, reproducibility, scores, marker_idx)
      values (datasetId, markerId, callRate, datasetMarkerMaf, datasetMarkerReproducibility, '{}'::jsonb, datasetMarkerIdx); 
    select lastval() into id;
  END;
$$ LANGUAGE plpgsql;

--update all columns
CREATE OR REPLACE FUNCTION updateDatasetMarker(id integer, datasetId integer, markerId integer, callRate real, datasetMarkerMaf real, datasetMarkerReproducibility real, datasetMarkerIdx integer)
RETURNS void AS $$
    BEGIN
    update dataset_marker set dataset_id=datasetId, marker_id=markerId, call_rate=callRate, maf=datasetMarkerMaf, reproducibility=datasetMarkerReproducibility, scores='{}'::jsonb, marker_idx=datasetMarkerIdx
     where dataset_marker_id = id;
    END;
$$ LANGUAGE plpgsql;

--delete
CREATE OR REPLACE FUNCTION deleteDatasetMarker(id integer)
RETURNS integer AS $$
    BEGIN
    delete from dataset_marker where dataset_marker_id = id;
    return id;
    END;
$$ LANGUAGE plpgsql;

--### Variant ###--

--create a new row, you may supply null for columns that are nullable
CREATE OR REPLACE FUNCTION createVariant(variantCode text, createdBy integer, createdDate date, modifiedBy integer, modifiedDate date, OUT id integer)
RETURNS integer AS $$
  BEGIN
    insert into variant (code, created_by, created_date, modified_by, modified_date)
      values (variantCode, createdBy, createdDate, modifiedBy, modifiedDate); 
    select lastval() into id;
  END;
$$ LANGUAGE plpgsql;

--update all columns
CREATE OR REPLACE FUNCTION updateVariant(id integer, variantCode text, createdBy integer, createdDate date, modifiedBy integer, modifiedDate date)
RETURNS void AS $$
    BEGIN
    update variant set code=variantCode, created_by=createdBy, created_date=createdDate, modified_by=modifiedBy, modified_date=modifiedDate
     where variant_id = id;
    END;
$$ LANGUAGE plpgsql;

--delete
CREATE OR REPLACE FUNCTION deleteVariant(id integer)
RETURNS integer AS $$
    BEGIN
    delete from variant where variant_id = id;
    return id;
    END;
$$ LANGUAGE plpgsql;

--### Organization ###--

--create a new manifest, you may supply null for columns that are nullable
CREATE OR REPLACE FUNCTION createOrganization(orgName text, orgAddress text, orgWebsite text, createdBy integer, createdDate date, modifiedBy integer, modifiedDate date, orgStatus integer, OUT id integer)
RETURNS integer AS $$
  BEGIN
    insert into organization (name, address, website, created_by, created_date, modified_by, modified_date, status)
      values (orgName, orgAddress, orgWebsite, createdBy, createdDate, modifiedBy, modifiedDate, orgStatus); 
    select lastval() into id;
  END;
$$ LANGUAGE plpgsql;

--update all manifest columns
--You can "avoid" updating certain columns by passing the same value as what's currently in that column
--OR I can create update functions that updates only certain columns, just let me know.
CREATE OR REPLACE FUNCTION updateOrganization(orgId integer, orgName text, orgAddress text, orgWebsite text, createdBy integer, createdDate date, modifiedBy integer, modifiedDate date, orgStatus integer)
RETURNS void AS $$
    BEGIN
    update organization set name=orgName, address=orgAddress, website=orgWebsite, created_by=createdBy, created_date=createdDate, modified_by=modifiedBy, modified_date=modifiedDate, status=orgStatus
     where organization_id = orgId;
    END;
$$ LANGUAGE plpgsql;


--delete
CREATE OR REPLACE FUNCTION deleteOrganization(id integer)
RETURNS integer AS $$
    BEGIN
    delete from organization where organization_id = id;
    return id;
    END;
$$ LANGUAGE plpgsql;


--######################################################################
-- Extraction functions 
--######################################################################
--drop function getMinimalMarkerMetadataByDataset(datasetId integer);
CREATE OR REPLACE FUNCTION getMinimalMarkerMetadataByDataset(datasetId integer)
RETURNS table (marker_name text, alleles text, chrom varchar, pos numeric, strand text) AS $$
  BEGIN
    return query
    with dm as (select dm.marker_id, dm.marker_idx from dataset_marker dm where dm.dataset_id=datasetId)
    select m.name as marker_name, m.ref || '/' || array_to_string(m.alts, ',', '?') as alleles, mlp.linkage_group_name as chrom, mlp.stop as pos, cv.term as strand
    from dm inner join marker m on m.marker_id = dm.marker_id 
    left join v_marker_linkage_physical mlp on m.marker_id = mlp.marker_id
    left join cv on m.strand_id = cv.cv_id
    order by dm.marker_idx;
  END;
$$ LANGUAGE plpgsql;

--drop function getAllMarkerMetadataByDataset(datasetId integer);
CREATE OR REPLACE FUNCTION getAllMarkerMetadataByDataset(datasetId integer)
RETURNS table (marker_name text, linkage_group_name varchar, start numeric, stop numeric, mapset_name text, platform_name text, variant_id integer, code text, ref text, alts text, sequence text, reference_name text, primers jsonb, probsets jsonb, strand_name text) AS $$
  BEGIN
    return query
    with dm as (select dm.marker_id, dm.marker_idx from dataset_marker dm where dm.dataset_id=datasetId)
    select m.name as marker_name, mlp.linkage_group_name, mlp.start, mlp.stop, mlp.mapset_name, p.name as platform_name, m.variant_id, m.code, m.ref, array_to_string(m.alts, ',', '?'), m.sequence, r.name as reference_name, m.primers, m.probsets, cv.term as strand_name
      from marker m inner join platform p on m.platform_id = p.platform_id
      inner join dm on m.marker_id = dm.marker_id 
      left join reference r on m.reference_id = r.reference_id
      left join cv on m.strand_id = cv.cv_id 
      left join v_marker_linkage_physical mlp on m.marker_id = mlp.marker_id
      order by dm.marker_idx;
  END;
$$ LANGUAGE plpgsql;

--drop function getMinimalSampleMetadataByDataset(datasetId integer)
CREATE OR REPLACE FUNCTION getMinimalSampleMetadataByDataset(datasetId integer)
RETURNS table (dnarun_name text, sample_name text, germplasm_name text, external_code text, germplasm_type text, species text, platename text, num text, well_row text, well_col text) AS $$
  BEGIN
    return query
    with dd as (select dd.dnarun_id, dd.dnarun_idx from dataset_dnarun dd where dd.dataset_id=datasetId)
    select dr.name as dnarun_name, ds.name as sample_name, g.name as germplasm_name, g.external_code, c1.term as germplasm_type, c2.term as species, ds.platename, ds.num, ds.well_row, ds.well_col
    from dd inner join dnarun dr on dr.dnarun_id = dd.dnarun_id 
    inner join dnasample ds on dr.dnasample_id = ds.dnasample_id 
    inner join germplasm g on ds.germplasm_id = g.germplasm_id 
    left join cv as c1 on g.type_id = c1.cv_id 
    left join cv as c2 on g.species_id = c2.cv_id
    order by dd.dnarun_idx;
  END;
$$ LANGUAGE plpgsql;
 
--drop function getAllSampleMetadataByDataset(datasetId integer);
CREATE OR REPLACE FUNCTION getAllSampleMetadataByDataset(datasetId integer)
RETURNS table (dnarun_name text, sample_name text, germplasm_name text, external_code text, germplasm_type text, species text, platename text, num text, well_row text, well_col text) AS $$
  BEGIN
    return query
    with dd as (select dd.dnarun_id, dd.dnarun_idx from dataset_dnarun dd where dd.dataset_id=datasetId)
      select dr.name as dnarun_name, ds.name as sample_name, g.name as germplasm_name, g.external_code, c1.term as germplasm_type, c2.term as species, ds.platename, ds.num, ds.well_row, ds.well_col
      from dd inner join dnarun dr on dr.dnarun_id = dd.dnarun_id 
      inner join dnasample ds on dr.dnasample_id = ds.dnasample_id 
      inner join germplasm g on ds.germplasm_id = g.germplasm_id 
      left join cv as c1 on g.type_id = c1.cv_id 
      left join cv as c2 on g.species_id = c2.cv_id
      order by dd.dnarun_idx;
  END;
$$ LANGUAGE plpgsql;


/*
CREATE OR REPLACE FUNCTION getAllMarkerMetadataByDataset(datasetId integer)
RETURNS table (marker_name text, linkage_group_name varchar, start numeric, stop numeric, mapset_name text, platform_name text, variant_id integer, code text, ref text, alts text, sequence text, reference_name text, primers jsonb, probsets jsonb, strand_name text) AS $$
  BEGIN
    return query
    with dm as (select dm.marker_id, dm.marker_idx from dataset_marker dm where dm.dataset_id=datasetId)
    select m.name as marker_name, mlp.linkage_group_name, mlp.start, mlp.stop, mlp.mapset_name, p.name as platform_name, m.variant_id, m.code, m.ref, array_to_string(m.alts, ',', '?'), m.sequence, r.name as reference_name, m.primers, m.probsets, cv.term as strand_name
      from marker m, platform p, reference r, cv, v_marker_linkage_physical mlp, dm
      where m.marker_id = dm.marker_id 
      and m.platform_id = p.platform_id
      and m.reference_id = r.reference_id
      and m.strand_id = cv.cv_id
      and m.marker_id = mlp.marker_id
      order by dm.marker_idx;
  END;
$$ LANGUAGE plpgsql;

--drop function getMinimalMarkerMetadataByDataset(integer)
CREATE OR REPLACE FUNCTION getMinimalMarkerMetadataByDataset(datasetId integer)
RETURNS table (marker_name text, alleles text, chrom varchar, pos integer, strand text) AS $$
  BEGIN
    return query
    with dm as (select dm.marker_id, dm.marker_idx from dataset_marker dm where dm.dataset_id=datasetId)
    select m.name as marker_name, m.ref || '/' || array_to_string(m.alts, ',', '?') as alleles, mlp.linkage_group_name as chrom, mlp.stop as pos, cv.term as strand
      from marker m, cv, v_marker_linkage_genetic mlp, dm
      where m.marker_id = dm.marker_id 
      and m.strand_id = cv.cv_id
      and m.marker_id = mlp.marker_id
      order by dm.marker_idx;
  END;
$$ LANGUAGE plpgsql;
*/

CREATE OR REPLACE FUNCTION getMarkerNamesByDataset(datasetId integer)
RETURNS table (marker_id integer, marker_name text) AS $$
  BEGIN
    return query
    with dm as (select dm.marker_id, dm.marker_idx from dataset_marker dm where dm.dataset_id=datasetId)
    select m.marker_id, m.name as marker_name
      from marker m, dm
      where m.marker_id = dm.marker_id 
      order by dm.marker_idx;
  END;
$$ LANGUAGE plpgsql;

/*
--drop function getAllSampleMetadataByDataset(datasetId integer);
CREATE OR REPLACE FUNCTION getAllSampleMetadataByDataset(datasetId integer)
RETURNS table (dnarun_name text, dnasample_name text, platename text, num text, well_row text, well_col text, germplasm_name text, external_code text, germplasm_type text, species text) AS $$
  BEGIN
    return query
    with dd as (select dd.dnarun_id, dd.dnarun_idx from dataset_dnarun dd where dd.dataset_id=datasetId)
    select dr.name as dnarun_name, ds.name as dnasample_name, ds.platename, ds.num, ds.well_row, ds.well_col, g.name as germplasm_name, g.external_code, c1.term as germplasm_type, c2.term as species
      from dnarun dr, dnasample ds, germplasm g, cv as c1, cv as c2, dd
      where dr.dnarun_id = dd.dnarun_id
      and dr.dnasample_id = ds.dnasample_id
      and ds.germplasm_id = g.germplasm_id
      and g.type_id = c1.cv_id
      and g.species_id = c2.cv_id
      order by dd.dnarun_idx;
  END;
$$ LANGUAGE plpgsql;

--drop function getMinimalSampleMetadataByDataset(datasetId integer);
CREATE OR REPLACE FUNCTION getMinimalSampleMetadataByDataset(datasetId integer)
RETURNS table (sample_name text, platename text, num text, well_row text, well_col text, germplasm_name text, germplasm_type text, species text) AS $$
  BEGIN
    return query
    with dd as (select dd.dnarun_id, dd.dnarun_idx from dataset_dnarun dd where dd.dataset_id=datasetId)
    select ds.name as sample_name, ds.platename, ds.num, ds.well_row, ds.well_col, g.name as germplasm_name, c1.term as germplasm_type, c2.term as species
      from dnarun dr, dnasample ds, germplasm g, cv as c1, cv as c2, dd
      where dr.dnarun_id = dd.dnarun_id
      and dr.dnasample_id = ds.dnasample_id
      and ds.germplasm_id = g.germplasm_id
      and g.type_id = c1.cv_id
      and g.species_id = c2.cv_id
      order by dd.dnarun_idx;
  END;
$$ LANGUAGE plpgsql;
*/
CREATE OR REPLACE FUNCTION getDnarunNamesByDataset(datasetId integer)
RETURNS table (dnarun_id integer, dnarun_name text) AS $$
  BEGIN
    return query
    with dd as (select dd.dnarun_id, dd.dnarun_idx from dataset_dnarun dd where dd.dataset_id=datasetId)
    select  dr.dnarun_id, dr.name as dnarun_name 
      from dnarun dr, dd
      where dr.dnarun_id = dd.dnarun_id
      order by dd.dnarun_idx;
  END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION getAllProjectMetadataByDataset(datasetId integer)
RETURNS table (project_name text, description text, PI text, experiment_name text, platform_name text, dataset_name text, analysis_name text) AS $$
  BEGIN
    return query
    select p.name as project_name, p.description, c.firstname || ' ' || c.lastname as PI, e.name as experiment_name, pf.name as platform_name, d.name as dataset_name, a.name as analysis_name
      from dataset d, experiment e, project p, contact c, platform pf, analysis a
      where d.dataset_id = datasetId
      and d.callinganalysis_id = a.analysis_id
      and d.experiment_id = e.experiment_id
      and e.project_id = p.project_id
      and p.pi_contact = c.contact_id
      and e.platform_id = pf.platform_id;
  END;
$$ LANGUAGE plpgsql;

--######################################################################
-- Phil's functions
-- select get_contacts('');
-- fetch all in "<unnamed portal 2>";
-- was: get_contact_names_by_type
CREATE OR REPLACE FUNCTION getContactNamesByRole(_role_name varchar(25))
RETURNS refcursor AS $$
  DECLARE
    contacts refcursor;           -- Declare cursor variables                         
  BEGIN
    OPEN contacts FOR 
    SELECT c.contact_id,
				c.lastname,
				c.firstname
		from contact c
		join role r on (r.role_id=ANY(c.roles))
		where r.role_name=_role_name;
    RETURN contacts;
  END;
$$ LANGUAGE plpgsql;
  
--was: get_contacts_by_type
CREATE OR REPLACE FUNCTION getContactsByRole(_role_name varchar(25))
RETURNS refcursor AS $$
    DECLARE
      contacts refcursor;           -- Declare cursor variables                         
    BEGIN
      OPEN contacts FOR 
      SELECT c.contact_id,
					c.lastname,
					c.firstname,
					c.code,
					c.email,
					r.role_id,
					r.role_name,
					r.role_code
			from contact c
			join role r on (r.role_id=ANY(c.roles))
			where r.role_name=_role_name;
      RETURN contacts;
    END;
$$ LANGUAGE plpgsql;
  
--was: get_contacts
--removed parameter as it was not needed
--this can also be removed as you can query the contacts table directly
CREATE OR REPLACE FUNCTION getAllContacts()
RETURNS refcursor AS $$
    DECLARE
      contacts refcursor;           -- Declare cursor variables                         
    BEGIN
      OPEN contacts FOR 
      SELECT c.contact_id,
					c.lastname ,
					c.firstname ,
					c.code ,
					c.email, 
					null as "roles", 
					c.created_by,
					c.created_date, 
					c.modified_by, 
					c.modified_date 
			from contact c;
      RETURN contacts;
    END;
$$ LANGUAGE plpgsql;

--was: get_project_names_by_pi
CREATE OR REPLACE FUNCTION getProjectNamesByPI(_contact_id integer)
RETURNS refcursor AS $$
    DECLARE
      projects refcursor;
    BEGIN
      OPEN projects FOR 
      select p.project_id, 
					p.name 
			from project p
			where p.pi_contact=_contact_id;
      RETURN projects;
    END;
$$ LANGUAGE plpgsql;


  
  
