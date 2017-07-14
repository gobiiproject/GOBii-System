--liquibase formatted sql

--changeset venice.juanillas:createreference_fxn context:general splitStatements:false
DROP FUNCTION IF EXISTS createreference(referencename text, referenceversion text, referencelink text, filepath text, OUT id integer);
CREATE OR REPLACE FUNCTION createreference(referencename text, referenceversion text, referencelink text, filepath text,createdBy integer, createdDate date, modifiedBy integer, modifiedDate date, OUT id integer) 
RETURNS integer AS $$
	BEGIN
		insert into reference(name,version,link, file_path, created_by, created_date,modified_by, modified_date) values(referencename, referenceversion, referencelink, filepath, createdBy, createdDate, modifiedBy, modifiedDate);
		select lastval() into id;
	END;
$$ LANGUAGE plpgsql;

--changeset venice.juanillas:updatereference_fxn context:general splitStatements:false
DROP FUNCTION IF EXISTS updateReference(id integer, referenceName text, referenceVersion text, referenceLink text, filePath text);

CREATE OR REPLACE FUNCTION updateReference(id integer, referenceName text, referenceVersion text, referenceLink text, filePath text,createdBy integer, createdDate date, modifiedBy integer, modifiedDate date)
RETURNS void AS $$
    BEGIN
    	update reference set name=referenceName, version=referenceVersion, link=referenceLink, file_path=filePath, created_by = createdBy, created_date = createdDate, modified_by = modifiedBy, modified_date = modifiedDate
     	where reference_id = id;
    END;
$$ LANGUAGE plpgsql;

--changeset venice.juanillas:createanalysis_fxn1 context:general splitStatements:false
DROP FUNCTION IF EXISTS createAnalysis(analysisname text, analysisdescription text, typeid integer, analysisprogram text, analysisprogramversion text, aanalysisalgorithm text, analysissourcename text, analysissourceversion text, analysissourceuri text, referenceid integer, analysisparameters jsonb, analysistimeexecuted timestamp without time zone, analysisstatus integer, OUT id integer);

CREATE OR REPLACE FUNCTION createAnalysis(analysisname text, analysisdescription text, typeid integer, analysisprogram text, analysisprogramversion text, aanalysisalgorithm text, analysissourcename text, analysissourceversion text, analysissourceuri text, referenceid integer, analysisparameters jsonb, analysistimeexecuted timestamp without time zone, analysisstatus integer,createdBy integer, createdDate date, modifiedBy integer, modifiedDate date,OUT id integer)
RETURNS integer AS $$
    BEGIN   
        insert into analysis (name, description, type_id, program, programversion, algorithm, sourcename, sourceversion, sourceuri, reference_id, parameters, timeexecuted, status,created_by, created_date, modified_by, modified_date)
        values (analysisName, analysisDescription, typeId, analysisProgram, analysisProgramversion, aanalysisAlgorithm, analysisSourcename, analysisSourceversion, analysisSourceuri, referenceId, analysisparameters, analysisTimeexecuted, analysisStatus, createdBy, createdDate, modifiedBy, modifiedDate);
            select lastval() into id;
    END;
$$ LANGUAGE plpgsql;

--changeset venice.juanillas:createanalysis_fxn2 context:general splitStatements:false
DROP FUNCTION IF EXISTS createAnalysis(analysisname text, analysisdescription text, typeid integer, analysisprogram text, analysisprogramversion text, aanalysisalgorithm text, analysissourcename text, analysissourceversion text, analysissourceuri text, referenceid integer, analysistimeexecuted timestamp without time zone, analysisstatus integer, OUT id integer);

CREATE OR REPLACE FUNCTION createAnalysis(analysisname text, analysisdescription text, typeid integer, analysisprogram text, analysisprogramversion text, aanalysisalgorithm text, analysissourcename text, analysissourceversion text, analysissourceuri text, referenceid integer, analysistimeexecuted timestamp without time zone, analysisstatus integer,createdBy integer, createdDate date, modifiedBy integer, modifiedDate date,OUT id integer)
RETURNS integer AS $$
	BEGIN	
		insert into analysis (name, description, type_id, program, programversion, algorithm, sourcename, sourceversion, sourceuri, reference_id, parameters, timeexecuted, status,created_by, created_date, modified_by, modified_date)
		values (analysisName, analysisDescription, typeId, analysisProgram, analysisProgramversion, aanalysisAlgorithm, analysisSourcename, analysisSourceversion, analysisSourceuri, referenceId, '{}'::jsonb, analysisTimeexecuted, analysisStatus, createdBy, createdDate, modifiedBy, modifiedDate);
    		select lastval() into id;
	END;
$$ LANGUAGE plpgsql;

