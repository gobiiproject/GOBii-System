--liquibase formatted sql

--changeset raza:platform_type_id_null context:general
ALTER TABLE PLATFORM 
ALTER COLUMN TYPE_ID DROP NOT NULL;
