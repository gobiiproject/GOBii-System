--liquibase formatted sql

--changeset raza:fix_marker_qc_output_names context:general splitStatements:false
DROP FUNCTION IF EXISTS getMarkerQCMetadataByDataset(integer);

CREATE OR REPLACE FUNCTION getMarkerQCMetadataByDataset(datasetId integer)
RETURNS table (marker_name text,platform_name text, variant_id integer, variant_code text, marker_ref text, marker_alts text, marker_sequence text, marker_strand text
		,marker_primer_forw1 text
		,marker_primer_forw2 text
		,marker_primer_rev1 text
		,marker_primer_rev2 text
		,marker_probe1 text
		,marker_probe2 text
		,marker_polymorphism_type text
		,marker_synonym text
		,marker_source text
		,marker_gene_id text
		,marker_gene_annotation text
		,marker_polymorphism_annotation text
		,marker_marker_dom text
		,marker_clone_id_pos text
		,marker_genome_build text
		,marker_typeofrefallele_alleleorder text
		,marker_strand_data_read text
) AS $$
  BEGIN
    return query
    select m.name as marker_name, p.name as platform_name, v.variant_id, v.code, m.ref, array_to_string(m.alts, ',', '?'), m.sequence, cv.term as strand_name
		,(m.props->>getPropertyIdByNamesAndType('marker_prop','primer_forw1',1)::text)
		,(m.props->>getPropertyIdByNamesAndType('marker_prop','primer_forw2',1)::text)
		,(m.props->>getPropertyIdByNamesAndType('marker_prop','primer_rev1',1)::text)
		,(m.props->>getPropertyIdByNamesAndType('marker_prop','primer_rev2',1)::text)
		,(m.props->>getPropertyIdByNamesAndType('marker_prop','probe1',1)::text)
		,(m.props->>getPropertyIdByNamesAndType('marker_prop','probe2',1)::text)
		,(m.props->>getPropertyIdByNamesAndType('marker_prop','polymorphism_type',1)::text)
		,(m.props->>getPropertyIdByNamesAndType('marker_prop','synonym',1)::text)
		,(m.props->>getPropertyIdByNamesAndType('marker_prop','source',1)::text)
		,(m.props->>getPropertyIdByNamesAndType('marker_prop','gene_id',1)::text)
		,(m.props->>getPropertyIdByNamesAndType('marker_prop','gene_annotation',1)::text)
		,(m.props->>getPropertyIdByNamesAndType('marker_prop','polymorphism_annotation',1)::text)
		,(m.props->>getPropertyIdByNamesAndType('marker_prop','marker_dom',1)::text)
		,(m.props->>getPropertyIdByNamesAndType('marker_prop','clone_id_pos',1)::text)
		,(m.props->>getPropertyIdByNamesAndType('marker_prop','genome_build',1)::text)
		,(m.props->>getPropertyIdByNamesAndType('marker_prop','typeofrefallele_alleleorder',1)::text)
		,(m.props->>getPropertyIdByNamesAndType('marker_prop','strand_data_read',1)::text)
	from marker m left join platform p on m.platform_id = p.platform_id
	left join cv on m.strand_id = cv.cv_id 
	left join variant v on m.variant_id = v.variant_id
	where m.dataset_marker_idx ? datasetId::text
	order by (m.dataset_marker_idx->>datasetId::text)::integer; 
  END;
$$ LANGUAGE plpgsql;


--changeset raza:fix_marker_qc_metadata_by_marker_list_output_names context:general splitStatements:false
DROP FUNCTION IF EXISTS getMarkerQCMetadataByMarkerList(text);

CREATE OR REPLACE FUNCTION getMarkerQCMetadataByMarkerList(markerList text)
RETURNS table (marker_name text,platform_name text, variant_id integer, variant_code text, marker_ref text, marker_alts text, marker_sequence text, marker_strand text
		,marker_primer_forw1 text
		,marker_primer_forw2 text
		,marker_primer_rev1 text
		,marker_primer_rev2 text
		,marker_probe1 text
		,marker_probe2 text
		,marker_polymorphism_type text
		,marker_synonym text
		,marker_source text
		,marker_gene_id text
		,marker_gene_annotation text
		,marker_polymorphism_annotation text
		,marker_marker_dom text
		,marker_clone_id_pos text
		,marker_genome_build text
		,marker_typeofrefallele_alleleorder text
		,marker_strand_data_read text
) AS $$
  BEGIN
    return query
    select m.name as marker_name, p.name as platform_name, v.variant_id, v.code, m.ref, array_to_string(m.alts, ',', '?'), m.sequence, cv.term as strand_name
		,(m.props->>getPropertyIdByNamesAndType('marker_prop','primer_forw1',1)::text)
		,(m.props->>getPropertyIdByNamesAndType('marker_prop','primer_forw2',1)::text)
		,(m.props->>getPropertyIdByNamesAndType('marker_prop','primer_rev1',1)::text)
		,(m.props->>getPropertyIdByNamesAndType('marker_prop','primer_rev2',1)::text)
		,(m.props->>getPropertyIdByNamesAndType('marker_prop','probe1',1)::text)
		,(m.props->>getPropertyIdByNamesAndType('marker_prop','probe2',1)::text)
		,(m.props->>getPropertyIdByNamesAndType('marker_prop','polymorphism_type',1)::text)
		,(m.props->>getPropertyIdByNamesAndType('marker_prop','synonym',1)::text)
		,(m.props->>getPropertyIdByNamesAndType('marker_prop','source',1)::text)
		,(m.props->>getPropertyIdByNamesAndType('marker_prop','gene_id',1)::text)
		,(m.props->>getPropertyIdByNamesAndType('marker_prop','gene_annotation',1)::text)
		,(m.props->>getPropertyIdByNamesAndType('marker_prop','polymorphism_annotation',1)::text)
		,(m.props->>getPropertyIdByNamesAndType('marker_prop','marker_dom',1)::text)
		,(m.props->>getPropertyIdByNamesAndType('marker_prop','clone_id_pos',1)::text)
		,(m.props->>getPropertyIdByNamesAndType('marker_prop','genome_build',1)::text)
		,(m.props->>getPropertyIdByNamesAndType('marker_prop','typeofrefallele_alleleorder',1)::text)
		,(m.props->>getPropertyIdByNamesAndType('marker_prop','strand_data_read',1)::text)
	from unnest(markerList::integer[]) ml(m_id) --implicit lateral join
	left join marker m on ml.m_id = m.marker_id
	left join platform p on m.platform_id = p.platform_id
	left join cv on m.strand_id = cv.cv_id 
	left join variant v on m.variant_id = v.variant_id
	order by m.marker_id;
  END;
