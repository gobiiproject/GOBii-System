--liquibase formatted sql

--changeset kpalis:fix_missing_linkagegroup_stop context:general splitStatements:false
CREATE OR REPLACE FUNCTION getmarkermapsetinfobydataset(dsid integer, mapid integer)
 RETURNS TABLE(marker_name text, platform_name text, mapset_name text, mapset_type text, linkage_group_name text, linkage_group_start text, linkage_group_stop text, marker_linkage_group_start text, marker_linkage_group_stop text, reference_name text, reference_version text)
 LANGUAGE plpgsql
AS $function$
BEGIN
        RETURN QUERY
        with mlgt as (
                        select distinct on (mr.marker_id, mapset_id) mr.marker_id, lg.name as linkage_group_name, lg.start as lg_start,lg.stop as lg_stop,  mlg.start, mlg.stop,ms.mapset_id, ms.name as mapset_name,ms.type_id,mr.name as marker_name,mr.platform_id,mr.reference_id
                        from marker mr
                        left join marker_linkage_group mlg on mr.marker_id = mlg.marker_id
                        left join linkage_group lg on lg.linkage_group_id = mlg.linkage_group_id
                        left join mapset ms on ms.mapset_id = lg.map_id
                        where mr.dataset_marker_idx ? dsId::text
                )
                select mlgt.marker_name,p.name
                        , COALESCE(t.mpsn,mlgt.mapset_name) as mapset_name
                        , COALESCE(t.mpstype,cv.term) as mapset_type
                        ,COALESCE(t.lgn,mlgt.linkage_group_name::text) as lg_name
                        ,COALESCE(t.lgst,mlgt.lg_start::text) as lg_start
                        ,COALESCE(t.lgsp,mlgt.lg_stop::text) as lg_stop
                        ,COALESCE(t.mlgst,mlgt.start::text) as mlg_start
                        ,COALESCE(t.mlgst,mlgt.stop::text) as mlg_stop
                        ,r.name,r.version
                from mlgt
                left join platform p on p.platform_id = mlgt.platform_id
                left join reference r on r.reference_id = mlgt.reference_id
                left join cv on cv_id =  mlgt.type_id
                left join (
                        select  ' '::text as lgn
                                ,' '::text as mpsn
                                ,' '::text as lgst
                                ,' '::text as lgsp
                                ,' '::text as mlgst
                                ,' '::text as mlgsp
                                ,' '::text as mpstype
                ) t on mlgt.mapset_id != mapId
                order by mlgt.mapset_id;
END;
$function$;

CREATE OR REPLACE FUNCTION getmarkerallmapsetinfobydataset(dsid integer, mapid integer)
 RETURNS TABLE(marker_name text, platform_name text, mapset_id integer, mapset_name text, mapset_type text, linkage_group_name text, linkage_group_start text, linkage_group_stop text, marker_linkage_group_start text, marker_linkage_group_stop text, reference_name text, reference_version text)
 LANGUAGE plpgsql
AS $function$
BEGIN
        RETURN QUERY
        with mlgt as (
                        select distinct on (mr.marker_id, mapset_id) mr.marker_id, lg.name as linkage_group_name, lg.start as lg_start,lg.stop as lg_stop,  mlg.start, mlg.stop,ms.mapset_id, ms.name as mapset_name,ms.type_id,mr.name as marker_name,mr.platform_id,mr.reference_id,mr.dataset_marker_idx
                        from marker mr
                        left join marker_linkage_group mlg on mr.marker_id = mlg.marker_id
                        left join linkage_group lg on lg.linkage_group_id = mlg.linkage_group_id
                        left join mapset ms on ms.mapset_id = lg.map_id
                        where mr.dataset_marker_idx ? dsId::text
                )
                select mlgt.marker_name
                                ,p.name
                                ,mlgt.mapset_id
                        ,mlgt.mapset_name as mapset_name
                        ,cv.term as map_type
                        ,mlgt.linkage_group_name::text as lg_name
                        ,mlgt.lg_start::text as lg_start
                        ,mlgt.lg_stop::text as lg_stop
                        ,mlgt.start::text as mlg_start
                        ,mlgt.stop::text as mlg_stop
                        ,r.name,r.version
                from mlgt
                left join platform p on p.platform_id = mlgt.platform_id
                left join reference r on r.reference_id = mlgt.reference_id
                left join cv on cv_id =  mlgt.type_id
                order by mlgt.mapset_id, (mlgt.dataset_marker_idx->>dsId::text)::integer;
