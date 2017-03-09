--liquibase formatted sql

--### CV Table Change and Migration ###---
--## CVGroup ##--
--changeset kpalis:create_cv_group_table context:general splitStatements:false
CREATE TABLE cvgroup ( 
  cvgroup_id           serial  NOT NULL,
  name                 text  NOT NULL,
  definition           text  ,
  type              integer  NOT NULL DEFAULT 1,
  props                jsonb DEFAULT '{}'::jsonb,
  CONSTRAINT cv_pkey PRIMARY KEY ( cvgroup_id ),
  CONSTRAINT unique_cvgroup_name_type UNIQUE ( name, type ) 
 );

CREATE INDEX idx_cvgroup ON cvgroup ( type );

COMMENT ON TABLE cvgroup IS 'A controlled vocabulary or ontology. A cv is
composed of cvterms (AKA terms, classes, types, universals - relations
and properties are also stored in cvterm) and the relationships
between them.';

COMMENT ON COLUMN cvgroup.name IS 'The name of the group.';

COMMENT ON COLUMN cvgroup.definition IS 'A text description of the criteria for membership of this group.';

COMMENT ON COLUMN cvgroup.type IS 'Determines if CV group is of type "System CV" (1) or "Custom CV" (2). More types can be added as needed';

--populate cvgroup with current distinct groups from seed data + a user created group
--for system cvs
INSERT INTO cvgroup (name, definition, type) values ('dataset_type', '', 1);
INSERT INTO cvgroup (name, definition, type) values ('germplasm_prop', '', 1);
INSERT INTO cvgroup (name, definition, type) values ('dnarun_prop', '', 1);
INSERT INTO cvgroup (name, definition, type) values ('status', '', 1);
INSERT INTO cvgroup (name, definition, type) values ('marker_strand', '', 1);
INSERT INTO cvgroup (name, definition, type) values ('germplasm_species', '', 1);
INSERT INTO cvgroup (name, definition, type) values ('marker_prop', '', 1);
INSERT INTO cvgroup (name, definition, type) values ('dnasample_prop', '', 1);
INSERT INTO cvgroup (name, definition, type) values ('platform_type', '', 1);
INSERT INTO cvgroup (name, definition, type) values ('analysis_type', '', 1);
INSERT INTO cvgroup (name, definition, type) values ('project_prop', '', 1);
INSERT INTO cvgroup (name, definition, type) values ('germplasm_type', '', 1);
INSERT INTO cvgroup (name, definition, type) values ('mapset_type', '', 1);
--for user-created cvs
INSERT INTO cvgroup (name, definition, type) values ('dataset_type', '', 2);
INSERT INTO cvgroup (name, definition, type) values ('germplasm_prop', '', 2);
INSERT INTO cvgroup (name, definition, type) values ('dnarun_prop', '', 2);
INSERT INTO cvgroup (name, definition, type) values ('status', '', 2);
INSERT INTO cvgroup (name, definition, type) values ('marker_strand', '', 2);
INSERT INTO cvgroup (name, definition, type) values ('germplasm_species', '', 2);
INSERT INTO cvgroup (name, definition, type) values ('marker_prop', '', 2);
INSERT INTO cvgroup (name, definition, type) values ('dnasample_prop', '', 2);
INSERT INTO cvgroup (name, definition, type) values ('platform_type', '', 2);
INSERT INTO cvgroup (name, definition, type) values ('analysis_type', '', 2);
INSERT INTO cvgroup (name, definition, type) values ('project_prop', '', 2);
INSERT INTO cvgroup (name, definition, type) values ('germplasm_type', '', 2);
INSERT INTO cvgroup (name, definition, type) values ('mapset_type', '', 2);

-----

--## CV ##--
--changeset kpalis:normalize_and_migrate_cv context:general splitStatements:false

ALTER TABLE cv ADD COLUMN cvgroup_id integer;
ALTER TABLE cv ADD CONSTRAINT unique_cvterm_term_cvgroupid UNIQUE ( term, cvgroup_id );
ALTER TABLE cv ADD CONSTRAINT cv_cvgroupid_fkey FOREIGN KEY ( cvgroup_id ) REFERENCES cvgroup( cvgroup_id );

--migrate data: group name -> group id
UPDATE cv SET cvgroup_id=(select cvgroup_id from cvgroup where name=cv."group" and type=1);

--set column to required
ALTER TABLE cv ALTER COLUMN cvgroup_id SET NOT NULL;