--changeset venice.juanillas:updateanalysis_fxn1 context:general splitStatements:false
DROP FUNCTION IF EXISTS updateAnalysis(id integer, analysisName text, analysisDescription text, typeId integer, analysisProgram text, analysisProgramversion text, aanalysisAlgorithm text, analysisSourcename text, analysisSourceversion text, analysisSourceuri text, referenceId integer, analysisTimeexecuted timestamp, analysisStatus integer);

CREATE OR REPLACE FUNCTION updateAnalysis(id integer, analysisName text, analysisDescription text, typeId integer, analysisProgram text, analysisProgramversion text, aanalysisAlgorithm text, analysisSourcename text, analysisSourceversion text, analysisSourceuri text, referenceId integer, analysisParameters jsonb, analysisTimeexecuted timestamp, analysisStatus integer, createdBy integer,createdDate date, modifiedBy integer, modifiedDate date)
RETURNS void AS $$
    BEGIN
    update analysis set name=analysisName, description=analysisDescription, type_id=typeId, program=analysisProgram, programversion=analysisProgramversion, algorithm=aanalysisAlgorithm, sourcename=analysisSourcename, sourceversion=analysisSourceversion, sourceuri=analysisSourceuri, reference_id=referenceId, parameters=analysisParameters, timeexecuted=analysisTimeexecuted, status=analysisStatus, created_by = createdBy, created_date = createdDate, modified_by = modifiedBy, modified_date = modifiedDate
     where analysis_id = id;
    END;
$$ LANGUAGE plpgsql;

--changeset venice.juanillas:updateanalysis_fxn2 context:general splitStatements:false
DROP FUNCTION IF EXISTS updateAnalysis(id integer, analysisName text, analysisDescription text, typeId integer, analysisProgram text, analysisProgramversion text, aanalysisAlgorithm text, analysisSourcename text, analysisSourceversion text, analysisSourceuri text, referenceId integer, analysisTimeexecuted timestamp, analysisStatus integer);

CREATE OR REPLACE FUNCTION updateAnalysis(id integer, analysisName text, analysisDescription text, typeId integer, analysisProgram text, analysisProgramversion text, aanalysisAlgorithm text, analysisSourcename text, analysisSourceversion text, analysisSourceuri text, referenceId integer, analysisTimeexecuted timestamp, analysisStatus integer, createdBy integer,createdDate date, modifiedBy integer, modifiedDate date)
RETURNS void AS $$
    BEGIN
    update analysis set name=analysisName, description=analysisDescription, type_id=typeId, program=analysisProgram, programversion=analysisProgramversion, algorithm=aanalysisAlgorithm, sourcename=analysisSourcename, sourceversion=analysisSourceversion, sourceuri=analysisSourceuri, reference_id=referenceId, timeexecuted=analysisTimeexecuted, status=analysisStatus, created_by = createdBy, created_date = createdDate, modified_by = modifiedBy, modified_date = modifiedDate
     where analysis_id = id;
    END;
$$ LANGUAGE plpgsql;

--changeset venice.juanillas:createlinkagegroup_fxn context:general splitStatements:false
DROP FUNCTION IF EXISTS createLinkageGroup(linkageGroupName text, linkageGroupStart integer, linkageGroupStop integer, mapId integer,OUT id integer);

CREATE OR REPLACE FUNCTION createLinkageGroup(linkageGroupName text, linkageGroupStart integer, linkageGroupStop integer, mapId integer,createdBy integer, createdDate date, modifiedBy integer, modifiedDate date, OUT id integer) 
RETURNS integer AS $$
	BEGIN
		insert into linkage_group(name, start, stop, map_id, created_by, created_date,modified_by, modified_date) values(linkageGroupName, linkageGroupStart, linkageGroupStop, mapId, createdBy, createdDate, modifiedBy, modifiedDate);
		select lastval() into id;
	END;
$$ LANGUAGE plpgsql;


--changeset venice.juanillas:updatelinkagegroup_fxn context:general splitStatements:false
DROP FUNCTION IF EXISTS updateLinkageGroup(id integer, linkageGroupName text, linkageGroupStart integer, linkageGroupStop integer, mapId integer);

CREATE OR REPLACE FUNCTION updateLinkageGroup(id integer, linkageGroupName text, linkageGroupStart integer, linkageGroupStop integer, mapId integer,createdBy integer ,createdDate date, modifiedBy integer, modifiedDate date)
RETURNS void AS $$
    BEGIN
    update linkage_group set name=linkageGroupName, start=linkageGroupStart, stop=linkageGroupStop, map_id=mapId, created_by = createdBy, created_date = createdDate, modified_by = modifiedBy, modified_date = modifiedDate
     where linkage_group_id = id;
    END;
$$ LANGUAGE plpgsql;



