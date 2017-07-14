--liquibase formatted sql

--changeset kpalis:add_dataset_marker_idx context:general
alter table marker add column dataset_marker_idx jsonb default '{}'::jsonb;
--rollback alter table marker drop column dataset_marker_idx

--changeset kpalis:migrate_dataset_marker_data context:general splitStatements:false
do $$
declare
    ds record;
begin
	for ds in
		select distinct dataset_id from dataset_marker
	loop
		update marker m set dataset_marker_idx = dataset_marker_idx || ('{"'||dm.dataset_id||'": '||dm.marker_idx||'}')::jsonb
		from dataset_marker dm
		where m.marker_id = dm.marker_id
		and dm.dataset_id=ds.dataset_id
		and dm.marker_idx is not null;
	end loop;
end;
$$;
--will see if rollback is needed here

--changeset kpalis:add_dataset_dnarun_idx context:general
alter table dnarun add column dataset_dnarun_idx jsonb default '{}'::jsonb;
--rollback alter table dnarun drop column dataset_dnarun_idx

--changeset kpalis:migrate_dataset_dnarun_data context:general splitStatements:false
do $$
declare
    ds record;
begin
	for ds in
		select distinct dataset_id from dataset_marker
	loop
		update dnarun d set dataset_dnarun_idx = dataset_dnarun_idx || ('{"'||dd.dataset_id||'": '||dd.dnarun_idx||'}')::jsonb
		from dataset_dnarun dd
		where d.dnarun_id = dd.dnarun_id
		and dd.dataset_id = ds.dataset_id
		and dd.dnarun_idx is not null;
	end loop;
end;
$$;
--will see if rollback is needed here

--changeset kpalis:create_datasetmarkeridx_gin context:general
drop view if exists v_marker_metadata_by_dataset;
create index idx_marker_datasetmarkeridx on marker using gin (dataset_marker_idx); 
--changeset kpalis:create_datasetdnarunidx_gin context:general
create index idx_dnarun_datasetdnarunidx on dnarun using gin (dataset_dnarun_idx);

--changeset kpalis:drop_ds_marker context:general
drop table dataset_marker;

--changeset kpalis:drop_ds_dnarun context:general
drop table dataset_dnarun;
