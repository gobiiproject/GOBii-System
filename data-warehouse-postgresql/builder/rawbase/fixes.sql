
/* 
  Set a default value to the status column for every table that has it
  	select 'ALTER TABLE ' || table_name || ' ALTER COLUMN ' || column_name || ' SET DEFAULT 1;'
	from information_schema.columns
	where column_name = 'status';
*/
--Column additons/modifications
ALTER TABLE germplasm ALTER COLUMN status SET DEFAULT 1;
ALTER TABLE marker_group ALTER COLUMN status SET DEFAULT 1;
ALTER TABLE platform ALTER COLUMN status SET DEFAULT 1;
ALTER TABLE project ALTER COLUMN status SET DEFAULT 1;
ALTER TABLE analysis ALTER COLUMN status SET DEFAULT 1;
ALTER TABLE dnasample ALTER COLUMN status SET DEFAULT 1;
ALTER TABLE experiment ALTER COLUMN status SET DEFAULT 1;
ALTER TABLE mapset ALTER COLUMN status SET DEFAULT 1;
ALTER TABLE marker ALTER COLUMN status SET DEFAULT 1;
ALTER TABLE dataset ALTER COLUMN status SET DEFAULT 1;
ALTER TABLE mapset ALTER COLUMN status SET DEFAULT 1;
ALTER TABLE linkage_group ALTER COLUMN stop SET DEFAULT 0;

ALTER TABLE dataset_marker ADD COLUMN marker_idx integer;
ALTER TABLE dataset_dnarun ADD COLUMN dnarun_idx integer;
ALTER TABLE display add column rank integer;
ALTER TABLE dataset ADD COLUMN type_id integer;
ALTER TABLE dataset ADD COLUMN name text;
/*
  Some tables are not consistent on the column type of created_by and modified_by.
  The following commands will fix that.
*/
 
ALTER TABLE dataset ALTER COLUMN created_by type integer using created_by::integer;
ALTER TABLE dataset ALTER COLUMN modified_by type integer using modified_by::integer;

ALTER TABLE marker_group ALTER COLUMN created_by type integer using created_by::integer;
ALTER TABLE marker_group ALTER COLUMN modified_by type integer using modified_by::integer;

ALTER TABLE platform ALTER COLUMN created_by type integer using created_by::integer;
ALTER TABLE platform ALTER COLUMN modified_by type integer using modified_by::integer;

--to support genetic maps
ALTER TABLE marker_linkage_group ALTER COLUMN start type DECIMAL(13,3);
ALTER TABLE marker_linkage_group ALTER COLUMN stop type DECIMAL(13,3);
--because we can't convert text[] to jsonb
--ALTER TABLE marker DROP COLUMN probsets;
--ALTER TABLE marker ADD COLUMN probsets jsonb;

--Constraint additions/modifications
--compound-unique constraint on the project table for pi contact (i.e., principle investigator user id) and project name.
ALTER TABLE project ADD CONSTRAINT pi_project_name_key UNIQUE (pi_contact, name);
ALTER TABLE cv ADD CONSTRAINT group_term_key UNIQUE ("group", term);

-- compound unique: in the experiment table: name, projectid,platformid
ALTER TABLE experiment ADD CONSTRAINT name_project_id_platform_id_key UNIQUE (name, project_id, platform_id);

--unique
ALTER TABLE contact ADD CONSTRAINT email_key UNIQUE (email);

ALTER TABLE experiment ALTER COLUMN platform_id SET NOT NULL;
ALTER TABLE dataset ADD CONSTRAINT dataset_fk3 FOREIGN KEY (type_id) REFERENCES cv(cv_id)

--drop constraints
ALTER TABLE marker ALTER COLUMN code DROP NOT NULL;
ALTER TABLE variant ALTER COLUMN code DROP NOT NULL;
ALTER TABLE germplasm ALTER COLUMN code DROP NOT NULL;
ALTER TABLE dnasample ALTER COLUMN code DROP NOT NULL;
ALTER TABLE dnarun ALTER COLUMN code DROP NOT NULL;

--enable foreign data wrapper
CREATE EXTENSION file_fdw;
--create the foreign server for IFLs to work
CREATE SERVER idatafilesrvr FOREIGN DATA WRAPPER file_fdw; 

