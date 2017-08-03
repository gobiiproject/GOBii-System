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
-- Data for Name: contact; Type: TABLE DATA; Schema: public; Owner: -
--

COPY contact (contact_id, lastname, firstname, code, email, roles, created_by, created_date, modified_by, modified_date) FROM stdin;
1	GOBII	User	gobii_user_1	user.gobii@gobii.com	{5}	1	2016-05-27	\N	2016-05-27
\.


--
-- Name: contact_contact_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('contact_contact_id_seq', 1, true);


--
-- PostgreSQL database dump complete
--

