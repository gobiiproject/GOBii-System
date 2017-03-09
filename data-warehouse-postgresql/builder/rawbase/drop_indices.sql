-- drop specifically created indices in create_indices.sql

--dnarun.name
DROP INDEX IF EXISTS idx_dnarun_name;

--marker.name
DROP INDEX IF EXISTS idx_marker_name;

--experiment.name
DROP INDEX IF EXISTS idx_experiment_name;

--dnasample.name
DROP INDEX IF EXISTS idx_dnasample_name ;

--dnasample.platename
DROP INDEX IF EXISTS  idx_dnasample_platename;

--dnasample.num
DROP INDEX IF EXISTS  idx_dnasample_num;

-- dnasample.well_row & dnasample.well_col
DROP INDEX IF EXISTS idx_dnasample_rowcol;


--germplasm.name
DROP INDEX IF EXISTS  idx_germplasm_name;

--germplasm.external_code
DROP INDEX IF EXISTS idx_germplasm_external_code;

--germplasm.type_id
DROP INDEX IF EXISTS  idx_germplasm_type_id;

--project.name
DROP INDEX IF EXISTS idx_project_name;

--marker_linkage_group.marker_id
DROP INDEX IF EXISTS idx_marker_linkage_group_mrk_id;

-- analysis.type_id
DROP INDEX IF EXISTS idx_analysis_type;

--role.role_name
DROP INDEX IF EXISTS  idx_role_name;

-- experiment.project_id
DROP INDEX IF EXISTS idx_experiment_proj_id;

--dataset.experiment_id
DROP INDEX IF EXISTS  idx_dataset_experiment_id;

--display.lower(table_name)
DROP INDEX IF EXISTS idx_display_table_name;
