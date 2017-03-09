--liquibase formatted sql

--changeset venice.juanillas:alterTableAnalysis context:general splitStatements:false
ALTER TABLE analysis ADD created_by integer;
ALTER TABLE analysis ADD created_date date default('now'::text)::date;
ALTER TABLE analysis ADD modified_by integer;
ALTER TABLE analysis ADD modified_date date default('now'::text)::date;

--changeset venice.juanillas:alterTableLinkageGroup context:general splitStatements:false
ALTER TABLE linkage_group ADD created_by integer;
ALTER TABLE linkage_group ADD created_date date default('now'::text)::date;
ALTER TABLE linkage_group ADD modified_by integer;
ALTER TABLE linkage_group ADD modified_date date default('now'::text)::date;

--changeset venice.juanillas:alterTableReference context:general splitStatements:false
ALTER TABLE reference ADD created_by integer;
ALTER TABLE reference ADD created_date date default('now'::text)::date;
ALTER TABLE reference ADD modified_by integer;
ALTER TABLE reference ADD modified_date date default('now'::text)::date;

/*
--not sure whether we need this... but just in case we need to link created_by and modified_by columns to contact person
--run after fixReference.sql and fixInconsistentTables.sql
*/

--changeset venice.juanillas:addFK_Reference context:general splitStatements:false
ALTER TABLE reference ADD CONSTRAINT reference_created_by_fkey FOREIGN KEY (created_by) REFERENCES contact (contact_id) ON UPDATE restrict ON DELETE cascade;
ALTER TABLE reference ADD CONSTRAINT reference_modified_by_fkey FOREIGN KEY (modified_by) REFERENCES contact (contact_id) ON UPDATE restrict ON DELETE cascade;

--changeset venice.juanillas:addFK_Analysis context:general splitStatements:false
ALTER TABLE analysis ADD CONSTRAINT analysis_created_by_fkey FOREIGN KEY (created_by) REFERENCES contact (contact_id) ON UPDATE restrict ON DELETE cascade;
ALTER TABLE analysis ADD CONSTRAINT analysis_modified_by_fkey FOREIGN KEY (modified_by) REFERENCES contact (contact_id) ON UPDATE restrict ON DELETE cascade;

--changeset venice.juanillas:addFK_LinkageGroup context:general splitStatements:false
ALTER TABLE linkage_group ADD CONSTRAINT linkage_group_created_by_fkey FOREIGN KEY (created_by) REFERENCES contact (contact_id) ON UPDATE restrict ON DELETE cascade;
ALTER TABLE linkage_group ADD CONSTRAINT linkage_group_modified_by_fkey FOREIGN KEY (modified_by) REFERENCES contact (contact_id) ON UPDATE restrict ON DELETE cascade;

