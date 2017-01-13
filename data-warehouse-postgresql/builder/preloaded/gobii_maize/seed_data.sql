

-- Supplement to rice database
INSERT INTO public.role
(role_id, role_name, role_code, read_tables, write_tables)
VALUES(1, 'PI', 'r1', null, null);


INSERT INTO public.role
(role_id, role_name, role_code, read_tables, write_tables)
VALUES(2, 'Curator', 'r1', null, null);

update contact
set roles = '{1}'
where contact_id=1;

update contact
set roles = '{2}'
where contact_id=2;

update contact
set roles = '{1,2}'
where contact_id=3;

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



-- ******************* POPULATE DISPLAY TABLE
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
1,'analysis','algorithm','algorithm',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
2,'analysis','analysis_id','analysis_id',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
3,'analysis','description','description',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
4,'analysis','name','name',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
5,'analysis','parameters','parameters',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
6,'analysis','program','program',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
7,'analysis','programversion','programversion',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
8,'analysis','reference_id','reference_id',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
9,'analysis','sourcename','sourcename',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
10,'analysis','sourceuri','sourceuri',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
11,'analysis','sourceversion','sourceversion',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
12,'analysis','status','status',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
13,'analysis','timeexecuted','timeexecuted',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
14,'analysis','type_id','type_id',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
15,'contact','code','code',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
16,'contact','contact_id','contact_id',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
17,'contact','created_by','created_by',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
18,'contact','created_date','created_date',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
19,'contact','email','email',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
20,'contact','firstname','firstname',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
21,'contact','lastname','lastname',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
22,'contact','modified_by','modified_by',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
23,'contact','modified_date','modified_date',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
24,'contact','roles','roles',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
25,'cv','cv_id','cv_id',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
26,'cv','definition','definition',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
27,'cv','group','group',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
28,'cv','rank','rank',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
29,'cv','term','term',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
30,'dataset','analyses','analyses',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
31,'dataset','callinganalysis_id','callinganalysis_id',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
32,'dataset','created_by','created_by',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
33,'dataset','created_date','created_date',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
34,'dataset','data_file','data_file',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
35,'dataset','data_table','data_table',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
36,'dataset','dataset_id','dataset_id',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
37,'dataset','experiment_id','experiment_id',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
38,'dataset','modified_by','modified_by',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
39,'dataset','modified_date','modified_date',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
40,'dataset','quality_file','quality_file',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
41,'dataset','quality_table','quality_table',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
42,'dataset','scores','scores',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
43,'dataset','status','status',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
44,'dataset_dnarun','dataset_dnarun_id','dataset_dnarun_id',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
45,'dataset_dnarun','dataset_id','dataset_id',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
46,'dataset_dnarun','dnarun_id','dnarun_id',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
47,'dataset_marker','call_rate','call_rate',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
48,'dataset_marker','dataset_id','dataset_id',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
49,'dataset_marker','dataset_marker_id','dataset_marker_id',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
50,'dataset_marker','maf','maf',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
51,'dataset_marker','marker_id','marker_id',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
52,'dataset_marker','reproducibility','reproducibility',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
53,'dataset_marker','scores','scores',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
54,'display','column_name','column_name',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
55,'display','created_by','created_by',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
56,'display','created_date','created_date',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
57,'display','display_id','display_id',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
58,'display','display_name','display_name',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
59,'display','modified_by','modified_by',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
60,'display','modified_date','modified_date',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
61,'display','table_name','table_name',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
62,'dnarun','code','code',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
63,'dnarun','dnarun_id','dnarun_id',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
64,'dnarun','dnasample_id','dnasample_id',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
65,'dnarun','experiment_id','experiment_id',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
66,'dnarun','name','name',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
67,'dnarun_prop','dnarun_id','dnarun_id',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
68,'dnarun_prop','dnarun_prop_id','dnarun_prop_id',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
69,'dnarun_prop','props','props',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
70,'dnasample','code','code',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
71,'dnasample','created_by','created_by',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
72,'dnasample','created_date','created_date',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
73,'dnasample','dnasample_id','dnasample_id',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
74,'dnasample','germplasm_id','germplasm_id',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
75,'dnasample','modified_by','modified_by',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
76,'dnasample','modified_date','modified_date',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
77,'dnasample','name','name',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
78,'dnasample','num','num',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
79,'dnasample','platename','platename',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
80,'dnasample','project_id','project_id',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
81,'dnasample','status','status',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
82,'dnasample','well_col','well_col',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
83,'dnasample','well_row','well_row',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
84,'dnasample_prop','dnasample_id','dnasample_id',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
85,'dnasample_prop','dnasample_prop_id','dnasample_prop_id',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
86,'dnasample_prop','props','props',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
87,'experiment','code','code',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
88,'experiment','created_by','created_by',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
89,'experiment','created_date','created_date',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
90,'experiment','data_file','data_file',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
91,'experiment','experiment_id','experiment_id',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
92,'experiment','manifest_id','manifest_id',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
93,'experiment','modified_by','modified_by',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
94,'experiment','modified_date','modified_date',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
95,'experiment','name','name',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
96,'experiment','platform_id','platform_id',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
97,'experiment','project_id','project_id',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
98,'experiment','status','status',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
99,'germplasm','code','code',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
100,'germplasm','created_by','created_by',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
101,'germplasm','created_date','created_date',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
102,'germplasm','germplasm_id','germplasm_id',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
103,'germplasm','modified_by','modified_by',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
104,'germplasm','modified_date','modified_date',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
105,'germplasm','name','name',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
106,'germplasm','species_id','species_id',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
107,'germplasm','status','status',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
108,'germplasm','type_id','type_id',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
109,'germplasm_prop','germplasm_id','germplasm_id',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
110,'germplasm_prop','germplasm_prop_id','germplasm_prop_id',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
111,'germplasm_prop','props','props',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
112,'manifest','code','code',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
113,'manifest','created_by','created_by',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
114,'manifest','created_date','created_date',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
115,'manifest','file_path','file_path',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
116,'manifest','manifest_id','manifest_id',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
117,'manifest','modified_by','modified_by',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
118,'manifest','modified_date','modified_date',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
119,'manifest','name','name',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
120,'map','code','code',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
121,'map','created_by','created_by',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
122,'map','created_date','created_date',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
123,'map','description','description',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
124,'map','map_id','map_id',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
125,'map','modified_by','modified_by',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
126,'map','modified_date','modified_date',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
127,'map','name','name',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
128,'map','reference_id','reference_id',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
129,'map','status','status',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
130,'map','type','type',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
131,'map_prop','map_id','map_id',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
132,'map_prop','map_prop_id','map_prop_id',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
133,'map_prop','props','props',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
134,'marker','alts','alts',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
135,'marker','code','code',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
136,'marker','marker_id','marker_id',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
137,'marker','name','name',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
138,'marker','platform_id','platform_id',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
139,'marker','primers','primers',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
140,'marker','probsets','probsets',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
141,'marker','ref','ref',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
142,'marker','reference_id','reference_id',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
143,'marker','sequence','sequence',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
144,'marker','status','status',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
145,'marker','strand_id','strand_id',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
146,'marker','variant_id','variant_id',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
147,'marker_group','code','code',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
148,'marker_group','create_date','create_date',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
149,'marker_group','created_by','created_by',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
150,'marker_group','germplasm_group','germplasm_group',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
151,'marker_group','marker_group_id','marker_group_id',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
152,'marker_group','markers','markers',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
153,'marker_group','modified_by','modified_by',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
154,'marker_group','modified_date','modified_date',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
155,'marker_group','name','name',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
156,'marker_group','status','status',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
157,'marker_map','linkage_group_id','linkage_group_id',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
158,'marker_map','map_id','map_id',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
159,'marker_map','marker_id','marker_id',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
160,'marker_map','marker_map_id','marker_map_id',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
161,'marker_map','position','position',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
162,'marker_map','start','start',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
163,'marker_map','stop','stop',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
164,'marker_prop','marker_id','marker_id',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
165,'marker_prop','marker_prop_id','marker_prop_id',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
166,'marker_prop','props','props',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
167,'platform','code','code',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
168,'platform','created_by','created_by',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
169,'platform','created_date','created_date',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
170,'platform','description','description',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
171,'platform','modified_by','modified_by',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
172,'platform','modified_date','modified_date',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
173,'platform','name','name',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
174,'platform','platform_id','platform_id',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
175,'platform','status','status',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
176,'platform','vendor_id','vendor_id',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
177,'platform_prop','platform_id','platform_id',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
178,'platform_prop','platform_prop_id','platform_prop_id',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
179,'platform_prop','props','props',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
180,'project','code','code',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
181,'project','created_by','created_by',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
182,'project','created_date','created_date',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
183,'project','description','description',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
184,'project','modified_by','modified_by',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
185,'project','modified_date','modified_date',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
186,'project','name','name',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
187,'project','pi_contact','pi_contact',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
188,'project','project_id','project_id',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
189,'project','status','status',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
190,'project_prop','project_id','project_id',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
191,'project_prop','project_prop_id','project_prop_id',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
192,'project_prop','props','props',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
193,'project_view','code','code',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
194,'project_view','description','description',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
195,'project_view','name','name',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
196,'project_view','pi_contact','pi_contact',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
197,'project_view','project_id','project_id',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
198,'reference','file_path','file_path',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
199,'reference','link','link',NULL,NULL,NULL,NULL);	
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
200,'reference','name','name',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
201,'reference','reference_id','reference_id',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
202,'reference','version','version',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
203,'role','read_tables','read_tables',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
204,'role','role_code','role_code',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
205,'role','role_id','role_id',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
206,'role','role_name','role_name',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
207,'role','write_tables','write_tables',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
208,'snp_map','alt_allele','alt_allele',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
209,'snp_map','chr','chr',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
210,'snp_map','name','name',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
211,'snp_map','pos','pos',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
212,'snp_map','ref_allele','ref_allele',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
213,'variant','code','code',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
214,'variant','created_by','created_by',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
215,'variant','created_date','created_date',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
216,'variant','modified_by','modified_by',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
217,'variant','modified_date','modified_date',NULL,NULL,NULL,NULL);
INSERT INTO display (display_id,table_name,column_name,display_name,created_by,created_date,modified_by,modified_date) VALUES (
218,'variant','variant_id','variant_id',NULL,NULL,NULL,NULL);




INSERT INTO public.platform (platform_id,"name",code,vendor_id,description,created_by,created_date,modified_by,modified_date,status, type_id) VALUES (
2,'Illumina 02','dummycode',3,'Illumina Genotyping Platform','1',TO_DATE('2016-04-05','YYYY-MM-DD'),'1',TO_DATE('2016-04-05','YYYY-MM-DD'),1,35);
INSERT INTO public.platform (platform_id,"name",code,vendor_id,description,created_by,created_date,modified_by,modified_date,status, type_id) VALUES (
3,'Illumina 03','dummycode',3,'Illumina Genotyping Platform','1',TO_DATE('2016-04-05','YYYY-MM-DD'),'1',TO_DATE('2016-04-05','YYYY-MM-DD'),1,35);
 