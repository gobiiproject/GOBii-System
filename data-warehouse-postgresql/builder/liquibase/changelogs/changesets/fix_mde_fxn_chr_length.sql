--liquibase formatted sql

--changeset raza:fix_all_chrlen_by_dataset context:general splitStatements:false
DROP FUNCTION IF EXISTS getAllChrLenByDataset(integer);

CREATE OR REPLACE FUNCTION getAllChrLenByDataset(datasetId integer)
RETURNS table (linkage_group_name varchar, linkage_group_length integer) AS $$
  BEGIN
    return query
    select distinct mlp.linkage_group_name, (mlp.linkage_group_stop - mlp.linkage_group_stop)::integer
    from marker m
    left join v_marker_linkage_physical mlp on m.marker_id = mlp.marker_id
    where m.dataset_marker_idx ? datasetId::text;
  END;
$$ LANGUAGE plpgsql;

--changeset raza:fix_all_chrlen_by_dataset_map_id context:general splitStatements:false
DROP FUNCTION IF EXISTS getAllChrLenByDatasetAndMap(datasetId integer, mapId integer);

CREATE OR REPLACE FUNCTION getAllChrLenByDatasetAndMap(datasetId integer, mapId integer)
RETURNS table (linkage_group_name varchar, linkage_group_length integer) AS $$
  BEGIN
  return query
  select distinct mlp.linkage_group_name, (mlp.linkage_group_stop - mlp.linkage_group_stop)::integer
  from marker m
  left join v_marker_linkage_physical mlp on m.marker_id = mlp.marker_id
  where m.dataset_marker_idx ? datasetId::text
  and mlp.map_id=mapId;
  END;
$$ LANGUAGE plpgsql;

--changeset raza:fix_all_chrlen_by_markerlist context:general splitStatements:false
DROP FUNCTION IF EXISTS getallchrlenbymarkerlist(markerList text);

CREATE OR REPLACE FUNCTION getallchrlenbymarkerlist(markerList text)
 RETURNS TABLE(linkage_group_name character varying, linkage_group_length integer)
 LANGUAGE plpgsql
AS $function$
  BEGIN
    return query
    select distinct mlp.linkage_group_name, (mlp.linkage_group_stop - mlp.linkage_group_stop)::integer
    from unnest(markerList::integer[]) ml(m_id) 
    left join marker m on ml.m_id = m.marker_id
    left join v_marker_linkage_physical mlp on m.marker_id = mlp.marker_id;
  END;
$function$;
