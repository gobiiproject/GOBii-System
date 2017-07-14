--liquibase formatted sql

/*
* Functions that derive dnarun IDs based on piId, projectId, or sampleType + sampleNames. This will be used by the extraction by samples list.
*/

--changeset kpalis:createFunctionsToDeriveSampleIds context:general splitStatements:false
DROP FUNCTION IF EXISTS getDnarunIdsByPI(piId integer);
CREATE OR REPLACE FUNCTION getDnarunIdsByPI(piId integer)
RETURNS table (dnarun_id integer)
  AS $$
  BEGIN
    return query
	select dr.dnarun_id
	from project p
	inner join dnasample ds on p.project_id = ds.project_id
	inner join dnarun dr on ds.dnasample_id = dr.dnasample_id
	where p.pi_contact = piId
	order by dr.dnarun_id;
  END;
$$ LANGUAGE plpgsql;

DROP FUNCTION IF EXISTS getDnarunIdsByProject(projectId integer);
CREATE OR REPLACE FUNCTION getDnarunIdsByProject(projectId integer)
RETURNS table (dnarun_id integer)
  AS $$
  BEGIN
    return query
	select dr.dnarun_id
	from project p
	inner join dnasample ds on p.project_id = ds.project_id
	inner join dnarun dr on ds.dnasample_id = dr.dnasample_id
	where p.project_id = projectId
	order by dr.dnarun_id;
  END;
$$ LANGUAGE plpgsql;

DROP FUNCTION IF EXISTS getDnarunIdsByDnasampleNames(dnasampleNames text);
CREATE OR REPLACE FUNCTION getDnarunIdsByDnasampleNames(dnasampleNames text)
RETURNS table (dnarun_id integer)
  AS $$
  BEGIN
    return query
	select dr.dnarun_id
	from dnasample ds
	inner join unnest(dnasampleNames::text[]) dsn(s_name) on ds.name = dsn.s_name
	inner join dnarun dr on dr.dnasample_id = ds.dnasample_id
	order by dr.dnarun_id;
  END;
$$ LANGUAGE plpgsql;

DROP FUNCTION IF EXISTS getDnarunIdsByExternalCodes(externalCodes text);
CREATE OR REPLACE FUNCTION getDnarunIdsByExternalCodes(externalCodes text)
RETURNS table (dnarun_id integer)
  AS $$
  BEGIN
    return query
	select dr.dnarun_id
	from germplasm g
	inner join unnest(externalCodes::text[]) gx(ex_code) on g.external_code = gx.ex_code
	inner join dnasample ds on ds.germplasm_id = g.germplasm_id
	inner join dnarun dr on dr.dnasample_id = ds.dnasample_id
	order by dr.dnarun_id;
  END;
$$ LANGUAGE plpgsql;

DROP FUNCTION IF EXISTS getDnarunIdsByGermplasmNames(germplasmNames text);
CREATE OR REPLACE FUNCTION getDnarunIdsByGermplasmNames(germplasmNames text)
RETURNS table (dnarun_id integer)
  AS $$
  BEGIN
    return query
	select dr.dnarun_id
	from germplasm g
	inner join unnest(germplasmNames::text[]) gn(g_name) on g.name = gn.g_name
	inner join dnasample ds on ds.germplasm_id = g.germplasm_id
	inner join dnarun dr on dr.dnasample_id = ds.dnasample_id
	order by dr.dnarun_id;
  END;
$$ LANGUAGE plpgsql;

