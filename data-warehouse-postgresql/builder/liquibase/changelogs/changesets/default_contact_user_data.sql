--liquibase formatted sql

--changeset raza:default_contact_user_data context:seed_general splitStatements:false    
select * from createcontact('GOBII','User1','default_user_1','user1.gobii@gobii.org','{5,1,6}',1,current_date,1,current_date,NULL,'USER_READER');
select * from createcontact('GOBII Ldap','User2','default_user_2','user2.gobii@gobii.org','{5,1,6}',1,current_date,1,current_date,NULL,'gobii-user2');
