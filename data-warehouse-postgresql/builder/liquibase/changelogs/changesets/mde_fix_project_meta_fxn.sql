--liquibase formatted sql

--changeset kpalis:fix_project_meta_fxn context:general splitStatements:false
DROP FUNCTION IF EXISTS getallprojectmetadatabydataset(datasetid integer);

CREATE OR REPLACE FUNCTION getallprojectmetadatabydataset(datasetid integer)
  RETURNS TABLE(project_name text, description text, pi text, experiment_name text, dataset_name text, analysis_name text)
  LANGUAGE plpgsql
 AS $function$
   BEGIN
     return query
     select p.name as project_name, p.description, c.firstname || ' ' || c.lastname as PI, e.name as experiment_name, d.name as dataset_name, a.name as analysis_name
       from dataset d, experiment e, project p, contact c, analysis a
       where d.dataset_id = datasetId
       and d.callinganalysis_id = a.analysis_id
       and d.experiment_id = e.experiment_id
       and e.project_id = p.project_id
       and p.pi_contact = c.contact_id;
   END;
 $function$;