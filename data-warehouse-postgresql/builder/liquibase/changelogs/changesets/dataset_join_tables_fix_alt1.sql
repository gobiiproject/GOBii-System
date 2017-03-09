--liquibase formatted sql

--changeset kpalis:add_dataset_marker_idx context:general_alt
alter table marker add column dataset_marker_idx jsonb default '{}'::jsonb;
--rollback alter table marker drop column dataset_marker_idx

--changeset kpalis:migrate_dataset_marker_data context:general_alt splitStatements:false
do $$
declare
    ds record;	
begin
	for ds in
		select distinct dataset_id from dataset_marker
	loop
		--select jsonb_set(dataset_marker_idx, '{"d"}', dataset_marker_idx->'d' || '[11]', true) from t;
		update marker m set dataset_marker_idx = jsonb_set(dataset_marker_idx, '{"d"}', dataset_marker_idx->'d' || (concat('[',dm.dataset_id,']'))::jsonb, true)
		from dataset_marker dm
		where m.marker_id = dm.marker_id
		and dm.dataset_id=ds.dataset_id
		and dm.marker_idx is not null;

		update marker m set dataset_marker_idx = jsonb_set(dataset_marker_idx, (concat('{"',dm.dataset_id,'"}'))::text[], dm.marker_idx::text, true)
		from dataset_marker dm
		where m.marker_id = dm.marker_id
		and dm.dataset_id=ds.dataset_id
		and dm.marker_idx is not null;
	end loop;
end;
$$;
--will see if rollback is needed here
--changeset kpalis:add_dataset_dnarun_idx context:general_alt
alter table dnarun add column dataset_dnarun_idx jsonb default '{}'::jsonb;
--rollback alter table dnarun drop column dataset_dnarun_idx

--changeset kpalis:migrate_dataset_dnarun_data context:general_alt splitStatements:false
do $$
declare
    ds record;
begin
	for ds in
		select distinct dataset_id from dataset_marker
	loop 
		update dnarun d set dataset_dnarun_idx = dataset_dnarun_idx || ('{"d'||dd.dataset_id||'":'||dd.dataset_id||', "'||dd.dataset_id||'":'||dd.dnarun_idx||'}')::jsonb
		from dataset_dnarun dd
		where d.dnarun_id = dd.dnarun_id
		and dd.dataset_id = ds.dataset_id
		and dd.dnarun_idx is not null;
	end loop;
end;
$$;

--will see if rollback is needed here

--abc changeset kpalis:drop_ds_marker context:general_alt
--drop table dataset_marker;

--abc changeset kpalis:drop_ds_dnarun context:general_alt
--drop table dataset_dnarun;

--create indices
--create index idx_marker_datasetmarkeridx on marker using gin (dataset_marker_idx jsonb_path_ops); --note: this only supports @>
--changeset kpalis:create_datasetmarkeridx_ginp context:general_alt
create index idxginops_marker_datasetmarkeridx on marker using gin (dataset_marker_idx jsonb_path_ops); 
--changeset kpalis:create_datasetmarkeridx_gin context:general_alt
create index idxgin_marker_datasetmarkeridx on marker using gin (dataset_marker_idx); 
--changeset kpalis:create_datasetdnarunidx_ginp context:general_alt
create index idxginops_dnarun_datasetdnarunidx on dnarun using gin (dataset_dnarun_idx jsonb_path_ops);
	--changeset kpalis:create_datasetdnarunidx_gin context:general_alt
create index idxgin_dnarun_datasetdnarunidx on dnarun using gin (dataset_dnarun_idx);

--NOTE: Since GIN index won't be used for -> ops, we query in two steps, use ? first, the query the result for the values!
/*select m.dataset_marker_idx = m.dataset_marker_idx || ('{"'||dm.dataset_id::text||'": '||dm.marker_idx||'}')::jsonb
	from dataset_marker dm, marker m
	where m.marker_id = dm.marker_id
	and m.name = '10003037';

select ('{"'||dm.dataset_id::text||'": '||dm.marker_idx||'}')::jsonb
	from dataset_marker dm, marker m
	where m.marker_id = dm.marker_id
	and m.name = '10003037';

select m.marker_id, dm.dataset_marker_id, m.name, dm.dataset_id, dm.marker_idx
	from dataset_marker dm, marker m
	where m.marker_id = dm.marker_id
	and name = '10003037';

select * from marker where name = '10003037';*/