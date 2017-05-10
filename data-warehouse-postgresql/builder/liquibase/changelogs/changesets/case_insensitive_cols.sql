--liquibase formatted sql

/*
* GP1-888: This adds the citext extension and convert certain columns to citext for case-insensitive text comparisons.
*/

--changeset kpalis:converColsToCitext context:general splitStatements:false
--enable the extension first
CREATE EXTENSION IF NOT EXISTS citext;

--drop the offensive view
DROP VIEW IF EXISTS v_all_projects_full_details;
DROP VIEW IF EXISTS v_marker_linkage_physical;
DROP VIEW IF EXISTS v_marker_linkage_genetic;
--convert the columns
ALTER TABLE germplasm ALTER COLUMN name TYPE citext;
ALTER TABLE dnasample ALTER COLUMN name TYPE citext;
ALTER TABLE dnarun ALTER COLUMN name TYPE citext;
ALTER TABLE marker ALTER COLUMN name TYPE citext;
ALTER TABLE cv ALTER COLUMN term TYPE citext;
ALTER TABLE platform ALTER COLUMN name TYPE citext;
ALTER TABLE contact ALTER COLUMN firstname TYPE citext;
ALTER TABLE contact ALTER COLUMN lastname TYPE citext;
ALTER TABLE contact ALTER COLUMN email TYPE citext;
ALTER TABLE protocol ALTER COLUMN name TYPE citext;
ALTER TABLE vendor_protocol ALTER COLUMN name TYPE citext;
ALTER TABLE project ALTER COLUMN name TYPE citext;
ALTER TABLE experiment ALTER COLUMN name TYPE citext;
ALTER TABLE dataset ALTER COLUMN name TYPE citext;
ALTER TABLE mapset ALTER COLUMN name TYPE citext;


--put the offensive view back
CREATE VIEW v_marker_linkage_physical as
	SELECT mlg.marker_id,
	lg.linkage_group_id,
	lg.name AS linkage_group_name,
	lg.start AS linkage_group_start,
	lg.stop AS linkage_group_stop,
	mlg.start,
	mlg.stop,
	ms.name AS mapset_name,
	lg.map_id
	FROM marker_linkage_group mlg,
	linkage_group lg,
	mapset ms
	WHERE mlg.linkage_group_id = lg.linkage_group_id AND lg.map_id = ms.mapset_id;

CREATE VIEW v_marker_linkage_genetic as
SELECT mlg.marker_id,
	lg.name AS linkage_group_name,
	mlg.start::integer AS start,
	mlg.stop::integer AS stop,
	ms.name AS mapset_name
	FROM marker_linkage_group mlg,
	linkage_group lg,
	mapset ms
	WHERE mlg.linkage_group_id = lg.linkage_group_id AND lg.map_id = ms.mapset_id;

--changeset kpalis:revertCitextChange context:general splitStatements:false

--drop the offensive view
DROP VIEW IF EXISTS v_all_projects_full_details;
DROP VIEW IF EXISTS v_marker_linkage_physical;
DROP VIEW IF EXISTS v_marker_linkage_genetic;
--convert the columns
ALTER TABLE germplasm ALTER COLUMN name TYPE text;
ALTER TABLE dnasample ALTER COLUMN name TYPE text;
ALTER TABLE dnarun ALTER COLUMN name TYPE text;
ALTER TABLE marker ALTER COLUMN name TYPE text;
ALTER TABLE cv ALTER COLUMN term TYPE text;
ALTER TABLE platform ALTER COLUMN name TYPE text;
ALTER TABLE contact ALTER COLUMN firstname TYPE text;
ALTER TABLE contact ALTER COLUMN lastname TYPE text;
ALTER TABLE contact ALTER COLUMN email TYPE text;
ALTER TABLE protocol ALTER COLUMN name TYPE text;
ALTER TABLE vendor_protocol ALTER COLUMN name TYPE text;
ALTER TABLE project ALTER COLUMN name TYPE text;
ALTER TABLE experiment ALTER COLUMN name TYPE text;
ALTER TABLE dataset ALTER COLUMN name TYPE text;
ALTER TABLE mapset ALTER COLUMN name TYPE text;


--put the offensive view back
CREATE VIEW v_marker_linkage_physical as
	SELECT mlg.marker_id,
	lg.linkage_group_id,
	lg.name AS linkage_group_name,
	lg.start AS linkage_group_start,
	lg.stop AS linkage_group_stop,
	mlg.start,
	mlg.stop,
	ms.name AS mapset_name,
	lg.map_id
	FROM marker_linkage_group mlg,
	linkage_group lg,
	mapset ms
	WHERE mlg.linkage_group_id = lg.linkage_group_id AND lg.map_id = ms.mapset_id;

CREATE VIEW v_marker_linkage_genetic as
SELECT mlg.marker_id,
	lg.name AS linkage_group_name,
	mlg.start::integer AS start,
	mlg.stop::integer AS stop,
	ms.name AS mapset_name
	FROM marker_linkage_group mlg,
	linkage_group lg,
	mapset ms
	WHERE mlg.linkage_group_id = lg.linkage_group_id AND lg.map_id = ms.mapset_id;