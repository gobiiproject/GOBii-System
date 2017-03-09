--liquibase formatted sql

--changeset kpalis:add_map_id_to_mlp_view context:general splitStatements:false
CREATE OR REPLACE VIEW v_marker_linkage_physical AS
	select mlg.marker_id, lg.name as linkage_group_name, mlg.start, mlg.stop, ms.name as mapset_name, lg.map_id
	from marker_linkage_group mlg, linkage_group lg, mapset ms
	where mlg.linkage_group_id = lg.linkage_group_id
	and lg.map_id = ms.mapset_id;

--changeset kpalis:add_getmin_by_dataset_and_map_fxn context:general splitStatements:false
CREATE OR REPLACE FUNCTION getMinimalMarkerMetadataByDatasetAndMap(datasetId integer, mapId integer)
RETURNS table (marker_name text, alleles text, chrom varchar, pos numeric, strand text) AS $$
  BEGIN
	return query
	select m.name as marker_name, m.ref || '/' || array_to_string(m.alts, ',', '?') as alleles, mlp.linkage_group_name as chrom, mlp.stop as pos, cv.term as strand
	from marker m
	left join v_marker_linkage_physical mlp on m.marker_id = mlp.marker_id
	left join cv on m.strand_id = cv.cv_id
	where m.dataset_marker_idx ? datasetId::text
	and mlp.map_id=mapId
	order by m.dataset_marker_idx->datasetId::text;
  END;
$$ LANGUAGE plpgsql;

--changeset kpalis:add_getall_by_dataset_and_map_fxn context:general splitStatements:false
CREATE OR REPLACE FUNCTION getAllMarkerMetadataByDatasetAndMap(datasetId integer, mapId integer)
RETURNS table (marker_name text, linkage_group_name varchar, start numeric, stop numeric, mapset_name text, platform_name text, variant_id integer, code text, ref text, alts text, sequence text, reference_name text, primers jsonb, probsets jsonb, strand_name text) AS $$
  BEGIN
    return query
    select m.name as marker_name, mlp.linkage_group_name, mlp.start, mlp.stop, mlp.mapset_name, p.name as platform_name, m.variant_id, m.code, m.ref, array_to_string(m.alts, ',', '?'), m.sequence, r.name as reference_name, m.primers, m.probsets, cv.term as strand_name
	from marker m inner join platform p on m.platform_id = p.platform_id
	left join reference r on m.reference_id = r.reference_id
	left join cv on m.strand_id = cv.cv_id 
	left join v_marker_linkage_physical mlp on m.marker_id = mlp.marker_id
	where m.dataset_marker_idx ? datasetId::text
	and mlp.map_id=mapId
	order by m.dataset_marker_idx->datasetId::text;
  END;
$$ LANGUAGE plpgsql;

--changeset kpalis:add_getMarkerNamesByDatasetAndMap_fxn context:general splitStatements:false
CREATE OR REPLACE FUNCTION getMarkerNamesByDatasetAndMap(datasetId integer, mapId integer)
RETURNS table (marker_id integer, marker_name text) AS $$
  BEGIN
    return query
    select m.marker_id, m.name as marker_name
	from marker m
	left join v_marker_linkage_physical mlp on m.marker_id = mlp.marker_id
	where m.dataset_marker_idx ? datasetId::text
	and mlp.map_id=mapId
	order by m.dataset_marker_idx->datasetId::text;
  END;
$$ LANGUAGE plpgsql;