--drop all not null constraints of created and modified columns
ALTER TABLE marker_group RENAME COLUMN create_date TO created_date;
--select 'ALTER TABLE ' || table_name || ' ALTER COLUMN modified_by DROP NOT NULL;' from information_schema.columns where column_name = 'modified_by';
ALTER TABLE contact ALTER COLUMN modified_by DROP NOT NULL;
ALTER TABLE dataset ALTER COLUMN modified_by DROP NOT NULL;
ALTER TABLE display ALTER COLUMN modified_by DROP NOT NULL;
ALTER TABLE dnasample ALTER COLUMN modified_by DROP NOT NULL;
ALTER TABLE experiment ALTER COLUMN modified_by DROP NOT NULL;
ALTER TABLE germplasm ALTER COLUMN modified_by DROP NOT NULL;
ALTER TABLE manifest ALTER COLUMN modified_by DROP NOT NULL;
ALTER TABLE mapset ALTER COLUMN modified_by DROP NOT NULL;
ALTER TABLE marker_group ALTER COLUMN modified_by DROP NOT NULL;
ALTER TABLE platform ALTER COLUMN modified_by DROP NOT NULL;
ALTER TABLE project ALTER COLUMN modified_by DROP NOT NULL;
ALTER TABLE variant ALTER COLUMN modified_by DROP NOT NULL;
--select 'ALTER TABLE ' || table_name || ' ALTER COLUMN created_by DROP NOT NULL;' from information_schema.columns where column_name = 'created_by';
ALTER TABLE contact ALTER COLUMN created_by DROP NOT NULL;
ALTER TABLE dataset ALTER COLUMN created_by DROP NOT NULL;
ALTER TABLE display ALTER COLUMN created_by DROP NOT NULL;
ALTER TABLE dnasample ALTER COLUMN created_by DROP NOT NULL;
ALTER TABLE experiment ALTER COLUMN created_by DROP NOT NULL;
ALTER TABLE germplasm ALTER COLUMN created_by DROP NOT NULL;
ALTER TABLE manifest ALTER COLUMN created_by DROP NOT NULL;
ALTER TABLE mapset ALTER COLUMN created_by DROP NOT NULL;
ALTER TABLE marker_group ALTER COLUMN created_by DROP NOT NULL;
ALTER TABLE platform ALTER COLUMN created_by DROP NOT NULL;
ALTER TABLE project ALTER COLUMN created_by DROP NOT NULL;
ALTER TABLE variant ALTER COLUMN created_by DROP NOT NULL;
--select 'ALTER TABLE ' || table_name || ' ALTER COLUMN modified_date DROP NOT NULL;' from information_schema.columns where column_name = 'modified_date';
ALTER TABLE contact ALTER COLUMN modified_date DROP NOT NULL;
ALTER TABLE dataset ALTER COLUMN modified_date DROP NOT NULL;
ALTER TABLE display ALTER COLUMN modified_date DROP NOT NULL;
ALTER TABLE dnasample ALTER COLUMN modified_date DROP NOT NULL;
ALTER TABLE experiment ALTER COLUMN modified_date DROP NOT NULL;
ALTER TABLE germplasm ALTER COLUMN modified_date DROP NOT NULL;
ALTER TABLE manifest ALTER COLUMN modified_date DROP NOT NULL;
ALTER TABLE mapset ALTER COLUMN modified_date DROP NOT NULL;
ALTER TABLE marker_group ALTER COLUMN modified_date DROP NOT NULL;
ALTER TABLE platform ALTER COLUMN modified_date DROP NOT NULL;
ALTER TABLE project ALTER COLUMN modified_date DROP NOT NULL;
ALTER TABLE variant ALTER COLUMN modified_date DROP NOT NULL;
--select 'ALTER TABLE ' || table_name || ' ALTER COLUMN created_date DROP NOT NULL;' from information_schema.columns where column_name = 'created_date';
ALTER TABLE contact ALTER COLUMN created_date DROP NOT NULL;
ALTER TABLE dataset ALTER COLUMN created_date DROP NOT NULL;
ALTER TABLE display ALTER COLUMN created_date DROP NOT NULL;
ALTER TABLE dnasample ALTER COLUMN created_date DROP NOT NULL;
ALTER TABLE experiment ALTER COLUMN created_date DROP NOT NULL;
ALTER TABLE germplasm ALTER COLUMN created_date DROP NOT NULL;
ALTER TABLE manifest ALTER COLUMN created_date DROP NOT NULL;
ALTER TABLE mapset ALTER COLUMN created_date DROP NOT NULL;
ALTER TABLE marker_group ALTER COLUMN created_date DROP NOT NULL;
ALTER TABLE platform ALTER COLUMN created_date DROP NOT NULL;
ALTER TABLE project ALTER COLUMN created_date DROP NOT NULL;
ALTER TABLE variant ALTER COLUMN created_date DROP NOT NULL;

ALTER TABLE dataset ALTER COLUMN data_table DROP NOT NULL;
ALTER TABLE dataset ALTER COLUMN data_file DROP NOT NULL;

ALTER TABLE platform ALTER COLUMN vendor_id DROP NOT NULL;
ALTER TABLE germplasm ALTER COLUMN species_id DROP NOT NULL;

ALTER TABLE marker ALTER COLUMN ref DROP NOT NULL;
ALTER TABLE marker ALTER COLUMN alts DROP NOT NULL;
ALTER TABLE germplasm ALTER COLUMN name DROP NOT NULL;
--adding new table: Organization
CREATE TABLE organization (
    organization_id serial PRIMARY KEY NOT NULL,
    name text NOT NULL,
    address text,
    website text,
    created_by integer,
    created_date date DEFAULT ('now'::text)::date,
    modified_by integer,
    modified_date date DEFAULT ('now'::text)::date,
    status integer NOT NULL DEFAULT 1
);

ALTER TABLE organization ADD CONSTRAINT organization_name_key UNIQUE (name);

ALTER TABLE contact ADD COLUMN organization_id integer;
ALTER TABLE contact ADD CONSTRAINT fk_organization_contact FOREIGN KEY (organization_id) REFERENCES organization(organization_id);

#temp - for the lack of proper organization of CV terms, and the lack of a proper DB revision control system
UPDATE CV SET term = 'nucleotide_2_letter' WHERE CV_ID = 93;
UPDATE CV SET term = 'dominant_non_nucleotide' WHERE CV_ID = 95;
UPDATE CV SET term = 'codominant_non_nucleotide' WHERE CV_ID = 96;

select * from createRole('User', 'User', NULL, NULL);

