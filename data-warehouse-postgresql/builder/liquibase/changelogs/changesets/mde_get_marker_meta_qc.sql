--liquibase formatted sql

--changeset raza:getPropertyIdByNamesAndType context:general splitStatements:false
CREATE OR REPLACE FUNCTION getPropertyIdByNamesAndType(groupName text, propName text, cvType integer)
RETURNS integer AS $$
BEGIN
	RETURN ( 
	select cv.cv_id
	from cv inner join cvgroup cg on cv.cvgroup_id = cg.cvgroup_id
	where cg.name = groupName
	and cg.type = cvType
	and term=propName);
END;
$$ LANGUAGE plpgsql;


--changeset raza:upd_getMinimalMarkerMetadataByDataset context:general splitStatements:false
DROP FUNCTION IF EXISTS getMarkerQCMetadataByDataset(integer);

CREATE OR REPLACE FUNCTION getMarkerQCMetadataByDataset(datasetId integer)
RETURNS table (marker_name text,platform_name text, variant_id integer, variant_code text, ref text, alts text, sequence text, marker_strand text
		,marker_primer_forw1 text
		,marker_primer_forw2 text
		,marker_primer_rev1 text
		,marker_primer_rev2 text
		,marker_probe1 text
		,marker_probe2 text
		,marker_polymorphism_type text
		,marker_synonym text
		,marker_source text
		,marker_gene_id text
		,marker_gene_annotation text
		,marker_polymorphism_annotation text
		,marker_marker_dom text
		,marker_clone_id_pos text
		,marker_genome_build text
		,marker_typeofrefallele_alleleorder text
		,marker_strand_data_read text
) AS $$
  BEGIN
    return query
    select m.name as marker_name, p.name as platform_name, v.variant_id, v.code, m.ref, array_to_string(m.alts, ',', '?'), m.sequence, cv.term as strand_name
		,(m.props->>getPropertyIdByNamesAndType('marker_prop','primer_forw1',1)::text)
		,(m.props->>getPropertyIdByNamesAndType('marker_prop','primer_forw2',1)::text)
		,(m.props->>getPropertyIdByNamesAndType('marker_prop','primer_rev1',1)::text)
		,(m.props->>getPropertyIdByNamesAndType('marker_prop','primer_rev2',1)::text)
		,(m.props->>getPropertyIdByNamesAndType('marker_prop','probe1',1)::text)
		,(m.props->>getPropertyIdByNamesAndType('marker_prop','probe2',1)::text)
		,(m.props->>getPropertyIdByNamesAndType('marker_prop','polymorphism_type',1)::text)
		,(m.props->>getPropertyIdByNamesAndType('marker_prop','synonym',1)::text)
		,(m.props->>getPropertyIdByNamesAndType('marker_prop','source',1)::text)
		,(m.props->>getPropertyIdByNamesAndType('marker_prop','gene_id',1)::text)
		,(m.props->>getPropertyIdByNamesAndType('marker_prop','gene_annotation',1)::text)
		,(m.props->>getPropertyIdByNamesAndType('marker_prop','polymorphism_annotation',1)::text)
		,(m.props->>getPropertyIdByNamesAndType('marker_prop','marker_dom',1)::text)
		,(m.props->>getPropertyIdByNamesAndType('marker_prop','clone_id_pos',1)::text)
		,(m.props->>getPropertyIdByNamesAndType('marker_prop','genome_build',1)::text)
		,(m.props->>getPropertyIdByNamesAndType('marker_prop','typeofrefallele_alleleorder',1)::text)
		,(m.props->>getPropertyIdByNamesAndType('marker_prop','strand_data_read',1)::text)
	from marker m left join platform p on m.platform_id = p.platform_id
	left join cv on m.strand_id = cv.cv_id 
	left join variant v on m.variant_id = v.variant_id
	where m.dataset_marker_idx ? datasetId::text
	order by (m.dataset_marker_idx->>datasetId::text)::integer; 
  END;
$$ LANGUAGE plpgsql;
