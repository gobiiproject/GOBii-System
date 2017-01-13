#!/usr/bin/env bash
psql -U postgres -c "create user appuser with superuser password 'g0b11isw3s0m3' valid until 'infinity';"
