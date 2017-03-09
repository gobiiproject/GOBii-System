--liquibase formatted sql

--changeset kpalis:remove_reference_cascading_fks context:general splitStatements:false
ALTER TABLE reference DROP CONSTRAINT reference_created_by_fkey;
ALTER TABLE reference DROP CONSTRAINT reference_modified_by_fkey;

--changeset kpalis:remove_analysis_cascading_fks context:general splitStatements:false
ALTER TABLE analysis DROP CONSTRAINT analysis_created_by_fkey;
ALTER TABLE analysis DROP CONSTRAINT analysis_modified_by_fkey;

--changeset kpalis:remove_linkage_group_cascading_fks context:general splitStatements:false
ALTER TABLE linkage_group DROP CONSTRAINT linkage_group_created_by_fkey;
ALTER TABLE linkage_group DROP CONSTRAINT linkage_group_modified_by_fkey;