END;
$function$;

CREATE OR REPLACE FUNCTION getmarkermapsetinfobymarkerlist(markerlist text)
 RETURNS TABLE(marker_name text, platform_name text, mapset_id integer, mapset_name text, mapset_type text, linkage_group_name text, linkage_group_start text, linkage_group_stop text, marker_linkage_group_start text, marker_linkage_group_stop text, reference_name text, reference_version text)
 LANGUAGE plpgsql
AS $function$
BEGIN
        RETURN QUERY
        with mlgt as (
                        select distinct on (mr.marker_id, mapset_id) mr.marker_id, lg.name as linkage_group_name, lg.start as lg_start,lg.stop as lg_stop,  mlg.start, mlg.stop,ms.mapset_id, ms.name as mapset_name,ms.type_id,mr.name as marker_name,mr.platform_id,mr.reference_id
                        from unnest(markerList::integer[]) ml(m_id)
                        left join marker mr on ml.m_id = mr.marker_id
                        left join marker_linkage_group mlg on mr.marker_id = mlg.marker_id
                        left join linkage_group lg on lg.linkage_group_id = mlg.linkage_group_id
                        left join mapset ms on ms.mapset_id = lg.map_id
                )
                select mlgt.marker_name
                                ,p.name
                                ,mlgt.mapset_id
                        ,mlgt.mapset_name as mapset_name
                        ,cv.term as map_type
                        ,mlgt.linkage_group_name::text as lg_name
                        ,mlgt.lg_start::text as lg_start
                        ,mlgt.lg_stop::text as lg_stop
                        ,mlgt.start::text as mlg_start
                        ,mlgt.stop::text as mlg_stop
                        ,r.name,r.version
                from mlgt
                left join platform p on p.platform_id = mlgt.platform_id
                left join reference r on r.reference_id = mlgt.reference_id
                left join cv on cv_id =  mlgt.type_id
                order by mlgt.mapset_id, mlgt.marker_id;
END;
$function$;

CREATE OR REPLACE FUNCTION getmarkerallmapsetinfobydataset(dsid integer, mapid integer)
 RETURNS TABLE(marker_name text, platform_name text, mapset_id integer, mapset_name text, mapset_type text, linkage_group_name text, linkage_group_start text, linkage_group_stop text, marker_linkage_group_start text, marker_linkage_group_stop text, reference_name text, reference_version text)
 LANGUAGE plpgsql
AS $function$
BEGIN
        RETURN QUERY
        with mlgt as (
                        select distinct on (mr.marker_id, mapset_id) mr.marker_id, lg.name as linkage_group_name, lg.start as lg_start,lg.stop as lg_stop,  mlg.start, mlg.stop,ms.mapset_id, ms.name as mapset_name,ms.type_id,mr.name as marker_name,mr.platform_id,mr.reference_id,mr.dataset_marker_idx
                        from marker mr
                        left join marker_linkage_group mlg on mr.marker_id = mlg.marker_id
                        left join linkage_group lg on lg.linkage_group_id = mlg.linkage_group_id
                        left join mapset ms on ms.mapset_id = lg.map_id
                        where mr.dataset_marker_idx ? dsId::text
                )
                select mlgt.marker_name
                                ,p.name
                                ,mlgt.mapset_id
                        ,mlgt.mapset_name as mapset_name
                        ,cv.term as map_type
                        ,mlgt.linkage_group_name::text as lg_name
                        ,mlgt.lg_start::text as lg_start
                        ,mlgt.lg_stop::text as lg_stop
                        ,mlgt.start::text as mlg_start
                        ,mlgt.stop::text as mlg_stop
                        ,r.name,r.version
                from mlgt
                left join platform p on p.platform_id = mlgt.platform_id
                left join reference r on r.reference_id = mlgt.reference_id
                left join cv on cv_id =  mlgt.type_id
                order by mlgt.mapset_id, (mlgt.dataset_marker_idx->>dsId::text)::integer;
END;
$function$;