CREATE INDEX idx_cv_cvgroupid ON cv ( cvgroup_id );
--drop freetext group column
ALTER TABLE cv DROP COLUMN "group";

--## DBXREF ##--
CREATE TABLE dbxref ( 
  dbxref_id            serial  NOT NULL,
  accession            text  NOT NULL default '',
  ver                  text  ,
  description          text  ,
  db_name              text  ,
  url                  text  ,
  props                jsonb  DEFAULT '{}'::jsonb,
  CONSTRAINT dbxref_pkey PRIMARY KEY ( dbxref_id ),
  CONSTRAINT unique_dbxref_accession_version UNIQUE ( accession, ver ) 
 );

CREATE INDEX idx_dbxref_accession ON dbxref ( accession );

CREATE INDEX idx_dbxref_ver ON dbxref ( ver );

COMMENT ON TABLE dbxref IS 'A unique, global, public, stable identifier. Not necessarily an external reference - can reference data items inside the particular instance being used. Typically a row in a table can be uniquely identified with a primary identifier (called dbxref_id); a table may also have secondary identifiers (in a linking table <T>_dbxref). A dbxref is generally written as <DB>:<ACCESSION> or as <DB>:<ACCESSION>:<VERSION>.';

COMMENT ON COLUMN dbxref.accession IS 'The local part of the identifier. Guaranteed by the db authority to be unique for that db.';

COMMENT ON COLUMN dbxref.db_name IS 'source name, ex. EDAM Ontology
A database authority. Typical databases in
bioinformatics are FlyBase, GO, UniProt, NCBI, MGI, etc. The authority
is generally known by this shortened form, which is unique within the
bioinformatics and biomedical realm. ';


--## CV Extension ##--
--changeset kpalis:extend_cv_table context:general splitStatements:false
ALTER TABLE cv ADD COLUMN abbreviation text;
ALTER TABLE cv ADD COLUMN dbxref_id integer;
ALTER TABLE cv ADD COLUMN status integer DEFAULT 1 NOT NULL;
ALTER TABLE cv ADD COLUMN props jsonb DEFAULT '{}'::jsonb;

ALTER TABLE cv ADD CONSTRAINT cv_dbxrefid_fkey FOREIGN KEY ( dbxref_id ) REFERENCES dbxref( dbxref_id );
CREATE INDEX idx_cv_dbxrefid ON cv ( dbxref_id );
CREATE INDEX idx_cv_term ON cv ( term );
--changeset kpalis:add_doc_cv context:general splitStatements:false
COMMENT ON TABLE cv IS 'A term, class, universal or type within an
ontology or controlled vocabulary.  This table is also used for
relations and properties. cvterms constitute nodes in the graph
defined by the collection of cvterms and cvterm_relationships.';

COMMENT ON COLUMN cv.cvgroup_id IS 'The cv or ontology or namespace to which
this cvterm belongs.';

COMMENT ON COLUMN cv.term IS 'A concise human-readable name or
label for the cvterm. Uniquely identifies a cvterm within a cv.';

COMMENT ON COLUMN cv.definition IS 'A human-readable text
definition.';

COMMENT ON COLUMN cv.dbxref_id IS 'Primary identifier dbxref - The
unique global OBO identifier for this cvterm.  ';



--################################# FUNCTIONS ##########################################---
/* A summary of what changed:
*  - CV.group freetext column dropped and replaced by a normalized cvgroup table, then migrated data accordingly
*  - Added several columns to CV, mostly due to CIMMYT's request
*  - DBXREF table added and relationship to CV established via cv.dbxref_id
*/

--changeset kpalis:update_fxns_affected_by_cv_mods context:general splitStatements:false

DROP FUNCTION IF EXISTS createcv(cvgroup text, cvterm text, cvdefinition text, cvrank integer, OUT id integer);
CREATE OR REPLACE FUNCTION createcv(pcvgroupid integer, pcvterm text, pcvdefinition text, pcvrank integer, pabbreviation text, pdbxrefid integer, pstatus integer, OUT id integer)
  RETURNS integer
  LANGUAGE plpgsql
 AS $function$
   BEGIN
     insert into cv (cvgroup_id, term, definition, rank, abbreviation, dbxref_id, status)
       values (pcvgroupid, pcvterm, pcvdefinition, pcvrank, pabbreviation, pdbxrefid, pstatus);
     select lastval() into id;
   END;
 $function$;

