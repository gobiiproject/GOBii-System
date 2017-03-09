--liquibase formatted sql

--changeset venice.juanillas:alterConstraint_fk_project_contact  context:general splitStatements:false
ALTER TABLE IF EXISTS project RENAME CONSTRAINT fk_project_contact TO project_pi_contact;

--changeset venice.juanillas:alterConstraint_dataset_fk1  context:general splitStatements:false
ALTER TABLE IF EXISTS dataset RENAME CONSTRAINT dataset_fk1 TO dataset_experiment_id_fkey;

--changeset venice.juanillas:alterConstraint_dataset_fk2  context:general splitStatements:false
ALTER TABLE IF EXISTS dataset RENAME CONSTRAINT	dataset_fk2 TO dataset_callinganalysis_id_fkey;

--changeset venice.juanillas:alterConstraint_dnarun_fk1  context:general splitStatements:false
ALTER TABLE IF EXISTS dnarun RENAME CONSTRAINT dnarun_fk1  TO dnarun_experiment_id_fkey;

--changeset venice.juanillas:alterConstraint_dnarun_fk2  context:general splitStatements:false
ALTER TABLE IF EXISTS dnarun RENAME CONSTRAINT	dnarun_fk2 TO dnarun_dnasample_id_fkey;

--changeset venice.juanillas:alterConstraint_dnasample_fk1  context:general splitStatements:false
ALTER TABLE IF EXISTS dnasample RENAME CONSTRAINT dnasample_fk1 TO dnasample_project_id_fkey;

--changeset venice.juanillas:alterConstraint_dnasample_fk2  context:general splitStatements:false
ALTER TABLE IF EXISTS dnasample RENAME CONSTRAINT dnasample_fk2 TO dnasample_germplasm_id_fkey;

--changeset venice.juanillas:alterConstraint_dnasample_prop_fk1  context:general splitStatements:false
ALTER TABLE IF EXISTS dnasample_prop RENAME CONSTRAINT	dnasample_prop_fk1 TO dnasample_prop_dnasample_id_fkey;

--changeset venice.juanillas:alterConstraint_experiment_fk1  context:general splitStatements:false
ALTER TABLE IF EXISTS experiment RENAME CONSTRAINT experiment_fk1 TO experiment_project_id_fkey;

--changeset venice.juanillas:alterConstraint_experiment_fk2  context:general splitStatements:false
ALTER TABLE IF EXISTS experiment RENAME CONSTRAINT experiment_fk2 TO experiment_platform_id_fkey;


--changeset venice.juanillas:alterConstraint_experiment_fk3  context:general splitStatements:false
ALTER TABLE IF EXISTS experiment RENAME CONSTRAINT experiment_fk3 TO experiment_manifest_id_fkey;

--changeset venice.juanillas:alterConstraint_fk_germplasm_species_id_cv  context:general splitStatements:false
ALTER TABLE IF EXISTS germplasm RENAME CONSTRAINT fk_germplasm_species_id_cv TO germplasm_species_id_fkey;

--changeset venice.juanillas:alterConstraint_fk_linkage_group_map  context:general splitStatements:false
ALTER TABLE IF EXISTS linkage_group RENAME CONSTRAINT fk_linkage_group_map TO linkage_group_map_id_fkey;

--changeset venice.juanillas:alterConstraint_fk_marker_cv  context:general splitStatements:false
ALTER TABLE IF EXISTS marker RENAME CONSTRAINT fk_marker_cv TO marker_strand_id_fkey;

--changeset venice.juanillas:alterConstraint_fk_marker_linkage_group  context:general splitStatements:false
ALTER TABLE IF EXISTS marker_linkage_group RENAME CONSTRAINT fk_marker_linkage_group TO marker_linkage_group_linkage_group_id_fkey;

--changeset venice.juanillas:alterConstraint_fk_marker_reference  context:general splitStatements:false
ALTER TABLE IF EXISTS marker RENAME CONSTRAINT fk_marker_reference TO marker_reference_id_fkey;

--changeset venice.juanillas:alterConstraint_fk_organization_contact  context:general splitStatements:false
ALTER TABLE IF EXISTS contact RENAME CONSTRAINT fk_organization_contact TO contact_organization_id_fkey;

--changeset venice.juanillas:alterConstraint_fk_platform_contact  context:general splitStatements:false
ALTER TABLE IF EXISTS platform RENAME CONSTRAINT fk_platform_contact TO platform_vendor_id_fkey;

--changeset venice.juanillas:alterConstraint_fk_platform_cv  context:general splitStatements:false
ALTER TABLE IF EXISTS platform RENAME CONSTRAINT fk_platform_cv TO platform_type_id_fkey;

--changeset venice.juanillas:alterConstraint_project_prop_fk1  context:general splitStatements:false
ALTER TABLE IF EXISTS project_prop RENAME CONSTRAINT project_prop_fk1 TO project_prop_project_id_fkey;

--changeset venice.juanillas:alterConstraint_marker_fk2 context:general splitStatements:false
ALTER TABLE IF EXISTS marker RENAME CONSTRAINT marker_fk2 TO marker_variant_id_fkey;

--changeset venice.juanillas:alterConstraint_marker_fk1 context:general splitStatements:false
ALTER TABLE IF EXISTS marker RENAME CONSTRAINT marker_fk1 TO marker_platform_id_fkey;

