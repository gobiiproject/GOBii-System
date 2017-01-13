#!/usr/bin/env bash
psql -U appuser postgres -c "create database gobii_wheat owner appuser;"
psql -U appuser gobii_wheat < build_wheat_vanilla.sql
