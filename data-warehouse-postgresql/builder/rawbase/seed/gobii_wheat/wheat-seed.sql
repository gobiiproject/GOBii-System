--Create Dummy Organizations
select * from createOrganization('Big Dummy Corporation','Main St. Bigtown USA','http://www.bigcorp.org/',1,NULL,NULL,NULL,1);
select * from createOrganization('Mom n Pop LLC.','My way, Smalltown USA','',1,NULL,NULL,NULL,1);

-- contact for the Orgs
select * from createContact('Manager','PI','contact_1','pi-big@there.com',( select array_agg(role_id) from role where role_name in ('PI','User')),1,NULL,NULL,NULL,(select organization_id from organization where name = 'Big Dummy Corporation'));
select * from createContact('Clerk','Curator','contact_2','curator-big@there.com',( select array_agg(role_id) from role where role_name in ('Curator','User')),1,NULL,NULL,NULL,(select organization_id from organization where name = 'Big Dummy Corporation'));
select * from createContact('Mom','PI','contact_1','pi-mp@there.com',( select array_agg(role_id) from role where role_name in ('PI','User')),1,NULL,NULL,NULL,(select organization_id from organization where name = 'Mom n Pop LLC.'));
select * from createContact('Pop','PI','contact_2','curator-mp@there.com',( select array_agg(role_id) from role where role_name in ('Curator','User')),1,NULL,NULL,NULL,(select organization_id from organization where name = 'Mom n Pop LLC.'));

