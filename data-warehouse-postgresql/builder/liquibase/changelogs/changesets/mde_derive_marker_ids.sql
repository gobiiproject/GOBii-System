--liquibase formatted sql

/*
* Functions that derive marker IDs based on either markerNames, platformList, or both. This will be used by the extraction by marker list.
*/

--changeset kpalis:createFunctionsForExtractionByMarkerList context:general splitStatements:false
DROP FUNCTION IF EXISTS getMarkerIdsByMarkerNamesAndPlatformList(markerNames text, platformList text);
CREATE OR REPLACE FUNCTION getMarkerIdsByMarkerNamesAndPlatformList(markerNames text, platformList text)
RETURNS table (marker_id integer)
  AS $$
  BEGIN
    return query
    select m.marker_id
	from marker m
	inner join unnest(markerNames::text[]) mn(m_name) on m.name = mn.m_name
	inner join unnest(platformList::integer[]) p(p_id) on m.platform_id = p.p_id
	order by m.marker_id;
  END;
$$ LANGUAGE plpgsql;

DROP FUNCTION IF EXISTS getMarkerIdsByMarkerNames(markerNames text);
CREATE OR REPLACE FUNCTION getMarkerIdsByMarkerNames(markerNames text)
RETURNS table (marker_id integer)
  AS $$
  BEGIN
    return query
    select m.marker_id
	from marker m
	inner join unnest(markerNames::text[]) mn(m_name) on m.name = mn.m_name
	order by m.marker_id;
  END;
$$ LANGUAGE plpgsql;

DROP FUNCTION IF EXISTS getMarkerIdsByPlatformList(platformList text);
CREATE OR REPLACE FUNCTION getMarkerIdsByPlatformList(platformList text)
RETURNS table (marker_id integer)
  AS $$
  BEGIN
    return query
    select m.marker_id
	from marker m
	inner join unnest(platformList::integer[]) p(p_id) on m.platform_id = p.p_id
	order by m.marker_id;
  END;
$$ LANGUAGE plpgsql;