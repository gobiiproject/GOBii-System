/*
	GOBII FlatMeta Views

	This file will contain views that the GOBII applications (or middle tier) can use, 
	especially in cases when the physical structure is different from the logical structure of the schema.

	IMPORTANT: ALL Views SHOULD start with V_
*/

--
CREATE OR REPLACE VIEW v_marker_linkage_genetic AS
	select mlg.marker_id, lg.name as linkage_group_name, mlg.start::integer, mlg.stop::integer, ms.name as mapset_name
	from marker_linkage_group mlg, linkage_group lg, mapset ms
	where mlg.linkage_group_id = lg.linkage_group_id
	and lg.map_id = ms.mapset_id;

CREATE OR REPLACE VIEW v_marker_linkage_physical AS
	select mlg.marker_id, lg.name as linkage_group_name, mlg.start, mlg.stop, ms.name as mapset_name
	from marker_linkage_group mlg, linkage_group lg, mapset ms
	where mlg.linkage_group_id = lg.linkage_group_id
	and lg.map_id = ms.mapset_id;

--In progress. Test views for converting JSONB props column of varying length to records. Quite a pain.
-- Need to find a dynamic way to generate the rowtype, although it seems impossible with the current version
CREATE TYPE myrowtype AS ("23" text, "24" text, "25" text, "26" text, "27" text);
CREATE OR REPLACE VIEW v_marker_with_props AS
	select m.marker_id, m.name, mpr."23" as genome_build, mpr."24" as whatever, mp.props
	from marker m, marker_prop mp, jsonb_populate_record(null::myrowtype, mp.props) as mpr
	where m.marker_id=mp.marker_id;

--not used, keeping this here for reference
CREATE OR REPLACE VIEW v_marker_metadata_by_dataset AS
	select m.marker_id, dm.dataset_id, p.name as platform_name, m.variant_id, m.name, m.code, m.ref, m.alts, m.sequence, r.name as reference_name, m.primers, m.probsets, cv.term as strand_name, m.status
      from marker m, platform p, reference r, cv, dataset_marker dm
      where m.marker_id = dm.marker_id
      and m.platform_id = p.platform_id
      and m.reference_id = r.reference_id
      and m.strand_id = cv.cv_id;

--to check if this is being used, will drop if not
CREATE OR REPLACE VIEW v_all_projects_full_details AS
	select p.project_id, p.name, p.code, p.description, p.pi_contact as pi_contact_id,
		c.firstname as pi_first_name, c.lastname as pi_last_name, p.created_by, p.created_date, p.modified_by, p.modified_date, p.status
	from project p 
	join contact c on p.pi_contact = c.contact_id;
