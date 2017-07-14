#!/usr/bin/env bash
psql -U appuser postgres -c "create database gobii_sorghum owner appuser;"
psql -U appuser gobii_sorghum < build_sorghum_vanilla.sql
