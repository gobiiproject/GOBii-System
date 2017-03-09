#!/usr/bin/env bash
java -Xmx2G -jar bin/liquibase.jar --changeLogFile="./data/exportTablesData.sql" --diffTypes="data" generateChangeLog