$$ LANGUAGE plpgsql;



--changeset raza:fix_all_chrlen_by_dataset_output_name context:general splitStatements:false
DROP FUNCTION IF EXISTS getAllChrLenByDataset(integer);

CREATE OR REPLACE FUNCTION getAllChrLenByDataset(datasetId integer)
RETURNS table (linkage_grouup_name varchar, linkage_group_stop integer) AS $$
  BEGIN
    return query
    select distinct mlp.linkage_group_name, mlp.linkage_group_stop::integer
    from marker m
    left join v_marker_linkage_physical mlp on m.marker_id = mlp.marker_id
    where m.dataset_marker_idx ? datasetId::text;
  END;
$$ LANGUAGE plpgsql;

--changeset raza:fix_all_chrlen_by_dataset_map_output_name context:general splitStatements:false
DROP FUNCTION IF EXISTS getAllChrLenByDatasetAndMap(datasetId integer, mapId integer);

CREATE OR REPLACE FUNCTION getAllChrLenByDatasetAndMap(datasetId integer, mapId integer)
RETURNS table (linkage_grouup_name varchar, linkage_group_stop integer) AS $$
  BEGIN
  return query
  select distinct mlp.linkage_group_name, mlp.linkage_group_stop::integer
  from marker m
  left join v_marker_linkage_physical mlp on m.marker_id = mlp.marker_id
  where m.dataset_marker_idx ? datasetId::text
  and mlp.map_id=mapId;
  END;
$$ LANGUAGE plpgsql;

--changeset raza:fix_all_chrlen_by_markerlist_output_names context:general splitStatements:false
DROP FUNCTION IF EXISTS getallchrlenbymarkerlist(markerList text);

CREATE OR REPLACE FUNCTION getallchrlenbymarkerlist(markerList text)
 RETURNS TABLE(linkage_grouup_name character varying, linkage_group_stop integer)
 LANGUAGE plpgsql
AS $function$
  BEGIN
    return query
    select distinct mlp.linkage_group_name, mlp.linkage_group_stop::integer
    from unnest(markerList::integer[]) ml(m_id) --implicit lateral join
	left join marker m on ml.m_id = m.marker_id
    left join v_marker_linkage_physical mlp on m.marker_id = mlp.marker_id;
  END;
$function$;

--changeset raza:upd_project_metadata_by_dataset context:general splitStatements:false
DROP FUNCTION IF EXISTS getallprojectmetadatabydataset(datasetid integer);

CREATE OR REPLACE FUNCTION getallprojectmetadatabydataset(datasetid integer)
  RETURNS TABLE(
		project_pi_contact text
		,project_name text
		,projct_genotyping_purpose text
		,project_date_sampled text
		,project_division text
		,project_study_name text
		,experiment_name text
		,platform_name text
		,verdor_protocol_name text
		,vendor_name text
		,protocol_name text
		,dataset_name text
		,dataset_type text
	)
  LANGUAGE plpgsql
 AS $function$
   BEGIN
     return query
     select c.firstname || ' ' || c.lastname as PI
	    ,p.name as project_name
	    ,(p.props->>getPropertyIdByNamesAndType('project_prop','genotyping_purpose',1)::text)
	    ,(p.props->>getPropertyIdByNamesAndType('project_prop','date_sampled',1)::text)
	    ,(p.props->>getPropertyIdByNamesAndType('project_prop','division',1)::text)
	    ,(p.props->>getPropertyIdByNamesAndType('project_prop','study_name',1)::text)
	    ,e.name as exp_name
	    ,plt.name  as plt_name
	    ,vp.name as vp_name
	    ,v.name as v_name
	    ,pr.name as pr_name
	    ,d.name as dataset_name
	    ,cv.term as dateset_type
       from dataset d
	left join experiment e on d.experiment_id = e.experiment_id
	left join project p on p.project_id = e.project_id
	left join contact c on p.pi_contact = c.contact_id
	left join vendor_protocol vp on vp.vendor_protocol_id = e.vendor_protocol_id
	left join organization v on v.organization_id = vp.vendor_id
	left join protocol pr on pr.protocol_id = vp.protocol_id
	left join platform plt on pr.platform_id = plt.platform_id
	left join cv on cv.cv_id = d.type_id
       where d.dataset_id = datasetId;
   END;
 $function$;



