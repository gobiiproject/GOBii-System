--liquibase formatted sql

--changeset raza:fix_2_project_metadata_by_dataset context:general splitStatements:false
DROP FUNCTION IF EXISTS getallprojectmetadatabydataset(datasetid integer);

CREATE OR REPLACE FUNCTION getallprojectmetadatabydataset(datasetid integer)
  RETURNS TABLE(
		project_pi_contact text
		,project_name text
		,project_genotyping_purpose text
		,project_date_sampled text
		,project_division text
		,project_study_name text
		,experiment_name text
		,platform_name text
		,vendor_protocol_name text
		,vendor_name text
		,protocol_name text
		,analysis_name text
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
	    ,a.name as analysis_name
	    ,d.name as dataset_name
	    ,cv.term as dateset_type
       from dataset d
	left join analysis a on d.callinganalysis_id = a.analysis_id
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