DROP FUNCTION IF EXISTS updatecv(id integer, cvgroup text, cvterm text, cvdefinition text, cvrank integer);
CREATE OR REPLACE FUNCTION updatecv(pid integer, pcvgroupid integer, pcvterm text, pcvdefinition text, pcvrank integer, pabbreviation text, pdbxrefid integer, pstatus integer)
  RETURNS integer
  LANGUAGE plpgsql
 AS $function$
     DECLARE
        i integer;
     BEGIN
     update cv set cvgroup_id=pcvgroupid, term=pcvterm, definition=pcvdefinition, rank=pcvrank, abbreviation=pabbreviation, dbxref_id=pdbxrefid, status=pstatus
      where cv_id = pid;
     GET DIAGNOSTICS i = ROW_COUNT;
     return i;
     END;
 $function$;

 --new function to create CVs - Can be used to create both system CVs and userCVs via pgrouptype parameter.
 --Parameters to note: Pgroupname = same as before, simply the group name, ie. 'mapset_type'. Pgrouptype = 1 - system, 2 - custom/user
CREATE OR REPLACE FUNCTION createCVinGroup(pgroupname text, pgrouptype integer, pcvterm text, pcvdefinition text, pcvrank integer, pabbreviation text, pdbxrefid integer, pstatus integer, OUT id integer)
  RETURNS integer
  LANGUAGE plpgsql
 AS $function$
   DECLARE
    groupId integer;
   BEGIN
     select cvgroup_id into groupId from cvgroup where name=pgroupname and type=pgrouptype;
     insert into cv (cvgroup_id, term, definition, rank, abbreviation, dbxref_id, status)
       values (groupId, pcvterm, pcvdefinition, pcvrank, pabbreviation, pdbxrefid, pstatus);
     select lastval() into id;
   END;
 $function$;

--changeset kpalis:crud_fxns_for_cvgroup context:general splitStatements:false

CREATE OR REPLACE FUNCTION createcvgroup(pname text, pdefinition text, ptype integer, OUT id integer)
  RETURNS integer
  LANGUAGE plpgsql
 AS $function$
   BEGIN
     insert into cvgroup (name, definition, type)
       values (pname, pdefinition, ptype);
     select lastval() into id;
   END;
 $function$;

CREATE OR REPLACE FUNCTION updatecvgroup(pid integer, pname text, pdefinition text, ptype integer)
  RETURNS integer
  LANGUAGE plpgsql
 AS $function$
     DECLARE
        i integer;
     BEGIN
     update cvgroup set name=pname, definition=pdefinition, type=ptype
      where cvgroup_id = pid;
     GET DIAGNOSTICS i = ROW_COUNT;
     return i;
     END;
 $function$;

CREATE OR REPLACE FUNCTION deletecvgroup(id integer)
  RETURNS integer
  LANGUAGE plpgsql
 AS $function$
     DECLARE
      i integer;
     BEGIN
     delete from cvgroup where cvgroup_id = id;
     GET DIAGNOSTICS i = ROW_COUNT;
     return i;
     END;
 $function$


--changeset kpalis:crud_fxns_for_dbxref context:general splitStatements:false

CREATE OR REPLACE FUNCTION createdbxref(paccession text, pver text, pdescription text, pdbname text, purl text, OUT id integer)
  RETURNS integer
  LANGUAGE plpgsql
 AS $function$
   BEGIN
     insert into dbxref (accession, ver, description, db_name, url)
       values (paccession, pver, pdescription, pdbname, purl);
     select lastval() into id;
   END;
 $function$;

CREATE OR REPLACE FUNCTION updatedbxref(pid integer, paccession text, pver text, pdescription text, pdbname text, purl text)
  RETURNS integer
  LANGUAGE plpgsql
 AS $function$
     DECLARE
        i integer;
     BEGIN
     update dbxref set accession=paccession, ver=pver, description=pdescription, db_name=pdbname, url=purl
      where dbxref_id = pid;
     GET DIAGNOSTICS i = ROW_COUNT;
     return i;
     END;
 $function$;

CREATE OR REPLACE FUNCTION deletedbxref(id integer)
  RETURNS integer
  LANGUAGE plpgsql
 AS $function$
     DECLARE
      i integer;
     BEGIN
     delete from dbxref where dbxref_id = id;
     GET DIAGNOSTICS i = ROW_COUNT;
     return i;
     END;
 $function$


