--liquibase formatted sql

/*
* Marker and mapset metadata will be ordered by marker_id, while sample metadata will be grouped by increasing dataset_id and ordered by dnarun_idx.
*/

--changeset kpalis:getMarkerQCMetadataByMarkerList context:general splitStatements:false
DROP FUNCTION IF EXISTS getMarkerQCMetadataByMarkerList(text);

CREATE OR REPLACE FUNCTION getMarkerQCMetadataByMarkerList(markerList text)
RETURNS table (marker_name text,platform_name text, variant_id integer, variant_code text, ref text, alts text, sequence text, marker_strand text
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

--changeset kpalis:getMarkerMapsetInfoByMarkerList context:general splitStatements:false
DROP FUNCTION IF EXISTS getMarkerMapsetInfoByMarkerList(markerList text);

CREATE OR REPLACE FUNCTION getMarkerMapsetInfoByMarkerList(markerList text)
RETURNS table (marker_name text,platform_name text,mapset_name text,mapset_type text,linkage_group_name text,linkage_group_start text,linkage_group_stop text,marker_linkage_group_start text,marker_linkage_group_stop text,reference_name text, reference_version text) AS $$
BEGIN
	RETURN QUERY 
	with mlgt as (
			select distinct on (mr.marker_id, mapset_id) mr.marker_id, lg.name as linkage_group_name, lg.start as lg_start,lg.start as lg_stop,  mlg.start, mlg.stop,ms.mapset_id, ms.name as mapset_name,ms.type_id,mr.name as marker_name,mr.platform_id,mr.reference_id
			from unnest(markerList::integer[]) ml(m_id) --implicit lateral join
			left join marker mr on ml.m_id = mr.marker_id
			left join marker_linkage_group mlg on mr.marker_id = mlg.marker_id 
			left join linkage_group lg on lg.linkage_group_id = mlg.linkage_group_id
			left join mapset ms on ms.mapset_id = lg.map_id
		)
		select mlgt.marker_name,p.name
			,mlgt.mapset_name as mapset_name
			,cv.term as map_type
			,mlgt.linkage_group_name::text as lg_name 
			,mlgt.lg_start::text as lg_start
			,mlgt.lg_stop::text as lg_stop
			,mlgt.start::text as mlg_start
			,mlgt.stop::text as mlg_stop
			,r.name,r.version
		from mlgt
		left join platform p on p.platform_id = mlgt.platform_id
		left join reference r on r.reference_id = mlgt.reference_id
		left join cv on cv_id =  mlgt.type_id
		order by mlgt.mapset_id;
END;
$$ LANGUAGE plpgsql;

--changeset kpalis:getSampleQCMetadataByMarkerList context:general splitStatements:false
DROP FUNCTION IF EXISTS getSampleQCMetadataByMarkerList(text);
CREATE OR REPLACE FUNCTION getSampleQCMetadataByMarkerList(markerList text)
RETURNS table (dnarun_name text
		,dnarun_barcode text
		,project_name text
		,project_pi_contact text
		,project_genotyping_purpose text
		,project_date_sampled text
		,project_division text
		,project_study_name text
		,experiment_name text
		,vendor_protocol_name text
		,vendor_name text
		,protocol_name text
		,dataset_name text
		,germplasm_name text
		,germplasm_external_code text
		,germplasm_species text
		,germplasm_type text
		,germplasm_id text
		,germplasm_seed_source_id text
		,germplasm_subsp text
		,germplasm_heterotic_group text
		,germplasm_par1 text
		,germplasm_par2 text
		,germplasm_par3 text
		,germplasm_par4 text
		,germplasm_pedigree text
		,dnasample_name text
		,dnasample_platename text
		,dnasample_num text
		,dnasample_well_row text
		,dnasample_well_col text
		,dnasample_trial_name text
		,dnasample_sample_group text
		,dnasample_sample_group_cycle text
		,dnasample_sample_type text
		,dnasample_sample_parent text
		,dnasample_ref_sample text
		) AS $$
  BEGIN
	return query
	with dataset_list as (
			select distinct jsonb_object_keys(dataset_marker_idx)::integer as ds_id
			from unnest(markerList::integer[]) ml(m_id) --implicit lateral join
			left join marker m on ml.m_id = m.marker_id
			order by ds_id
		)
	select t.dnarun_name, t.dnarun_barcode, t.project_name, t.project_pi_contact, t.project_genotyping_purpose, t.project_date_sampled, t.project_division, t.project_study_name, t.experiment_name, t.vp_name, t.v_name, t.pr_name, t.dataset_name, t.germplasm_name, t.exc, t.species, t.type  , t.gid, t.gssd, t.gs, t.ghg, t.gp1, t.gp2, t.gp3, t.gp4, t.gped, t.dnasample_name, t.plate, t.dnum, t.wr, t.wc, t.dtn, t.dsg, t.dsgc, t.dst, t.dsp, t.drs
	from (
		select distinct on (dl.ds_id, dr.dataset_dnarun_idx->>dl.ds_id::text) 
			dl.ds_id as did
			,(dr.dataset_dnarun_idx->>dl.ds_id::text)::integer as ds_idx
			,dr.name as dnarun_name
			,(dr.props->>getPropertyIdByNamesAndType('dnarun_prop','barcode',1)::text) as dnarun_barcode
			,p.name as project_name
			,c.firstname||' '||c.lastname as project_pi_contact
			,(p.props->>getPropertyIdByNamesAndType('project_prop','genotyping_purpose',1)::text) as project_genotyping_purpose
			,(p.props->>getPropertyIdByNamesAndType('project_prop','date_sampled',1)::text) as project_date_sampled
			,(p.props->>getPropertyIdByNamesAndType('project_prop','division',1)::text) as project_division
			,(p.props->>getPropertyIdByNamesAndType('project_prop','study_name',1)::text) as project_study_name
			,e.name as experiment_name
			,vp.name as vp_name
			,v.name as v_name
			,pr.name as pr_name
			,ds.name as dataset_name
			,g.name as germplasm_name
			,g.external_code as exc
			,cv.term as species
			,cv2.term as type  
			,(p.props->>getPropertyIdByNamesAndType('germplasm_prop','germplasm_id',1)::text) as gid
			,(p.props->>getPropertyIdByNamesAndType('germplasm_prop','germplasm_seed_source_id',1)::text) as gssd
			,(p.props->>getPropertyIdByNamesAndType('germplasm_prop','germplasm_subsp',1)::text) as gs
			,(p.props->>getPropertyIdByNamesAndType('germplasm_prop','germplasm_heterotic_group',1)::text) as ghg
			,(p.props->>getPropertyIdByNamesAndType('germplasm_prop','germplasm_par1',1)::text) as gp1
			,(p.props->>getPropertyIdByNamesAndType('germplasm_prop','germplasm_par2',1)::text) as gp2
			,(p.props->>getPropertyIdByNamesAndType('germplasm_prop','germplasm_par3',1)::text) as gp3
			,(p.props->>getPropertyIdByNamesAndType('germplasm_prop','germplasm_par4',1)::text) as gp4
			,(p.props->>getPropertyIdByNamesAndType('germplasm_prop','germplasm_pedigree',1)::text) as gped
			,dns.name  as dnasample_name
			,dns.platename as plate
			,dns.num as dnum
			,dns.well_row as wr
			,dns.well_col as wc
			,(p.props->>getPropertyIdByNamesAndType('dnasample_prop','dnasample_trial_name',1)::text) as dtn
			,(p.props->>getPropertyIdByNamesAndType('dnasample_prop','dnasample_sample_group',1)::text) as dsg
			,(p.props->>getPropertyIdByNamesAndType('dnasample_prop','dnasample_sample_group_cycle',1)::text) as dsgc
			,(p.props->>getPropertyIdByNamesAndType('dnasample_prop','dnasample_sample_type',1)::text) as dst
			,(p.props->>getPropertyIdByNamesAndType('dnasample_prop','dnasample_sample_parent',1)::text) as dsp
			,(p.props->>getPropertyIdByNamesAndType('dnasample_prop','dnasample_ref_sample',1)::text) as drs
		from dataset_list dl
		left join dnarun dr on dr.dataset_dnarun_idx ? dl.ds_id::text
		left join dnasample dns on dr.dnasample_id = dns.dnasample_id 
		left join germplasm g on dns.germplasm_id = g.germplasm_id 
		left join project p on dns.project_id = p.project_id
		left join contact c on c.contact_id = p.pi_contact
		left join experiment e on e.experiment_id = dr.experiment_id
		left join dataset ds on ds.experiment_id = e.experiment_id
		left join cv on g.species_id = cv.cv_id
		left join cv cv2 on g.type_id = cv2.cv_id
		left join vendor_protocol vp on vp.vendor_protocol_id = e.vendor_protocol_id
		left join organization v on v.organization_id = vp.vendor_id
		left join protocol pr on pr.protocol_id = vp.protocol_id
		) t
	order by (t.did, t.ds_idx);
  END;
$$ LANGUAGE plpgsql;

DROP FUNCTION IF EXISTS getSampleQCMetadataByMarkerListX(text);
CREATE OR REPLACE FUNCTION getSampleQCMetadataByMarkerListX(markerList text)
RETURNS table (ds_id integer, idx integer
		,dnarun_name text
		,dnarun_barcode text
		,project_name text
		,project_pi_contact text
		,project_genotyping_purpose text
		,project_date_sampled text
		,project_division text
		,project_study_name text
		,experiment_name text
		,vendor_protocol_name text
		,vendor_name text
		,protocol_name text
		,dataset_name text
		,germplasm_name text
		,germplasm_external_code text
		,germplasm_species text
		,germplasm_type text
		,germplasm_id text
		,germplasm_seed_source_id text
		,germplasm_subsp text
		,germplasm_heterotic_group text
		,germplasm_par1 text
		,germplasm_par2 text
		,germplasm_par3 text
		,germplasm_par4 text
		,germplasm_pedigree text
		,dnasample_name text
		,dnasample_platename text
		,dnasample_num text
		,dnasample_well_row text
		,dnasample_well_col text
		,dnasample_trial_name text
		,dnasample_sample_group text
		,dnasample_sample_group_cycle text
		,dnasample_sample_type text
		,dnasample_sample_parent text
		,dnasample_ref_sample text
		) AS $$
  BEGIN
	return query
	with dataset_list as (
			select distinct jsonb_object_keys(dataset_marker_idx)::integer as ds_id
			from unnest(markerList::integer[]) ml(m_id) --implicit lateral join
			left join marker m on ml.m_id = m.marker_id
			order by ds_id
		)
	select * from (
		select distinct on (dl.ds_id, dr.dataset_dnarun_idx->>dl.ds_id::text) dl.ds_id as did, (dr.dataset_dnarun_idx->>dl.ds_id::text)::integer as ds_idx
			,dr.name as dnarun_name
			,(dr.props->>getPropertyIdByNamesAndType('dnarun_prop','barcode',1)::text)
			,p.name as project_name
			,c.firstname||' '||c.lastname as pi_contact
			,(p.props->>getPropertyIdByNamesAndType('project_prop','genotyping_purpose',1)::text) as prj
			,(p.props->>getPropertyIdByNamesAndType('project_prop','date_sampled',1)::text)
			,(p.props->>getPropertyIdByNamesAndType('project_prop','division',1)::text)
			,(p.props->>getPropertyIdByNamesAndType('project_prop','study_name',1)::text)
			,e.name as experiment_name
			,vp.name as vp_name
			,v.name as v_name
			,pr.name as pr_name
			,ds.name as dataset_name
			,g.name as germplasm_name
			,g.external_code
			,cv.term as species
			,cv2.term as type  
			,(p.props->>getPropertyIdByNamesAndType('germplasm_prop','germplasm_id',1)::text)
			,(p.props->>getPropertyIdByNamesAndType('germplasm_prop','germplasm_seed_source_id',1)::text)
			,(p.props->>getPropertyIdByNamesAndType('germplasm_prop','germplasm_subsp',1)::text)
			,(p.props->>getPropertyIdByNamesAndType('germplasm_prop','germplasm_heterotic_group',1)::text)
			,(p.props->>getPropertyIdByNamesAndType('germplasm_prop','germplasm_par1',1)::text)
			,(p.props->>getPropertyIdByNamesAndType('germplasm_prop','germplasm_par2',1)::text)
			,(p.props->>getPropertyIdByNamesAndType('germplasm_prop','germplasm_par3',1)::text)
			,(p.props->>getPropertyIdByNamesAndType('germplasm_prop','germplasm_par4',1)::text)
			,(p.props->>getPropertyIdByNamesAndType('germplasm_prop','germplasm_pedigree',1)::text)
			,dns.name  as dnasample_name
			,dns.platename
			,dns.num
			,dns.well_row
			,dns.well_col	
			,(p.props->>getPropertyIdByNamesAndType('dnasample_prop','dnasample_trial_name',1)::text)
			,(p.props->>getPropertyIdByNamesAndType('dnasample_prop','dnasample_sample_group',1)::text)
			,(p.props->>getPropertyIdByNamesAndType('dnasample_prop','dnasample_sample_group_cycle',1)::text)
			,(p.props->>getPropertyIdByNamesAndType('dnasample_prop','dnasample_sample_type',1)::text)
			,(p.props->>getPropertyIdByNamesAndType('dnasample_prop','dnasample_sample_parent',1)::text)
			,(p.props->>getPropertyIdByNamesAndType('dnasample_prop','dnasample_ref_sample',1)::text)
		from dataset_list dl
		left join dnarun dr on dr.dataset_dnarun_idx ? dl.ds_id::text
		left join dnasample dns on dr.dnasample_id = dns.dnasample_id 
		left join germplasm g on dns.germplasm_id = g.germplasm_id 
		left join project p on dns.project_id = p.project_id
		left join contact c on c.contact_id = p.pi_contact
		left join experiment e on e.experiment_id = dr.experiment_id
		left join dataset ds on ds.experiment_id = e.experiment_id
		left join cv on g.species_id = cv.cv_id
		left join cv cv2 on g.type_id = cv2.cv_id
		left join vendor_protocol vp on vp.vendor_protocol_id = e.vendor_protocol_id
		left join organization v on v.organization_id = vp.vendor_id
		left join protocol pr on pr.protocol_id = vp.protocol_id
		) t
	order by (t.did, t.ds_idx);
	--order by (dl.ds_id, (dr.dataset_dnarun_idx->>dl.ds_id::text)::integer);
  END;
$$ LANGUAGE plpgsql;

--changeset kpalis:getallchrlenbymarkerlist context:general splitStatements:false
DROP FUNCTION IF EXISTS getallchrlenbymarkerlist(markerList text);
CREATE OR REPLACE FUNCTION getallchrlenbymarkerlist(markerList text)
 RETURNS TABLE(chr_name character varying, length integer)
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

--changeset kpalis:getMatrixPosOfMarkers context:general splitStatements:false
--This returns a list of positions in a genotype matrix for a given set of markers, sorted by marker_id.
DROP FUNCTION IF EXISTS getMatrixPosOfMarkers(markerList text);
CREATE OR REPLACE FUNCTION getMatrixPosOfMarkers(markerList text)
 RETURNS TABLE(dataset_id integer, positions text)
 LANGUAGE plpgsql
AS $function$
  BEGIN
    return query
	with marker_list as ( select *
	from unnest(markerList::integer[]) ml(m_id) 
	left join marker m on ml.m_id = m.marker_id),
	dataset_list as (
		select  distinct jsonb_object_keys(dataset_marker_idx)::integer as dataset_id
		from marker_list ml
		order by dataset_id)
	select dl.dataset_id, string_agg(COALESCE(ml.dataset_marker_idx ->> dl.dataset_id::text, '-1'), ', ') as idx
	from marker_list ml cross join
	dataset_list dl
	group by dl.dataset_id
	order by dl.dataset_id;
  END;
$function$;
/* Sample calls and direct queries:
	select * from getMatrixPosOfMarkers('{1,2,3}');
	select * from getMatrixPosOfMarkers('{1000,1200,1023,10000,5791,30000,80000}');

	with marker_list as ( select *
		from unnest('{1000,1200,1023,10000,5791,30000,80000}'::integer[]) ml(m_id) 
		left join marker m on ml.m_id = m.marker_id),
	dataset_list as (
		select  distinct jsonb_object_keys(dataset_marker_idx)::integer as dataset_id
		from marker_list ml
		order by dataset_id)
	select dl.dataset_id, string_agg(COALESCE(ml.dataset_marker_idx ->> dl.dataset_id::text, '-1'), ', ') as idx
	from marker_list ml cross join
	dataset_list dl
	group by dl.dataset_id
	order by dl.dataset_id;
*/
