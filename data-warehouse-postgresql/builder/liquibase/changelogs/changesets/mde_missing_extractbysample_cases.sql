--liquibase formatted sql

--changeset kpalis:fix_support_missing_extractbysample_cases context:general splitStatements:false
--Germplasm Names
CREATE OR REPLACE FUNCTION getDnarunIdsByGermplasmNamesAndProject(germplasmnames text, projectId integer)
 RETURNS TABLE(dnarun_id integer)
 LANGUAGE plpgsql
AS $function$
  BEGIN
    return query
        select dr.dnarun_id
        from germplasm g
        inner join unnest(germplasmNames::text[]) gn(g_name) on g.name = gn.g_name
        inner join dnasample ds on ds.germplasm_id = g.germplasm_id
        inner join dnarun dr on dr.dnasample_id = ds.dnasample_id
        where ds.project_id = projectId
        order by dr.dnarun_id;
  END;
$function$;

CREATE OR REPLACE FUNCTION getDnarunIdsByGermplasmNamesAndPI(germplasmnames text, piId integer)
 RETURNS TABLE(dnarun_id integer)
 LANGUAGE plpgsql
AS $function$
  BEGIN
    return query
        select dr.dnarun_id
        from germplasm g
        inner join unnest(germplasmNames::text[]) gn(g_name) on g.name = gn.g_name
        inner join dnasample ds on ds.germplasm_id = g.germplasm_id
        inner join project p on p.project_id = ds.project_id
        inner join dnarun dr on dr.dnasample_id = ds.dnasample_id
        where p.pi_contact = piId
        order by dr.dnarun_id;
  END;
$function$;

--External Codes
CREATE OR REPLACE FUNCTION getDnarunIdsByExternalCodesAndProject(externalcodes text, projectId integer)
 RETURNS TABLE(dnarun_id integer)
 LANGUAGE plpgsql
AS $function$
  BEGIN
    return query
        select dr.dnarun_id
        from germplasm g
        inner join unnest(externalCodes::text[]) gx(ex_code) on g.external_code = gx.ex_code
        inner join dnasample ds on ds.germplasm_id = g.germplasm_id
        inner join dnarun dr on dr.dnasample_id = ds.dnasample_id
        where ds.project_id = projectId
        order by dr.dnarun_id;
  END;
$function$;

CREATE OR REPLACE FUNCTION getDnarunIdsByExternalCodesAndPI(externalcodes text, piId integer)
 RETURNS TABLE(dnarun_id integer)
 LANGUAGE plpgsql
AS $function$
  BEGIN
    return query
        select dr.dnarun_id
        from germplasm g
        inner join unnest(externalCodes::text[]) gx(ex_code) on g.external_code = gx.ex_code
        inner join dnasample ds on ds.germplasm_id = g.germplasm_id
        inner join project p on p.project_id = ds.project_id
        inner join dnarun dr on dr.dnasample_id = ds.dnasample_id
        where p.pi_contact = piId
        order by dr.dnarun_id;
  END;
$function$;

--Dnasample Names
CREATE OR REPLACE FUNCTION getDnarunIdsByDnasampleNamesAndProject(dnasamplenames text, projectId integer)
 RETURNS TABLE(dnarun_id integer)
 LANGUAGE plpgsql
AS $function$
  BEGIN
    return query
        select dr.dnarun_id
        from dnasample ds
        inner join unnest(dnasampleNames::text[]) dsn(s_name) on ds.name = dsn.s_name
        inner join dnarun dr on dr.dnasample_id = ds.dnasample_id
        where ds.project_id = projectId
        order by dr.dnarun_id;
  END;
$function$;

CREATE OR REPLACE FUNCTION getDnarunIdsByDnasampleNamesAndPI(dnasamplenames text, piId integer)
 RETURNS TABLE(dnarun_id integer)
 LANGUAGE plpgsql
AS $function$
  BEGIN
    return query
        select dr.dnarun_id
        from dnasample ds
        inner join unnest(dnasampleNames::text[]) dsn(s_name) on ds.name = dsn.s_name
        inner join project p on p.project_id = ds.project_id
        inner join dnarun dr on dr.dnasample_id = ds.dnasample_id
        where p.pi_contact = piId
        order by dr.dnarun_id;
  END;
$function$;