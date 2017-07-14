--liquibase formatted sql

--### PROTOCOL TABLE AND FUNCTIONS ###---
--changeset kpalis:create_protocol_table context:general splitStatements:false
CREATE TABLE protocol ( 
	protocol_id          serial  NOT NULL,
	name                 text  NOT NULL,
	description          text  ,
	type_id              integer  ,
	platform_id          integer  ,
	props                jsonb default '{}'::jsonb,
	created_by           integer  ,
	created_date         date DEFAULT current_date ,
	modified_by          integer  ,
	modified_date        date DEFAULT current_date ,
	status               integer  default 1,
	CONSTRAINT pk_protocol PRIMARY KEY ( protocol_id )
 );

CREATE INDEX idx_protocol_type_id ON protocol ( type_id );

CREATE INDEX idx_protocol_platform_id ON protocol ( platform_id );

COMMENT ON TABLE protocol IS 'A Platform can have multiple protocols and more than one protocol can be run by more than one vendor. ';

ALTER TABLE protocol ADD CONSTRAINT protocol_type_id_fkey FOREIGN KEY ( type_id ) REFERENCES cv( cv_id );

ALTER TABLE protocol ADD CONSTRAINT protocol_platform_id_fkey FOREIGN KEY ( platform_id ) REFERENCES platform( platform_id );

--changeset kpalis:protocol_CRUD_functions context:general splitStatements:false
CREATE OR REPLACE FUNCTION createProtocol(pname text, pdescription text, ptypeid integer, pplatformid integer, pcreatedby integer, pcreateddate date, pmodifiedby integer, pmodifieddate date, pstatus integer, OUT id integer)
RETURNS integer AS $$
    BEGIN
    insert into protocol (name, description, type_id, platform_id, created_by, created_date, modified_by, modified_date, status)
      values (pname, pdescription, ptypeid, pplatformid, pcreatedby, pcreateddate, pmodifiedby, pmodifieddate, pstatus); 
    select lastval() into id;
    END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION updateProtocol(pid integer, pname text, pdescription text, ptypeid integer, pplatformid integer, pcreatedby integer, pcreateddate date, pmodifiedby integer, pmodifieddate date, pstatus integer)
RETURNS integer AS $$
	DECLARE
        i integer;
    BEGIN
    update protocol set name=pname, description=pdescription, type_id=ptypeid, platform_id=pplatformid, created_by=pcreatedby, created_date=pcreateddate, modified_by=pmodifiedby, modified_date=pmodifieddate, status=pstatus
     where protocol_id = pid;
      GET DIAGNOSTICS i = ROW_COUNT;
      return i;
    END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION deleteProtocol(pId integer)
RETURNS integer AS $$
    DECLARE
        i integer;
    BEGIN
    delete from protocol where protocol_id = pId;
    GET DIAGNOSTICS i = ROW_COUNT;
    return i;
    END;
$$ LANGUAGE plpgsql;


--###upsert by id
CREATE OR REPLACE FUNCTION upsertprotocolPropertyById(id integer, propertyId integer, propertyValue text)
RETURNS void AS $$
  BEGIN
    update protocol set props = props || ('{"'||propertyId::text||'": "'||propertyValue||'"}')::jsonb
      where protocol_id=id;
  END;
$$ LANGUAGE plpgsql;

--upsert by name
CREATE OR REPLACE FUNCTION upsertprotocolPropertyByName(id integer, propertyName text, propertyValue text)
RETURNS integer AS $$
  DECLARE
    propertyId integer;
  BEGIN
    select cv_id into propertyId from cv where term=propertyName;
    update protocol set props = props || ('{"'||propertyId::text||'": "'||propertyValue||'"}')::jsonb
      where protocol_id=id;
    return propertyId;
  END;
$$ LANGUAGE plpgsql;


--read/select all
CREATE OR REPLACE FUNCTION getAllPropertiesOfprotocol(protocolId integer)
RETURNS table (property_id integer, property_name text, property_value text) AS $$
  BEGIN
    return query
    select p1.key::int as property_id, cv.term as property_name, p1.value as property_value
    from cv, (select (jsonb_each_text(props)).* from protocol where protocol_id=protocolId) as p1
    where cv.cv_id = p1.key::int;
    END;
$$ LANGUAGE plpgsql;

--read/select a property of a single protocol given the protocol id and property id (cv_id of that property)
CREATE OR REPLACE FUNCTION getprotocolPropertyById(protocolId integer, propertyId integer)
RETURNS text AS $$
  DECLARE
    value text;
  BEGIN
    select props->propertyId::text into value from protocol where protocol_id=protocolId;
    return value;
  END;
$$ LANGUAGE plpgsql;

--read/select a property of a single protocol given the protocol id and property name --> should match with the property name in the CV table
CREATE OR REPLACE FUNCTION getprotocolPropertyByName(protocolId integer, propertyName text)
RETURNS table (property_id integer, property_value text) AS $$
  BEGIN
    return query
    (with property as (select cv_id from cv where term=propertyName)
    select property.cv_id, (props->property.cv_id::text)::text as value
      from protocol, property
      where protocol_id=protocolId);
  END;
