--liquibase formatted sql

--changeset raza:add_get_sampleQC_metadata_by_dataset context:general splitStatements:false
DROP FUNCTION IF EXISTS getSampleQCMetadataByDataset(integer);
CREATE OR REPLACE FUNCTION getSampleQCMetadataByDataset(datasetId integer)
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
	select dr.name as dnarun_name
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
	from dnarun dr
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
	where dr.dataset_dnarun_idx ? datasetId::text
	order by (dr.dataset_dnarun_idx->>datasetId::text)::integer;
  END;
$$ LANGUAGE plpgsql;
