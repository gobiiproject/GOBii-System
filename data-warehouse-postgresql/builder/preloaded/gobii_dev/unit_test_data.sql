-- ********************************** TEST DATA **********************************

-- ***************************** ANALYSES
delete from analysis where analysis_id in (10,11,12);
update dataset
set analyses = null; 
insert
	into
		public.analysis(
			analysis_id,
			"name",
			description,
			type_id,
			program,
			programversion,
			algorithm,
			sourcename,
			sourceversion,
			sourceuri,
			reference_id,
			parameters,
			timeexecuted,
			status
		)
	values(
		10,
		'analysis 1',
		'description of analysis 1',
		33,
		'foo program',
		'1',
		'foo algorithm',
		'foo source',
		'',
		'foo source url',
		0,
		null,
		clock_timestamp(),
		0
	);


insert
	into
		public.analysis(
			analysis_id,
			"name",
			description,
			type_id,
			program,
			programversion,
			algorithm,
			sourcename,
			sourceversion,
			sourceuri,
			reference_id,
			parameters,
			timeexecuted,
			status
		)
	values(
		11,
		'analysis 2',
		'description of analysis 2',
		33,
		'bar program',
		'1',
		'bar algorithm',
		'foo source',
		'',
		'bar source url',
		0,
		null,
		clock_timestamp(),
		0
	);


insert
	into
		public.analysis(
			analysis_id,
			"name",
			description,
			type_id,
			program,
			programversion,
			algorithm,
			sourcename,
			sourceversion,
			sourceuri,
			reference_id,
			parameters,
			timeexecuted,
			status
		)
	values(
		12,
		'analysis 3',
		'description of analysis 3',
		33,
		'foobar program',
		'1',
		'foobar algorithm',
		'foobar source',
		'',
		'foobar source url',
		0,
		null,
		clock_timestamp(),
		0
	);


update dataset
set analyses = '{10,11,12}'
where dataset_id=2; 



INSERT INTO public.platform (platform_id,"name",code,vendor_id,description,created_by,created_date,modified_by,modified_date,status, type_id) VALUES (
2,'Illumina 02','dummycode',3,'Illumina Genotyping Platform','1',TO_DATE('2016-04-05','YYYY-MM-DD'),'1',TO_DATE('2016-04-05','YYYY-MM-DD'),1,35);
INSERT INTO public.platform (platform_id,"name",code,vendor_id,description,created_by,created_date,modified_by,modified_date,status, type_id) VALUES (
3,'Illumina 03','dummycode',3,'Illumina Genotyping Platform','1',TO_DATE('2016-04-05','YYYY-MM-DD'),'1',TO_DATE('2016-04-05','YYYY-MM-DD'),1,35);
 
 
 
 
 
 
 
 