$$ LANGUAGE plpgsql;

--delete property by ID
CREATE OR REPLACE FUNCTION deleteprotocolPropertyById(protocolId integer, propertyId integer)
RETURNS integer AS $$
  BEGIN
    update protocol 
    set props = props - propertyId::text
    where protocol_id=protocolId;
    return propertyId;
  END;
$$ LANGUAGE plpgsql;

--delete property by name
CREATE OR REPLACE FUNCTION deleteprotocolPropertyByName(protocolId integer, propertyName text)
RETURNS text AS $$
  BEGIN
    with property as (select cv_id from cv where term=propertyName)
    update protocol 
      set props = props - property.cv_id::text
      from property
      where protocol_id=protocolId;
    return propertyName;
  END;
$$ LANGUAGE plpgsql;

--### MODIFY PLATFORM TABLE ###--
--changeset kpalis:drop_platform_vendor_id context:general splitStatements:false
ALTER TABLE platform DROP COLUMN vendor_id;

DROP FUNCTION IF EXISTS updateplatform(id integer, platformname text, platformcode text, vendorid integer, platformdescription text, createdby integer, createddate date, modifiedby integer, modifieddate date, platformstatus integer, typeid integer);

CREATE OR REPLACE FUNCTION updateplatform(id integer, platformname text, platformcode text, platformdescription text, createdby integer, createddate date, modifiedby integer, modifieddate date, platformstatus integer, typeid integer)
  RETURNS integer
  LANGUAGE plpgsql
 AS $function$
 	 DECLARE
 	 	i integer;
     BEGIN
     update platform set name=platformName, code=platformCode, description=platformDescription, created_by=createdBy, created_date=createdDate, modified_by=modifiedBy, modified_date=modifiedDate, status=platformStatus, type_id=typeId
      where platform_id = id;
      GET DIAGNOSTICS i = ROW_COUNT;
      return i;
     END;
 $function$;

DROP FUNCTION IF EXISTS createplatform(platformname text, platformcode text, vendorid integer, platformdescription text, createdby integer, createddate date, modifiedby integer, modifieddate date, platformstatus integer, typeid integer, OUT id integer);

CREATE OR REPLACE FUNCTION createplatform(platformname text, platformcode text, platformdescription text, createdby integer, createddate date, modifiedby integer, modifieddate date, platformstatus integer, typeid integer, OUT id integer)
  RETURNS integer
  LANGUAGE plpgsql
 AS $function$
   BEGIN
     insert into platform (name, code, description, created_by, created_date, modified_by, modified_date, status, type_id)
       values (platformName, platformCode, platformDescription, createdBy, createdDate, modifiedBy, modifiedDate, platformStatus, typeId);
     select lastval() into id;
   END;
 $function$;

DROP FUNCTION IF EXISTS deleteplatform(id integer);

CREATE OR REPLACE FUNCTION deleteplatform(id integer)
  RETURNS integer
  LANGUAGE plpgsql
 AS $function$
 	 DECLARE
 	 	i integer;
     BEGIN
     delete from platform where platform_id = id;
     GET DIAGNOSTICS i = ROW_COUNT;
     return i;
     END;
 $function$;

--=============
--### VENDOR_PROTOCOL TABLE ###---
--changeset kpalis:create_vendor_protocol context:general splitStatements:false
CREATE TABLE vendor_protocol ( 
	vendor_protocol_id   serial  NOT NULL,
	name                 text  ,
	vendor_id            integer  NOT NULL,
	protocol_id          integer  NOT NULL,
	status               integer  default 1,
	CONSTRAINT pk_vendor_protocol PRIMARY KEY ( vendor_protocol_id )
 );

CREATE INDEX idx_vendor_protocol_vendor_id ON vendor_protocol ( vendor_id );

CREATE INDEX idx_vendor_protocol_protocol_id ON vendor_protocol ( protocol_id );

COMMENT ON TABLE vendor_protocol IS 'Vendors reside in the Organization table. A vendor can provide multiple protocols, and a particular protocol can be offered by multiple vendors, hence the N:M relationship table.';

COMMENT ON COLUMN vendor_protocol.name IS 'This is optional.';

ALTER TABLE vendor_protocol ADD CONSTRAINT vendor_protocol_vendor_id_fkey FOREIGN KEY ( vendor_id ) REFERENCES organization( organization_id );

ALTER TABLE vendor_protocol ADD CONSTRAINT vendor_protocol_protocol_id_fkey FOREIGN KEY ( protocol_id ) REFERENCES protocol( protocol_id );

