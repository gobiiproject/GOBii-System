--
-- PostgreSQL database dump
--

-- Dumped from database version 9.5.1
-- Dumped by pg_dump version 9.5.1

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

SET search_path = public, pg_catalog;

--
-- Data for Name: role; Type: TABLE DATA; Schema: public; Owner: -
--

COPY role (role_id, role_name, role_code, read_tables, write_tables) FROM stdin;
1	PI	PI_code	\N	\N
2	Read-only	Readonly_code	\N	\N
3	Curator	Curator_code	\N	\N
4	Vendor	Vendor	\N	\N
5	Admin	Admin	\N	\N
6	User	User	\N	\N
\.


--
-- Name: role_role_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('role_role_id_seq', 5, true);


--
-- PostgreSQL database dump complete
--

