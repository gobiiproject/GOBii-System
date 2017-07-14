--liquibase formatted sql
/*
	These are the needed functions which will be used by the MDE includeChrLen (-l) feature.
*/
--changeset kpalis:add_lg_id_to_mlp_view context:general splitStatements:false
DROP VIEW IF EXISTS v_marker_linkage_physical;
CREATE OR REPLACE VIEW v_marker_linkage_physical AS
  select mlg.marker_id, lg.linkage_group_id, lg.name as linkage_group_name, lg.start as linkage_group_start, lg.stop as linkage_group_stop, mlg.start, mlg.stop, ms.name as mapset_name, lg.map_id
  from marker_linkage_group mlg, linkage_group lg, mapset ms
  where mlg.linkage_group_id = lg.linkage_group_id
  and lg.map_id = ms.mapset_id;


--changeset kpalis:add_getAllChrLenByDataset_fxn context:general splitStatements:false
DROP FUNCTION IF EXISTS getAllChrLenByDataset(integer);
CREATE OR REPLACE FUNCTION getAllChrLenByDataset(datasetId integer)
RETURNS table (chr_name varchar, length integer) AS $$
  BEGIN
    return query
    select distinct mlp.linkage_group_name, mlp.linkage_group_stop::integer
    from marker m
    left join v_marker_linkage_physical mlp on m.marker_id = mlp.marker_id
    where m.dataset_marker_idx ? datasetId::text;
  END;
$$ LANGUAGE plpgsql;

--changeset kpalis:add_getAllChrLenByDatasetAndMap_fxn context:general splitStatements:false
DROP FUNCTION IF EXISTS getAllChrLenByDatasetAndMap(datasetId integer, mapId integer);
CREATE OR REPLACE FUNCTION getAllChrLenByDatasetAndMap(datasetId integer, mapId integer)
RETURNS table (chr_name varchar, length integer) AS $$
  BEGIN
  return query
  select distinct mlp.linkage_group_name, mlp.linkage_group_stop::integer
  from marker m
  left join v_marker_linkage_physical mlp on m.marker_id = mlp.marker_id
  where m.dataset_marker_idx ? datasetId::text
  and mlp.map_id=mapId;
  END;
$$ LANGUAGE plpgsql;

