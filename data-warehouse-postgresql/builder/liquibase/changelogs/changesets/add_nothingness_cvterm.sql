--liquibase formatted sql

--changeset kpalis:fix_blanks_issue_GP1-857 context:general splitStatements:false
insert into cv (term, definition, rank, cvgroup_id) select '', 'The representation of nothingness.', 0, cvgroup_id from cvgroup where name='gobii_datawarehouse';