--changeset kpalis:vendor_protocol_CRUD_functions context:general splitStatements:false
CREATE OR REPLACE FUNCTION createVendorProtocol(pname text, pvendorid integer, pprotocolid integer, pstatus integer, OUT id integer)
RETURNS integer AS $$
    BEGIN
    insert into vendor_protocol (name, vendor_id, protocol_id, status)
      values (pname, pvendorid, pprotocolid, pstatus); 
    select lastval() into id;
    END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION updateVendorProtocol(pid integer, pname text, pvendorid integer, pprotocolid integer, pstatus integer)
RETURNS integer AS $$
	DECLARE
        i integer;
    BEGIN
    update vendor_protocol set name=pname, vendor_id=pvendorid, protocol_id=pprotocolid, status=pstatus
     where vendor_protocol_id = pid;
      GET DIAGNOSTICS i = ROW_COUNT;
      return i;
    END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION deleteVendorProtocol(pId integer)
RETURNS integer AS $$
    DECLARE
        i integer;
    BEGIN
    delete from vendor_protocol where vendor_protocol_id = pId;
    GET DIAGNOSTICS i = ROW_COUNT;
    return i;
    END;
$$ LANGUAGE plpgsql;



--### EXPERIMENT AND MARKER MODS ###---
--changeset kpalis:redirect_experiment_fk context:general splitStatements:false
ALTER TABLE experiment DROP COLUMN platform_id;

ALTER TABLE experiment ADD COLUMN vendor_protocol_id integer;

CREATE INDEX idx_experiment_vendor_protocol_id ON experiment ( vendor_protocol_id );

ALTER TABLE experiment ADD CONSTRAINT experiment_vendor_protocol_id_fkey FOREIGN KEY ( vendor_protocol_id ) REFERENCES vendor_protocol( vendor_protocol_id );

--function changes
DROP FUNCTION IF EXISTS createexperiment(expname text, expcode text, projectid integer, platformid integer, manifestid integer, datafile text, createdby integer, createddate date, modifiedby integer, modifieddate date, expstatus integer, OUT expid integer);

CREATE OR REPLACE FUNCTION createexperiment(pname text, pcode text, pprojectid integer, pvendorprotocolid integer, pmanifestid integer, pdatafile text, pcreatedby integer, pcreateddate date, pmodifiedby integer, pmodifieddate date, pstatus integer, OUT expid integer)
  RETURNS integer
  LANGUAGE plpgsql
 AS $function$
     BEGIN
     insert into experiment (name, code, project_id, manifest_id, data_file, created_by, created_date, modified_by, modified_date, status, vendor_protocol_id)
       values (pname, pcode, pprojectid, pmanifestid, pdatafile, pcreatedby, pcreateddate, pmodifiedby, pmodifieddate, pstatus, pvendorprotocolid);
     select lastval() into expId;
     END;
 $function$;

DROP FUNCTION IF EXISTS updateexperiment(eid integer, expname text, expcode text, projectid integer, platformid integer, manifestid integer, datafile text, createdby integer, createddate date, modifiedby integer, modifieddate date, expstatus integer);

CREATE OR REPLACE FUNCTION updateexperiment(pid integer, pname text, pcode text, pprojectid integer, pvendorprotocolid integer, pmanifestid integer, pdatafile text, pcreatedby integer, pcreateddate date, pmodifiedby integer, pmodifieddate date, pstatus integer)
  RETURNS integer
  LANGUAGE plpgsql
 AS $function$
 	DECLARE
        i integer;
     BEGIN
     update experiment set name=pname, code=pcode, project_id=pprojectid, manifest_id=pmanifestid, data_file=pdatafile,
       created_by=pcreatedby, created_date=pcreateddate, modified_by=pmodifiedby, modified_date=pmodifieddate, status=pstatus, vendor_protocol_id=pvendorprotocolid 
       where experiment_id = pId;
     GET DIAGNOSTICS i = ROW_COUNT;
     return i;
     END;
 $function$;

--changeset kpalis:add_marker_to_vendor_protocol_link context:general splitStatements:false
ALTER TABLE marker ADD COLUMN dataset_vendor_protocol jsonb DEFAULT '{}'::jsonb;
COMMENT ON COLUMN marker.dataset_vendor_protocol IS 'Key-value-pair JSONB that stores the vendor_protocol ID for each marker-dataset combination.';
CREATE INDEX idx_marker_datasetvendorprotocol ON marker ( dataset_vendor_protocol );

--NOTE: The column dataset_vendor_protocol is set (upserted) during bulk loading via the IFLs so the current CRUD functions of marker doesn't need to be changed. But for convenience of testing, I'm adding the function below that upserts to this column one at a time.

CREATE OR REPLACE FUNCTION upsertDatasetVendorProtocol(pid integer, pdatasetid integer, pvendorprotocolid integer)
RETURNS integer AS $$
    DECLARE
        i integer;
    BEGIN
	    update marker m set dataset_vendor_protocol = dataset_vendor_protocol || ('{"'||pdatasetid||'": "'||pvendorprotocolid||'"}')::jsonb
		where m.marker_id = pid;
	    GET DIAGNOSTICS i = ROW_COUNT;
	    return i;
    END;
$$ LANGUAGE plpgsql;

