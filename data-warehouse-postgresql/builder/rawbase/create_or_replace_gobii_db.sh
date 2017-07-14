#!/usr/bin/env bash
#params: $1 = database_name; $2 = user_name
#Just to demonstrate GOBII CI/CD to the team
echo "Params: " $1 $2 $3 $4 $5
export PGPASSWORD=$5
echo "Killing existing sessions for $1 if any..."
psql -h "$3" -p $4 -U $2 postgres -c "SELECT pg_terminate_backend(pg_stat_activity.pid) FROM pg_stat_activity WHERE pg_stat_activity.datname = '$1'  AND pid <> pg_backend_pid();"
echo "Dropping database $1..."
psql -h "$3" -p $4 -U $2 postgres -c "drop database $1;"
echo "Creating database $1..."
psql -h "$3" -p $4 -U $2 postgres -c "create database $1 owner $2;"
echo "Populating database $1..."
psql -h "$3" -p $4 -U $2 $1 -f build_gobii_pg.sql
