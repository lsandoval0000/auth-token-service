--
-- PostgreSQL database dump
--

-- Dumped from database version 16.8
-- Dumped by pg_dump version 17.5

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: abc_bank; Type: SCHEMA; Schema: -; Owner: testing0000
--

CREATE SCHEMA abc_bank;


ALTER SCHEMA abc_bank OWNER TO testing0000;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: exchange_rate; Type: TABLE; Schema: abc_bank; Owner: testing0000
--

CREATE TABLE abc_bank.exchange_rate (
                                        id bigint NOT NULL,
                                        dni character varying(20) NOT NULL,
                                        report_date character varying(20) NOT NULL,
                                        request_date character varying(20) NOT NULL,
                                        sunat_value double precision NOT NULL,
                                        buy_price double precision NOT NULL,
                                        sell_price double precision NOT NULL
);


ALTER TABLE abc_bank.exchange_rate OWNER TO testing0000;

--
-- Name: exchange_rate_id_seq; Type: SEQUENCE; Schema: abc_bank; Owner: testing0000
--

CREATE SEQUENCE abc_bank.exchange_rate_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE abc_bank.exchange_rate_id_seq OWNER TO testing0000;

--
-- Name: exchange_rate_id_seq; Type: SEQUENCE OWNED BY; Schema: abc_bank; Owner: testing0000
--

ALTER SEQUENCE abc_bank.exchange_rate_id_seq OWNED BY abc_bank.exchange_rate.id;


--
-- Name: exchange_rate id; Type: DEFAULT; Schema: abc_bank; Owner: testing0000
--

ALTER TABLE ONLY abc_bank.exchange_rate ALTER COLUMN id SET DEFAULT nextval('abc_bank.exchange_rate_id_seq'::regclass);


--
-- Name: exchange_rate exchange_rate_pkey; Type: CONSTRAINT; Schema: abc_bank; Owner: testing0000
--

ALTER TABLE ONLY abc_bank.exchange_rate
    ADD CONSTRAINT exchange_rate_pkey PRIMARY KEY (id);


--
-- Name: generated_token_history; Type: TABLE; Schema: abc_bank; Owner: testing0000
--

CREATE TABLE abc_bank.generated_token_history (
                                                  id bigint NOT NULL,
                                                  token text NOT NULL,
                                                  refresh_token text,
                                                  expires_in bigint NOT NULL,
                                                  status character(1) NOT NULL
);


ALTER TABLE abc_bank.generated_token_history OWNER TO testing0000;

--
-- Name: generated_token_history_id_seq; Type: SEQUENCE; Schema: abc_bank; Owner: testing0000
--

CREATE SEQUENCE abc_bank.generated_token_history_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE abc_bank.generated_token_history_id_seq OWNER TO testing0000;

--
-- Name: generated_token_history_id_seq; Type: SEQUENCE OWNED BY; Schema: abc_bank; Owner: testing0000
--

ALTER SEQUENCE abc_bank.generated_token_history_id_seq OWNED BY abc_bank.generated_token_history.id;


--
-- Name: generated_token_history id; Type: DEFAULT; Schema: abc_bank; Owner: testing0000
--

ALTER TABLE ONLY abc_bank.generated_token_history ALTER COLUMN id SET DEFAULT nextval('abc_bank.generated_token_history_id_seq'::regclass);


--
-- Name: generated_token_history generated_token_history_pkey; Type: CONSTRAINT; Schema: abc_bank; Owner: testing0000
--

ALTER TABLE ONLY abc_bank.generated_token_history
    ADD CONSTRAINT generated_token_history_pkey PRIMARY KEY (id);

--
-- PostgreSQL database dump complete
--
