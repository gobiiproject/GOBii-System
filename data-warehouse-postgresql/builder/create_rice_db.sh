#!/usr/bin/env bash
psql -U appuser postgres -c "create database gobii_rice owner appuser;"
psql -U appuser gobii_rice < build_rice_vanilla.sql
