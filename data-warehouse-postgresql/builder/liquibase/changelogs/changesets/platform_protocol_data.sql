--liquibase formatted sql

--changeset raza:Platform_seed_data context:seed_general splitStatements:false
select * from createplatform('Illumina_Infinium','Illumina_Infinium','',1,NULL,NULL,NULL,1,NULL);
select * from createplatform('Illumina_Goldengate','Illumina_Goldengate','',1,NULL,NULL,NULL,1,NULL); 
select * from createplatform('Illumina_TSCA','Illumina_TSCA','',1,NULL,NULL,NULL,1,NULL); 
select * from createplatform('KASP','KASP','',1,NULL,NULL,NULL,1,NULL); 
select * from createplatform('Sequencing','Sequencing','',1,NULL,NULL,NULL,1,NULL); 
select * from createplatform('Affymetrix_Axiom','Affymetrix_Axiom','',1,NULL,NULL,NULL,1,NULL); 
select * from createplatform('Dartseq_snps','Dartseq_snps','',1,NULL,NULL,NULL,1,NULL); 
select * from createplatform('Dart_clone','Dart_clone','',1,NULL,NULL,NULL,1,NULL); 
select * from createplatform('Dart_Amplicon','Dart_Amplicon','',1,NULL,NULL,NULL,1,NULL); 
select * from createplatform('SSR_STS_CAPS','SSR_STS_CAPS','',1,NULL,NULL,NULL,1,NULL); 
select * from createplatform('InDels','InDels','',1,NULL,NULL,NULL,1,NULL); 

--changeset raza:Protocol_seed_data context:seed_general splitStatements:false
select * from createProtocol('Illumina_Infinium','Illumina_Infinium',NULL,(select platform_id from platform where name ='Illumina_Infinium' ),null,null,null,null,1);
select * from createProtocol('Illumina_Goldengate','Illumina_Goldengate',NULL,(select platform_id from platform where name ='Illumina_Goldengate' ),null,null,null,null,1);
select * from createProtocol('Illumina_TSCA','Illumina_TSCA',NULL,(select platform_id from platform where name ='Illumina_TSCA' ),null,null,null,null,1);
select * from createProtocol('KASP','KASP',NULL,(select platform_id from platform where name ='KASP' ),null,null,null,null,1);
select * from createProtocol('WGRS','WGRS',NULL,(select platform_id from platform where name ='Illumina_Goldengate' ),null,null,null,null,1);
select * from createProtocol('GbS_Pst','GbS_Pst',NULL,(select platform_id from platform where name ='Sequencing' ),null,null,null,null,1);
select * from createProtocol('GbS_Apek','GbS_Apek',NULL,(select platform_id from platform where name ='Sequencing' ),null,null,null,null,1);
select * from createProtocol('rAmpSeq','Repeat Amplicon Sequencing',NULL,(select platform_id from platform where name ='Sequencing' ),null,null,null,null,1);
select * from createProtocol('Affymetrix_Axiom','Affymetrix_Axiom',NULL,(select platform_id from platform where name ='Affymetrix_Axiom' ),null,null,null,null,1);
select * from createProtocol('Dartseq_snps','Dartseq_snps',NULL,(select platform_id from platform where name ='Dartseq_snps' ),null,null,null,null,1);
select * from createProtocol('Dart_clone','Dart_clone',NULL,(select platform_id from platform where name ='Dart_clone' ),null,null,null,null,1);
select * from createProtocol('Silico_DArT','Silico_DArT',NULL,(select platform_id from platform where name ='Dart_clone' ),null,null,null,null,1);
select * from createProtocol('Dart_Amplicon','Dart_Amplicon',NULL,(select platform_id from platform where name ='Dart_Amplicon' ),null,null,null,null,1);
select * from createProtocol('SSR','SSR',NULL,(select platform_id from platform where name ='SSR_STS_CAPS' ),null,null,null,null,1);
select * from createProtocol('InDels','InDels',NULL,(select platform_id from platform where name ='InDels' ),null,null,null,null,1);

