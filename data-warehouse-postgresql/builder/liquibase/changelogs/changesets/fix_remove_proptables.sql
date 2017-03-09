--liquibase formatted sql
/*
Issue #: GP1-686
Liquibase changeset: Add props column of jsonb datatype to main entity tables and drop corresponding *_prop tables
*/

--changeset venice.juanillas:fix_dnasample_addprops context:general splitStatements:false
ALTER TABLE dnasample ADD COLUMN props jsonb default '{}'::jsonb;
CREATE INDEX idx_dnasample_props ON dnasample USING gin(props);

--changeset raza:move_dnasample_prop_data context:general splitStatements:false
UPDATE  dnasample ds set props = dsp.props
from  dnasample_prop dsp
where ds.dnasample_id = dsp.dnasample_id;


--changeset venice.juanillas:fix_dnasampleprop_droptable context:general splitStatements:false
DROP TABLE dnasample_prop CASCADE;

--changeset venice.juanillas:fix_platform_addprops context:general splitStatements:false
ALTER TABLE platform ADD COLUMN props jsonb default '{}'::jsonb;
CREATE INDEX idx_platform_props ON platform USING gin (props);

--changeset raza:move_platform_prop_data context:general splitStatements:false
UPDATE  platform p set props = pp.props
from  platform_prop pp
where p.platform_id = pp.platform_id;

--changeset venice.juanillas:fix_platform_droptable context:general splitStatements:false
DROP TABLE platform_prop CASCADE;

--changeset venice.juanillas:fix_project_addprops context:general splitStatements:false
ALTER TABLE project ADD COLUMN props jsonb default '{}'::jsonb;
CREATE INDEX idx_project_props ON project USING gin (props);

--changeset raza:move_project_prop_data context:general splitStatements:false
UPDATE  project p set props = pp.props
from  project_prop pp
where p.project_id = pp.project_id;

--changeset venice.juanillas:fix_projectprop_droptable context:general splitStatements:false
DROP TABLE project_prop CASCADE;

--changeset venice.juanillas:fix_germplasm_addprops context:general splitStatements:false
ALTER TABLE germplasm ADD COLUMN props jsonb default '{}'::jsonb;
CREATE INDEX idx_germplasm_props ON germplasm USING gin (props);

--changeset raza:move_germplasm_prop_data context:general splitStatements:false
UPDATE  germplasm g set props = p.props
from  germplasm_prop p
where g.germplasm_id = p.germplasm_id;

--changeset venice.juanillas:fix_germplasmprop_droptable context:general splitStatements:false
DROP TABLE germplasm_prop CASCADE;

--changeset venice.juanillas:fix_mapset_addprops context:general splitStatements:false
ALTER TABLE mapset ADD COLUMN props jsonb default '{}'::jsonb;
CREATE INDEX idx_mapset_props ON mapset USING gin (props);

--changeset raza:move_mapset_prop_data context:general splitStatements:false
UPDATE  mapset mp set props = p.props
from  map_prop p
where mp.mapset_id = p.map_id;

--changeset venice.juanillas:fix_maprop_droptable context:general splitStatements:false
DROP TABLE map_prop CASCADE;

--changeset venice.juanillas:fix_marker_addprops context:general splitStatements:false
ALTER TABLE marker ADD COLUMN props jsonb default '{}'::jsonb;
CREATE INDEX idx_marker_props ON marker USING gin (props);

--changeset raza:move_marker_prop_data context:general splitStatements:false
UPDATE  marker m set props = p.props
from  marker_prop p
where m.marker_id = p.marker_id;

--changeset venice.juanillas:fix_markerprop_droptable context:general splitStatements:false
DROP TABLE marker_prop CASCADE;

--changeset venice.juanillas:fix_dnarun_addprops context:general splitStatements:false
ALTER TABLE dnarun ADD COLUMN props jsonb default '{}'::jsonb;
CREATE INDEX idx_dnarun_props ON dnarun USING gin (props);

--changeset raza:move_dnarun_data context:general splitStatements:false
UPDATE  dnarun dr set props = p.props
from  dnarun_prop p
where dr.dnarun_id = p.dnarun_id;

--changeset venice.juanillas:fix_dnarunprop_droptable context:general splitStatements:false
DROP TABLE dnarun_prop CASCADE;

