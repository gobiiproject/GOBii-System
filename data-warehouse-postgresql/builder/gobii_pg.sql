--
-- PostgreSQL database dump
--

-- Dumped from database version 9.5.1
-- Dumped by pg_dump version 9.5.1

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: -
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


--
-- Name: file_fdw; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS file_fdw WITH SCHEMA public;


--
-- Name: EXTENSION file_fdw; Type: COMMENT; Schema: -; Owner: -
--

COMMENT ON EXTENSION file_fdw IS 'foreign-data wrapper for flat file access';


SET search_path = public, pg_catalog;

--
-- Name: keyvaluepair_type; Type: TYPE; Schema: public; Owner: -
--

CREATE TYPE keyvaluepair_type AS (
	key integer,
	val text
);


--
-- Name: myrowtype; Type: TYPE; Schema: public; Owner: -
--

CREATE TYPE myrowtype AS (
	"23" text,
	"24" text,
	"25" text,
	"26" text,
	"27" text
);


--
-- Name: addanalysistodataset(integer, integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION addanalysistodataset(datasetid integer, analysisid integer) RETURNS void
    LANGUAGE plpgsql
    AS $$
    BEGIN
    update dataset set analyses=array_append(analyses, analysisId)
     where dataset_id = id;
    END;
$$;


--
-- Name: appendanalysistodataset(integer, integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION appendanalysistodataset(datasetid integer, analysisid integer) RETURNS void
    LANGUAGE plpgsql
    AS $$
    BEGIN
    update dataset set analyses=array_append(analyses, analysisId)
     where dataset_id = datasetId;
    END;
$$;


--
-- Name: appendreadtabletorole(integer, integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION appendreadtabletorole(roleid integer, tableid integer) RETURNS void
    LANGUAGE plpgsql
    AS $$
    BEGIN
    update role set read_tables=array_append(read_tables, tableId)
     where role_id = roleId;
    END;
$$;


--
-- Name: appendroletocontact(integer, integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION appendroletocontact(contactid integer, roleid integer) RETURNS void
    LANGUAGE plpgsql
    AS $$
    BEGIN
    update contact set roles=array_append(roles, roleId)
     where contact_id = contactId;
    END;
$$;


--
-- Name: appendwritetabletorole(integer, integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION appendwritetabletorole(roleid integer, tableid integer) RETURNS void
    LANGUAGE plpgsql
    AS $$
    BEGIN
    update role set write_tables=array_append(write_tables, tableId)
     where role_id = roleId;
    END;
$$;


--
-- Name: createanalysis(text, text, integer, text, text, text, text, text, text, integer, timestamp without time zone, integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION createanalysis(analysisname text, analysisdescription text, typeid integer, analysisprogram text, analysisprogramversion text, aanalysisalgorithm text, analysissourcename text, analysissourceversion text, analysissourceuri text, referenceid integer, analysistimeexecuted timestamp without time zone, analysisstatus integer, OUT id integer) RETURNS integer
    LANGUAGE plpgsql
    AS $$
  BEGIN
    insert into analysis (name, description, type_id, program, programversion, algorithm, sourcename, sourceversion, sourceuri, reference_id, parameters, timeexecuted, status)
      values (analysisName, analysisDescription, typeId, analysisProgram, analysisProgramversion, aanalysisAlgorithm, analysisSourcename, analysisSourceversion, analysisSourceuri, referenceId, '{}'::jsonb, analysisTimeexecuted, analysisStatus); 
    select lastval() into id;
  END;
$$;


--
-- Name: createanalysis(text, text, integer, text, text, text, text, text, text, integer, jsonb, timestamp without time zone, integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION createanalysis(analysisname text, analysisdescription text, typeid integer, analysisprogram text, analysisprogramversion text, aanalysisalgorithm text, analysissourcename text, analysissourceversion text, analysissourceuri text, referenceid integer, analysisparameters jsonb, analysistimeexecuted timestamp without time zone, analysisstatus integer, OUT id integer) RETURNS integer
    LANGUAGE plpgsql
    AS $$
  BEGIN
    insert into analysis (name, description, type_id, program, programversion, algorithm, sourcename, sourceversion, sourceuri, reference_id, parameters, timeexecuted, status)
      values (analysisName, analysisDescription, typeId, analysisProgram, analysisProgramversion, aanalysisAlgorithm, analysisSourcename, analysisSourceversion, analysisSourceuri, referenceId, analysisParameters, analysisTimeexecuted, analysisStatus); 
    select lastval() into id;
  END;
$$;


--
-- Name: createcontact(text, text, text, text, integer[], integer, date, integer, date, integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION createcontact(lastname text, firstname text, contactcode text, contactemail text, contactroles integer[], createdby integer, createddate date, modifiedby integer, modifieddate date, organizationid integer, OUT id integer) RETURNS integer
    LANGUAGE plpgsql
    AS $$
  BEGIN
    insert into contact (lastname, firstname, code, email, roles, created_by, created_date, modified_by, modified_date, organization_id)
      values (lastName, firstName, contactCode, contactEmail, contactRoles, createdBy, createdDate, modifiedBy, modifiedDate, organizationId); 
    select lastval() into id;
  END;
$$;


--
-- Name: createcv(text, text, text, integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION createcv(cvgroup text, cvterm text, cvdefinition text, cvrank integer, OUT id integer) RETURNS integer
    LANGUAGE plpgsql
    AS $$
  BEGIN
    insert into cv ("group", term, definition, rank)
      values (cvGroup, cvTerm, cvDefinition, cvRank); 
    select lastval() into id;
  END;
$$;


--
-- Name: createdataset(text, integer, integer, integer[], text, text, text, text, integer, date, integer, date, integer, integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION createdataset(datasetname text, experimentid integer, callinganalysisid integer, datasetanalyses integer[], datatable text, datafile text, qualitytable text, qualityfile text, createdby integer, createddate date, modifiedby integer, modifieddate date, datasetstatus integer, typeid integer, OUT id integer) RETURNS integer
    LANGUAGE plpgsql
    AS $$
  BEGIN
    insert into dataset (experiment_id, callinganalysis_id, analyses, data_table, data_file, quality_table, quality_file, scores, created_by, created_date, modified_by, modified_date, status, type_id, name)
      values (experimentId, callinganalysisId, datasetAnalyses, dataTable, dataFile, qualityTable, qualityFile, '{}'::jsonb, createdBy, createdDate, modifiedBy, modifiedDate, datasetStatus, typeId, datasetName); 
    select lastval() into id;
  END;
$$;


--
-- Name: createdatasetdnarun(integer, integer, integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION createdatasetdnarun(datasetid integer, dnarunid integer, dnarunidx integer, OUT id integer) RETURNS integer
    LANGUAGE plpgsql
    AS $$
  BEGIN
    insert into dataset_dnarun (dataset_id, dnarun_id, dnarun_idx)
      values (datasetId, dnarunId, dnarunIdx); 
    select lastval() into id;
  END;
$$;


--
-- Name: createdatasetmarker(integer, integer, real, real, real, integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION createdatasetmarker(datasetid integer, markerid integer, callrate real, datasetmarkermaf real, datasetmarkerreproducibility real, datasetmarkeridx integer, OUT id integer) RETURNS integer
    LANGUAGE plpgsql
    AS $$
  BEGIN
    insert into dataset_marker (dataset_id, marker_id, call_rate, maf, reproducibility, scores, marker_idx)
      values (datasetId, markerId, callRate, datasetMarkerMaf, datasetMarkerReproducibility, '{}'::jsonb, datasetMarkerIdx); 
    select lastval() into id;
  END;
$$;


--
-- Name: createdatasetmarker(integer, integer, real, real, real, jsonb); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION createdatasetmarker(datasetid integer, markerid integer, callrate real, datasetmarkermaf real, datasetmarkerreproducibility real, datasetmarkerscores jsonb, OUT id integer) RETURNS integer
    LANGUAGE plpgsql
    AS $$
  BEGIN
    insert into dataset_marker (dataset_id, marker_id, call_rate, maf, reproducibility, scores)
      values (datasetId, markerId, callRate, datasetMarkerMaf, datasetMarkerReproducibility, datasetMarkerScores); 
    select lastval() into id;
  END;
$$;


--
-- Name: createdisplay(text, text, text, integer, date, integer, date, integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION createdisplay(tablename text, columnname text, displayname text, createdby integer, createddate date, modifiedby integer, modifieddate date, displayrank integer, OUT id integer) RETURNS integer
    LANGUAGE plpgsql
    AS $$
  BEGIN
    insert into display (table_name, column_name, display_name, created_by, created_date, modified_by, modified_date, rank)
      values (tableName, columnName, displayName, createdBy, createdDate, modifiedBy, modifiedDate, displayRank); 
    select lastval() into id;
  END;
$$;


--
-- Name: creatednarun(integer, integer, text, text); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION creatednarun(experimentid integer, dnasampleid integer, dnarunname text, dnaruncode text, OUT id integer) RETURNS integer
    LANGUAGE plpgsql
    AS $$
  BEGIN
    insert into dnarun (experiment_id, dnasample_id, name, code)
      values (experimentId, dnasampleId, dnarunName, dnarunCode); 
    select lastval() into id;
    insert into dnarun_prop (dnarun_id, props) values (id, '{}'::jsonb);
  END;
$$;


--
-- Name: creatednasample(text, text, text, text, text, text, integer, integer, integer, date, integer, date, integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION creatednasample(dnasamplename text, dnasamplecode text, dnasampleplatename text, dnasamplenum text, wellrow text, wellcol text, projectid integer, germplasmid integer, createdby integer, createddate date, modifiedby integer, modifieddate date, dnasamplestatus integer, OUT id integer) RETURNS integer
    LANGUAGE plpgsql
    AS $$
  BEGIN
    insert into dnasample (name, code, platename, num, well_row, well_col, project_id, germplasm_id, created_by, created_date, modified_by, modified_date, status)
      values (dnaSampleName, dnaSampleCode, dnaSamplePlateName, dnaSampleNum, wellRow, wellCol, projectId, germplasmId, createdBy, createdDate, modifiedBy, modifiedDate, dnaSampleStatus); 
    select lastval() into id;
    insert into dnasample_prop (dnasample_id, props) values (id, '{}'::jsonb);
  END;
$$;


--
-- Name: createexperiment(text, text, integer, integer, integer, text, integer, date, integer, date, integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION createexperiment(expname text, expcode text, projectid integer, platformid integer, manifestid integer, datafile text, createdby integer, createddate date, modifiedby integer, modifieddate date, expstatus integer, OUT expid integer) RETURNS integer
    LANGUAGE plpgsql
    AS $$
    BEGIN
    insert into experiment (name, code, project_id, platform_id, manifest_id, data_file, created_by, created_date, modified_by, modified_date, status)
      values (expName, expCode, projectId, platformId, manifestId, dataFile, createdBy, createdDate, modifiedBy, modifiedDate, expStatus); 
    select lastval() into expId;
    END;
$$;


--
-- Name: creategermplasm(text, text, integer, integer, integer, date, integer, date, integer, text); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION creategermplasm(germplasmname text, externalcode text, speciesid integer, typeid integer, createdby integer, createddate date, modifiedby integer, modifieddate date, germplasmstatus integer, germplasmcode text, OUT id integer) RETURNS integer
    LANGUAGE plpgsql
    AS $$
  BEGIN
    insert into germplasm (name, external_code, species_id, type_id, created_by, created_date, modified_by, modified_date, status, code)
      values (germplasmName, externalCode, speciesId, typeId, createdBy, createdDate, modifiedBy, modifiedDate, germplasmStatus, germplasmCode); 
    select lastval() into id;
    insert into germplasm_prop (germplasm_id, props) values (id, '{}'::jsonb);
  END;
$$;


--
-- Name: createlinkagegroup(text, integer, integer, integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION createlinkagegroup(linkagegroupname text, linkagegroupstart integer, linkagegroupstop integer, mapid integer, OUT id integer) RETURNS integer
    LANGUAGE plpgsql
    AS $$
  BEGIN
    insert into linkage_group (name, start, stop, map_id)
      values (linkageGroupName, linkageGroupStart, linkageGroupStop, mapId); 
    select lastval() into id;
  END;
$$;


--
-- Name: createmanifest(text, text, text, integer, date, integer, date); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION createmanifest(manifestname text, manifestcode text, filepath text, createdby integer, createddate date, modifiedby integer, modifieddate date, OUT mid integer) RETURNS integer
    LANGUAGE plpgsql
    AS $$
  BEGIN
    insert into manifest (name, code, file_path, created_by, created_date, modified_by, modified_date)
      values (manifestName, manifestCode, filePath, createdBy, createdDate, modifiedBy, modifiedDate); 
    select lastval() into mId;
  END;
$$;


--
-- Name: createmapset(text, text, text, integer, integer, integer, date, integer, date, integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION createmapset(mapsetname text, mapsetcode text, mapsetdescription text, referenceid integer, typeid integer, createdby integer, createddate date, modifiedby integer, modifieddate date, mapsetstatus integer, OUT id integer) RETURNS integer
    LANGUAGE plpgsql
    AS $$
  BEGIN
    insert into mapset (name, code, description, reference_id, type_id, created_by, created_date, modified_by, modified_date, status)
      values (mapsetName, mapsetCode, mapsetDescription, referenceId, typeId, createdBy, createdDate, modifiedBy, modifiedDate, mapsetStatus); 
    select lastval() into id;
    insert into map_prop (map_id, props) values (id, '{}'::jsonb);
  END;
$$;


--
-- Name: createmarker(integer, integer, text, text, text, text[], text, integer, integer, integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION createmarker(platformid integer, variantid integer, markername text, markercode text, markerref text, markeralts text[], markersequence text, referenceid integer, strandid integer, markerstatus integer, OUT id integer) RETURNS integer
    LANGUAGE plpgsql
    AS $$
  BEGIN
    insert into marker (marker_id, platform_id, variant_id, name, code, ref, alts, sequence, reference_id, primers, probsets, strand_id, status)
      values (markerId, platformId, variantId, markerName, markerCode, markerRef, markerAlts, markerSequence, referenceId, '{}'::jsonb, '{}'::jsonb, strandId, markerStatus); 
    select lastval() into id;
  END;
$$;


--
-- Name: createmarker(integer, integer, text, text, text, text[], text, integer, jsonb, text[], integer, integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION createmarker(platformid integer, variantid integer, markername text, markercode text, markerref text, markeralts text[], markersequence text, referenceid integer, markerprimers jsonb, markerprobsets text[], strandid integer, markerstatus integer, OUT id integer) RETURNS integer
    LANGUAGE plpgsql
    AS $$
  BEGIN
    insert into marker (marker_id, platform_id, variant_id, name, code, ref, alts, sequence, reference_id, primers, probsets, strand_id, status)
      values (markerId, platformId, variantId, markerName, markerCode, markerRef, markerAlts, markerSequence, referenceId, markerPrimers, markerProbsets, strandId, markerStatus); 
    select lastval() into id;
  END;
$$;


--
-- Name: createmarkergroup(text, text, text, integer, date, integer, date, integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION createmarkergroup(markergroupname text, markergroupcode text, germplasmgroup text, createdby integer, createddate date, modifiedby integer, modifieddate date, markergroupstatus integer, OUT id integer) RETURNS integer
    LANGUAGE plpgsql
    AS $$
  BEGIN
    insert into marker_group (name, code, markers, germplasm_group, created_by, created_date, modified_by, modified_date, status)
      values (markerGroupName, markerGroupCode, '{}'::jsonb, germplasmGroup, createdBy, createdDate, modifiedBy, modifiedDate, markerGroupStatus); 
    select lastval() into id;
  END;
$$;


--
-- Name: createmarkerlinkagegroup(integer, integer, integer, integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION createmarkerlinkagegroup(markerid integer, markerlinkagegroupstart integer, markerlinkagegroupstop integer, linkagegroupid integer, OUT id integer) RETURNS integer
    LANGUAGE plpgsql
    AS $$
  BEGIN
    insert into marker_linkage_group (marker_id, start, stop, linkage_group_id)
      values (markerId, markerLinkageGroupStart, markerLinkageGroupStop, linkageGroupId); 
    select lastval() into id;
  END;
$$;


--
-- Name: createmarkerlinkagegroup(integer, numeric, numeric, integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION createmarkerlinkagegroup(markerid integer, markerlinkagegroupstart numeric, markerlinkagegroupstop numeric, linkagegroupid integer, OUT id integer) RETURNS integer
    LANGUAGE plpgsql
    AS $$
  BEGIN
    insert into marker_linkage_group (marker_id, start, stop, linkage_group_id)
      values (markerId, markerLinkageGroupStart, markerLinkageGroupStop, linkageGroupId); 
    select lastval() into id;
  END;
$$;


--
-- Name: createorganization(text, text, text, integer, date, integer, date, integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION createorganization(orgname text, orgaddress text, orgwebsite text, createdby integer, createddate date, modifiedby integer, modifieddate date, orgstatus integer, OUT id integer) RETURNS integer
    LANGUAGE plpgsql
    AS $$
  BEGIN
    insert into organization (name, address, website, created_by, created_date, modified_by, modified_date, status)
      values (orgName, orgAddress, orgWebsite, createdBy, createdDate, modifiedBy, modifiedDate, orgStatus); 
    select lastval() into id;
  END;
$$;


--
-- Name: createplatform(text, text, integer, text, integer, date, integer, date, integer, integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION createplatform(platformname text, platformcode text, vendorid integer, platformdescription text, createdby integer, createddate date, modifiedby integer, modifieddate date, platformstatus integer, typeid integer, OUT id integer) RETURNS integer
    LANGUAGE plpgsql
    AS $$
  BEGIN
    insert into platform (name, code, vendor_id, description, created_by, created_date, modified_by, modified_date, status, type_id)
      values (platformName, platformCode, vendorId, platformDescription, createdBy, createdDate, modifiedBy, modifiedDate, platformStatus, typeId); 
    select lastval() into id;
    insert into platform_prop (platform_id, props) values (id, '{}'::jsonb);
  END;
$$;


--
-- Name: createproject(text, text, text, integer, integer, date, integer, date, integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION createproject(projectname text, projectcode text, projectdescription text, picontact integer, createdby integer, createddate date, modifiedby integer, modifieddate date, projectstatus integer, OUT projectid integer) RETURNS integer
    LANGUAGE plpgsql
    AS $$
    BEGIN
    insert into project (name, code, description, pi_contact, created_by, created_date, modified_by, modified_date, status)
      values (projectName, projectCode, projectDescription, piContact, createdBy, createdDate, modifiedBy, modifiedDate, projectStatus); 
          
    select lastval() into projectId;
    insert into project_prop (project_id, props) values (projectId, '{}'::jsonb);
    END;
$$;


--
-- Name: createreference(text, text, text, text); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION createreference(referencename text, referenceversion text, referencelink text, filepath text, OUT id integer) RETURNS integer
    LANGUAGE plpgsql
    AS $$
  BEGIN
    insert into reference (name, version, link, file_path)
      values (referenceName, referenceVersion, referenceLink, filePath); 
    select lastval() into id;
  END;
$$;


--
-- Name: createrole(text, text, integer[], integer[]); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION createrole(rolename text, rolecode text, readtables integer[], writetables integer[], OUT id integer) RETURNS integer
    LANGUAGE plpgsql
    AS $$
  BEGIN
    insert into role (role_name, role_code, read_tables, write_tables)
      values (roleName, roleCode, readTables, writeTables); 
    select lastval() into id;
  END;
$$;


--
-- Name: createvariant(text, integer, date, integer, date); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION createvariant(variantcode text, createdby integer, createddate date, modifiedby integer, modifieddate date, OUT id integer) RETURNS integer
    LANGUAGE plpgsql
    AS $$
  BEGIN
    insert into variant (code, created_by, created_date, modified_by, modified_date)
      values (variantCode, createdBy, createdDate, modifiedBy, modifiedDate); 
    select lastval() into id;
  END;
$$;


--
-- Name: deleteanalysis(integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION deleteanalysis(id integer) RETURNS integer
    LANGUAGE plpgsql
    AS $$
    BEGIN
    delete from analysis where analysis_id = id;
    return id;
    END;
$$;


--
-- Name: deleteanalysisparameter(integer, text); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION deleteanalysisparameter(id integer, parametername text) RETURNS text
    LANGUAGE plpgsql
    AS $$
  BEGIN
    update analysis set parameters = parameters - parameterName
      where analysis_id=id;
    return parameterName;
  END;
$$;


--
-- Name: deletecontact(integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION deletecontact(id integer) RETURNS integer
    LANGUAGE plpgsql
    AS $$
    BEGIN
    delete from contact where contact_id = id;
    return id;
    END;
$$;


--
-- Name: deletecv(integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION deletecv(id integer) RETURNS integer
    LANGUAGE plpgsql
    AS $$
    BEGIN
    delete from cv where cv_id = id;
    return id;
    END;
$$;


--
-- Name: deletedataset(integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION deletedataset(id integer) RETURNS integer
    LANGUAGE plpgsql
    AS $$
    BEGIN
    delete from dataset where dataset_id = id;
    return id;
    END;
$$;


--
-- Name: deletedatasetdnarun(integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION deletedatasetdnarun(id integer) RETURNS integer
    LANGUAGE plpgsql
    AS $$
    BEGIN
    delete from dataset where dataset_id = id;
    return id;
    END;
$$;


--
-- Name: deletedatasetmarker(integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION deletedatasetmarker(id integer) RETURNS integer
    LANGUAGE plpgsql
    AS $$
    BEGIN
    delete from dataset_marker where dataset_marker_id = id;
    return id;
    END;
$$;


--
-- Name: deletedisplay(integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION deletedisplay(id integer) RETURNS integer
    LANGUAGE plpgsql
    AS $$
    BEGIN
    delete from display where display_id = id;
    return id;
    END;
$$;


--
-- Name: deletednarun(integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION deletednarun(id integer) RETURNS integer
    LANGUAGE plpgsql
    AS $$
    BEGIN
    delete from dnarun where dnarun_id = id;
    return id;
    END;
$$;


--
-- Name: deletednarunpropertybyid(integer, integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION deletednarunpropertybyid(id integer, propertyid integer) RETURNS integer
    LANGUAGE plpgsql
    AS $$
  BEGIN
    update dnarun_prop 
    set props = props - propertyId::text
    where dnarun_id=id;
    return propertyId;
  END;
$$;


--
-- Name: deletednarunpropertybyname(integer, text); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION deletednarunpropertybyname(id integer, propertyname text) RETURNS text
    LANGUAGE plpgsql
    AS $$
  BEGIN
    with property as (select cv_id from cv where term=propertyName)
    update dnarun_prop 
      set props = props - property.cv_id::text
      from property
      where dnarun_id=id;
    return propertyName;
  END;
$$;


--
-- Name: deletednasample(integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION deletednasample(id integer) RETURNS integer
    LANGUAGE plpgsql
    AS $$
    BEGIN
    delete from dnasample where dnasample_id = id;
    return id;
    END;
$$;


--
-- Name: deletednasamplepropertybyid(integer, integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION deletednasamplepropertybyid(id integer, propertyid integer) RETURNS integer
    LANGUAGE plpgsql
    AS $$
  BEGIN
    update dnasample_prop 
    set props = props - propertyId::text
    where dnasample_id=id;
    return propertyId;
  END;
$$;


--
-- Name: deletednasamplepropertybyname(integer, text); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION deletednasamplepropertybyname(id integer, propertyname text) RETURNS text
    LANGUAGE plpgsql
    AS $$
  BEGIN
    with property as (select cv_id from cv where term=propertyName)
    update dnasample_prop 
      set props = props - property.cv_id::text
      from property
      where dnasample_id=id;
    return propertyName;
  END;
$$;


--
-- Name: deleteexperiment(integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION deleteexperiment(eid integer) RETURNS integer
    LANGUAGE plpgsql
    AS $$
    BEGIN
    delete from experiment where experiment_id = eId;
    return eId;
    END;
$$;


--
-- Name: deletegermplasm(integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION deletegermplasm(id integer) RETURNS integer
    LANGUAGE plpgsql
    AS $$
    BEGIN
    delete from germplasm where germplasm_id = id;
    return id;
    END;
$$;


--
-- Name: deletegermplasmpropertybyid(integer, integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION deletegermplasmpropertybyid(id integer, propertyid integer) RETURNS integer
    LANGUAGE plpgsql
    AS $$
  BEGIN
    update germplasm_prop 
    set props = props - propertyId::text
    where germplasm_id=id;
    return propertyId;
  END;
$$;


--
-- Name: deletegermplasmpropertybyname(integer, text); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION deletegermplasmpropertybyname(id integer, propertyname text) RETURNS text
    LANGUAGE plpgsql
    AS $$
  BEGIN
    with property as (select cv_id from cv where term=propertyName)
    update germplasm_prop 
      set props = props - property.cv_id::text
      from property
      where germplasm_id=id;
    return propertyName;
  END;
$$;


--
-- Name: deletelinkagegroup(integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION deletelinkagegroup(id integer) RETURNS integer
    LANGUAGE plpgsql
    AS $$
    BEGIN
    delete from linkage_group where linkage_group_id = id;
    return id;
    END;
$$;


--
-- Name: deletemanifest(integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION deletemanifest(id integer) RETURNS integer
    LANGUAGE plpgsql
    AS $$
    BEGIN
    delete from manifest where manifest_id = id;
    return id;
    END;
$$;


--
-- Name: deletemapset(integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION deletemapset(id integer) RETURNS integer
    LANGUAGE plpgsql
    AS $$
    BEGIN
    delete from mapset where mapset_id = id;
    return id;
    END;
$$;


--
-- Name: deletemapsetpropertybyid(integer, integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION deletemapsetpropertybyid(id integer, propertyid integer) RETURNS integer
    LANGUAGE plpgsql
    AS $$
  BEGIN
    update map_prop 
    set props = props - propertyId::text
    where map_id=id;
    return propertyId;
  END;
$$;


--
-- Name: deletemapsetpropertybyname(integer, text); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION deletemapsetpropertybyname(id integer, propertyname text) RETURNS text
    LANGUAGE plpgsql
    AS $$
  BEGIN
    with property as (select cv_id from cv where term=propertyName)
    update map_prop 
      set props = props - property.cv_id::text
      from property
      where map_id=id;
    return propertyName;
  END;
$$;


--
-- Name: deletemarker(integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION deletemarker(id integer) RETURNS integer
    LANGUAGE plpgsql
    AS $$
    BEGIN
    delete from marker where marker_id = id;
    return id;
    END;
$$;


--
-- Name: deletemarkergroup(integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION deletemarkergroup(id integer) RETURNS integer
    LANGUAGE plpgsql
    AS $$
    BEGIN
    delete from marker_group where marker_group_id = id;
    return id;
    END;
$$;


--
-- Name: deletemarkerinmarkergroupbyid(integer, integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION deletemarkerinmarkergroupbyid(id integer, markerid integer) RETURNS void
    LANGUAGE plpgsql
    AS $$
  BEGIN
    update marker_group 
    set markers = markers - markerId::text
    where marker_group_id=id;
  END;
$$;


--
-- Name: deletemarkerinmarkergroupbyname(integer, text); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION deletemarkerinmarkergroupbyname(id integer, markername text) RETURNS text
    LANGUAGE plpgsql
    AS $$
  BEGIN
    with markerInfo as (select marker_id from marker where name=markerName)
    update marker_group 
      set markers = markers - markerInfo.marker_id::text
      from markerInfo
      where marker_group_id=id;
    return markerName;
  END;
$$;


--
-- Name: deletemarkerlinkagegroup(integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION deletemarkerlinkagegroup(id integer) RETURNS integer
    LANGUAGE plpgsql
    AS $$
    BEGIN
    delete from marker_linkage_group where marker_linkage_group_id = id;
    return id;
    END;
$$;


--
-- Name: deletemarkerpropertybyid(integer, integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION deletemarkerpropertybyid(id integer, propertyid integer) RETURNS integer
    LANGUAGE plpgsql
    AS $$
  BEGIN
    update marker_prop 
    set props = props - propertyId::text
    where marker_id=id;
    return propertyId;
  END;
$$;


--
-- Name: deletemarkerpropertybyname(integer, text); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION deletemarkerpropertybyname(id integer, propertyname text) RETURNS text
    LANGUAGE plpgsql
    AS $$
  BEGIN
    with property as (select cv_id from cv where term=propertyName)
    update marker_prop 
      set props = props - property.cv_id::text
      from property
      where marker_id=id;
    return propertyName;
  END;
$$;


--
-- Name: deleteorganization(integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION deleteorganization(id integer) RETURNS integer
    LANGUAGE plpgsql
    AS $$
    BEGIN
    delete from organization where organization_id = id;
    return id;
    END;
$$;


--
-- Name: deleteplatform(integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION deleteplatform(id integer) RETURNS integer
    LANGUAGE plpgsql
    AS $$
    BEGIN
    delete from platform where platform_id = id;
    return id;
    END;
$$;


--
-- Name: deleteplatformpropertybyid(integer, integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION deleteplatformpropertybyid(id integer, propertyid integer) RETURNS integer
    LANGUAGE plpgsql
    AS $$
  BEGIN
    update platform_prop 
    set props = props - propertyId::text
    where platform_id=id;
    return propertyId;
  END;
$$;


--
-- Name: deleteplatformpropertybyname(integer, text); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION deleteplatformpropertybyname(id integer, propertyname text) RETURNS text
    LANGUAGE plpgsql
    AS $$
  BEGIN
    with property as (select cv_id from cv where term=propertyName)
    update platform_prop 
      set props = props - property.cv_id::text
      from property
      where platform_id=id;
    return propertyName;
  END;
$$;


--
-- Name: deleteproject(integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION deleteproject(pid integer) RETURNS integer
    LANGUAGE plpgsql
    AS $$
    BEGIN
    delete from project where project_id = pId;
    return pId;
    END;
$$;


--
-- Name: deleteprojectpropertybyid(integer, integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION deleteprojectpropertybyid(projectid integer, propertyid integer) RETURNS integer
    LANGUAGE plpgsql
    AS $$
  BEGIN
    update project_prop 
    set props = props - propertyId::text
    where project_id=projectId;
    return propertyId;
  END;
$$;


--
-- Name: deleteprojectpropertybyname(integer, text); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION deleteprojectpropertybyname(projectid integer, propertyname text) RETURNS text
    LANGUAGE plpgsql
    AS $$
  BEGIN
    with property as (select cv_id from cv where term=propertyName)
    update project_prop 
      set props = props - property.cv_id::text
      from property
      where project_id=projectId;
    return propertyName;
  END;
$$;


--
-- Name: deletereference(integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION deletereference(id integer) RETURNS integer
    LANGUAGE plpgsql
    AS $$
    BEGIN
    delete from reference where reference_id = id;
    return id;
    END;
$$;


--
-- Name: deleterole(integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION deleterole(id integer) RETURNS integer
    LANGUAGE plpgsql
    AS $$
    BEGIN
    delete from role where role_id = id;
    return id;
    END;
$$;


--
-- Name: deletevariant(integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION deletevariant(id integer) RETURNS integer
    LANGUAGE plpgsql
    AS $$
    BEGIN
    delete from variant where variant_id = id;
    return id;
    END;
$$;


--
-- Name: getallanalysisparameters(integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION getallanalysisparameters(id integer) RETURNS TABLE(property_name text, property_value text)
    LANGUAGE plpgsql
    AS $$
  BEGIN
    return query
    select (jsonb_each_text(parameters)).* from analysis where analysis_id=id;
    END;
$$;


--
-- Name: getallcontacts(); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION getallcontacts() RETURNS refcursor
    LANGUAGE plpgsql
    AS $$
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
$$;


SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: contact; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE contact (
    contact_id integer NOT NULL,
    lastname text NOT NULL,
    firstname text NOT NULL,
    code text NOT NULL,
    email text NOT NULL,
    roles integer[],
    created_by integer,
    created_date date DEFAULT ('now'::text)::date,
    modified_by integer,
    modified_date date DEFAULT ('now'::text)::date,
    organization_id integer
);


--
-- Name: getallcontactsbyrole(integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION getallcontactsbyrole(roleid integer) RETURNS SETOF contact
    LANGUAGE plpgsql
    AS $$
  BEGIN
    return query
    select c.* from contact c, role r where r.role_id = roleId and r.role_id = any(c.roles);
  END;
$$;


--
-- Name: getallmarkermetadatabydataset(integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION getallmarkermetadatabydataset(datasetid integer) RETURNS TABLE(marker_name text, linkage_group_name character varying, start numeric, stop numeric, mapset_name text, platform_name text, variant_id integer, code text, ref text, alts text, sequence text, reference_name text, primers jsonb, probsets jsonb, strand_name text)
    LANGUAGE plpgsql
    AS $$
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
$$;


--
-- Name: getallmarkersinmarkergroup(integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION getallmarkersinmarkergroup(id integer) RETURNS TABLE(marker_id integer, marker_name text, favorable_allele text)
    LANGUAGE plpgsql
    AS $$
  BEGIN
    return query
    select p1.key::int as marker_id, marker.name as marker_name, p1.value as favorable_allele
    from marker, (select (jsonb_each_text(markers)).* from marker_group where marker_group_id=id) as p1
    where marker.marker_id = p1.key::int;
    END;
$$;


--
-- Name: getallprojectmetadatabydataset(integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION getallprojectmetadatabydataset(datasetid integer) RETURNS TABLE(project_name text, description text, pi text, experiment_name text, platform_name text, dataset_name text, analysis_name text)
    LANGUAGE plpgsql
    AS $$
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
$$;


--
-- Name: getallpropertiesofdnarun(integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION getallpropertiesofdnarun(id integer) RETURNS TABLE(property_id integer, property_name text, property_value text)
    LANGUAGE plpgsql
    AS $$
  BEGIN
    return query
    select p1.key::int as property_id, cv.term as property_name, p1.value as property_value
    from cv, (select (jsonb_each_text(props)).* from dnarun_prop where dnarun_id=id) as p1
    where cv.cv_id = p1.key::int;
    END;
$$;


--
-- Name: getallpropertiesofdnasample(integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION getallpropertiesofdnasample(id integer) RETURNS TABLE(property_id integer, property_name text, property_value text)
    LANGUAGE plpgsql
    AS $$
  BEGIN
    return query
    select p1.key::int as property_id, cv.term as property_name, p1.value as property_value
    from cv, (select (jsonb_each_text(props)).* from dnasample_prop where dnasample_id=id) as p1
    where cv.cv_id = p1.key::int;
    END;
$$;


--
-- Name: getallpropertiesofgermplasm(integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION getallpropertiesofgermplasm(id integer) RETURNS TABLE(property_id integer, property_name text, property_value text)
    LANGUAGE plpgsql
    AS $$
  BEGIN
    return query
    select p1.key::int as property_id, cv.term as property_name, p1.value as property_value
    from cv, (select (jsonb_each_text(props)).* from germplasm_prop where germplasm_id=id) as p1
    where cv.cv_id = p1.key::int;
    END;
$$;


--
-- Name: getallpropertiesofmapset(integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION getallpropertiesofmapset(id integer) RETURNS TABLE(property_id integer, property_name text, property_value text)
    LANGUAGE plpgsql
    AS $$
  BEGIN
    return query
    select p1.key::int as property_id, cv.term as property_name, p1.value as property_value
    from cv, (select (jsonb_each_text(props)).* from map_prop where map_id=id) as p1
    where cv.cv_id = p1.key::int;
    END;
$$;


--
-- Name: getallpropertiesofmarker(integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION getallpropertiesofmarker(id integer) RETURNS TABLE(property_id integer, property_name text, property_value text)
    LANGUAGE plpgsql
    AS $$
  BEGIN
    return query
    select p1.key::int as property_id, cv.term as property_name, p1.value as property_value
    from cv, (select (jsonb_each_text(props)).* from marker_prop where marker_id=id) as p1
    where cv.cv_id = p1.key::int;
    END;
$$;


--
-- Name: getallpropertiesofplatform(integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION getallpropertiesofplatform(id integer) RETURNS TABLE(property_id integer, property_name text, property_value text)
    LANGUAGE plpgsql
    AS $$
  BEGIN
    return query
    select p1.key::int as property_id, cv.term as property_name, p1.value as property_value
    from cv, (select (jsonb_each_text(props)).* from platform_prop where platform_id=id) as p1
    where cv.cv_id = p1.key::int;
    END;
$$;


--
-- Name: getallpropertiesofproject(integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION getallpropertiesofproject(projectid integer) RETURNS TABLE(property_id integer, property_name text, property_value text)
    LANGUAGE plpgsql
    AS $$
  BEGIN
    return query
    select p1.key::int as property_id, cv.term as property_name, p1.value as property_value
    from cv, (select (jsonb_each_text(props)).* from project_prop where project_id=projectId) as p1
    where cv.cv_id = p1.key::int;
    END;
$$;


--
-- Name: getallsamplemetadatabydataset(integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION getallsamplemetadatabydataset(datasetid integer) RETURNS TABLE(dnarun_name text, sample_name text, germplasm_name text, external_code text, germplasm_type text, species text, platename text, num text, well_row text, well_col text)
    LANGUAGE plpgsql
    AS $$
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
$$;


--
-- Name: getcontactnamesbyrole(character varying); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION getcontactnamesbyrole(_role_name character varying) RETURNS refcursor
    LANGUAGE plpgsql
    AS $$
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
$$;


--
-- Name: getcontactsbyrole(character varying); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION getcontactsbyrole(_role_name character varying) RETURNS refcursor
    LANGUAGE plpgsql
    AS $$
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
$$;


--
-- Name: getdnarunnamesbydataset(integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION getdnarunnamesbydataset(datasetid integer) RETURNS TABLE(dnarun_id integer, dnarun_name text)
    LANGUAGE plpgsql
    AS $$
  BEGIN
    return query
    with dd as (select dd.dnarun_id, dd.dnarun_idx from dataset_dnarun dd where dd.dataset_id=datasetId)
    select  dr.dnarun_id, dr.name as dnarun_name 
      from dnarun dr, dd
      where dr.dnarun_id = dd.dnarun_id
      order by dd.dnarun_idx;
  END;
$$;


--
-- Name: getdnarunpropertybyid(integer, integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION getdnarunpropertybyid(id integer, propertyid integer) RETURNS text
    LANGUAGE plpgsql
    AS $$
  DECLARE
    value text;
  BEGIN
    select props->propertyId::text into value from dnarun_prop where dnarun_id=id;
    return value;
  END;
$$;


--
-- Name: getdnarunpropertybyname(integer, text); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION getdnarunpropertybyname(id integer, propertyname text) RETURNS TABLE(property_id integer, property_value text)
    LANGUAGE plpgsql
    AS $$
  BEGIN
    return query
    (with property as (select cv_id from cv where term=propertyName)
    select property.cv_id, (props->property.cv_id::text)::text as value
      from dnarun_prop, property
      where dnarun_id=id);
  END;
$$;


--
-- Name: getdnasamplepropertybyid(integer, integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION getdnasamplepropertybyid(id integer, propertyid integer) RETURNS text
    LANGUAGE plpgsql
    AS $$
  DECLARE
    value text;
  BEGIN
    select props->propertyId::text into value from dnasample_prop where dnasample_id=id;
    return value;
  END;
$$;


--
-- Name: getdnasamplepropertybyname(integer, text); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION getdnasamplepropertybyname(id integer, propertyname text) RETURNS TABLE(property_id integer, property_value text)
    LANGUAGE plpgsql
    AS $$
  BEGIN
    return query
    (with property as (select cv_id from cv where term=propertyName)
    select property.cv_id, (props->property.cv_id::text)::text as value
      from dnasample_prop, property
      where dnasample_id=id);
  END;
$$;


--
-- Name: getexperimentnamesbyprojectid(integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION getexperimentnamesbyprojectid(projectid integer) RETURNS TABLE(id integer, experiment_name text)
    LANGUAGE plpgsql
    AS $$
  BEGIN
    return query
    select experiment_id, name from experiment where project_id = projectId;
  END;
$$;


--
-- Name: experiment; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE experiment (
    experiment_id integer NOT NULL,
    name text NOT NULL,
    code text NOT NULL,
    project_id integer NOT NULL,
    platform_id integer NOT NULL,
    manifest_id integer,
    data_file text,
    created_by integer,
    created_date date DEFAULT ('now'::text)::date,
    modified_by integer,
    modified_date date DEFAULT ('now'::text)::date,
    status integer NOT NULL
);


--
-- Name: getexperimentsbyprojectid(integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION getexperimentsbyprojectid(projectid integer) RETURNS SETOF experiment
    LANGUAGE plpgsql
    AS $$
  BEGIN
    return query
    select * from experiment where project_id = projectId;
  END;
$$;


--
-- Name: getgermplasmpropertybyid(integer, integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION getgermplasmpropertybyid(id integer, propertyid integer) RETURNS text
    LANGUAGE plpgsql
    AS $$
  DECLARE
    value text;
  BEGIN
    select props->propertyId::text into value from germplasm_prop where germplasm_id=id;
    return value;
  END;
$$;


--
-- Name: getgermplasmpropertybyname(integer, text); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION getgermplasmpropertybyname(id integer, propertyname text) RETURNS TABLE(property_id integer, property_value text)
    LANGUAGE plpgsql
    AS $$
  BEGIN
    return query
    (with property as (select cv_id from cv where term=propertyName)
    select property.cv_id, (props->property.cv_id::text)::text as value
      from germplasm_prop, property
      where germplasm_id=id);
  END;
$$;


--
-- Name: manifest; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE manifest (
    manifest_id integer NOT NULL,
    name text NOT NULL,
    code text NOT NULL,
    file_path text,
    created_by integer,
    created_date date DEFAULT ('now'::text)::date,
    modified_by integer,
    modified_date date DEFAULT ('now'::text)::date
);


--
-- Name: getmanifestbyexperimentid(integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION getmanifestbyexperimentid(experimentid integer) RETURNS SETOF manifest
    LANGUAGE plpgsql
    AS $$
  BEGIN
    return query
    select * from manifest where manifest_id in (select manifest_id from experiment where experiment_id = experimentId);
  END;
$$;


--
-- Name: getmapsetpropertybyid(integer, integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION getmapsetpropertybyid(id integer, propertyid integer) RETURNS text
    LANGUAGE plpgsql
    AS $$
  DECLARE
    value text;
  BEGIN
    select props->propertyId::text into value from map_prop where map_id=id;
    return value;
  END;
$$;


--
-- Name: getmapsetpropertybyname(integer, text); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION getmapsetpropertybyname(id integer, propertyname text) RETURNS TABLE(property_id integer, property_value text)
    LANGUAGE plpgsql
    AS $$
  BEGIN
    return query
    (with property as (select cv_id from cv where term=propertyName)
    select property.cv_id, (props->property.cv_id::text)::text as value
      from map_prop, property
      where map_id=id);
  END;
$$;


--
-- Name: getmarkerinmarkergroupbyid(integer, integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION getmarkerinmarkergroupbyid(id integer, markerid integer) RETURNS text
    LANGUAGE plpgsql
    AS $$
  DECLARE
    value text;
  BEGIN
    select markers->markerId::text into value from marker_group where marker_group_id=id;
    return value;
  END;
$$;


--
-- Name: getmarkerinmarkergroupbyname(integer, text); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION getmarkerinmarkergroupbyname(id integer, markername text) RETURNS TABLE(marker_id integer, favorable_allele text)
    LANGUAGE plpgsql
    AS $$
  BEGIN
    return query
    with markerInfo as (select marker_id from marker where name=markerName)
    select markerInfo.marker_id, (props->markerInfo.marker_id::text)::text as favAllele
      from marker_group, markerInfo
      where marker_group_id=id;
  END;
$$;


--
-- Name: getmarkernamesbydataset(integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION getmarkernamesbydataset(datasetid integer) RETURNS TABLE(marker_id integer, marker_name text)
    LANGUAGE plpgsql
    AS $$
  BEGIN
    return query
    with dm as (select dm.marker_id, dm.marker_idx from dataset_marker dm where dm.dataset_id=datasetId)
    select m.marker_id, m.name as marker_name
      from marker m, dm
      where m.marker_id = dm.marker_id 
      order by dm.marker_idx;
  END;
$$;


--
-- Name: getmarkerpropertybyid(integer, integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION getmarkerpropertybyid(id integer, propertyid integer) RETURNS text
    LANGUAGE plpgsql
    AS $$
  DECLARE
    value text;
  BEGIN
    select props->propertyId::text into value from marker_prop where marker_id=id;
    return value;
  END;
$$;


--
-- Name: getmarkerpropertybyname(integer, text); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION getmarkerpropertybyname(id integer, propertyname text) RETURNS TABLE(property_id integer, property_value text)
    LANGUAGE plpgsql
    AS $$
  BEGIN
    return query
    (with property as (select cv_id from cv where term=propertyName)
    select property.cv_id, (props->property.cv_id::text)::text as value
      from marker_prop, property
      where marker_id=id);
  END;
$$;


--
-- Name: getminimalmarkermetadatabydataset(integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION getminimalmarkermetadatabydataset(datasetid integer) RETURNS TABLE(marker_name text, alleles text, chrom character varying, pos numeric, strand text)
    LANGUAGE plpgsql
    AS $$
  BEGIN
    return query
    with dm as (select dm.marker_id, dm.marker_idx from dataset_marker dm where dm.dataset_id=datasetId)
    select m.name as marker_name, m.ref || '/' || array_to_string(m.alts, ',', '?') as alleles, mlp.linkage_group_name as chrom, mlp.stop as pos, cv.term as strand
    from dm inner join marker m on m.marker_id = dm.marker_id 
    left join v_marker_linkage_physical mlp on m.marker_id = mlp.marker_id
    left join cv on m.strand_id = cv.cv_id
    order by dm.marker_idx;
  END;
$$;


--
-- Name: getminimalsamplemetadatabydataset(integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION getminimalsamplemetadatabydataset(datasetid integer) RETURNS TABLE(dnarun_name text, sample_name text, germplasm_name text, external_code text, germplasm_type text, species text, platename text, num text, well_row text, well_col text)
    LANGUAGE plpgsql
    AS $$
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
$$;


--
-- Name: getplatformpropertybyid(integer, integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION getplatformpropertybyid(id integer, propertyid integer) RETURNS text
    LANGUAGE plpgsql
    AS $$
  DECLARE
    value text;
  BEGIN
    select props->propertyId::text into value from platform_prop where platform_id=id;
    return value;
  END;
$$;


--
-- Name: getplatformpropertybyname(integer, text); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION getplatformpropertybyname(id integer, propertyname text) RETURNS TABLE(property_id integer, property_value text)
    LANGUAGE plpgsql
    AS $$
  BEGIN
    return query
    (with property as (select cv_id from cv where term=propertyName)
    select property.cv_id, (props->property.cv_id::text)::text as value
      from platform_prop, property
      where platform_id=id);
  END;
$$;


--
-- Name: getprojectnamesbypi(integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION getprojectnamesbypi(_contact_id integer) RETURNS refcursor
    LANGUAGE plpgsql
    AS $$
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
$$;


--
-- Name: getprojectpropertybyid(integer, integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION getprojectpropertybyid(projectid integer, propertyid integer) RETURNS text
    LANGUAGE plpgsql
    AS $$
  DECLARE
    value text;
  BEGIN
    select props->propertyId::text into value from project_prop where project_id=projectId;
    return value;
  END;
$$;


--
-- Name: getprojectpropertybyname(integer, text); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION getprojectpropertybyname(projectid integer, propertyname text) RETURNS TABLE(property_id integer, property_value text)
    LANGUAGE plpgsql
    AS $$
  BEGIN
    return query
    (with property as (select cv_id from cv where term=propertyName)
    select property.cv_id, (props->property.cv_id::text)::text as value
      from project_prop, property
      where project_id=projectId);
  END;
$$;


--
-- Name: role; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE role (
    role_id integer NOT NULL,
    role_name text NOT NULL,
    role_code text NOT NULL,
    read_tables integer[],
    write_tables integer[]
);


--
-- Name: getrolesofcontact(integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION getrolesofcontact(contactid integer) RETURNS SETOF role
    LANGUAGE plpgsql
    AS $$
  BEGIN
    return query
    select r.* from contact c, role r where c.contact_id = contactId and r.role_id = any(c.roles);
  END;
$$;


--
-- Name: gettotalprojects(); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION gettotalprojects() RETURNS integer
    LANGUAGE plpgsql
    AS $$
  DECLARE
    total integer; 
  BEGIN
    select count(*) into total from projects;
    return total;
  END;
$$;


--
-- Name: removeanalysisfromdataset(integer, integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION removeanalysisfromdataset(datasetid integer, analysisid integer) RETURNS void
    LANGUAGE plpgsql
    AS $$
    BEGIN
    update dataset set analyses=array_remove(analyses, analysisId)
     where dataset_id = datasetId;
    END;
$$;


--
-- Name: removereadtablefromrole(integer, integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION removereadtablefromrole(roleid integer, tableid integer) RETURNS void
    LANGUAGE plpgsql
    AS $$
    BEGIN
    update role set read_tables=array_remove(read_tables, tableId)
     where role_id = roleId;
    END;
$$;


--
-- Name: removerolefromcontact(integer, integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION removerolefromcontact(contactid integer, roleid integer) RETURNS void
    LANGUAGE plpgsql
    AS $$
    BEGIN
    update contact set roles=array_remove(roles, roleId)
     where contact_id = contactId;
    END;
$$;


--
-- Name: removewritetablefromrole(integer, integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION removewritetablefromrole(roleid integer, tableid integer) RETURNS void
    LANGUAGE plpgsql
    AS $$
    BEGIN
    update role set write_tables=array_remove(write_tables, tableId)
     where role_id = roleId;
    END;
$$;


--
-- Name: updateanalysis(integer, text, text, integer, text, text, text, text, text, text, integer, timestamp without time zone, integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION updateanalysis(id integer, analysisname text, analysisdescription text, typeid integer, analysisprogram text, analysisprogramversion text, aanalysisalgorithm text, analysissourcename text, analysissourceversion text, analysissourceuri text, referenceid integer, analysistimeexecuted timestamp without time zone, analysisstatus integer) RETURNS void
    LANGUAGE plpgsql
    AS $$
    BEGIN
    update analysis set name=analysisName, description=analysisDescription, type_id=typeId, program=analysisProgram, programversion=analysisProgramversion, algorithm=aanalysisAlgorithm, sourcename=analysisSourcename, sourceversion=analysisSourceversion, sourceuri=analysisSourceuri, reference_id=referenceId, parameters='{}'::jsonb, timeexecuted=analysisTimeexecuted, status=analysisStatus
     where analysis_id = id;
    END;
$$;


--
-- Name: updateanalysis(integer, text, text, integer, text, text, text, text, text, text, integer, jsonb, timestamp without time zone, integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION updateanalysis(id integer, analysisname text, analysisdescription text, typeid integer, analysisprogram text, analysisprogramversion text, aanalysisalgorithm text, analysissourcename text, analysissourceversion text, analysissourceuri text, referenceid integer, analysisparameters jsonb, analysistimeexecuted timestamp without time zone, analysisstatus integer) RETURNS void
    LANGUAGE plpgsql
    AS $$
    BEGIN
    update analysis set name=analysisName, description=analysisDescription, type_id=typeId, program=analysisProgram, programversion=analysisProgramversion, algorithm=aanalysisAlgorithm, sourcename=analysisSourcename, sourceversion=analysisSourceversion, sourceuri=analysisSourceuri, reference_id=referenceId, parameters=analysisParameters, timeexecuted=analysisTimeexecuted, status=analysisStatus
     where analysis_id = id;
    END;
$$;


--
-- Name: updatecontact(integer, text, text, text, text, integer[], integer, date, integer, date, integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION updatecontact(contactid integer, contactlastname text, contactfirstname text, contactcode text, contactemail text, contactroles integer[], createdby integer, createddate date, modifiedby integer, modifieddate date, organizationid integer) RETURNS void
    LANGUAGE plpgsql
    AS $$
    BEGIN
    update contact set lastname=contactLastName, firstname=contactFirstName, code=contactCode, email=contactEmail, roles=contactRoles, created_by=createdBy, created_date=createdDate, 
      modified_by=modifiedBy, modified_date=modifiedDate, organization_id=organizationId
     where contact_id = contactId;
    END;
$$;


--
-- Name: updatecv(integer, text, text, text, integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION updatecv(id integer, cvgroup text, cvterm text, cvdefinition text, cvrank integer) RETURNS void
    LANGUAGE plpgsql
    AS $$
    BEGIN
    update cv set "group"=cvGroup, term=cvTerm, definition=cvDefinition, rank=cvRank
     where cv_id = id;
    END;
$$;


--
-- Name: updatedataset(integer, text, integer, integer, integer[], text, text, text, text, integer, date, integer, date, integer, integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION updatedataset(id integer, datasetname text, experimentid integer, callinganalysisid integer, datasetanalyses integer[], datatable text, datafile text, qualitytable text, qualityfile text, createdby integer, createddate date, modifiedby integer, modifieddate date, datasetstatus integer, typeid integer) RETURNS void
    LANGUAGE plpgsql
    AS $$
    BEGIN
    update dataset set experiment_id=experimentId, callinganalysis_id=callinganalysisId, analyses=datasetAnalyses, data_table=dataTable, data_file=dataFile, quality_table=qualityTable, quality_file=qualityFile, scores='{}'::jsonb, created_by=createdBy, created_date=createdDate, modified_by=modifiedBy, modified_date=modifiedDate, status=datasetStatus, type_id=typeId, name=datasetName
     where dataset_id = id;
    END;
$$;


--
-- Name: updatedatasetdnarun(integer, integer, integer, integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION updatedatasetdnarun(id integer, datasetid integer, dnarunid integer, dnarunidx integer) RETURNS void
    LANGUAGE plpgsql
    AS $$
    BEGIN
    update dataset_dnarun set dataset_id=datasetId, dnarun_id=dnarunId, dnarun_idx=dnarunIdx
     where dataset_dnarun_id = id;
    END;
$$;


--
-- Name: updatedatasetmarker(integer, integer, integer, real, real, real, integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION updatedatasetmarker(id integer, datasetid integer, markerid integer, callrate real, datasetmarkermaf real, datasetmarkerreproducibility real, datasetmarkeridx integer) RETURNS void
    LANGUAGE plpgsql
    AS $$
    BEGIN
    update dataset_marker set dataset_id=datasetId, marker_id=markerId, call_rate=callRate, maf=datasetMarkerMaf, reproducibility=datasetMarkerReproducibility, scores='{}'::jsonb, marker_idx=datasetMarkerIdx
     where dataset_marker_id = id;
    END;
$$;


--
-- Name: updatedatasetmarker(integer, integer, integer, real, real, real, jsonb); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION updatedatasetmarker(id integer, datasetid integer, markerid integer, callrate real, datasetmarkermaf real, datasetmarkerreproducibility real, datasetmarkerscores jsonb) RETURNS void
    LANGUAGE plpgsql
    AS $$
    BEGIN
    update dataset_marker set dataset_id=datasetId, marker_id=markerId, call_rate=callRate, maf=datasetMarkerMaf, reproducibility=datasetMarkerReproducibility, scores=datasetMarkerScores
     where dataset_marker_id = id;
    END;
$$;


--
-- Name: updatedisplay(integer, text, text, text, integer, date, integer, date, integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION updatedisplay(id integer, tablename text, columnname text, displayname text, createdby integer, createddate date, modifiedby integer, modifieddate date, displayrank integer) RETURNS void
    LANGUAGE plpgsql
    AS $$
    BEGIN
    update display set table_name=tableName, column_name=columnName, display_name=displayName, created_by=createdBy, created_date=createdDate, 
      modified_by=modifiedBy, modified_date=modifiedDate, rank=displayRank
     where display_id = id;
    END;
$$;


--
-- Name: updatednarun(integer, integer, integer, text, text); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION updatednarun(id integer, experimentid integer, dnasampleid integer, dnarunname text, dnaruncode text) RETURNS void
    LANGUAGE plpgsql
    AS $$
    BEGIN
    update dnarun set experiment_id=experimentId, dnasample_id=dnasampleId, name=dnarunName, code=dnarunCode
     where dnarun_id = id;
    END;
$$;


--
-- Name: updatednarunpropertybyid(integer, integer, text); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION updatednarunpropertybyid(id integer, propertyid integer, propertyvalue text) RETURNS void
    LANGUAGE plpgsql
    AS $$
  BEGIN
    update dnarun_prop set props = jsonb_set(props, ('{'||propertyId::text||'}')::text[], ('"'||propertyValue||'"')::jsonb)
      where dnarun_id=id;
  END;
$$;


--
-- Name: updatednarunpropertybyname(integer, text, text); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION updatednarunpropertybyname(id integer, propertyname text, propertyvalue text) RETURNS void
    LANGUAGE plpgsql
    AS $$
  BEGIN
    with property as (select cv_id from cv where term=propertyName)
    update dnarun_prop 
      set props = jsonb_set(props, ('{'||property.cv_id::text||'}')::text[], ('"'||propertyValue||'"')::jsonb)
      from property
      where dnarun_id=id;
  END;
$$;


--
-- Name: updatednasample(integer, text, text, text, text, text, text, integer, integer, integer, date, integer, date, integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION updatednasample(id integer, dnasamplename text, dnasamplecode text, dnasampleplatename text, dnasamplenum text, wellrow text, wellcol text, projectid integer, germplasmid integer, createdby integer, createddate date, modifiedby integer, modifieddate date, dnasamplestatus integer) RETURNS void
    LANGUAGE plpgsql
    AS $$
    BEGIN
    update dnasample set name=dnaSampleName, code=dnaSampleCode, platename=dnaSamplePlateName, num=dnaSampleNum, well_row=wellRow, well_col=wellCol, project_id=projectId, germplasm_id=germplasmId, created_by=createdBy, created_date=createdDate, modified_by=modifiedBy, modified_date=modifiedDate, status=dnaSampleStatus
     where dnasample_id = id;
    END;
$$;


--
-- Name: updatednasamplepropertybyid(integer, integer, text); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION updatednasamplepropertybyid(id integer, propertyid integer, propertyvalue text) RETURNS void
    LANGUAGE plpgsql
    AS $$
  BEGIN
    update dnasample_prop set props = jsonb_set(props, ('{'||propertyId::text||'}')::text[], ('"'||propertyValue||'"')::jsonb)
      where dnasample_id=id;
  END;
$$;


--
-- Name: updatednasamplepropertybyname(integer, text, text); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION updatednasamplepropertybyname(id integer, propertyname text, propertyvalue text) RETURNS void
    LANGUAGE plpgsql
    AS $$
  BEGIN
    with property as (select cv_id from cv where term=propertyName)
    update dnasample_prop 
      set props = jsonb_set(props, ('{'||property.cv_id::text||'}')::text[], ('"'||propertyValue||'"')::jsonb)
      from property
      where dnasample_id=id;
  END;
$$;


--
-- Name: updateexperiment(integer, text, text, integer, integer, integer, text, integer, date, integer, date, integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION updateexperiment(eid integer, expname text, expcode text, projectid integer, platformid integer, manifestid integer, datafile text, createdby integer, createddate date, modifiedby integer, modifieddate date, expstatus integer) RETURNS void
    LANGUAGE plpgsql
    AS $$
    BEGIN
    update experiment set name=expName, code=expCode, project_id=projectId, platform_id=platformId, manifest_id=manifestId, data_file=dataFile, 
      created_by=createdBy, created_date=createdDate, modified_by=modifiedBy, modified_date=modifiedDate, status=expStatus where experiment_id = eId;
    END;
$$;


--
-- Name: updategermplasm(integer, text, text, integer, integer, integer, date, integer, date, integer, text); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION updategermplasm(id integer, germplasmname text, externalcode text, speciesid integer, typeid integer, createdby integer, createddate date, modifiedby integer, modifieddate date, germplasmstatus integer, germplasmcode text) RETURNS void
    LANGUAGE plpgsql
    AS $$
    BEGIN
    update germplasm set name=germplasmName, external_code=externalCode, species_id=speciesId, type_id=typeId, created_by=createdBy, created_date=createdDate, modified_by=modifiedBy, modified_date=modifiedDate, status=germplasmStatus, code=germplasmCode
     where germplasm_id = id;
    END;
$$;


--
-- Name: updategermplasmpropertybyid(integer, integer, text); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION updategermplasmpropertybyid(id integer, propertyid integer, propertyvalue text) RETURNS void
    LANGUAGE plpgsql
    AS $$
  BEGIN
    update germplasm_prop set props = jsonb_set(props, ('{'||propertyId::text||'}')::text[], ('"'||propertyValue||'"')::jsonb)
      where germplasm_id=id;
  END;
$$;


--
-- Name: updategermplasmpropertybyname(integer, text, text); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION updategermplasmpropertybyname(id integer, propertyname text, propertyvalue text) RETURNS void
    LANGUAGE plpgsql
    AS $$
  BEGIN
    with property as (select cv_id from cv where term=propertyName)
    update germplasm_prop 
      set props = jsonb_set(props, ('{'||property.cv_id::text||'}')::text[], ('"'||propertyValue||'"')::jsonb)
      from property
      where germplasm_id=id;
  END;
$$;


--
-- Name: updatelinkagegroup(integer, text, integer, integer, integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION updatelinkagegroup(id integer, linkagegroupname text, linkagegroupstart integer, linkagegroupstop integer, mapid integer) RETURNS void
    LANGUAGE plpgsql
    AS $$
    BEGIN
    update linkage_group set name=linkageGroupName, start=linkageGroupStart, stop=linkageGroupStop, map_id=mapId
     where linkage_group_id = id;
    END;
$$;


--
-- Name: updatemanifest(integer, text, text, text, integer, date, integer, date); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION updatemanifest(manifestid integer, manifestname text, manifestcode text, filepath text, createdby integer, createddate date, modifiedby integer, modifieddate date) RETURNS void
    LANGUAGE plpgsql
    AS $$
    BEGIN
    update manifest set name=manifestName, code=manifestCode, file_path=filePath, created_by=createdBy, 
      created_date=createdDate, modified_by=modifiedBy, modified_date=modifiedDate
     where manifest_id = manifestId;
    END;
$$;


--
-- Name: updatemapset(integer, text, text, text, integer, integer, integer, date, integer, date, integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION updatemapset(id integer, mapsetname text, mapsetcode text, mapsetdescription text, referenceid integer, typeid integer, createdby integer, createddate date, modifiedby integer, modifieddate date, mapsetstatus integer) RETURNS void
    LANGUAGE plpgsql
    AS $$
    BEGIN
    update mapset set name=mapsetName, code=mapsetCode, description=mapsetDescription, reference_id=referenceId, type_id=typeId, created_by=createdBy, created_date=createdDate, modified_by=modifiedBy, modified_date=modifiedDate, status=mapsetStatus
     where mapset_id = id;
    END;
$$;


--
-- Name: updatemapsetpropertybyid(integer, integer, text); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION updatemapsetpropertybyid(id integer, propertyid integer, propertyvalue text) RETURNS void
    LANGUAGE plpgsql
    AS $$
  BEGIN
    update map_prop set props = jsonb_set(props, ('{'||propertyId::text||'}')::text[], ('"'||propertyValue||'"')::jsonb)
      where map_id=id;
  END;
$$;


--
-- Name: updatemapsetpropertybyname(integer, text, text); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION updatemapsetpropertybyname(id integer, propertyname text, propertyvalue text) RETURNS void
    LANGUAGE plpgsql
    AS $$
  BEGIN
    with property as (select cv_id from cv where term=propertyName)
    update map_prop 
      set props = jsonb_set(props, ('{'||property.cv_id::text||'}')::text[], ('"'||propertyValue||'"')::jsonb)
      from property
      where map_id=id;
  END;
$$;


--
-- Name: updatemarker(integer, integer, integer, text, text, text, text[], text, integer, integer, integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION updatemarker(id integer, platformid integer, variantid integer, markername text, markercode text, markerref text, markeralts text[], markersequence text, referenceid integer, strandid integer, markerstatus integer) RETURNS void
    LANGUAGE plpgsql
    AS $$
    BEGIN
    update marker set  platform_id=platformId, variant_id=variantId, name=markerName, code=markerCode, ref=markerRef, alts=markerAlts, sequence=markerSequence, reference_id=referenceId, primers='{}'::jsonb, probsets='{}'::jsonb, strand_id=strandId, status=markerStatus
     where marker_id = id;
    END;
$$;


--
-- Name: updatemarker(integer, integer, integer, text, text, text, text[], text, integer, jsonb, text[], integer, integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION updatemarker(id integer, platformid integer, variantid integer, markername text, markercode text, markerref text, markeralts text[], markersequence text, referenceid integer, markerprimers jsonb, markerprobsets text[], strandid integer, markerstatus integer) RETURNS void
    LANGUAGE plpgsql
    AS $$
    BEGIN
    update marker set  platform_id=platformId, variant_id=variantId, name=markerName, code=markerCode, ref=markerRef, alts=markerAlts, sequence=markerSequence, reference_id=referenceId, primers=markerPrimers, probsets=markerProbsets, strand_id=strandId, status=markerStatus
     where marker_id = id;
    END;
$$;


--
-- Name: updatemarkergroup(integer, text, text, text, integer, date, integer, date, integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION updatemarkergroup(id integer, markergroupname text, markergroupcode text, germplasmgroup text, createdby integer, createddate date, modifiedby integer, modifieddate date, markergroupstatus integer) RETURNS void
    LANGUAGE plpgsql
    AS $$
    BEGIN
    update marker_group set name=markerGroupName, code=markerGroupCode, markers='{}'::jsonb, germplasm_group=germplasmGroup, created_by=createdBy, created_date=createdDate, modified_by=modifiedBy, modified_date=modifiedDate, status=markerGroupStatus
     where marker_group_id = id;
    END;
$$;


--
-- Name: updatemarkergroup(integer, text, text, jsonb, text, integer, date, text, date, integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION updatemarkergroup(id integer, markergroupname text, markergroupcode text, markergroupmarkers jsonb, germplasmgroup text, createdby integer, createdate date, modifiedby text, modifieddate date, markergroupstatus integer) RETURNS void
    LANGUAGE plpgsql
    AS $$
    BEGIN
    update marker_group set name=markerGroupName, code=markerGroupCode, markers=markerGroupMarkers, germplasm_group=germplasmGroup, created_by=createdBy, create_date=createdDate, modified_by=modifiedBy, modified_date=modifiedDate, status=markerGroupStatus
     where marker_group_id = id;
    END;
$$;


--
-- Name: updatemarkerlinkagegroup(integer, integer, integer, integer, integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION updatemarkerlinkagegroup(id integer, markerid integer, markerlinkagegroupstart integer, markerlinkagegroupstop integer, linkagegroupid integer) RETURNS void
    LANGUAGE plpgsql
    AS $$
    BEGIN
    update marker_linkage_group set marker_id=markerId, start=markerLinkageGroupStart, stop=markerLinkageGroupStop, linkage_group_id=linkageGroupId
     where marker_linkage_group_id = id;
    END;
$$;


--
-- Name: updatemarkerlinkagegroup(integer, integer, numeric, numeric, integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION updatemarkerlinkagegroup(id integer, markerid integer, markerlinkagegroupstart numeric, markerlinkagegroupstop numeric, linkagegroupid integer) RETURNS void
    LANGUAGE plpgsql
    AS $$
    BEGIN
    update marker_linkage_group set marker_id=markerId, start=markerLinkageGroupStart, stop=markerLinkageGroupStop, linkage_group_id=linkageGroupId
     where marker_linkage_group_id = id;
    END;
$$;


--
-- Name: updatemarkerpropertybyid(integer, integer, text); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION updatemarkerpropertybyid(id integer, propertyid integer, propertyvalue text) RETURNS void
    LANGUAGE plpgsql
    AS $$
  BEGIN
    update marker_prop set props = jsonb_set(props, ('{'||propertyId::text||'}')::text[], ('"'||propertyValue||'"')::jsonb)
      where marker_id=id;
  END;
$$;


--
-- Name: updatemarkerpropertybyname(integer, text, text); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION updatemarkerpropertybyname(id integer, propertyname text, propertyvalue text) RETURNS void
    LANGUAGE plpgsql
    AS $$
  BEGIN
    with property as (select cv_id from cv where term=propertyName)
    update marker_prop 
      set props = jsonb_set(props, ('{'||property.cv_id::text||'}')::text[], ('"'||propertyValue||'"')::jsonb)
      from property
      where marker_id=id;
  END;
$$;


--
-- Name: updateorganization(integer, text, text, text, integer, date, integer, date, integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION updateorganization(orgid integer, orgname text, orgaddress text, orgwebsite text, createdby integer, createddate date, modifiedby integer, modifieddate date, orgstatus integer) RETURNS void
    LANGUAGE plpgsql
    AS $$
    BEGIN
    update organization set name=orgName, address=orgAddress, website=orgWebsite, created_by=createdBy, created_date=createdDate, modified_by=modifiedBy, modified_date=modifiedDate, status=orgStatus
     where organization_id = orgId;
    END;
$$;


--
-- Name: updateplatform(integer, text, text, integer, text, integer, date, integer, date, integer, integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION updateplatform(id integer, platformname text, platformcode text, vendorid integer, platformdescription text, createdby integer, createddate date, modifiedby integer, modifieddate date, platformstatus integer, typeid integer) RETURNS void
    LANGUAGE plpgsql
    AS $$
    BEGIN
    update platform set name=platformName, code=platformCode, vendor_id=vendorId, description=platformDescription, created_by=createdBy, created_date=createdDate, modified_by=modifiedBy, modified_date=modifiedDate, status=platformStatus, type_id=typeId
     where platform_id = id;
    END;
$$;


--
-- Name: updateplatformpropertybyid(integer, integer, text); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION updateplatformpropertybyid(id integer, propertyid integer, propertyvalue text) RETURNS void
    LANGUAGE plpgsql
    AS $$
  BEGIN
    update platform_prop set props = jsonb_set(props, ('{'||propertyId::text||'}')::text[], ('"'||propertyValue||'"')::jsonb)
      where platform_id=id;
  END;
$$;


--
-- Name: updateplatformpropertybyname(integer, text, text); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION updateplatformpropertybyname(id integer, propertyname text, propertyvalue text) RETURNS void
    LANGUAGE plpgsql
    AS $$
  BEGIN
    with property as (select cv_id from cv where term=propertyName)
    update platform_prop 
      set props = jsonb_set(props, ('{'||property.cv_id::text||'}')::text[], ('"'||propertyValue||'"')::jsonb)
      from property
      where platform_id=id;
  END;
$$;


--
-- Name: updateproject(integer, text, text, text, integer, integer, date, integer, date, integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION updateproject(pid integer, projectname text, projectcode text, projectdescription text, picontact integer, createdby integer, createddate date, modifiedby integer, modifieddate date, projectstatus integer) RETURNS void
    LANGUAGE plpgsql
    AS $$
    BEGIN
    update project set name = projectName, code = projectCode, description = projectDescription, pi_contact = piContact, created_by = createdBy, created_date = createdDate, 
      modified_by = modifiedBy, modified_date = modifiedDate, status = projectStatus where project_id = pId;
    END;
$$;


--
-- Name: updateprojectpropertybyid(integer, integer, text); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION updateprojectpropertybyid(projectid integer, propertyid integer, propertyvalue text) RETURNS void
    LANGUAGE plpgsql
    AS $$
  BEGIN
    update project_prop set props = jsonb_set(props, ('{'||propertyId::text||'}')::text[], ('"'||propertyValue||'"')::jsonb)
      where project_id=projectId;
  END;
$$;


--
-- Name: updateprojectpropertybyname(integer, text, text); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION updateprojectpropertybyname(projectid integer, propertyname text, propertyvalue text) RETURNS void
    LANGUAGE plpgsql
    AS $$
  BEGIN
    with property as (select cv_id from cv where term=propertyName)
    update project_prop 
      set props = jsonb_set(props, ('{'||property.cv_id::text||'}')::text[], ('"'||propertyValue||'"')::jsonb)
      from property
      where project_id=projectId;
  END;
$$;


--
-- Name: updatereference(integer, text, text, text, text); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION updatereference(id integer, referencename text, referenceversion text, referencelink text, filepath text) RETURNS void
    LANGUAGE plpgsql
    AS $$
    BEGIN
    update reference set name=referenceName, version=referenceVersion, link=referenceLink, file_path=filePath
     where reference_id = id;
    END;
$$;


--
-- Name: updaterole(integer, text, text, integer[], integer[]); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION updaterole(roleid integer, rolename text, rolecode text, readtables integer[], writetables integer[]) RETURNS void
    LANGUAGE plpgsql
    AS $$
    BEGIN
    update role set role_name=roleName, role_code=roleCode, read_tables=readTables, write_tables=writeTables
     where role_id = roleId;
    END;
$$;


--
-- Name: updatevariant(integer, text, integer, date, integer, date); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION updatevariant(id integer, variantcode text, createdby integer, createddate date, modifiedby integer, modifieddate date) RETURNS void
    LANGUAGE plpgsql
    AS $$
    BEGIN
    update variant set code=variantCode, created_by=createdBy, created_date=createdDate, modified_by=modifiedBy, modified_date=modifiedDate
     where variant_id = id;
    END;
$$;


--
-- Name: upsertanalysisparameter(integer, text, text); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION upsertanalysisparameter(id integer, parametername text, parametervalue text) RETURNS void
    LANGUAGE plpgsql
    AS $$
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
$$;


--
-- Name: upsertdnarunpropertybyid(integer, integer, text); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION upsertdnarunpropertybyid(id integer, propertyid integer, propertyvalue text) RETURNS void
    LANGUAGE plpgsql
    AS $$
  BEGIN
    update dnarun_prop set props = props || ('{"'||propertyId::text||'": "'||propertyValue||'"}')::jsonb
      where dnarun_id=id;
  END;
$$;


--
-- Name: upsertdnarunpropertybyname(integer, text, text); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION upsertdnarunpropertybyname(id integer, propertyname text, propertyvalue text) RETURNS integer
    LANGUAGE plpgsql
    AS $$
  DECLARE
    propertyId integer;
  BEGIN
    select cv_id into propertyId from cv where term=propertyName;
    update dnarun_prop set props = props || ('{"'||propertyId::text||'": "'||propertyValue||'"}')::jsonb
      where dnarun_id=id;
    return propertyId;
  END;
$$;


--
-- Name: upsertdnasamplepropertybyid(integer, integer, text); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION upsertdnasamplepropertybyid(id integer, propertyid integer, propertyvalue text) RETURNS void
    LANGUAGE plpgsql
    AS $$
  BEGIN
    update dnasample_prop set props = props || ('{"'||propertyId::text||'": "'||propertyValue||'"}')::jsonb
      where dnasample_id=id;
  END;
$$;


--
-- Name: upsertdnasamplepropertybyname(integer, text, text); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION upsertdnasamplepropertybyname(id integer, propertyname text, propertyvalue text) RETURNS integer
    LANGUAGE plpgsql
    AS $$
  DECLARE
    propertyId integer;
  BEGIN
    select cv_id into propertyId from cv where term=propertyName;
    update dnasample_prop set props = props || ('{"'||propertyId::text||'": "'||propertyValue||'"}')::jsonb
      where dnasample_id=id;
    return propertyId;
  END;
$$;


--
-- Name: upsertgermplasmpropertybyid(integer, integer, text); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION upsertgermplasmpropertybyid(id integer, propertyid integer, propertyvalue text) RETURNS void
    LANGUAGE plpgsql
    AS $$
  BEGIN
    update germplasm_prop set props = props || ('{"'||propertyId::text||'": "'||propertyValue||'"}')::jsonb
      where germplasm_id=id;
  END;
$$;


--
-- Name: upsertgermplasmpropertybyname(integer, text, text); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION upsertgermplasmpropertybyname(id integer, propertyname text, propertyvalue text) RETURNS integer
    LANGUAGE plpgsql
    AS $$
  DECLARE
    propertyId integer;
  BEGIN
    select cv_id into propertyId from cv where term=propertyName;
    update germplasm_prop set props = props || ('{"'||propertyId::text||'": "'||propertyValue||'"}')::jsonb
      where germplasm_id=id;
    return propertyId;
  END;
$$;


--
-- Name: upsertmapsetpropertybyid(integer, integer, text); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION upsertmapsetpropertybyid(id integer, propertyid integer, propertyvalue text) RETURNS void
    LANGUAGE plpgsql
    AS $$
  BEGIN
    update map_prop set props = props || ('{"'||propertyId::text||'": "'||propertyValue||'"}')::jsonb
      where map_id=id;
  END;
$$;


--
-- Name: upsertmapsetpropertybyname(integer, text, text); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION upsertmapsetpropertybyname(id integer, propertyname text, propertyvalue text) RETURNS integer
    LANGUAGE plpgsql
    AS $$
  DECLARE
    propertyId integer;
  BEGIN
    select cv_id into propertyId from cv where term=propertyName;
    update map_prop set props = props || ('{"'||propertyId::text||'": "'||propertyValue||'"}')::jsonb
      where map_id=id;
    return propertyId;
  END;
$$;


--
-- Name: upsertmarkerpropertybyid(integer, integer, text); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION upsertmarkerpropertybyid(id integer, propertyid integer, propertyvalue text) RETURNS void
    LANGUAGE plpgsql
    AS $$
  BEGIN
    update marker_prop set props = props || ('{"'||propertyId::text||'": "'||propertyValue||'"}')::jsonb
      where marker_id=id;
  END;
$$;


--
-- Name: upsertmarkerpropertybyname(integer, text, text); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION upsertmarkerpropertybyname(id integer, propertyname text, propertyvalue text) RETURNS integer
    LANGUAGE plpgsql
    AS $$
  DECLARE
    propertyId integer;
  BEGIN
    select cv_id into propertyId from cv where term=propertyName;
    update marker_prop set props = props || ('{"'||propertyId::text||'": "'||propertyValue||'"}')::jsonb
      where marker_id=id;
    return propertyId;
  END;
$$;


--
-- Name: upsertmarkertomarkergroupbyid(integer, integer, text); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION upsertmarkertomarkergroupbyid(id integer, markerid integer, favallele text) RETURNS void
    LANGUAGE plpgsql
    AS $$
  BEGIN
    update marker_group set markers = markers || ('{"'||markerId::text||'": "'||favAllele||'"}')::jsonb
      where marker_group_id=id;
  END;
$$;


--
-- Name: upsertmarkertomarkergroupbyname(integer, text, text); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION upsertmarkertomarkergroupbyname(id integer, markername text, favallele text) RETURNS integer
    LANGUAGE plpgsql
    AS $$
  DECLARE
    markerId integer;
  BEGIN
    select marker_id into markerId from marker where name=markerName;
    update marker_group set markers = markers || ('{"'||markerId::text||'": "'||favAllele||'"}')::jsonb
      where marker_group_id=id;
    return propertyId;
  END;
$$;


--
-- Name: upsertplatformpropertybyid(integer, integer, text); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION upsertplatformpropertybyid(id integer, propertyid integer, propertyvalue text) RETURNS void
    LANGUAGE plpgsql
    AS $$
  BEGIN
    update platform_prop set props = props || ('{"'||propertyId::text||'": "'||propertyValue||'"}')::jsonb
      where platform_id=id;
  END;
$$;


--
-- Name: upsertplatformpropertybyname(integer, text, text); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION upsertplatformpropertybyname(id integer, propertyname text, propertyvalue text) RETURNS integer
    LANGUAGE plpgsql
    AS $$
  DECLARE
    propertyId integer;
  BEGIN
    select cv_id into propertyId from cv where term=propertyName;
    update platform_prop set props = props || ('{"'||propertyId::text||'": "'||propertyValue||'"}')::jsonb
      where platform_id=id;
    return propertyId;
  END;
$$;


--
-- Name: upsertprojectpropertybyid(integer, integer, text); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION upsertprojectpropertybyid(id integer, propertyid integer, propertyvalue text) RETURNS void
    LANGUAGE plpgsql
    AS $$
  BEGIN
    update project_prop set props = props || ('{"'||propertyId::text||'": "'||propertyValue||'"}')::jsonb
      where project_id=id;
  END;
$$;


--
-- Name: upsertprojectpropertybyname(integer, text, text); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION upsertprojectpropertybyname(id integer, propertyname text, propertyvalue text) RETURNS integer
    LANGUAGE plpgsql
    AS $$
  DECLARE
    propertyId integer;
  BEGIN
    select cv_id into propertyId from cv where term=propertyName;
    update project_prop set props = props || ('{"'||propertyId::text||'": "'||propertyValue||'"}')::jsonb
      where project_id=id;
    return propertyId;
  END;
$$;


--
-- Name: idatafilesrvr; Type: SERVER; Schema: -; Owner: -
--

CREATE SERVER idatafilesrvr FOREIGN DATA WRAPPER file_fdw;


--
-- Name: analysis; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE analysis (
    analysis_id integer NOT NULL,
    name text,
    description text,
    type_id integer NOT NULL,
    program text,
    programversion text,
    algorithm text,
    sourcename text,
    sourceversion text,
    sourceuri text,
    reference_id integer,
    parameters jsonb,
    timeexecuted timestamp without time zone,
    status integer NOT NULL
);


--
-- Name: analysis_analysis_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE analysis_analysis_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: analysis_analysis_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE analysis_analysis_id_seq OWNED BY analysis.analysis_id;


--
-- Name: contact_contact_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE contact_contact_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: contact_contact_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE contact_contact_id_seq OWNED BY contact.contact_id;


--
-- Name: cv; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE cv (
    cv_id integer NOT NULL,
    "group" text NOT NULL,
    term text NOT NULL,
    definition text NOT NULL,
    rank integer DEFAULT 0 NOT NULL
);


--
-- Name: COLUMN cv."group"; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN cv."group" IS 'Groups terms together, ex. map_type, analysis_type';


--
-- Name: cv_cv_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE cv_cv_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: cv_cv_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE cv_cv_id_seq OWNED BY cv.cv_id;


--
-- Name: dataset; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE dataset (
    dataset_id integer NOT NULL,
    experiment_id integer NOT NULL,
    callinganalysis_id integer NOT NULL,
    analyses integer[],
    data_table text,
    data_file text,
    quality_table text,
    quality_file text,
    scores jsonb,
    created_by integer,
    created_date date DEFAULT ('now'::text)::date,
    modified_by integer,
    modified_date date DEFAULT ('now'::text)::date,
    status integer,
    type_id integer,
    name text
);


--
-- Name: dataset_dataset_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE dataset_dataset_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: dataset_dataset_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE dataset_dataset_id_seq OWNED BY dataset.dataset_id;


--
-- Name: dataset_dnarun; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE dataset_dnarun (
    dataset_dnarun_id integer NOT NULL,
    dataset_id integer NOT NULL,
    dnarun_id integer NOT NULL,
    dnarun_idx integer
);


--
-- Name: dataset_dnarun_dataset_dnarun_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE dataset_dnarun_dataset_dnarun_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: dataset_dnarun_dataset_dnarun_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE dataset_dnarun_dataset_dnarun_id_seq OWNED BY dataset_dnarun.dataset_dnarun_id;


--
-- Name: dataset_marker; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE dataset_marker (
    dataset_marker_id integer NOT NULL,
    dataset_id integer NOT NULL,
    marker_id integer NOT NULL,
    call_rate real,
    maf real,
    reproducibility real,
    scores jsonb,
    marker_idx integer
);


--
-- Name: dataset_marker_dataset_marker_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE dataset_marker_dataset_marker_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: dataset_marker_dataset_marker_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE dataset_marker_dataset_marker_id_seq OWNED BY dataset_marker.dataset_marker_id;


--
-- Name: display; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE display (
    display_id integer NOT NULL,
    table_name text NOT NULL,
    column_name text,
    display_name text,
    created_by integer,
    created_date date,
    modified_by integer,
    modified_date date,
    rank integer
);


--
-- Name: display_display_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE display_display_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: display_display_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE display_display_id_seq OWNED BY display.display_id;


--
-- Name: dnarun; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE dnarun (
    dnarun_id integer NOT NULL,
    experiment_id integer NOT NULL,
    dnasample_id integer NOT NULL,
    name text,
    code text
);


--
-- Name: dnarun_dnarun_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE dnarun_dnarun_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: dnarun_dnarun_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE dnarun_dnarun_id_seq OWNED BY dnarun.dnarun_id;


--
-- Name: dnarun_prop; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE dnarun_prop (
    dnarun_prop_id integer NOT NULL,
    dnarun_id integer NOT NULL,
    props jsonb NOT NULL
);


--
-- Name: dnarun_prop_dnarun_prop_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE dnarun_prop_dnarun_prop_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: dnarun_prop_dnarun_prop_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE dnarun_prop_dnarun_prop_id_seq OWNED BY dnarun_prop.dnarun_prop_id;


--
-- Name: dnasample; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE dnasample (
    dnasample_id integer NOT NULL,
    name text NOT NULL,
    code text,
    platename text,
    num text,
    well_row text,
    well_col text,
    project_id integer NOT NULL,
    germplasm_id integer NOT NULL,
    created_by integer,
    created_date date DEFAULT ('now'::text)::date,
    modified_by integer,
    modified_date date DEFAULT ('now'::text)::date,
    status integer NOT NULL
);


--
-- Name: dnasample_dnasample_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE dnasample_dnasample_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: dnasample_dnasample_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE dnasample_dnasample_id_seq OWNED BY dnasample.dnasample_id;


--
-- Name: dnasample_prop; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE dnasample_prop (
    dnasample_prop_id integer NOT NULL,
    dnasample_id integer NOT NULL,
    props jsonb
);


--
-- Name: dnasample_prop_dnasample_prop_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE dnasample_prop_dnasample_prop_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: dnasample_prop_dnasample_prop_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE dnasample_prop_dnasample_prop_id_seq OWNED BY dnasample_prop.dnasample_prop_id;


--
-- Name: experiment_experiment_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE experiment_experiment_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: experiment_experiment_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE experiment_experiment_id_seq OWNED BY experiment.experiment_id;


--
-- Name: germplasm; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE germplasm (
    germplasm_id integer NOT NULL,
    name text,
    external_code text,
    species_id integer,
    type_id integer,
    created_by integer,
    created_date date DEFAULT ('now'::text)::date,
    modified_by integer,
    modified_date date DEFAULT ('now'::text)::date,
    status integer NOT NULL,
    code text DEFAULT 0
);


--
-- Name: germplasm_germplasm_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE germplasm_germplasm_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: germplasm_germplasm_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE germplasm_germplasm_id_seq OWNED BY germplasm.germplasm_id;


--
-- Name: germplasm_prop; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE germplasm_prop (
    germplasm_prop_id integer NOT NULL,
    germplasm_id integer NOT NULL,
    props jsonb
);


--
-- Name: germplasm_prop_germplasm_prop_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE germplasm_prop_germplasm_prop_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: germplasm_prop_germplasm_prop_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE germplasm_prop_germplasm_prop_id_seq OWNED BY germplasm_prop.germplasm_prop_id;


--
-- Name: linkage_group; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE linkage_group (
    linkage_group_id integer NOT NULL,
    name character varying NOT NULL,
    start integer DEFAULT 0 NOT NULL,
    stop integer DEFAULT 0 NOT NULL,
    map_id integer NOT NULL
);


--
-- Name: TABLE linkage_group; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE linkage_group IS 'This table will contain different linkage groups, ie. Chromosome 1, Chromosome 2, etc. along with their respective start and stop boundaries.';


--
-- Name: COLUMN linkage_group.name; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN linkage_group.name IS 'ex. Chromosome 1, Chromosome 2, ..., ChromosomeN, LG01, LG02, etc.';


--
-- Name: COLUMN linkage_group.start; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN linkage_group.start IS 'Start of the linkage group. 0-based, interbased coordinates.';


--
-- Name: COLUMN linkage_group.stop; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN linkage_group.stop IS 'The maximum position in the linkage group, ex. 200, 200000000
';


--
-- Name: COLUMN linkage_group.map_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN linkage_group.map_id IS 'Foreign key to the Map table. This defines which map the linkage group belongs to.';


--
-- Name: linkage_group_linkage_group_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE linkage_group_linkage_group_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: linkage_group_linkage_group_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE linkage_group_linkage_group_id_seq OWNED BY linkage_group.linkage_group_id;


--
-- Name: manifest_manifest_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE manifest_manifest_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: manifest_manifest_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE manifest_manifest_id_seq OWNED BY manifest.manifest_id;


--
-- Name: mapset; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE mapset (
    mapset_id integer NOT NULL,
    name text NOT NULL,
    code text NOT NULL,
    description text,
    reference_id integer,
    type_id integer NOT NULL,
    created_by integer,
    created_date date DEFAULT ('now'::text)::date,
    modified_by integer,
    modified_date date DEFAULT ('now'::text)::date,
    status integer NOT NULL
);


--
-- Name: map_map_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE map_map_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: map_map_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE map_map_id_seq OWNED BY mapset.mapset_id;


--
-- Name: map_prop; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE map_prop (
    map_prop_id integer NOT NULL,
    map_id integer NOT NULL,
    props jsonb
);


--
-- Name: map_prop_map_prop_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE map_prop_map_prop_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: map_prop_map_prop_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE map_prop_map_prop_id_seq OWNED BY map_prop.map_prop_id;


--
-- Name: marker; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE marker (
    marker_id integer NOT NULL,
    platform_id integer NOT NULL,
    variant_id integer,
    name text NOT NULL,
    code text,
    ref text,
    alts text[],
    sequence text,
    reference_id integer,
    primers jsonb,
    strand_id integer,
    status integer NOT NULL,
    probsets jsonb
);


--
-- Name: marker_group; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE marker_group (
    marker_group_id integer NOT NULL,
    name text NOT NULL,
    code text,
    markers jsonb NOT NULL,
    germplasm_group text,
    created_by integer,
    created_date date,
    modified_by integer,
    modified_date date,
    status integer NOT NULL
);


--
-- Name: marker_group_marker_group_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE marker_group_marker_group_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: marker_group_marker_group_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE marker_group_marker_group_id_seq OWNED BY marker_group.marker_group_id;


--
-- Name: marker_linkage_group; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE marker_linkage_group (
    marker_linkage_group_id integer NOT NULL,
    marker_id integer NOT NULL,
    start numeric(13,3),
    stop numeric(13,3),
    linkage_group_id integer NOT NULL
);


--
-- Name: marker_map_marker_map_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE marker_map_marker_map_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: marker_map_marker_map_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE marker_map_marker_map_id_seq OWNED BY marker_linkage_group.marker_linkage_group_id;


--
-- Name: marker_marker_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE marker_marker_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: marker_marker_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE marker_marker_id_seq OWNED BY marker.marker_id;


--
-- Name: marker_prop; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE marker_prop (
    marker_prop_id integer NOT NULL,
    marker_id integer NOT NULL,
    props jsonb
);


--
-- Name: marker_prop_marker_prop_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE marker_prop_marker_prop_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: marker_prop_marker_prop_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE marker_prop_marker_prop_id_seq OWNED BY marker_prop.marker_prop_id;


--
-- Name: organization; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE organization (
    organization_id integer NOT NULL,
    name text NOT NULL,
    address text,
    website text,
    created_by integer,
    created_date date DEFAULT ('now'::text)::date,
    modified_by integer,
    modified_date date DEFAULT ('now'::text)::date,
    status integer DEFAULT 1 NOT NULL
);


--
-- Name: organization_organization_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE organization_organization_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: organization_organization_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE organization_organization_id_seq OWNED BY organization.organization_id;


--
-- Name: platform; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE platform (
    platform_id integer NOT NULL,
    name text NOT NULL,
    code text NOT NULL,
    vendor_id integer,
    description text,
    created_by integer,
    created_date date DEFAULT ('now'::text)::date,
    modified_by integer,
    modified_date date DEFAULT ('now'::text)::date,
    status integer NOT NULL,
    type_id integer NOT NULL
);


--
-- Name: platform_platform_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE platform_platform_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: platform_platform_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE platform_platform_id_seq OWNED BY platform.platform_id;


--
-- Name: platform_prop; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE platform_prop (
    platform_prop_id integer NOT NULL,
    platform_id integer NOT NULL,
    props jsonb
);


--
-- Name: platform_prop_platform_prop_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE platform_prop_platform_prop_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: platform_prop_platform_prop_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE platform_prop_platform_prop_id_seq OWNED BY platform_prop.platform_prop_id;


--
-- Name: project; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE project (
    project_id integer NOT NULL,
    name text NOT NULL,
    code text,
    description text,
    pi_contact integer NOT NULL,
    created_by integer,
    created_date date DEFAULT ('now'::text)::date,
    modified_by integer,
    modified_date date DEFAULT ('now'::text)::date,
    status integer NOT NULL
);


--
-- Name: project_project_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE project_project_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: project_project_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE project_project_id_seq OWNED BY project.project_id;


--
-- Name: project_prop; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE project_prop (
    project_prop_id integer NOT NULL,
    project_id integer NOT NULL,
    props jsonb
);


--
-- Name: project_prop_project_prop_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE project_prop_project_prop_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: project_prop_project_prop_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE project_prop_project_prop_id_seq OWNED BY project_prop.project_prop_id;


--
-- Name: reference; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE reference (
    reference_id integer NOT NULL,
    name text NOT NULL,
    version text NOT NULL,
    link text,
    file_path text
);


--
-- Name: reference_reference_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE reference_reference_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: reference_reference_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE reference_reference_id_seq OWNED BY reference.reference_id;


--
-- Name: role_role_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE role_role_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: role_role_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE role_role_id_seq OWNED BY role.role_id;


--
-- Name: v_all_projects_full_details; Type: VIEW; Schema: public; Owner: -
--

CREATE VIEW v_all_projects_full_details AS
 SELECT p.project_id,
    p.name,
    p.code,
    p.description,
    p.pi_contact AS pi_contact_id,
    c.firstname AS pi_first_name,
    c.lastname AS pi_last_name,
    p.created_by,
    p.created_date,
    p.modified_by,
    p.modified_date,
    p.status
   FROM (project p
     JOIN contact c ON ((p.pi_contact = c.contact_id)));


--
-- Name: v_marker_linkage_genetic; Type: VIEW; Schema: public; Owner: -
--

CREATE VIEW v_marker_linkage_genetic AS
 SELECT mlg.marker_id,
    lg.name AS linkage_group_name,
    (mlg.start)::integer AS start,
    (mlg.stop)::integer AS stop,
    ms.name AS mapset_name
   FROM marker_linkage_group mlg,
    linkage_group lg,
    mapset ms
  WHERE ((mlg.linkage_group_id = lg.linkage_group_id) AND (lg.map_id = ms.mapset_id));


--
-- Name: v_marker_linkage_physical; Type: VIEW; Schema: public; Owner: -
--

CREATE VIEW v_marker_linkage_physical AS
 SELECT mlg.marker_id,
    lg.name AS linkage_group_name,
    mlg.start,
    mlg.stop,
    ms.name AS mapset_name
   FROM marker_linkage_group mlg,
    linkage_group lg,
    mapset ms
  WHERE ((mlg.linkage_group_id = lg.linkage_group_id) AND (lg.map_id = ms.mapset_id));


--
-- Name: v_marker_metadata_by_dataset; Type: VIEW; Schema: public; Owner: -
--

CREATE VIEW v_marker_metadata_by_dataset AS
 SELECT m.marker_id,
    dm.dataset_id,
    p.name AS platform_name,
    m.variant_id,
    m.name,
    m.code,
    m.ref,
    m.alts,
    m.sequence,
    r.name AS reference_name,
    m.primers,
    m.probsets,
    cv.term AS strand_name,
    m.status
   FROM marker m,
    platform p,
    reference r,
    cv,
    dataset_marker dm
  WHERE ((m.marker_id = dm.marker_id) AND (m.platform_id = p.platform_id) AND (m.reference_id = r.reference_id) AND (m.strand_id = cv.cv_id));


--
-- Name: v_marker_with_props; Type: VIEW; Schema: public; Owner: -
--

CREATE VIEW v_marker_with_props AS
 SELECT m.marker_id,
    m.name,
    mpr."23" AS genome_build,
    mpr."24" AS whatever,
    mp.props
   FROM marker m,
    marker_prop mp,
    LATERAL jsonb_populate_record(NULL::myrowtype, mp.props) mpr("23", "24", "25", "26", "27")
  WHERE (m.marker_id = mp.marker_id);


--
-- Name: variant; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE variant (
    variant_id integer NOT NULL,
    code text,
    created_by integer,
    created_date date,
    modified_by integer,
    modified_date date
);


--
-- Name: variant_variant_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE variant_variant_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: variant_variant_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE variant_variant_id_seq OWNED BY variant.variant_id;


--
-- Name: analysis_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY analysis ALTER COLUMN analysis_id SET DEFAULT nextval('analysis_analysis_id_seq'::regclass);


--
-- Name: contact_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY contact ALTER COLUMN contact_id SET DEFAULT nextval('contact_contact_id_seq'::regclass);


--
-- Name: cv_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY cv ALTER COLUMN cv_id SET DEFAULT nextval('cv_cv_id_seq'::regclass);


--
-- Name: dataset_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY dataset ALTER COLUMN dataset_id SET DEFAULT nextval('dataset_dataset_id_seq'::regclass);


--
-- Name: dataset_dnarun_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY dataset_dnarun ALTER COLUMN dataset_dnarun_id SET DEFAULT nextval('dataset_dnarun_dataset_dnarun_id_seq'::regclass);


--
-- Name: dataset_marker_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY dataset_marker ALTER COLUMN dataset_marker_id SET DEFAULT nextval('dataset_marker_dataset_marker_id_seq'::regclass);


--
-- Name: display_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY display ALTER COLUMN display_id SET DEFAULT nextval('display_display_id_seq'::regclass);


--
-- Name: dnarun_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY dnarun ALTER COLUMN dnarun_id SET DEFAULT nextval('dnarun_dnarun_id_seq'::regclass);


--
-- Name: dnarun_prop_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY dnarun_prop ALTER COLUMN dnarun_prop_id SET DEFAULT nextval('dnarun_prop_dnarun_prop_id_seq'::regclass);


--
-- Name: dnasample_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY dnasample ALTER COLUMN dnasample_id SET DEFAULT nextval('dnasample_dnasample_id_seq'::regclass);


--
-- Name: dnasample_prop_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY dnasample_prop ALTER COLUMN dnasample_prop_id SET DEFAULT nextval('dnasample_prop_dnasample_prop_id_seq'::regclass);


--
-- Name: experiment_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY experiment ALTER COLUMN experiment_id SET DEFAULT nextval('experiment_experiment_id_seq'::regclass);


--
-- Name: germplasm_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY germplasm ALTER COLUMN germplasm_id SET DEFAULT nextval('germplasm_germplasm_id_seq'::regclass);


--
-- Name: germplasm_prop_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY germplasm_prop ALTER COLUMN germplasm_prop_id SET DEFAULT nextval('germplasm_prop_germplasm_prop_id_seq'::regclass);


--
-- Name: linkage_group_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY linkage_group ALTER COLUMN linkage_group_id SET DEFAULT nextval('linkage_group_linkage_group_id_seq'::regclass);


--
-- Name: manifest_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY manifest ALTER COLUMN manifest_id SET DEFAULT nextval('manifest_manifest_id_seq'::regclass);


--
-- Name: map_prop_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY map_prop ALTER COLUMN map_prop_id SET DEFAULT nextval('map_prop_map_prop_id_seq'::regclass);


--
-- Name: mapset_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY mapset ALTER COLUMN mapset_id SET DEFAULT nextval('map_map_id_seq'::regclass);


--
-- Name: marker_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY marker ALTER COLUMN marker_id SET DEFAULT nextval('marker_marker_id_seq'::regclass);


--
-- Name: marker_group_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY marker_group ALTER COLUMN marker_group_id SET DEFAULT nextval('marker_group_marker_group_id_seq'::regclass);


--
-- Name: marker_linkage_group_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY marker_linkage_group ALTER COLUMN marker_linkage_group_id SET DEFAULT nextval('marker_map_marker_map_id_seq'::regclass);


--
-- Name: marker_prop_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY marker_prop ALTER COLUMN marker_prop_id SET DEFAULT nextval('marker_prop_marker_prop_id_seq'::regclass);


--
-- Name: organization_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY organization ALTER COLUMN organization_id SET DEFAULT nextval('organization_organization_id_seq'::regclass);


--
-- Name: platform_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY platform ALTER COLUMN platform_id SET DEFAULT nextval('platform_platform_id_seq'::regclass);


--
-- Name: platform_prop_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY platform_prop ALTER COLUMN platform_prop_id SET DEFAULT nextval('platform_prop_platform_prop_id_seq'::regclass);


--
-- Name: project_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY project ALTER COLUMN project_id SET DEFAULT nextval('project_project_id_seq'::regclass);


--
-- Name: project_prop_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY project_prop ALTER COLUMN project_prop_id SET DEFAULT nextval('project_prop_project_prop_id_seq'::regclass);


--
-- Name: reference_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY reference ALTER COLUMN reference_id SET DEFAULT nextval('reference_reference_id_seq'::regclass);


--
-- Name: role_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY role ALTER COLUMN role_id SET DEFAULT nextval('role_role_id_seq'::regclass);


--
-- Name: variant_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY variant ALTER COLUMN variant_id SET DEFAULT nextval('variant_variant_id_seq'::regclass);


--
-- Name: email_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY contact
    ADD CONSTRAINT email_key UNIQUE (email);


--
-- Name: group_term_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY cv
    ADD CONSTRAINT group_term_key UNIQUE ("group", term);


--
-- Name: idx_dnarun_prop; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY dnarun_prop
    ADD CONSTRAINT idx_dnarun_prop UNIQUE (dnarun_id);


--
-- Name: idx_dnasample_prop; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY dnasample_prop
    ADD CONSTRAINT idx_dnasample_prop UNIQUE (dnasample_id);


--
-- Name: idx_germplasm_prop; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY germplasm_prop
    ADD CONSTRAINT idx_germplasm_prop UNIQUE (germplasm_id);


--
-- Name: idx_map_prop; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY map_prop
    ADD CONSTRAINT idx_map_prop UNIQUE (map_id);


--
-- Name: idx_marker_prop; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY marker_prop
    ADD CONSTRAINT idx_marker_prop UNIQUE (marker_id);


--
-- Name: idx_platform_prop; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY platform_prop
    ADD CONSTRAINT idx_platform_prop UNIQUE (platform_id);


--
-- Name: idx_project_prop; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY project_prop
    ADD CONSTRAINT idx_project_prop UNIQUE (project_id);


--
-- Name: name_project_id_platform_id_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY experiment
    ADD CONSTRAINT name_project_id_platform_id_key UNIQUE (name, project_id, platform_id);


--
-- Name: organization_name_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY organization
    ADD CONSTRAINT organization_name_key UNIQUE (name);


--
-- Name: organization_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY organization
    ADD CONSTRAINT organization_pkey PRIMARY KEY (organization_id);


--
-- Name: pi_project_name_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY project
    ADD CONSTRAINT pi_project_name_key UNIQUE (pi_contact, name);


--
-- Name: pk_analysis; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY analysis
    ADD CONSTRAINT pk_analysis PRIMARY KEY (analysis_id);


--
-- Name: pk_contact; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY contact
    ADD CONSTRAINT pk_contact PRIMARY KEY (contact_id);


--
-- Name: pk_cv; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY cv
    ADD CONSTRAINT pk_cv PRIMARY KEY (cv_id);


--
-- Name: pk_dataset; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY dataset
    ADD CONSTRAINT pk_dataset PRIMARY KEY (dataset_id);


--
-- Name: pk_dataset_dnarun; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY dataset_dnarun
    ADD CONSTRAINT pk_dataset_dnarun PRIMARY KEY (dataset_dnarun_id);


--
-- Name: pk_dataset_marker; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY dataset_marker
    ADD CONSTRAINT pk_dataset_marker PRIMARY KEY (dataset_marker_id);


--
-- Name: pk_dnarun; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY dnarun
    ADD CONSTRAINT pk_dnarun PRIMARY KEY (dnarun_id);


--
-- Name: pk_dnarun_prop; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY dnarun_prop
    ADD CONSTRAINT pk_dnarun_prop PRIMARY KEY (dnarun_prop_id);


--
-- Name: pk_dnasample; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY dnasample
    ADD CONSTRAINT pk_dnasample PRIMARY KEY (dnasample_id);


--
-- Name: pk_dnasample_prop; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY dnasample_prop
    ADD CONSTRAINT pk_dnasample_prop PRIMARY KEY (dnasample_prop_id);


--
-- Name: pk_experiment; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY experiment
    ADD CONSTRAINT pk_experiment PRIMARY KEY (experiment_id);


--
-- Name: pk_germplasm; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY germplasm
    ADD CONSTRAINT pk_germplasm PRIMARY KEY (germplasm_id);


--
-- Name: pk_germplasm_prop; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY germplasm_prop
    ADD CONSTRAINT pk_germplasm_prop PRIMARY KEY (germplasm_prop_id);


--
-- Name: pk_linkage_group; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY linkage_group
    ADD CONSTRAINT pk_linkage_group PRIMARY KEY (linkage_group_id);


--
-- Name: pk_manifest; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY manifest
    ADD CONSTRAINT pk_manifest PRIMARY KEY (manifest_id);


--
-- Name: pk_map; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY mapset
    ADD CONSTRAINT pk_map PRIMARY KEY (mapset_id);


--
-- Name: pk_map_prop; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY map_prop
    ADD CONSTRAINT pk_map_prop PRIMARY KEY (map_prop_id);


--
-- Name: pk_marker; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY marker
    ADD CONSTRAINT pk_marker PRIMARY KEY (marker_id);


--
-- Name: pk_marker_group; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY marker_group
    ADD CONSTRAINT pk_marker_group PRIMARY KEY (marker_group_id);


--
-- Name: pk_marker_map; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY marker_linkage_group
    ADD CONSTRAINT pk_marker_map PRIMARY KEY (marker_linkage_group_id);


--
-- Name: pk_marker_prop; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY marker_prop
    ADD CONSTRAINT pk_marker_prop PRIMARY KEY (marker_prop_id);


--
-- Name: pk_platform; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY platform
    ADD CONSTRAINT pk_platform PRIMARY KEY (platform_id);


--
-- Name: pk_platform_prop; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY platform_prop
    ADD CONSTRAINT pk_platform_prop PRIMARY KEY (platform_prop_id);


--
-- Name: pk_project; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY project
    ADD CONSTRAINT pk_project PRIMARY KEY (project_id);


--
-- Name: pk_project_prop; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY project_prop
    ADD CONSTRAINT pk_project_prop PRIMARY KEY (project_prop_id);


--
-- Name: pk_reference; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY reference
    ADD CONSTRAINT pk_reference PRIMARY KEY (reference_id);


--
-- Name: pk_role; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY role
    ADD CONSTRAINT pk_role PRIMARY KEY (role_id);


--
-- Name: pk_table_display; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY display
    ADD CONSTRAINT pk_table_display PRIMARY KEY (display_id);


--
-- Name: pk_variant; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY variant
    ADD CONSTRAINT pk_variant PRIMARY KEY (variant_id);


--
-- Name: idx_germplasm; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_germplasm ON germplasm USING btree (status);


--
-- Name: idx_germplasm_0; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_germplasm_0 ON germplasm USING btree (species_id);


--
-- Name: idx_linkage_group; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_linkage_group ON linkage_group USING btree (map_id);


--
-- Name: idx_marker; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_marker ON marker USING btree (strand_id);


--
-- Name: idx_marker_0; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_marker_0 ON marker USING btree (reference_id);


--
-- Name: idx_marker_map; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_marker_map ON marker_linkage_group USING btree (linkage_group_id);


--
-- Name: idx_platform; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_platform ON platform USING btree (vendor_id);


--
-- Name: idx_platform_0; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_platform_0 ON platform USING btree (type_id);


--
-- Name: idx_project; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_project ON project USING btree (pi_contact);


--
-- Name: analysis_analysis_type_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY analysis
    ADD CONSTRAINT analysis_analysis_type_id_fkey FOREIGN KEY (type_id) REFERENCES cv(cv_id);


--
-- Name: dataset_dnarun_fk1; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY dataset_dnarun
    ADD CONSTRAINT dataset_dnarun_fk1 FOREIGN KEY (dataset_id) REFERENCES dataset(dataset_id);


--
-- Name: dataset_dnarun_fk2; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY dataset_dnarun
    ADD CONSTRAINT dataset_dnarun_fk2 FOREIGN KEY (dnarun_id) REFERENCES dnarun(dnarun_id);


--
-- Name: dataset_fk1; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY dataset
    ADD CONSTRAINT dataset_fk1 FOREIGN KEY (experiment_id) REFERENCES experiment(experiment_id);


--
-- Name: dataset_fk2; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY dataset
    ADD CONSTRAINT dataset_fk2 FOREIGN KEY (callinganalysis_id) REFERENCES analysis(analysis_id);


--
-- Name: dataset_marker_dataset_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY dataset_marker
    ADD CONSTRAINT dataset_marker_dataset_id_fkey FOREIGN KEY (dataset_id) REFERENCES dataset(dataset_id);


--
-- Name: dataset_marker_marker_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY dataset_marker
    ADD CONSTRAINT dataset_marker_marker_id_fkey FOREIGN KEY (marker_id) REFERENCES marker(marker_id);


--
-- Name: dnarun_fk1; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY dnarun
    ADD CONSTRAINT dnarun_fk1 FOREIGN KEY (experiment_id) REFERENCES experiment(experiment_id);


--
-- Name: dnarun_fk2; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY dnarun
    ADD CONSTRAINT dnarun_fk2 FOREIGN KEY (dnasample_id) REFERENCES dnasample(dnasample_id);


--
-- Name: dnarun_prop_dnarun_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY dnarun_prop
    ADD CONSTRAINT dnarun_prop_dnarun_id_fkey FOREIGN KEY (dnarun_id) REFERENCES dnarun(dnarun_id);


--
-- Name: dnasample_fk1; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY dnasample
    ADD CONSTRAINT dnasample_fk1 FOREIGN KEY (project_id) REFERENCES project(project_id);


--
-- Name: dnasample_fk2; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY dnasample
    ADD CONSTRAINT dnasample_fk2 FOREIGN KEY (germplasm_id) REFERENCES germplasm(germplasm_id);


--
-- Name: dnasample_prop_fk1; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY dnasample_prop
    ADD CONSTRAINT dnasample_prop_fk1 FOREIGN KEY (dnasample_id) REFERENCES dnasample(dnasample_id);


--
-- Name: experiment_fk1; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY experiment
    ADD CONSTRAINT experiment_fk1 FOREIGN KEY (project_id) REFERENCES project(project_id);


--
-- Name: experiment_fk2; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY experiment
    ADD CONSTRAINT experiment_fk2 FOREIGN KEY (platform_id) REFERENCES platform(platform_id);


--
-- Name: experiment_fk3; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY experiment
    ADD CONSTRAINT experiment_fk3 FOREIGN KEY (manifest_id) REFERENCES manifest(manifest_id);


--
-- Name: fk_germplasm_species_id_cv; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY germplasm
    ADD CONSTRAINT fk_germplasm_species_id_cv FOREIGN KEY (species_id) REFERENCES cv(cv_id);


--
-- Name: fk_linkage_group_map; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY linkage_group
    ADD CONSTRAINT fk_linkage_group_map FOREIGN KEY (map_id) REFERENCES mapset(mapset_id);


--
-- Name: fk_marker_cv; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY marker
    ADD CONSTRAINT fk_marker_cv FOREIGN KEY (strand_id) REFERENCES cv(cv_id);


--
-- Name: fk_marker_linkage_group; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY marker_linkage_group
    ADD CONSTRAINT fk_marker_linkage_group FOREIGN KEY (linkage_group_id) REFERENCES linkage_group(linkage_group_id);


--
-- Name: fk_marker_reference; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY marker
    ADD CONSTRAINT fk_marker_reference FOREIGN KEY (reference_id) REFERENCES reference(reference_id);


--
-- Name: fk_organization_contact; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY contact
    ADD CONSTRAINT fk_organization_contact FOREIGN KEY (organization_id) REFERENCES organization(organization_id);


--
-- Name: fk_platform_contact; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY platform
    ADD CONSTRAINT fk_platform_contact FOREIGN KEY (vendor_id) REFERENCES contact(contact_id);


--
-- Name: fk_platform_cv; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY platform
    ADD CONSTRAINT fk_platform_cv FOREIGN KEY (type_id) REFERENCES cv(cv_id);


--
-- Name: fk_project_contact; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY project
    ADD CONSTRAINT fk_project_contact FOREIGN KEY (pi_contact) REFERENCES contact(contact_id);


--
-- Name: germplasm_germplasm_type_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY germplasm
    ADD CONSTRAINT germplasm_germplasm_type_id_fkey FOREIGN KEY (type_id) REFERENCES cv(cv_id);


--
-- Name: germplasm_prop_fk1; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY germplasm_prop
    ADD CONSTRAINT germplasm_prop_fk1 FOREIGN KEY (germplasm_id) REFERENCES germplasm(germplasm_id);


--
-- Name: map_map_type_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY mapset
    ADD CONSTRAINT map_map_type_fkey FOREIGN KEY (type_id) REFERENCES cv(cv_id);


--
-- Name: map_prop_map_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY map_prop
    ADD CONSTRAINT map_prop_map_id_fkey FOREIGN KEY (map_id) REFERENCES mapset(mapset_id);


--
-- Name: map_reference_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY mapset
    ADD CONSTRAINT map_reference_id_fkey FOREIGN KEY (reference_id) REFERENCES reference(reference_id);


--
-- Name: marker_fk1; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY marker
    ADD CONSTRAINT marker_fk1 FOREIGN KEY (platform_id) REFERENCES platform(platform_id);


--
-- Name: marker_fk2; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY marker
    ADD CONSTRAINT marker_fk2 FOREIGN KEY (variant_id) REFERENCES variant(variant_id);


--
-- Name: marker_map_marker_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY marker_linkage_group
    ADD CONSTRAINT marker_map_marker_id_fkey FOREIGN KEY (marker_id) REFERENCES marker(marker_id);


--
-- Name: marker_prop_marker_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY marker_prop
    ADD CONSTRAINT marker_prop_marker_id_fkey FOREIGN KEY (marker_id) REFERENCES marker(marker_id);


--
-- Name: platform_prop_platform_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY platform_prop
    ADD CONSTRAINT platform_prop_platform_id_fkey FOREIGN KEY (platform_id) REFERENCES platform(platform_id);


--
-- Name: project_prop_fk1; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY project_prop
    ADD CONSTRAINT project_prop_fk1 FOREIGN KEY (project_id) REFERENCES project(project_id);


--
-- Name: public; Type: ACL; Schema: -; Owner: -
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- PostgreSQL database dump complete
--

