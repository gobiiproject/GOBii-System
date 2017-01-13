#!/usr/bin/env bash
psql -U appuser postgres -c "create database gobii_chickpea owner appuser;"
psql -U appuser gobii_chickpea < build_chickpea_vanilla.sql
