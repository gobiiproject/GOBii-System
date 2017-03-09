--liquibase formatted sql

--changeset vjuanillas:getCvtermsByCvgroup context:general splitStatements:false
DROP FUNCTION IF EXISTS getCvtermsByCvgroupname(cvgroupName text);

CREATE OR REPLACE FUNCTION getCvtermsByCvgroupname(cvgroupName text)
RETURNS TABLE (term text) AS $$
BEGIN
	RETURN query
	select cv.term from cv, cvgroup
	where cv.cvgroup_id = cvgroup.cvgroup_id and cvgroup.name = cvgroupName;
END;
$$ LANGUAGE plpgsql;
