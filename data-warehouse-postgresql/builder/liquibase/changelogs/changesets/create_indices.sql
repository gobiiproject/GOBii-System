--liquibase formatted sql

-- Indices on columns based from -map/-nmap for IFLs
--changeset raza:create_dnarun-name_index context:general
CREATE INDEX IF NOT EXISTS idx_dnarun_name on dnarun (name);

--changeset raza:create_marker-name_index context:general
CREATE INDEX IF NOT EXISTS idx_marker_name on marker (name);

--changeset raza:create_experiment-name_index context:general
CREATE INDEX IF NOT EXISTS idx_experiment_name on experiment (name);

--changeset raza:create_dnasample-name_index context:general
CREATE INDEX IF NOT EXISTS idx_dnasample_name on dnasample (name);

--changeset raza:create_dnasample-platename_index context:general
CREATE INDEX IF NOT EXISTS idx_dnasample_platename on dnasample (platename);

--changeset raza:create_dnasample-num_index context:general
CREATE INDEX IF NOT EXISTS idx_dnasample_num on dnasample (num);

--changeset raza:create_dnasample-rowcol_index context:general
CREATE INDEX IF NOT EXISTS idx_dnasample_wellrow_wellcol on dnasample (well_row, well_col);

--changeset raza:create_germplasm-name_index context:general
CREATE INDEX IF NOT EXISTS idx_germplasm_name on germplasm (name);

--changeset raza:create_germplasm-external_code_index context:general
CREATE INDEX IF NOT EXISTS idx_germplasm_external_code on germplasm (external_code);

--changeset raza:create_germplasm-type_id_index context:general
CREATE INDEX IF NOT EXISTS idx_germplasm_type_id on germplasm (type_id);

--changeset raza:create_project-name_index context:general
CREATE INDEX IF NOT EXISTS idx_project_name on project (name);

--marker_linkage_group-marker_id
--changeset raza:create_marker_linkage_group-marker_id_index context:general
CREATE INDEX IF NOT EXISTS idx_marker_linkage_group_marker_id on marker_linkage_group (marker_id);

--changeset raza:create_marker_linkage_group-start_index context:general
CREATE INDEX IF NOT EXISTS idx_marker_linkage_group_start on marker_linkage_group (start);

--changeset raza:create_marker_linkage_group-stop_index context:general
CREATE INDEX IF NOT EXISTS idx_marker_linkage_group_stop on marker_linkage_group (stop);

--changeset raza:create_linkage_group-name_index context:general
CREATE INDEX IF NOT EXISTS idx_linkage_group_name on linkage_group (name);

--changeset raza:create_linkage_group-map_id_index context:general
CREATE INDEX IF NOT EXISTS idx_linkage_group_map_id on linkage_group (map_id);

--changeset raza:create_analysis-type_id_index context:general
CREATE INDEX IF NOT EXISTS idx_analysis_type_id on analysis (type_id);

--changeset raza:create_role-role_name_index context:general
CREATE INDEX IF NOT EXISTS idx_role_role_name on role (role_name);

--changeset raza:create_experiment-project_id_index context:general
CREATE INDEX IF NOT EXISTS idx_experiment_project_id on experiment (project_id);

--changeset raza:create_dataset-experiment_id_index context:general
CREATE INDEX IF NOT EXISTS idx_dataset_experiment_id on dataset (experiment_id);

--changeset raza:create_display-table_name_index context:general
CREATE INDEX IF NOT EXISTS idx_display_table_name on display (lower(table_name));

--changeset kpalis:create_germplasm-status_index context:general
DROP INDEX IF EXISTS idx_germplasm;
CREATE INDEX IF NOT EXISTS idx_germplasm_status ON germplasm (status);

--changeset kpalis:create_germplasm-species_id_index context:general
DROP INDEX IF EXISTS idx_germplasm_0;
CREATE INDEX IF NOT EXISTS idx_germplasm_species_id ON germplasm (species_id);

--changeset kpalis:create_linkage_group-map_id_index context:general
DROP INDEX IF EXISTS idx_linkage_group;
CREATE INDEX IF NOT EXISTS idx_linkage_group_map_id ON linkage_group (map_id);

--changeset kpalis:create_marker-strand_id_index context:general
DROP INDEX IF EXISTS idx_marker;
CREATE INDEX IF NOT EXISTS idx_marker_strand_id ON marker (strand_id);

--changeset kpalis:create_marker-reference_id_index context:general
DROP INDEX IF EXISTS idx_marker_0;
CREATE INDEX IF NOT EXISTS idx_marker_reference_id ON marker (reference_id);

--changeset kpalis:create_marker_linkage_group-linkage_group_id_index context:general
DROP INDEX IF EXISTS idx_marker_map;
CREATE INDEX IF NOT EXISTS idx_marker_linkage_group_linkage_group_id ON marker_linkage_group (linkage_group_id);

--changeset kpalis:create_platform-vendor_id_index context:general
DROP INDEX IF EXISTS idx_platform;
CREATE INDEX IF NOT EXISTS idx_platform_vendor_id ON platform (vendor_id);

--changeset kpalis:create_platform-type_id_index context:general
DROP INDEX IF EXISTS idx_platform_0;
CREATE INDEX IF NOT EXISTS idx_platform_type_id ON platform (type_id);

--changeset kpalis:create_project-pi_contact_index context:general
DROP INDEX IF EXISTS idx_project;
CREATE INDEX IF NOT EXISTS idx_project_pi_contact ON project (pi_contact);