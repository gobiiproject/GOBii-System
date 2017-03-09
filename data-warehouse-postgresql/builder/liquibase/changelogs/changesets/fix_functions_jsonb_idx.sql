--liquibase formatted sql
/*
	As a result of removing the dataset_marker and dataset_dnarun tables and replacing them with jsonb columns in the 
	main entities instead, these are the necessary adjustments/replacements/deletions in functions.
*/
--changeset kpalis:getMinimalMarkerMetadataByDataset_fix context:general splitStatements:false
DROP FUNCTION IF EXISTS getMinimalMarkerMetadataByDataset(integer);
CREATE OR REPLACE FUNCTION getMinimalMarkerMetadataByDataset(datasetId integer)
RETURNS table (marker_name text, alleles text, chrom varchar, pos numeric, strand text) AS $$
  BEGIN
    return query
    select m.name as marker_name, m.ref || '/' || array_to_string(m.alts, ',', '?') as alleles, mlp.linkage_group_name as chrom, mlp.stop as pos, cv.term as strand
    from marker m
    left join v_marker_linkage_physical mlp on m.marker_id = mlp.marker_id
    left join cv on m.strand_id = cv.cv_id
    where m.dataset_marker_idx ? datasetId::text
	order by m.dataset_marker_idx->datasetId::text;
  END;
$$ LANGUAGE plpgsql;

--changeset kpalis:getAllMarkerMetadataByDataset_fix context:general splitStatements:false
DROP FUNCTION IF EXISTS getAllMarkerMetadataByDataset(integer);
CREATE OR REPLACE FUNCTION getAllMarkerMetadataByDataset(datasetId integer)
RETURNS table (marker_name text, linkage_group_name varchar, start numeric, stop numeric, mapset_name text, platform_name text, variant_id integer, code text, ref text, alts text, sequence text, reference_name text, primers jsonb, probsets jsonb, strand_name text) AS $$
  BEGIN
    return query
    select m.name as marker_name, mlp.linkage_group_name, mlp.start, mlp.stop, mlp.mapset_name, p.name as platform_name, m.variant_id, m.code, m.ref, array_to_string(m.alts, ',', '?'), m.sequence, r.name as reference_name, m.primers, m.probsets, cv.term as strand_name
	from marker m inner join platform p on m.platform_id = p.platform_id
	left join reference r on m.reference_id = r.reference_id
	left join cv on m.strand_id = cv.cv_id 
	left join v_marker_linkage_physical mlp on m.marker_id = mlp.marker_id
	where m.dataset_marker_idx ? datasetId::text
	order by m.dataset_marker_idx->datasetId::text;
  END;
$$ LANGUAGE plpgsql;

--changeset kpalis:getMinimalSampleMetadataByDataset_fix context:general splitStatements:false
DROP FUNCTION IF EXISTS getMinimalSampleMetadataByDataset(integer);
CREATE OR REPLACE FUNCTION getMinimalSampleMetadataByDataset(datasetId integer)
RETURNS table (dnarun_name text, sample_name text, germplasm_name text, external_code text, germplasm_type text, species text, platename text, num text, well_row text, well_col text) AS $$
  BEGIN
	return query
	select dr.name as dnarun_name, ds.name as sample_name, g.name as germplasm_name, g.external_code, c1.term as germplasm_type, c2.term as species, ds.platename, ds.num, ds.well_row, ds.well_col
	from dnarun dr
	inner join dnasample ds on dr.dnasample_id = ds.dnasample_id 
	inner join germplasm g on ds.germplasm_id = g.germplasm_id 
	left join cv as c1 on g.type_id = c1.cv_id 
	left join cv as c2 on g.species_id = c2.cv_id
	where dr.dataset_dnarun_idx ? datasetId::text
	order by dr.dataset_dnarun_idx->datasetId::text;
  END;
$$ LANGUAGE plpgsql;

--changeset kpalis:getAllSampleMetadataByDataset_fix context:general splitStatements:false
DROP FUNCTION IF EXISTS getAllSampleMetadataByDataset(integer);
CREATE OR REPLACE FUNCTION getAllSampleMetadataByDataset(datasetId integer)
RETURNS table (dnarun_name text, sample_name text, germplasm_name text, external_code text, germplasm_type text, species text, platename text, num text, well_row text, well_col text) AS $$
  BEGIN
	return query
	select dr.name as dnarun_name, ds.name as sample_name, g.name as germplasm_name, g.external_code, c1.term as germplasm_type, c2.term as species, ds.platename, ds.num, ds.well_row, ds.well_col
	from dnarun dr
	inner join dnasample ds on dr.dnasample_id = ds.dnasample_id 
	inner join germplasm g on ds.germplasm_id = g.germplasm_id 
	left join cv as c1 on g.type_id = c1.cv_id 
	left join cv as c2 on g.species_id = c2.cv_id
	where dr.dataset_dnarun_idx ? datasetId::text
	order by dr.dataset_dnarun_idx->datasetId::text;
  END;
$$ LANGUAGE plpgsql;

--changeset kpalis:getMarkerNamesByDataset_fix context:general splitStatements:false
DROP FUNCTION IF EXISTS getMarkerNamesByDataset(integer);
CREATE OR REPLACE FUNCTION getMarkerNamesByDataset(datasetId integer)
RETURNS table (marker_id integer, marker_name text) AS $$
  BEGIN
    return query
    select m.marker_id, m.name as marker_name
	from marker m
	where m.dataset_marker_idx ? datasetId::text
	order by m.dataset_marker_idx->datasetId::text;
  END;
$$ LANGUAGE plpgsql;

--changeset kpalis:getDnarunNamesByDataset_fix context:general splitStatements:false
DROP FUNCTION IF EXISTS getDnarunNamesByDataset(integer);
CREATE OR REPLACE FUNCTION getDnarunNamesByDataset(datasetId integer)
RETURNS table (dnarun_id integer, dnarun_name text) AS $$
  BEGIN
    return query
    select  dr.dnarun_id, dr.name as dnarun_name 
	from dnarun dr
	where dr.dataset_dnarun_idx ? datasetId::text
	order by dr.dataset_dnarun_idx->datasetId::text;
  END;
$$ LANGUAGE plpgsql;

--changeset kpalis:dropDatasetDnarunFxns context:general splitStatements:false
DROP FUNCTION IF EXISTS createDatasetDnaRun(integer, integer, integer, OUT integer);
DROP FUNCTION IF EXISTS updateDatasetDnaRun(integer, integer, integer, integer);
DROP FUNCTION IF EXISTS deleteDatasetDnaRun(integer);

--changeset kpalis:dropDatasetMarkerFxns context:general splitStatements:false tag:version_1.1
DROP FUNCTION IF EXISTS createDatasetMarker(integer, integer, real, real, real, integer, OUT integer);
DROP FUNCTION IF EXISTS updateDatasetMarker(integer, integer, integer, real, real, real, integer);
DROP FUNCTION IF EXISTS deleteDatasetMarker(integer);
