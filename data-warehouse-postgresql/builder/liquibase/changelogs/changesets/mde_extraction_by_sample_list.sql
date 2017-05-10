--liquibase formatted sql

/*
* All the functions here enable the MDE to do extraction by marker list based on the derived marker ids from the functions provided on mde_derive_sample_ids.sql.
*/


--Functions that derive marker IDs based on either markerNames, platformList, or both. This will be used by the extraction by marker list.
--changeset kpalis:createFunctionsForDerivingMarkerIdsFromSampleIds  context:general splitStatements:false
DROP FUNCTION IF EXISTS getMarkerIdsBySamplesPlatformsAndDatasetType(sampleList text, platformList text, datasetTypeId integer);
CREATE OR REPLACE FUNCTION getMarkerIdsBySamplesPlatformsAndDatasetType(sampleList text, platformList text, datasetTypeId integer)
RETURNS table (marker_id integer)
  AS $$
  BEGIN
    return query
    with dataset_list as (
			select distinct jsonb_object_keys(dataset_dnarun_idx)::integer as ds_id
			from unnest(sampleList::integer[]) sl(s_id)
			left join dnarun dr on sl.s_id = dr.dnarun_id
			order by ds_id
		)
    select m.marker_id
    from dataset_list dl inner join dataset d on dl.ds_id = d.dataset_id
    inner join marker m on m.dataset_marker_idx ? d.dataset_id::text
    inner join unnest(platformList::integer[]) p(p_id) on m.platform_id = p.p_id
    where d.type_id = datasetTypeId;
  END;
$$ LANGUAGE plpgsql;

--sample usage:
--select * from getMarkerIdsBySamplesPlatformsAndDatasetType('{1,2,3,4,5,6,7,8,9,10}', '{1,7,8}', 164);

DROP FUNCTION IF EXISTS getMarkerIdsBySamplesAndDatasetType(sampleList text, datasetTypeId integer);
CREATE OR REPLACE FUNCTION getMarkerIdsBySamplesAndDatasetType(sampleList text, datasetTypeId integer)
RETURNS table (marker_id integer)
  AS $$
  BEGIN
    return query
    with dataset_list as (
			select distinct jsonb_object_keys(dataset_dnarun_idx)::integer as ds_id
			from unnest(sampleList::integer[]) sl(s_id)
			left join dnarun dr on sl.s_id = dr.dnarun_id
			order by ds_id
		)
    select m.marker_id
    from dataset_list dl inner join dataset d on dl.ds_id = d.dataset_id
    inner join marker m on m.dataset_marker_idx ? d.dataset_id::text
    where d.type_id = datasetTypeId;
  END;
$$ LANGUAGE plpgsql;

--sample usage:
--select * from getMarkerIdsBySamplesAndDatasetType('{1,2,3,4,5,6,7,8,9,10}', 164);

--changeset kpalis:getSampleMetadataBySampleList  context:general splitStatements:false
DROP FUNCTION IF EXISTS getSampleQCMetadataBySampleList(text, integer);
CREATE OR REPLACE FUNCTION getSampleQCMetadataBySampleList(sampleList text, datasetTypeId integer)
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
			select distinct jsonb_object_keys(dataset_dnarun_idx)::integer as ds_id
			from unnest(sampleList::integer[]) sl(s_id)
			left join dnarun d on sl.s_id = d.dnarun_id
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
		from
		(select ddl.ds_id from dataset_list ddl inner join dataset d on ddl.ds_id = d.dataset_id where d.type_id=datasetTypeId) dl
		inner join dnarun dr on dr.dataset_dnarun_idx ? dl.ds_id::text
		inner join unnest(sampleList::integer[]) sl2(s_id) on sl2.s_id = dr.dnarun_id
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
-- sample usage: select * from getSampleQCMetadataBySampleList('{1,2,3,4,5,6,7,8,9,10}', 164);

--changeset kpalis:getMatrixPosOfSamples context:general splitStatements:false
--This returns a list of positions in a genotype matrix for a given set of samples, sorted by dnarun_id.
DROP FUNCTION IF EXISTS getMatrixPosOfSamples(sampleList text, datasetTypeId integer);
CREATE OR REPLACE FUNCTION getMatrixPosOfSamples(sampleList text, datasetTypeId integer)
 RETURNS TABLE(dataset_id integer, positions text)
 LANGUAGE plpgsql
AS $function$
  BEGIN
    return query
	with sample_list as ( select *
		from unnest(sampleList::integer[]) sl(s_id) 
		left join dnarun dr on sl.s_id = dr.dnarun_id
		order by sl.s_id),
	dataset_list as (
		select distinct jsonb_object_keys(dataset_dnarun_idx)::integer as dataset_id
		from sample_list sl
		order by dataset_id)
	select rdl.dataset_id, string_agg(COALESCE(sl.dataset_dnarun_idx ->> rdl.dataset_id::text, '-1'), ', ') as idx
	from sample_list sl cross join
	(select dl.dataset_id from dataset_list dl inner join dataset d on dl.dataset_id = d.dataset_id where d.type_id=datasetTypeId) rdl
	group by rdl.dataset_id
	order by rdl.dataset_id;
  END;
$function$;
--sample usage: select * from getMatrixPosOfSamples('{1,2,3,4,5,6,7,8,9,10}', 164);
--sample usage: select * from getMatrixPosOfSamples('{401,402,403,404,405,4601,4602,4603,6081,6082,6083}', 161);


