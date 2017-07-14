#!/usr/bin/env bash
#usage: bash config_wrapper.sh <path-of-gobii_install.params>
#run this from the directory where gobiiconfig.jar is

source $1
echo "Path to bundle: " $BUNDLE_PATH/config
echo "Updating $CONFIG_XML via $1..."
cd $BUNDLE_PATH/config;
#Set root gobii directory (global)
java -jar gobiiconfig.jar -a -wfqpn $CONFIG_XML -gR "$BUNDLE_PATH";
#LDAP authentication options as well as "run as" user for digester/extractor.
java -jar gobiiconfig.jar -a -wfqpn $CONFIG_XML -auT $AUTH_TYPE -ldUDN "$LDAP_DN" -ldURL $LDAP_URL -ldBUSR "$LDAP_BIND_USER" -ldBPAS $LDAP_BIND_PASSWORD -ldraUSR $LDAP_BACKGROUND_USER -ldraPAS $LDAP_BACKGROUND_PASSWORD;
#Configure email server (global)
java -jar gobiiconfig.jar -a -wfqpn $CONFIG_XML -stE -soH $MAIL_HOST -soN $MAIL_PORT -soU $MAIL_USERNAME -soP $MAIL_PASSWORD -stT $MAIL_TYPE -stH $MAIL_HASH;

#Configure web server for crop1
java -jar gobiiconfig.jar -a -wfqpn $CONFIG_XML -c  $CROP1  -stW  -soH $WEB_HOST -soN $WEB_PORT -soR $CROP1_CONTEXT_PATH;
#Configure PostGRES server for crop1
java -jar gobiiconfig.jar -a -wfqpn $CONFIG_XML -c  $CROP1  -stP -soH $DB_HOST -soN $DB_PORT -soU $DB_USERNAME -soP $DB_PASS -soR $DB_NAME_CROP1;
#Configure MonetDB server for crop1
#java -jar gobiiconfig.jar -a -wfqpn $CONFIG_XML -c  ${bamboo.sys_int.crop1.name} -stM  -soH ${bamboo.sys_int.monetdb.host} -soN ${bamboo.sys_int.monetdb.port} -soU ${bamboo.sys_int.monetdb.appuser.name} -soP ${bamboo.sys_int.monetdb.appuser.password} -soR ${bamboo.sys_int.db.crop1.name};
#Set default crop to crop1 (global)
java -jar gobiiconfig.jar -a -wfqpn $CONFIG_XML -gD $CROP1;

#Configure web server for crop2
java -jar gobiiconfig.jar -a -wfqpn $CONFIG_XML -c  $CROP2  -stW  -soH $WEB_HOST -soN $WEB_PORT -soR $CROP2_CONTEXT_PATH;
#Configure PostGRES server for crop2
java -jar gobiiconfig.jar -a -wfqpn $CONFIG_XML -c  $CROP2  -stP -soH $DB_HOST -soN $DB_PORT -soU $DB_USERNAME -soP $DB_PASS -soR $DB_NAME_CROP2;
#Set default crop to crop2 (global)
java -jar gobiiconfig.jar -a -wfqpn $CONFIG_XML -gD $CROP2;

#Set log file directory (global)
java -jar gobiiconfig.jar -a -wfqpn $CONFIG_XML -gL  $BUNDLE_PATH/logs;
#Create the crop directory structure, ex. /data/gobii_bundle/crops/rice/*
#java -jar gobiiconfig.jar -a -wfqpn $CONFIG_XML -wdirs
#unfortunately, I can't get rid of this now. This is for setting the parameters for integration testing, which we don't need for production
java -jar gobiiconfig.jar -a -wfqpn $CONFIG_XML -gt  -gtcd $BUNDLE_PATH/test -gtcr  $CROP1  -gtcs  "java -jar gobiiconfig.jar"  -gtiu http://localhost:8080/$CROP1_CONTEXT_PATH -gtsf false -gtsh localhost -gtsp 22 -gtsu localhost -gtldu user2 -gtldp dummypass;
java -jar gobiiconfig.jar -a -wfqpn $CONFIG_XML -gt  -gtcd $BUNDLE_PATH/test -gtcr  $CROP2  -gtcs  "java -jar gobiiconfig.jar"  -gtiu http://localhost:8080/$CROP2_CONTEXT_PATH -gtsf false -gtsh localhost -gtsp 22 -gtsu localhost -gtldu user2 -gtldp dummypass;
#validate the new gobii configuration xml
java -jar gobiiconfig.jar -validate -wfqpn $CONFIG_XML;

echo "Done."