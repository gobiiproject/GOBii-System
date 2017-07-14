--liquibase formatted sql

--## GOBIIProp ##--
--changeset kpalis:create_gobiiprop_table context:general splitStatements:false
CREATE TABLE gobiiprop ( 
  gobiiprop_id         serial  NOT NULL,
  type_id              integer  NOT NULL,
  value              text,
  rank                 integer DEFAULT 0 NOT NULL,
  CONSTRAINT gobiiprop_pkey PRIMARY KEY ( gobiiprop_id ),
  CONSTRAINT gobiiprop_c1 UNIQUE ( type_id, rank ) 
 );

COMMENT ON TABLE gobiiprop IS 'This table is different from other prop tables/columns in the database, as it is for storing information about the database itself, like schema version';

COMMENT ON COLUMN gobiiprop.type_id IS 'The name of the property or slot is a cvterm. The meaning of the property is defined in that cvterm.';

COMMENT ON COLUMN gobiiprop."value" IS 'The value of the property, represented as text. Numeric values are converted to their text representation.';

COMMENT ON COLUMN gobiiprop.rank IS 'Property-Value ordering. Any
cv can have multiple values for any particular property type -
these are ordered in a list using rank, counting from zero. For
properties that are single-valued rather than multi-valued, the
default 0 value should be used.';

--changeset kpalis:start_tracking_dbversion context:general splitStatements:false
select createcvgroup('gobii_datawarehouse', 'CV terms that are for the internal usage of the data warehosue', 1);
select createCVinGroup('gobii_datawarehouse', 1, 'version', 'The version of a software, ex. the data warehouse.', 1, '', null, 1);
--first version
insert into gobiiprop (type_id, value, rank)
select cv_id, '1.0.0', 1 from cv where term='version' and cvgroup_id=(select cvgroup_id from cvgroup where name='gobii_datawarehouse' and type=1 );

--No need for other functions here but this one, as this table is for the internal use of the datawarehouse
--changeset kpalis:add_fxn_to_set_dbversion context:general splitStatements:false
CREATE OR REPLACE FUNCTION setDatawarehouseVersion(ver text)
  RETURNS integer
  LANGUAGE plpgsql
 AS $function$
     DECLARE
        i integer;
        propId integer;
     BEGIN
     select cv_id into propId from cv where term='version' and cvgroup_id=(select cvgroup_id from cvgroup where name='gobii_datawarehouse' and type=1 );
     update gobiiprop set value=ver
      where type_id=propId
      and rank=1;
     GET DIAGNOSTICS i = ROW_COUNT;
     return i;
     END;
 $function$;

