#!/usr/bin/env bash
psql -U appuser postgres -c "create database gobii_maize owner appuser;"
psql -U appuser gobii_maize < build_maize_vanilla.sql
