--
-- PostgreSQL database dump
--

-- Dumped from database version 8.4.12
-- Dumped by pg_dump version 9.4.0
-- Started on 2016-07-18 00:16:49

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = off;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET escape_string_warning = off;

--
-- TOC entry 639 (class 2612 OID 16386)
-- Name: plpgsql; Type: PROCEDURAL LANGUAGE; Schema: -; Owner: postgres
--

CREATE OR REPLACE PROCEDURAL LANGUAGE plpgsql;


ALTER PROCEDURAL LANGUAGE plpgsql OWNER TO postgres;

SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 140 (class 1259 OID 159336)
-- Name: analista; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE analista (
    administrador boolean,
    id_analista integer NOT NULL
);


ALTER TABLE analista OWNER TO postgres;

--
-- TOC entry 141 (class 1259 OID 159341)
-- Name: banco; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE banco (
    id_banco integer NOT NULL,
    fecha_creacion timestamp without time zone,
    fecha_ultima_modificacion timestamp without time zone,
    estatus integer,
    nombre character varying(255)
);


ALTER TABLE banco OWNER TO postgres;

--
-- TOC entry 175 (class 1259 OID 159777)
-- Name: banco_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE banco_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE banco_id_seq OWNER TO postgres;

--
-- TOC entry 142 (class 1259 OID 159346)
-- Name: ciudad; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE ciudad (
    id_ciudad integer NOT NULL,
    fecha_creacion timestamp without time zone,
    fecha_ultima_modificacion timestamp without time zone,
    nombre character varying(255),
    id_estado integer
);


ALTER TABLE ciudad OWNER TO postgres;

--
-- TOC entry 143 (class 1259 OID 159351)
-- Name: clasificacion_repuesto; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE clasificacion_repuesto (
    id_clasificacion_repuesto integer NOT NULL,
    fecha_creacion timestamp without time zone,
    fecha_ultima_modificacion timestamp without time zone,
    descripcion character varying(255),
    estatus character varying(255)
);


ALTER TABLE clasificacion_repuesto OWNER TO postgres;

--
-- TOC entry 176 (class 1259 OID 159779)
-- Name: clasificacion_repuesto_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE clasificacion_repuesto_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE clasificacion_repuesto_id_seq OWNER TO postgres;

--
-- TOC entry 144 (class 1259 OID 159359)
-- Name: cliente; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE cliente (
    id_cliente integer NOT NULL
);


ALTER TABLE cliente OWNER TO postgres;

--
-- TOC entry 145 (class 1259 OID 159364)
-- Name: compra; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE compra (
    id_compra integer NOT NULL,
    fecha_creacion timestamp without time zone,
    fecha_ultima_modificacion timestamp without time zone,
    estatus character varying(255),
    observacion character varying(255),
    precio_flete real,
    precio_venta real,
    tipo_flete boolean,
    id_historico_moneda integer,
    id_requerimiento integer
);


ALTER TABLE compra OWNER TO postgres;

--
-- TOC entry 177 (class 1259 OID 159781)
-- Name: compra_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE compra_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE compra_id_seq OWNER TO postgres;

--
-- TOC entry 146 (class 1259 OID 159372)
-- Name: configuracion; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE configuracion (
    id_configuracion integer NOT NULL,
    fecha_creacion timestamp without time zone,
    fecha_ultima_modificacion timestamp without time zone,
    porct_ganancia real,
    porct_iva real,
    valor_libra real
);


ALTER TABLE configuracion OWNER TO postgres;

--
-- TOC entry 147 (class 1259 OID 159377)
-- Name: cotizacion; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE cotizacion (
    id_cotizacion integer NOT NULL,
    fecha_creacion timestamp without time zone,
    fecha_ultima_modificacion timestamp without time zone,
    condiciones character varying(255),
    estatus character varying(255),
    fecha_vencimiento timestamp without time zone,
    mensaje character varying(255),
    precio_flete real,
    tipo boolean,
    id_historico_moneda integer,
    id_proveedor integer
);


ALTER TABLE cotizacion OWNER TO postgres;

--
-- TOC entry 178 (class 1259 OID 159783)
-- Name: cotizacion_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE cotizacion_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE cotizacion_id_seq OWNER TO postgres;

--
-- TOC entry 148 (class 1259 OID 159385)
-- Name: deposito; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE deposito (
    id_deposito integer NOT NULL,
    fecha_creacion timestamp without time zone,
    fecha_ultima_modificacion timestamp without time zone,
    descripcion character varying(255),
    estatus character varying(255),
    fecha_deposito timestamp without time zone,
    monto real,
    numero character varying(255),
    id_pago integer
);


ALTER TABLE deposito OWNER TO postgres;

--
-- TOC entry 179 (class 1259 OID 159785)
-- Name: deposito_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE deposito_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE deposito_id_seq OWNER TO postgres;

--
-- TOC entry 149 (class 1259 OID 159393)
-- Name: detalle_cotizacion; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE detalle_cotizacion (
    id_detalle_cotizacion integer NOT NULL,
    fecha_creacion timestamp without time zone,
    fecha_ultima_modificacion timestamp without time zone,
    cantidad bigint,
    estatus character varying(255),
    marca_repuesto character varying(255),
    precio_flete real,
    precio_venta real,
    tipo_repuesto boolean,
    id_cotizacion integer,
    id_detalle_requerimiento integer
);


ALTER TABLE detalle_cotizacion OWNER TO postgres;

--
-- TOC entry 180 (class 1259 OID 159787)
-- Name: detalle_cotizacion_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE detalle_cotizacion_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE detalle_cotizacion_id_seq OWNER TO postgres;

--
-- TOC entry 150 (class 1259 OID 159401)
-- Name: detalle_cotizacion_internacional; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE detalle_cotizacion_internacional (
    alto real,
    ancho real,
    forma_envio boolean,
    largo real,
    peso real,
    tipo_flete boolean,
    valor_libra real,
    id_detalle_cotizacion_internacional integer NOT NULL
);


ALTER TABLE detalle_cotizacion_internacional OWNER TO postgres;

--
-- TOC entry 151 (class 1259 OID 159406)
-- Name: detalle_oferta; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE detalle_oferta (
    id_detalle_oferta integer NOT NULL,
    fecha_creacion timestamp without time zone,
    fecha_ultima_modificacion timestamp without time zone,
    aprobado boolean,
    cantidad_seleccionada bigint,
    estatus character varying(255),
    estatus_favorito boolean,
    id_compra integer,
    id_detalle_cotizacion integer,
    id_oferta integer,
    id_orden_compra integer
);


ALTER TABLE detalle_oferta OWNER TO postgres;

--
-- TOC entry 181 (class 1259 OID 159789)
-- Name: detalle_oferta_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE detalle_oferta_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE detalle_oferta_id_seq OWNER TO postgres;

--
-- TOC entry 152 (class 1259 OID 159411)
-- Name: detalle_requerimiento; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE detalle_requerimiento (
    id_detalle_requerimiento integer NOT NULL,
    fecha_creacion timestamp without time zone,
    fecha_ultima_modificacion timestamp without time zone,
    cantidad bigint,
    codigo_oem character varying(255),
    descripcion character varying(255),
    estatus character varying(255),
    foto bytea,
    peso real,
    id_clasificacion_repuesto integer,
    id_requerimiento integer
);


ALTER TABLE detalle_requerimiento OWNER TO postgres;

--
-- TOC entry 182 (class 1259 OID 159791)
-- Name: detalle_requerimiento_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE detalle_requerimiento_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE detalle_requerimiento_id_seq OWNER TO postgres;

--
-- TOC entry 153 (class 1259 OID 159419)
-- Name: estado; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE estado (
    id_estado integer NOT NULL,
    fecha_creacion timestamp without time zone,
    fecha_ultima_modificacion timestamp without time zone,
    nombre character varying(255)
);


ALTER TABLE estado OWNER TO postgres;

--
-- TOC entry 154 (class 1259 OID 159424)
-- Name: forma_pago; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE forma_pago (
    id_forma_pago integer NOT NULL,
    fecha_creacion timestamp without time zone,
    fecha_ultima_modificacion timestamp without time zone,
    estatus character varying(255),
    nombre character varying(255),
    url character varying(255)
);


ALTER TABLE forma_pago OWNER TO postgres;

--
-- TOC entry 183 (class 1259 OID 159793)
-- Name: forma_pago_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE forma_pago_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE forma_pago_id_seq OWNER TO postgres;

--
-- TOC entry 184 (class 1259 OID 159795)
-- Name: hibernate_sequence; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE hibernate_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE hibernate_sequence OWNER TO postgres;

--
-- TOC entry 155 (class 1259 OID 159432)
-- Name: historico_moneda; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE historico_moneda (
    id_historia integer NOT NULL,
    fecha_creacion timestamp without time zone,
    fecha_ultima_modificacion timestamp without time zone,
    estatus character varying(255),
    monto_conversion real,
    id_moneda integer
);


ALTER TABLE historico_moneda OWNER TO postgres;

--
-- TOC entry 185 (class 1259 OID 159797)
-- Name: historico_moneda_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE historico_moneda_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE historico_moneda_id_seq OWNER TO postgres;

--
-- TOC entry 156 (class 1259 OID 159437)
-- Name: history_logins; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE history_logins (
    id integer NOT NULL,
    fecha_creacion timestamp without time zone,
    fecha_ultima_modificacion timestamp without time zone,
    date_login timestamp without time zone,
    date_logout timestamp without time zone,
    username character varying(20) NOT NULL
);


ALTER TABLE history_logins OWNER TO postgres;

--
-- TOC entry 186 (class 1259 OID 159799)
-- Name: history_logins_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE history_logins_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE history_logins_id_seq OWNER TO postgres;

--
-- TOC entry 157 (class 1259 OID 159442)
-- Name: marca_vehiculo; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE marca_vehiculo (
    id_marca_vehiculo integer NOT NULL,
    fecha_creacion timestamp without time zone,
    fecha_ultima_modificacion timestamp without time zone,
    estatus character varying(255),
    nombre character varying(255)
);


ALTER TABLE marca_vehiculo OWNER TO postgres;

--
-- TOC entry 187 (class 1259 OID 159801)
-- Name: marca_vehiculo_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE marca_vehiculo_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE marca_vehiculo_id_seq OWNER TO postgres;

--
-- TOC entry 158 (class 1259 OID 159450)
-- Name: menu; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE menu (
    id_menu integer NOT NULL,
    fecha_creacion timestamp without time zone,
    fecha_ultima_modificacion timestamp without time zone,
    actividad character varying(255),
    icono character varying(255),
    nombre character varying(255),
    ruta character varying(255),
    tipo integer,
    id_padre integer
);


ALTER TABLE menu OWNER TO postgres;

--
-- TOC entry 188 (class 1259 OID 159803)
-- Name: menu_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE menu_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE menu_id_seq OWNER TO postgres;

--
-- TOC entry 159 (class 1259 OID 159458)
-- Name: moneda; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE moneda (
    id_moneda integer NOT NULL,
    fecha_creacion timestamp without time zone,
    fecha_ultima_modificacion timestamp without time zone,
    estatus character varying(255),
    nombre character varying(255),
    pais boolean,
    simbolo character varying(255)
);


ALTER TABLE moneda OWNER TO postgres;

--
-- TOC entry 189 (class 1259 OID 159805)
-- Name: moneda_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE moneda_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE moneda_id_seq OWNER TO postgres;

--
-- TOC entry 160 (class 1259 OID 159466)
-- Name: motor; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE motor (
    id_motor integer NOT NULL,
    fecha_creacion timestamp without time zone,
    fecha_ultima_modificacion timestamp without time zone,
    nombre character varying(255)
);


ALTER TABLE motor OWNER TO postgres;

--
-- TOC entry 190 (class 1259 OID 159807)
-- Name: motor_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE motor_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE motor_id_seq OWNER TO postgres;

--
-- TOC entry 161 (class 1259 OID 159471)
-- Name: oferta; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE oferta (
    id_oferta integer NOT NULL,
    fecha_creacion timestamp without time zone,
    fecha_ultima_modificacion timestamp without time zone,
    estatus character varying(255),
    porct_ganancia real,
    porct_iva real,
    id_re_cotizacion integer
);


ALTER TABLE oferta OWNER TO postgres;

--
-- TOC entry 191 (class 1259 OID 159809)
-- Name: oferta_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE oferta_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE oferta_id_seq OWNER TO postgres;

--
-- TOC entry 162 (class 1259 OID 159476)
-- Name: orden_compra; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE orden_compra (
    id_orden_compra integer NOT NULL,
    fecha_creacion timestamp without time zone,
    fecha_ultima_modificacion timestamp without time zone,
    estatus character varying(255),
    iva real,
    observacion character varying(255),
    id_pago_proveedor integer
);


ALTER TABLE orden_compra OWNER TO postgres;

--
-- TOC entry 192 (class 1259 OID 159811)
-- Name: orden_compra_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE orden_compra_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE orden_compra_id_seq OWNER TO postgres;

--
-- TOC entry 163 (class 1259 OID 159484)
-- Name: pago; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE pago (
    id integer NOT NULL,
    fecha_creacion timestamp without time zone,
    fecha_ultima_modificacion timestamp without time zone,
    descripcion character varying(255),
    estatus character varying(255),
    fechapago timestamp without time zone,
    monto real,
    transaction_id character varying(255),
    id_banco integer,
    id_forma_pago integer
);


ALTER TABLE pago OWNER TO postgres;

--
-- TOC entry 164 (class 1259 OID 159492)
-- Name: pago_cliente; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE pago_cliente (
    id_pago_cliente integer NOT NULL,
    id_compra integer
);


ALTER TABLE pago_cliente OWNER TO postgres;

--
-- TOC entry 193 (class 1259 OID 159813)
-- Name: pago_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE pago_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE pago_id_seq OWNER TO postgres;

--
-- TOC entry 165 (class 1259 OID 159497)
-- Name: pago_proveedor; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE pago_proveedor (
    id_pago_proveedor integer NOT NULL
);


ALTER TABLE pago_proveedor OWNER TO postgres;

--
-- TOC entry 166 (class 1259 OID 159502)
-- Name: pais; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE pais (
    id_pais integer NOT NULL,
    nombre character varying(255),
    id_moneda integer
);


ALTER TABLE pais OWNER TO postgres;

--
-- TOC entry 194 (class 1259 OID 159815)
-- Name: pais_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE pais_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE pais_id_seq OWNER TO postgres;

--
-- TOC entry 167 (class 1259 OID 159507)
-- Name: persistent_logins; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE persistent_logins (
    series character varying(64) NOT NULL,
    fecha_creacion timestamp without time zone,
    fecha_ultima_modificacion timestamp without time zone,
    last_used timestamp without time zone NOT NULL,
    token character varying(64) NOT NULL,
    username character varying(20) NOT NULL
);


ALTER TABLE persistent_logins OWNER TO postgres;

--
-- TOC entry 168 (class 1259 OID 159512)
-- Name: persona; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE persona (
    id integer NOT NULL,
    fecha_creacion timestamp without time zone,
    fecha_ultima_modificacion timestamp without time zone,
    apellido character varying(255),
    cedula character varying(255) NOT NULL,
    correo character varying(255),
    direccion character varying(255),
    estatus character varying(50),
    nombre character varying(255),
    telefono character varying(255),
    tipo_menu integer,
    id_ciudad integer
);


ALTER TABLE persona OWNER TO postgres;

--
-- TOC entry 195 (class 1259 OID 159817)
-- Name: persona_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE persona_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE persona_id_seq OWNER TO postgres;

--
-- TOC entry 169 (class 1259 OID 159520)
-- Name: proveedor; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE proveedor (
    tipo_proveedor boolean,
    id_proveedor integer NOT NULL,
    id_pais integer
);


ALTER TABLE proveedor OWNER TO postgres;

--
-- TOC entry 170 (class 1259 OID 159525)
-- Name: proveedor_clasificacion_repuesto; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE proveedor_clasificacion_repuesto (
    id_proveedor integer NOT NULL,
    id_clasificacion_repuesto integer NOT NULL
);


ALTER TABLE proveedor_clasificacion_repuesto OWNER TO postgres;

--
-- TOC entry 171 (class 1259 OID 159528)
-- Name: proveedor_marca_vehiculo; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE proveedor_marca_vehiculo (
    id_proveedor integer NOT NULL,
    id_marca_vehiculo integer NOT NULL
);


ALTER TABLE proveedor_marca_vehiculo OWNER TO postgres;

--
-- TOC entry 172 (class 1259 OID 159531)
-- Name: requerimiento; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE requerimiento (
    id_requerimiento integer NOT NULL,
    fecha_creacion timestamp without time zone,
    fecha_ultima_modificacion timestamp without time zone,
    anno_v integer,
    estatus character varying(255),
    fecha_cierre timestamp without time zone,
    fecha_solicitud timestamp without time zone,
    fecha_vencimiento timestamp without time zone,
    modelo_v character varying(255),
    serial_carroceria_v character varying(255),
    tipo_repuesto boolean,
    traccion_v boolean,
    transmision_v boolean,
    id_analista integer,
    id_cliente integer,
    id_marca_v integer,
    id_motor_v integer
);


ALTER TABLE requerimiento OWNER TO postgres;

--
-- TOC entry 196 (class 1259 OID 159819)
-- Name: requerimiento_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE requerimiento_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE requerimiento_id_seq OWNER TO postgres;

--
-- TOC entry 173 (class 1259 OID 159539)
-- Name: usuario; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE usuario (
    id integer NOT NULL,
    fecha_creacion timestamp without time zone,
    fecha_ultima_modificacion timestamp without time zone,
    activo boolean NOT NULL,
    foto bytea,
    pasword character varying(100),
    username character varying(20),
    persona_id integer
);


ALTER TABLE usuario OWNER TO postgres;

--
-- TOC entry 197 (class 1259 OID 159821)
-- Name: usuario_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE usuario_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE usuario_id_seq OWNER TO postgres;

--
-- TOC entry 174 (class 1259 OID 159549)
-- Name: vehiculo; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE vehiculo (
    id integer NOT NULL,
    fecha_creacion timestamp without time zone,
    fecha_ultima_modificacion timestamp without time zone,
    anno integer,
    estatus character varying(255),
    modelo character varying(255),
    serial_carroceria character varying(255),
    traccion boolean,
    transmision boolean,
    id_cliente integer,
    id_marca integer,
    id_motor integer
);


ALTER TABLE vehiculo OWNER TO postgres;

--
-- TOC entry 198 (class 1259 OID 159823)
-- Name: vehiculo_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE vehiculo_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE vehiculo_id_seq OWNER TO postgres;

--
-- TOC entry 2082 (class 0 OID 159336)
-- Dependencies: 140
-- Data for Name: analista; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO analista (administrador, id_analista) VALUES (true, 1);
INSERT INTO analista (administrador, id_analista) VALUES (false, 2);


--
-- TOC entry 2083 (class 0 OID 159341)
-- Dependencies: 141
-- Data for Name: banco; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2148 (class 0 OID 0)
-- Dependencies: 175
-- Name: banco_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('banco_id_seq', 1, false);


--
-- TOC entry 2084 (class 0 OID 159346)
-- Dependencies: 142
-- Data for Name: ciudad; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (1, NULL, NULL, 'Maroa', 1);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (2, NULL, NULL, 'Puerto Ayacucho', 1);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (3, NULL, NULL, 'San Fernando De Atabapo', 1);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (4, NULL, NULL, 'Anaco', 2);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (5, NULL, NULL, 'Aragua De Barcelona', 2);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (6, NULL, NULL, 'Barcelona', 2);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (7, NULL, NULL, 'Boca De Uchire', 2);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (8, NULL, NULL, 'Cantaura', 2);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (9, NULL, NULL, 'Clarines', 2);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (10, NULL, NULL, 'El Chaparro', 2);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (11, NULL, NULL, 'El Pao Anzoategui', 2);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (12, NULL, NULL, 'El Tigre', 2);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (13, NULL, NULL, 'El Tigrito', 2);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (14, NULL, NULL, 'Guanape', 2);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (15, NULL, NULL, 'Guanta', 2);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (16, NULL, NULL, 'Lecherias', 2);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (17, NULL, NULL, 'Onoto', 2);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (18, NULL, NULL, 'Pariaguan', 2);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (19, NULL, NULL, 'PÃƒÂ­ritu', 2);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (20, NULL, NULL, 'Puerto La Cruz', 2);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (21, NULL, NULL, 'Puerto PÃƒÂ­ritu', 2);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (22, NULL, NULL, 'Sabana De Uchire', 2);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (23, NULL, NULL, 'San Mateo Anzoategui', 2);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (24, NULL, NULL, 'San Pablo Anzoategui', 2);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (25, NULL, NULL, 'San Tome', 2);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (26, NULL, NULL, 'Santa Ana De Anzoategui', 2);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (27, NULL, NULL, 'Santa Fe Anzoategui', 2);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (28, NULL, NULL, 'Santa Rosa', 2);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (29, NULL, NULL, 'Soledad', 2);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (30, NULL, NULL, 'Urica', 2);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (31, NULL, NULL, 'Valle De Guanape', 2);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (43, NULL, NULL, 'Achaguas', 3);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (44, NULL, NULL, 'Biruaca', 3);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (45, NULL, NULL, 'Bruzual', 3);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (46, NULL, NULL, 'El Amparo', 3);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (47, NULL, NULL, 'El Nula', 3);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (48, NULL, NULL, 'Elorza', 3);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (49, NULL, NULL, 'Guasdualito', 3);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (50, NULL, NULL, 'Mantecal', 3);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (51, NULL, NULL, 'Puerto Paez', 3);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (52, NULL, NULL, 'San Fernando De Apure', 3);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (53, NULL, NULL, 'San Juan De Payara', 3);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (54, NULL, NULL, 'Barbacoas', 4);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (55, NULL, NULL, 'Cagua', 4);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (56, NULL, NULL, 'Camatagua', 4);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (58, NULL, NULL, 'Choroni', 4);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (59, NULL, NULL, 'Colonia Tovar', 4);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (60, NULL, NULL, 'El Consejo', 4);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (61, NULL, NULL, 'La Victoria', 4);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (62, NULL, NULL, 'Las Tejerias', 4);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (63, NULL, NULL, 'Magdaleno', 4);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (64, NULL, NULL, 'Maracay', 4);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (65, NULL, NULL, 'Ocumare De La Costa', 4);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (66, NULL, NULL, 'Palo Negro', 4);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (67, NULL, NULL, 'San Casimiro', 4);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (68, NULL, NULL, 'San Mateo', 4);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (69, NULL, NULL, 'San Sebastian', 4);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (70, NULL, NULL, 'Santa Cruz De Aragua', 4);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (71, NULL, NULL, 'Tocoron', 4);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (72, NULL, NULL, 'Turmero', 4);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (73, NULL, NULL, 'Villa De Cura', 4);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (74, NULL, NULL, 'Zuata', 5);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (75, NULL, NULL, 'Barinas', 5);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (76, NULL, NULL, 'Barinitas', 5);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (77, NULL, NULL, 'Barrancas', 5);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (78, NULL, NULL, 'Calderas', 5);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (79, NULL, NULL, 'Capitanejo', 5);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (80, NULL, NULL, 'Ciudad Bolivia', 5);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (81, NULL, NULL, 'El Canton', 5);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (82, NULL, NULL, 'Las Veguitas', 5);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (83, NULL, NULL, 'Libertad Barinas', 5);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (84, NULL, NULL, 'Sabaneta', 5);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (85, NULL, NULL, 'Santa Barbara De Barinas', 5);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (86, NULL, NULL, 'Socopo', 5);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (87, NULL, NULL, 'Caicara Del Orinoco', 6);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (88, NULL, NULL, 'Canaima', 6);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (89, NULL, NULL, 'Ciudad Bolivar', 6);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (90, NULL, NULL, 'Ciudad Piar', 6);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (91, NULL, NULL, 'El Callao', 6);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (92, NULL, NULL, 'El Dorado', 6);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (93, NULL, NULL, 'El Manteco', 6);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (94, NULL, NULL, 'El Palmar Bolivar', 6);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (95, NULL, NULL, 'El Pao Bolivar', 6);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (96, NULL, NULL, 'Guasipati', 6);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (97, NULL, NULL, 'Guri', 6);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (98, NULL, NULL, 'La Paragua', 6);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (99, NULL, NULL, 'Matanzas', 6);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (100, NULL, NULL, 'Puerto Ordaz', 6);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (101, NULL, NULL, 'San Felix', 6);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (102, NULL, NULL, 'Santa Elena De Uairen', 6);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (103, NULL, NULL, 'Tumeremo', 6);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (104, NULL, NULL, 'Unare', 6);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (105, NULL, NULL, 'Upata', 6);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (106, NULL, NULL, 'Bejuma', 7);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (107, NULL, NULL, 'Belen', 7);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (108, NULL, NULL, 'Campo De Carabobo', 7);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (109, NULL, NULL, 'Canoabo', 7);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (110, NULL, NULL, 'Central Tacarigua', 7);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (111, NULL, NULL, 'Chirgua', 7);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (112, NULL, NULL, 'Ciudad Alianza', 7);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (113, NULL, NULL, 'El Palito', 7);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (114, NULL, NULL, 'Guacara', 7);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (115, NULL, NULL, 'Guigue', 7);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (116, NULL, NULL, 'Las Trincheras', 7);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (117, NULL, NULL, 'Los Guayos', 7);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (118, NULL, NULL, 'Mariara', 7);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (119, NULL, NULL, 'Miranda', 7);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (120, NULL, NULL, 'Montalban', 7);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (121, NULL, NULL, 'Moron', 7);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (122, NULL, NULL, 'Naguanagua', 7);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (123, NULL, NULL, 'Puerto Cabello', 7);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (124, NULL, NULL, 'San Joaquin', 7);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (125, NULL, NULL, 'Tocuyito', 7);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (126, NULL, NULL, 'Urama', 7);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (127, NULL, NULL, 'Valencia', 7);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (128, NULL, NULL, 'Vigirimita', 7);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (129, NULL, NULL, 'Aguirre', 8);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (130, NULL, NULL, 'Apartaderos Cojedes', 8);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (131, NULL, NULL, 'Arismendi', 8);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (132, NULL, NULL, 'Camuriquito', 8);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (133, NULL, NULL, 'El Baul', 8);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (134, NULL, NULL, 'El Limon', 8);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (135, NULL, NULL, 'El Pao Cojedes', 8);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (136, NULL, NULL, 'Hato El Socorro', 8);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (137, NULL, NULL, 'La Aguadita', 8);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (138, NULL, NULL, 'Las Vegas', 8);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (139, NULL, NULL, 'Libertad Cojedes', 8);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (140, NULL, NULL, 'Mapuey', 8);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (141, NULL, NULL, 'PiÃƒÂ±edo', 8);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (142, NULL, NULL, 'Samancito', 8);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (143, NULL, NULL, 'San Carlos', 8);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (144, NULL, NULL, 'Sucre', 8);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (145, NULL, NULL, 'Tinaco', 8);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (146, NULL, NULL, 'Tinaquillo', 8);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (147, NULL, NULL, 'Vallecito', 8);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (148, NULL, NULL, 'Tucupita', 9);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (149, NULL, NULL, 'Caracas', 24);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (150, NULL, NULL, 'El Junquito', 24);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (151, NULL, NULL, 'Adicora', 10);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (152, NULL, NULL, 'Boca De Aroa', 10);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (153, NULL, NULL, 'Cabure', 10);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (154, NULL, NULL, 'Capadare', 10);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (155, NULL, NULL, 'Capatarida', 10);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (156, NULL, NULL, 'Chichiriviche', 10);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (157, NULL, NULL, 'Churuguara', 10);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (158, NULL, NULL, 'Coro', 10);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (159, NULL, NULL, 'Cumarebo', 10);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (160, NULL, NULL, 'Dabajuro', 10);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (161, NULL, NULL, 'Judibana', 10);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (162, NULL, NULL, 'La Cruz De Taratara', 10);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (163, NULL, NULL, 'La Vela De Coro', 10);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (164, NULL, NULL, 'Los Taques', 10);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (165, NULL, NULL, 'Maparari', 10);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (166, NULL, NULL, 'Mene De Mauroa', 10);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (167, NULL, NULL, 'Mirimire', 10);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (168, NULL, NULL, 'Pedregal', 10);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (169, NULL, NULL, 'PÃƒÂ­ritu Falcon', 10);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (170, NULL, NULL, 'Pueblo Nuevo Falcon', 10);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (171, NULL, NULL, 'Puerto Cumarebo', 10);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (172, NULL, NULL, 'Punta Cardon', 10);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (173, NULL, NULL, 'Punto Fijo', 10);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (174, NULL, NULL, 'San Juan De Los Cayos', 10);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (175, NULL, NULL, 'San Luis', 10);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (176, NULL, NULL, 'Santa Ana Falcon', 10);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (177, NULL, NULL, 'Santa Cruz De Bucaral', 10);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (178, NULL, NULL, 'Tocopero', 10);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (179, NULL, NULL, 'Tocuyo De La Costa', 10);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (180, NULL, NULL, 'Tucacas', 10);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (181, NULL, NULL, 'Yaracal', 10);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (182, NULL, NULL, 'Altagracia De Orituco', 11);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (183, NULL, NULL, 'Cabruta', 11);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (184, NULL, NULL, 'Calabozo', 11);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (185, NULL, NULL, 'Camaguan', 11);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (196, NULL, NULL, 'Chaguaramas Guarico', 11);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (197, NULL, NULL, 'El Socorro', 11);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (198, NULL, NULL, 'El Sombrero', 11);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (199, NULL, NULL, 'Las Mercedes De Los Llanos', 11);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (200, NULL, NULL, 'Lezama', 11);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (201, NULL, NULL, 'Onoto', 11);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (202, NULL, NULL, 'Ortiz', 11);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (203, NULL, NULL, 'San Jose De Guaribe', 11);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (204, NULL, NULL, 'San Juan De Los Morros', 11);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (205, NULL, NULL, 'San Rafael De Laya', 11);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (206, NULL, NULL, 'Santa Maria De Ipire', 11);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (207, NULL, NULL, 'Tucupido', 11);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (208, NULL, NULL, 'Valle De La Pascua', 11);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (209, NULL, NULL, 'Zaraza', 11);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (210, NULL, NULL, 'Aguada Grande', 12);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (211, NULL, NULL, 'Atarigua', 12);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (212, NULL, NULL, 'Barquisimeto', 12);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (213, NULL, NULL, 'Bobare', 12);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (214, NULL, NULL, 'Cabudare', 12);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (215, NULL, NULL, 'Carora', 12);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (216, NULL, NULL, 'Cubiro', 12);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (217, NULL, NULL, 'Cuji', 12);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (218, NULL, NULL, 'Duaca', 12);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (219, NULL, NULL, 'El Manzano', 12);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (220, NULL, NULL, 'El Tocuyo', 12);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (221, NULL, NULL, 'Guarico', 12);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (222, NULL, NULL, 'Humocaro Alto', 12);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (223, NULL, NULL, 'Humocaro Bajo', 12);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (224, NULL, NULL, 'La Miel', 12);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (225, NULL, NULL, 'Moroturo', 12);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (226, NULL, NULL, 'Quibor', 12);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (227, NULL, NULL, 'Rio Claro', 12);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (228, NULL, NULL, 'Sanare', 12);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (229, NULL, NULL, 'Santa Ines', 12);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (230, NULL, NULL, 'Sarare', 12);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (231, NULL, NULL, 'Siquisique', 12);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (232, NULL, NULL, 'Tintorero', 12);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (233, NULL, NULL, 'Apartaderos Merida', 13);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (234, NULL, NULL, 'Arapuey', 13);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (235, NULL, NULL, 'Bailadores', 13);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (236, NULL, NULL, 'Caja Seca', 13);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (237, NULL, NULL, 'CanaguÃƒÂ¡', 13);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (238, NULL, NULL, 'Chachopo', 13);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (239, NULL, NULL, 'Chiguara', 13);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (240, NULL, NULL, 'Ejido', 13);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (241, NULL, NULL, 'El Vigia', 13);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (242, NULL, NULL, 'La Azulita', 13);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (243, NULL, NULL, 'La Playa', 13);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (244, NULL, NULL, 'Lagunillas Merida', 13);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (245, NULL, NULL, 'Merida', 13);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (246, NULL, NULL, 'Mesa De Bolivar', 13);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (247, NULL, NULL, 'MucuchÃƒÂ­es', 13);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (248, NULL, NULL, 'Mucujepe', 13);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (249, NULL, NULL, 'Mucuruba', 13);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (250, NULL, NULL, 'Nueva Bolivia', 13);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (251, NULL, NULL, 'Palmarito', 13);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (252, NULL, NULL, 'Pueblo Llano', 13);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (253, NULL, NULL, 'Santa Cruz De Mora', 13);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (254, NULL, NULL, 'Santa Elena De Arenales', 13);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (255, NULL, NULL, 'Santo Domingo', 13);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (256, NULL, NULL, 'Tabay', 13);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (257, NULL, NULL, 'Timotes', 13);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (258, NULL, NULL, 'Torondoy', 13);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (259, NULL, NULL, 'Tovar', 13);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (260, NULL, NULL, 'TucanÃƒÂ­', 13);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (261, NULL, NULL, 'Zea', 13);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (262, NULL, NULL, 'Araguita', 14);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (263, NULL, NULL, 'Carrizal', 14);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (264, NULL, NULL, 'Caucagua', 14);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (265, NULL, NULL, 'Chaguaramas Miranda', 14);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (266, NULL, NULL, 'Charallave', 14);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (267, NULL, NULL, 'Chirimena', 14);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (268, NULL, NULL, 'Chuspa', 14);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (269, NULL, NULL, 'CÃƒÂºa', 14);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (270, NULL, NULL, 'CÃƒÂºpira', 14);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (271, NULL, NULL, 'Curiepe', 14);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (272, NULL, NULL, 'El Guapo', 14);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (273, NULL, NULL, 'El Jarillo', 14);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (274, NULL, NULL, 'Filas De Mariche', 14);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (275, NULL, NULL, 'Guarenas', 14);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (276, NULL, NULL, 'Guatire', 14);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (277, NULL, NULL, 'Higuerote', 14);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (278, NULL, NULL, 'Los Anaucos', 14);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (279, NULL, NULL, 'Los Teques', 14);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (280, NULL, NULL, 'Ocumare Del Tuy', 14);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (281, NULL, NULL, 'Panaquire', 14);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (282, NULL, NULL, 'Paracotos', 14);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (283, NULL, NULL, 'Rio Chico', 14);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (284, NULL, NULL, 'San Antonio De Los Altos', 14);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (285, NULL, NULL, 'San Diego De Los Altos', 14);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (286, NULL, NULL, 'San Fernando Del Guapo', 14);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (287, NULL, NULL, 'San Francisco De Yare', 14);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (288, NULL, NULL, 'San Jose De Los Altos', 14);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (289, NULL, NULL, 'San Jose De Rio Chico', 14);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (290, NULL, NULL, 'San Pedro De Los Altos', 14);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (291, NULL, NULL, 'Santa Lucia', 14);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (292, NULL, NULL, 'Santa Teresa', 14);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (293, NULL, NULL, 'Tacarigua De La Laguna', 14);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (295, NULL, NULL, 'Tacata', 14);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (296, NULL, NULL, 'Turumo', 14);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (297, NULL, NULL, 'Aguasay', 15);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (298, NULL, NULL, 'Aragua De Maturin', 15);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (299, NULL, NULL, 'Barrancas Del Orinoco', 15);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (300, NULL, NULL, 'Caicara De Maturin', 15);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (301, NULL, NULL, 'Caripe', 15);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (302, NULL, NULL, 'Caripito', 15);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (303, NULL, NULL, 'Chaguaramal', 15);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (304, NULL, NULL, 'Chaguaramal', 15);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (305, NULL, NULL, 'Chaguaramas Monagas', 15);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (306, NULL, NULL, 'El Furial', 15);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (307, NULL, NULL, 'El Furrial', 15);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (308, NULL, NULL, 'El Tejero', 15);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (309, NULL, NULL, 'Jusepin', 15);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (310, NULL, NULL, 'La Toscana', 15);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (311, NULL, NULL, 'Maturin', 15);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (312, NULL, NULL, 'Miraflores', 15);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (313, NULL, NULL, 'Punta De Mata', 15);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (314, NULL, NULL, 'Quiriquire', 15);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (315, NULL, NULL, 'San Antonio De Maturin', 15);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (316, NULL, NULL, 'San Vicente Monagas', 15);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (317, NULL, NULL, 'Santa Barbara', 15);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (318, NULL, NULL, 'Temblador', 15);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (319, NULL, NULL, 'TeresÃƒÂ©n', 15);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (320, NULL, NULL, 'Uracoa', 15);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (321, NULL, NULL, 'Altagracia', 16);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (322, NULL, NULL, 'Boca De Pozo', 16);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (323, NULL, NULL, 'Boca De Rio', 16);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (324, NULL, NULL, 'El Espinal', 16);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (325, NULL, NULL, 'El Valle Del Espiritu Santo', 16);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (326, NULL, NULL, 'El Yaque', 16);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (327, NULL, NULL, 'Juangriego', 16);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (328, NULL, NULL, 'La Asuncion', 16);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (329, NULL, NULL, 'La Guardia', 16);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (330, NULL, NULL, 'Pampatar', 16);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (331, NULL, NULL, 'Porlamar', 16);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (332, NULL, NULL, 'Puerto Fermin', 16);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (333, NULL, NULL, 'Punta De Piedras', 16);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (334, NULL, NULL, 'San Francisco De Macanao', 16);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (335, NULL, NULL, 'San Juan Bautista', 16);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (336, NULL, NULL, 'San Pedro De Coche', 16);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (337, NULL, NULL, 'Santa Ana De Nueva Esparta', 16);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (338, NULL, NULL, 'Villa Rosa', 16);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (339, NULL, NULL, 'Acarigua', 17);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (340, NULL, NULL, 'Agua Blanca', 17);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (341, NULL, NULL, 'Araure', 17);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (342, NULL, NULL, 'Biscucuy', 17);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (343, NULL, NULL, 'Boconoito', 17);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (344, NULL, NULL, 'Campo ElÃƒÂ­as', 17);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (345, NULL, NULL, 'Chabasquen', 17);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (346, NULL, NULL, 'Guanare', 17);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (347, NULL, NULL, 'Guanarito', 17);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (348, NULL, NULL, 'La Aparicion', 17);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (349, NULL, NULL, 'La Mision', 17);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (350, NULL, NULL, 'Mesa De Cavaca', 17);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (351, NULL, NULL, 'Ospino', 17);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (352, NULL, NULL, 'Papelon', 17);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (353, NULL, NULL, 'Payara', 17);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (354, NULL, NULL, 'Pimpinela', 17);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (355, NULL, NULL, 'PÃƒÂ­ritu Portuguesa', 17);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (356, NULL, NULL, 'San Rafael De Onoto', 17);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (357, NULL, NULL, 'Santa Rosalia', 17);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (358, NULL, NULL, 'Turen', 17);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (359, NULL, NULL, 'Altos De Sucre', 18);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (360, NULL, NULL, 'Araya', 18);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (361, NULL, NULL, 'Cariaco', 18);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (362, NULL, NULL, 'Carupano', 18);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (363, NULL, NULL, 'Casanay', 18);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (364, NULL, NULL, 'Cumana', 18);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (365, NULL, NULL, 'Cumanacoa', 18);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (366, NULL, NULL, 'El Morro Puerto Santo', 18);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (367, NULL, NULL, 'El Pilar', 18);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (368, NULL, NULL, 'El Poblado', 18);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (369, NULL, NULL, 'Guaca', 18);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (370, NULL, NULL, 'Guiria', 18);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (371, NULL, NULL, 'Irapa', 18);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (372, NULL, NULL, 'Manicuare', 18);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (373, NULL, NULL, 'Mariguitar', 18);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (374, NULL, NULL, 'Rio Caribe', 18);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (375, NULL, NULL, 'San Antonio Del Golfo', 18);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (376, NULL, NULL, 'San Jose De Aerocuar', 18);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (377, NULL, NULL, 'San Vicente Sucre', 18);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (378, NULL, NULL, 'Santa Fe Sucre', 18);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (379, NULL, NULL, 'Tunapuy', 18);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (380, NULL, NULL, 'Yaguaraparo', 18);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (381, NULL, NULL, 'Yoco', 18);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (382, NULL, NULL, 'Abejales', 19);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (383, NULL, NULL, 'Borota', 19);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (384, NULL, NULL, 'Bramon', 19);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (385, NULL, NULL, 'Capacho', 19);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (386, NULL, NULL, 'Colon', 19);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (387, NULL, NULL, 'Coloncito', 19);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (388, NULL, NULL, 'Cordero', 19);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (389, NULL, NULL, 'El Cobre', 19);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (390, NULL, NULL, 'El Pinal', 19);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (391, NULL, NULL, 'Independencia', 19);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (392, NULL, NULL, 'La FrÃƒÂ­a', 19);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (393, NULL, NULL, 'La Grita', 19);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (394, NULL, NULL, 'La Pedrera', 19);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (395, NULL, NULL, 'La Tendida', 19);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (396, NULL, NULL, 'Las Delicias', 19);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (397, NULL, NULL, 'Las Hernandez', 19);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (398, NULL, NULL, 'Lobatera', 19);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (399, NULL, NULL, 'Michelena', 19);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (400, NULL, NULL, 'Palmira', 19);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (401, NULL, NULL, 'Pregonero', 19);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (402, NULL, NULL, 'Queniquea', 19);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (403, NULL, NULL, 'Rubio', 19);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (404, NULL, NULL, 'San Antonio Del Tachira', 19);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (405, NULL, NULL, 'San CristÃƒÂ³bal', 19);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (406, NULL, NULL, 'San Jose De Bolivar', 19);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (407, NULL, NULL, 'San Josecito', 19);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (408, NULL, NULL, 'San Pedro Del Rio', 19);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (409, NULL, NULL, 'Santa Ana TÃƒÂ¡chira', 19);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (410, NULL, NULL, 'Seboruco', 19);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (411, NULL, NULL, 'Tariba', 19);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (412, NULL, NULL, 'Umuquena', 19);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (413, NULL, NULL, 'UreÃƒÂ±a', 19);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (414, NULL, NULL, 'Batatal', 20);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (415, NULL, NULL, 'Betijoque', 20);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (416, NULL, NULL, 'BoconÃƒÂ³', 20);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (417, NULL, NULL, 'Carache', 20);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (418, NULL, NULL, 'ChejendÃƒÂ©', 20);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (419, NULL, NULL, 'Cuicas', 20);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (420, NULL, NULL, 'El Dividive', 20);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (421, NULL, NULL, 'El Jaguito', 20);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (422, NULL, NULL, 'Escuque', 20);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (423, NULL, NULL, 'Isnotu', 20);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (424, NULL, NULL, 'Jajo', 20);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (425, NULL, NULL, 'La Ceiba', 20);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (426, NULL, NULL, 'La Concepcion Trujllo', 20);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (427, NULL, NULL, 'La Mesa De Esnujaque', 20);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (428, NULL, NULL, 'La Puerta', 20);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (429, NULL, NULL, 'La Quebrada', 20);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (430, NULL, NULL, 'Mendoza Fria', 20);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (431, NULL, NULL, 'Meseta De Chimpire', 20);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (432, NULL, NULL, 'Monay', 20);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (433, NULL, NULL, 'Motatan', 20);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (434, NULL, NULL, 'PampÃƒÂ¡n', 20);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (435, NULL, NULL, 'Pampanito', 20);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (436, NULL, NULL, 'Sabana De Mendoza', 20);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (437, NULL, NULL, 'San Lazaro', 20);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (438, NULL, NULL, 'Santa Ana Trujillo', 20);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (439, NULL, NULL, 'Tostos', 20);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (440, NULL, NULL, 'Trujillo', 20);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (441, NULL, NULL, 'Valera', 20);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (442, NULL, NULL, 'Carayaca', 21);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (443, NULL, NULL, 'Litoral', 21);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (444, NULL, NULL, 'ArchipiÃƒÂ©lago Los Roques', 25);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (445, NULL, NULL, 'Aroa', 22);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (446, NULL, NULL, 'Boraure', 22);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (447, NULL, NULL, 'Campo ElÃƒÂ­as Yaracuy', 22);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (448, NULL, NULL, 'Chivacoa', 22);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (449, NULL, NULL, 'Cocorote', 22);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (450, NULL, NULL, 'Farriar', 22);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (451, NULL, NULL, 'Guama', 22);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (452, NULL, NULL, 'Marin', 22);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (453, NULL, NULL, 'Nirgua', 22);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (454, NULL, NULL, 'Sabana De Parra', 22);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (455, NULL, NULL, 'Salom', 22);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (456, NULL, NULL, 'San Felipe', 22);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (457, NULL, NULL, 'San Pablo Yaracuy', 22);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (458, NULL, NULL, 'Urachiche', 22);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (459, NULL, NULL, 'Yaritagua', 22);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (460, NULL, NULL, 'Yumare', 22);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (461, NULL, NULL, 'Bachaquero', 23);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (462, NULL, NULL, 'Bobures', 23);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (463, NULL, NULL, 'Cabimas', 23);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (464, NULL, NULL, 'Campo Concepcion', 23);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (465, NULL, NULL, 'Campo Mara', 23);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (466, NULL, NULL, 'Campo Rojo', 23);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (467, NULL, NULL, 'Carrasquero', 23);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (468, NULL, NULL, 'Casigua', 23);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (469, NULL, NULL, 'Chiquinquira', 23);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (470, NULL, NULL, 'Ciudad Ojeda', 23);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (471, NULL, NULL, 'El Batey', 23);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (472, NULL, NULL, 'El Carmelo', 23);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (473, NULL, NULL, 'El Chivo', 23);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (474, NULL, NULL, 'El Guayabo', 23);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (475, NULL, NULL, 'El Mene', 23);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (476, NULL, NULL, 'El Venado', 23);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (477, NULL, NULL, 'Encontrados', 23);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (478, NULL, NULL, 'Gibraltar', 23);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (479, NULL, NULL, 'Isla De Toas', 23);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (480, NULL, NULL, 'La Concepcion Zulia', 23);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (481, NULL, NULL, 'La Paz', 23);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (482, NULL, NULL, 'La Sierrita', 23);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (483, NULL, NULL, 'Lagunillas Zulia', 23);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (484, NULL, NULL, 'Las Piedras De Perija', 23);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (485, NULL, NULL, 'Los Cortijos', 23);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (486, NULL, NULL, 'Machiques', 23);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (487, NULL, NULL, 'Maracaibo', 23);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (488, NULL, NULL, 'Mene Grande', 23);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (489, NULL, NULL, 'Palmarejo', 23);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (490, NULL, NULL, 'Paraguaipoa', 23);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (491, NULL, NULL, 'Potrerito', 23);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (492, NULL, NULL, 'Pueblo Nuevo Zulia', 23);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (493, NULL, NULL, 'Los Puertos De Altagracia', 23);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (494, NULL, NULL, 'Punta Gorda', 23);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (495, NULL, NULL, 'Sabaneta De Palma', 23);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (496, NULL, NULL, 'San Francisco', 23);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (497, NULL, NULL, 'San Jose De Perija', 23);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (498, NULL, NULL, 'San Rafael Del MojÃƒÂ¡n', 23);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (499, NULL, NULL, 'San Timoteo', 23);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (500, NULL, NULL, 'Santa Barbara Del Zulia', 23);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (501, NULL, NULL, 'Santa Cruz De Mara', 23);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (502, NULL, NULL, 'Santa Cruz Del Zulia', 23);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (503, NULL, NULL, 'Santa Rita', 23);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (504, NULL, NULL, 'Sinamaica', 23);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (505, NULL, NULL, 'Tamare', 23);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (506, NULL, NULL, 'Tia Juana', 23);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (507, NULL, NULL, 'Villa Del Rosario', 23);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (508, NULL, NULL, 'La Guaira', 21);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (509, NULL, NULL, 'Catia La Mar', 21);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (510, NULL, NULL, 'Macuto', 21);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (511, NULL, NULL, 'Naiguata', 21);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (512, NULL, NULL, 'Archipielago Los Monjes', 25);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (513, NULL, NULL, 'Isla La Tortuga y Cayos adyacentes', 25);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (514, NULL, NULL, 'Isla La Sola', 25);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (515, NULL, NULL, 'Islas Los Testigos', 25);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (516, NULL, NULL, 'Islas Los Frailes', 25);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (517, NULL, NULL, 'Isla La Orchila', 25);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (518, NULL, NULL, 'ArchipiÃƒÂ©lago Las Aves', 25);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (519, NULL, NULL, 'Isla de Aves', 25);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (520, NULL, NULL, 'Isla La Blanquilla', 25);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (521, NULL, NULL, 'Isla de Patos', 25);
INSERT INTO ciudad (id_ciudad, fecha_creacion, fecha_ultima_modificacion, nombre, id_estado) VALUES (522, NULL, NULL, 'Islas Los Hermanos', 25);


--
-- TOC entry 2085 (class 0 OID 159351)
-- Dependencies: 143
-- Data for Name: clasificacion_repuesto; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO clasificacion_repuesto (id_clasificacion_repuesto, fecha_creacion, fecha_ultima_modificacion, descripcion, estatus) VALUES (1, NULL, NULL, 'Motor', 'ACTIVO');
INSERT INTO clasificacion_repuesto (id_clasificacion_repuesto, fecha_creacion, fecha_ultima_modificacion, descripcion, estatus) VALUES (5, NULL, NULL, 'Sistema de Rodamiento', 'ACTIVO');
INSERT INTO clasificacion_repuesto (id_clasificacion_repuesto, fecha_creacion, fecha_ultima_modificacion, descripcion, estatus) VALUES (6, NULL, NULL, 'Sistema de Frenos', 'ACTIVO');
INSERT INTO clasificacion_repuesto (id_clasificacion_repuesto, fecha_creacion, fecha_ultima_modificacion, descripcion, estatus) VALUES (8, NULL, NULL, 'Sistema A/A', 'ACTIVO');
INSERT INTO clasificacion_repuesto (id_clasificacion_repuesto, fecha_creacion, fecha_ultima_modificacion, descripcion, estatus) VALUES (10, NULL, NULL, 'Lubricantes,Grasas y Silicones', 'ACTIVO');
INSERT INTO clasificacion_repuesto (id_clasificacion_repuesto, fecha_creacion, fecha_ultima_modificacion, descripcion, estatus) VALUES (11, NULL, NULL, 'Accesorios y Boutique', 'ACTIVO');
INSERT INTO clasificacion_repuesto (id_clasificacion_repuesto, fecha_creacion, fecha_ultima_modificacion, descripcion, estatus) VALUES (12, NULL, NULL, 'Sistema Electrico', 'ACTIVO');
INSERT INTO clasificacion_repuesto (id_clasificacion_repuesto, fecha_creacion, fecha_ultima_modificacion, descripcion, estatus) VALUES (13, NULL, NULL, 'Sistema de Combustible', 'ACTIVO');
INSERT INTO clasificacion_repuesto (id_clasificacion_repuesto, fecha_creacion, fecha_ultima_modificacion, descripcion, estatus) VALUES (2, NULL, NULL, 'Caja y Traccion', 'ACTIVO');
INSERT INTO clasificacion_repuesto (id_clasificacion_repuesto, fecha_creacion, fecha_ultima_modificacion, descripcion, estatus) VALUES (3, NULL, NULL, 'Suspension y Chasis', 'ACTIVO');
INSERT INTO clasificacion_repuesto (id_clasificacion_repuesto, fecha_creacion, fecha_ultima_modificacion, descripcion, estatus) VALUES (4, NULL, NULL, 'Sistema de Direccion', 'ACTIVO');
INSERT INTO clasificacion_repuesto (id_clasificacion_repuesto, fecha_creacion, fecha_ultima_modificacion, descripcion, estatus) VALUES (7, NULL, NULL, 'Sistema de Refrigeracion', 'ACTIVO');
INSERT INTO clasificacion_repuesto (id_clasificacion_repuesto, fecha_creacion, fecha_ultima_modificacion, descripcion, estatus) VALUES (9, NULL, NULL, 'Carroceria Int y Ext', 'ACTIVO');


--
-- TOC entry 2149 (class 0 OID 0)
-- Dependencies: 176
-- Name: clasificacion_repuesto_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('clasificacion_repuesto_id_seq', 1, false);


--
-- TOC entry 2086 (class 0 OID 159359)
-- Dependencies: 144
-- Data for Name: cliente; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO cliente (id_cliente) VALUES (3);
INSERT INTO cliente (id_cliente) VALUES (4);


--
-- TOC entry 2087 (class 0 OID 159364)
-- Dependencies: 145
-- Data for Name: compra; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2150 (class 0 OID 0)
-- Dependencies: 177
-- Name: compra_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('compra_id_seq', 1, false);


--
-- TOC entry 2088 (class 0 OID 159372)
-- Dependencies: 146
-- Data for Name: configuracion; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO configuracion (id_configuracion, fecha_creacion, fecha_ultima_modificacion, porct_ganancia, porct_iva, valor_libra) VALUES (1, '2016-07-16 11:27:00.266', NULL, 0.69999999, 0.12, 100);


--
-- TOC entry 2089 (class 0 OID 159377)
-- Dependencies: 147
-- Data for Name: cotizacion; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2151 (class 0 OID 0)
-- Dependencies: 178
-- Name: cotizacion_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('cotizacion_id_seq', 1, false);


--
-- TOC entry 2090 (class 0 OID 159385)
-- Dependencies: 148
-- Data for Name: deposito; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2152 (class 0 OID 0)
-- Dependencies: 179
-- Name: deposito_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('deposito_id_seq', 1, false);


--
-- TOC entry 2091 (class 0 OID 159393)
-- Dependencies: 149
-- Data for Name: detalle_cotizacion; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2153 (class 0 OID 0)
-- Dependencies: 180
-- Name: detalle_cotizacion_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('detalle_cotizacion_id_seq', 1, false);


--
-- TOC entry 2092 (class 0 OID 159401)
-- Dependencies: 150
-- Data for Name: detalle_cotizacion_internacional; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2093 (class 0 OID 159406)
-- Dependencies: 151
-- Data for Name: detalle_oferta; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2154 (class 0 OID 0)
-- Dependencies: 181
-- Name: detalle_oferta_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('detalle_oferta_id_seq', 1, false);


--
-- TOC entry 2094 (class 0 OID 159411)
-- Dependencies: 152
-- Data for Name: detalle_requerimiento; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2155 (class 0 OID 0)
-- Dependencies: 182
-- Name: detalle_requerimiento_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('detalle_requerimiento_id_seq', 1, false);


--
-- TOC entry 2095 (class 0 OID 159419)
-- Dependencies: 153
-- Data for Name: estado; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO estado (id_estado, fecha_creacion, fecha_ultima_modificacion, nombre) VALUES (1, NULL, NULL, 'Amazonas');
INSERT INTO estado (id_estado, fecha_creacion, fecha_ultima_modificacion, nombre) VALUES (2, NULL, NULL, 'Anzoategui');
INSERT INTO estado (id_estado, fecha_creacion, fecha_ultima_modificacion, nombre) VALUES (3, NULL, NULL, 'Apure');
INSERT INTO estado (id_estado, fecha_creacion, fecha_ultima_modificacion, nombre) VALUES (4, NULL, NULL, 'Aragua');
INSERT INTO estado (id_estado, fecha_creacion, fecha_ultima_modificacion, nombre) VALUES (5, NULL, NULL, 'Barinas');
INSERT INTO estado (id_estado, fecha_creacion, fecha_ultima_modificacion, nombre) VALUES (6, NULL, NULL, 'Bolivar');
INSERT INTO estado (id_estado, fecha_creacion, fecha_ultima_modificacion, nombre) VALUES (7, NULL, NULL, 'Carabobo');
INSERT INTO estado (id_estado, fecha_creacion, fecha_ultima_modificacion, nombre) VALUES (8, NULL, NULL, 'Cojedes');
INSERT INTO estado (id_estado, fecha_creacion, fecha_ultima_modificacion, nombre) VALUES (9, NULL, NULL, 'Delta Amacuro');
INSERT INTO estado (id_estado, fecha_creacion, fecha_ultima_modificacion, nombre) VALUES (10, NULL, NULL, 'Falcon');
INSERT INTO estado (id_estado, fecha_creacion, fecha_ultima_modificacion, nombre) VALUES (11, NULL, NULL, 'Guarico');
INSERT INTO estado (id_estado, fecha_creacion, fecha_ultima_modificacion, nombre) VALUES (12, NULL, NULL, 'Lara');
INSERT INTO estado (id_estado, fecha_creacion, fecha_ultima_modificacion, nombre) VALUES (13, NULL, NULL, 'Merida');
INSERT INTO estado (id_estado, fecha_creacion, fecha_ultima_modificacion, nombre) VALUES (14, NULL, NULL, 'Miranda');
INSERT INTO estado (id_estado, fecha_creacion, fecha_ultima_modificacion, nombre) VALUES (15, NULL, NULL, 'Monagas');
INSERT INTO estado (id_estado, fecha_creacion, fecha_ultima_modificacion, nombre) VALUES (16, NULL, NULL, 'Nueva Esparta');
INSERT INTO estado (id_estado, fecha_creacion, fecha_ultima_modificacion, nombre) VALUES (17, NULL, NULL, 'Portuguesa');
INSERT INTO estado (id_estado, fecha_creacion, fecha_ultima_modificacion, nombre) VALUES (18, NULL, NULL, 'Sucre');
INSERT INTO estado (id_estado, fecha_creacion, fecha_ultima_modificacion, nombre) VALUES (19, NULL, NULL, 'Tachira');
INSERT INTO estado (id_estado, fecha_creacion, fecha_ultima_modificacion, nombre) VALUES (20, NULL, NULL, 'Trujillo');
INSERT INTO estado (id_estado, fecha_creacion, fecha_ultima_modificacion, nombre) VALUES (21, NULL, NULL, 'Vargas');
INSERT INTO estado (id_estado, fecha_creacion, fecha_ultima_modificacion, nombre) VALUES (22, NULL, NULL, 'Yaracuy');
INSERT INTO estado (id_estado, fecha_creacion, fecha_ultima_modificacion, nombre) VALUES (23, NULL, NULL, 'Zulia');
INSERT INTO estado (id_estado, fecha_creacion, fecha_ultima_modificacion, nombre) VALUES (24, NULL, NULL, 'Distrito Capital');
INSERT INTO estado (id_estado, fecha_creacion, fecha_ultima_modificacion, nombre) VALUES (25, NULL, NULL, 'Dependencias Federales');


--
-- TOC entry 2096 (class 0 OID 159424)
-- Dependencies: 154
-- Data for Name: forma_pago; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2156 (class 0 OID 0)
-- Dependencies: 183
-- Name: forma_pago_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('forma_pago_id_seq', 1, false);


--
-- TOC entry 2157 (class 0 OID 0)
-- Dependencies: 184
-- Name: hibernate_sequence; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('hibernate_sequence', 1, false);


--
-- TOC entry 2097 (class 0 OID 159432)
-- Dependencies: 155
-- Data for Name: historico_moneda; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO historico_moneda (id_historia, fecha_creacion, fecha_ultima_modificacion, estatus, monto_conversion, id_moneda) VALUES (1, '2015-05-24 00:00:00', NULL, 'ACTIVO', 1, 1);
INSERT INTO historico_moneda (id_historia, fecha_creacion, fecha_ultima_modificacion, estatus, monto_conversion, id_moneda) VALUES (2, '2015-06-01 00:00:00', NULL, 'ACTIVO', 0.00089999998, 2);


--
-- TOC entry 2158 (class 0 OID 0)
-- Dependencies: 185
-- Name: historico_moneda_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('historico_moneda_id_seq', 1, false);


--
-- TOC entry 2098 (class 0 OID 159437)
-- Dependencies: 156
-- Data for Name: history_logins; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO history_logins (id, fecha_creacion, fecha_ultima_modificacion, date_login, date_logout, username) VALUES (1, '2016-07-17 23:54:59.966', '2016-07-17 23:54:59.966', '2016-07-17 23:54:58.837', '2016-07-17 23:54:58.837', 'admin');
INSERT INTO history_logins (id, fecha_creacion, fecha_ultima_modificacion, date_login, date_logout, username) VALUES (2, '2016-07-17 23:54:59.966', '2016-07-18 00:05:30.216', '2016-07-17 23:54:58.837', '2016-07-18 00:05:29.147', 'admin');
INSERT INTO history_logins (id, fecha_creacion, fecha_ultima_modificacion, date_login, date_logout, username) VALUES (3, '2016-07-18 00:05:30.216', NULL, '2016-07-18 00:05:29.147', NULL, 'eugeniohernandez17@g');


--
-- TOC entry 2159 (class 0 OID 0)
-- Dependencies: 186
-- Name: history_logins_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('history_logins_id_seq', 3, true);


--
-- TOC entry 2099 (class 0 OID 159442)
-- Dependencies: 157
-- Data for Name: marca_vehiculo; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO marca_vehiculo (id_marca_vehiculo, fecha_creacion, fecha_ultima_modificacion, estatus, nombre) VALUES (1, NULL, NULL, 'ACTIVO', 'Chana');
INSERT INTO marca_vehiculo (id_marca_vehiculo, fecha_creacion, fecha_ultima_modificacion, estatus, nombre) VALUES (2, NULL, NULL, 'ACTIVO', 'Chery');
INSERT INTO marca_vehiculo (id_marca_vehiculo, fecha_creacion, fecha_ultima_modificacion, estatus, nombre) VALUES (3, NULL, NULL, 'ACTIVO', 'Chevrolet');
INSERT INTO marca_vehiculo (id_marca_vehiculo, fecha_creacion, fecha_ultima_modificacion, estatus, nombre) VALUES (4, NULL, NULL, 'ACTIVO', 'Daewoo');
INSERT INTO marca_vehiculo (id_marca_vehiculo, fecha_creacion, fecha_ultima_modificacion, estatus, nombre) VALUES (5, NULL, NULL, 'ACTIVO', 'Fiat');
INSERT INTO marca_vehiculo (id_marca_vehiculo, fecha_creacion, fecha_ultima_modificacion, estatus, nombre) VALUES (6, NULL, NULL, 'ACTIVO', 'Ford');
INSERT INTO marca_vehiculo (id_marca_vehiculo, fecha_creacion, fecha_ultima_modificacion, estatus, nombre) VALUES (7, NULL, NULL, 'ACTIVO', 'Honda');
INSERT INTO marca_vehiculo (id_marca_vehiculo, fecha_creacion, fecha_ultima_modificacion, estatus, nombre) VALUES (8, NULL, NULL, 'ACTIVO', 'Hyundai');
INSERT INTO marca_vehiculo (id_marca_vehiculo, fecha_creacion, fecha_ultima_modificacion, estatus, nombre) VALUES (9, NULL, NULL, 'ACTIVO', 'Kia');
INSERT INTO marca_vehiculo (id_marca_vehiculo, fecha_creacion, fecha_ultima_modificacion, estatus, nombre) VALUES (10, NULL, NULL, 'ACTIVO', 'Mazda');
INSERT INTO marca_vehiculo (id_marca_vehiculo, fecha_creacion, fecha_ultima_modificacion, estatus, nombre) VALUES (11, NULL, NULL, 'ACTIVO', 'Mitsubishi');
INSERT INTO marca_vehiculo (id_marca_vehiculo, fecha_creacion, fecha_ultima_modificacion, estatus, nombre) VALUES (12, NULL, NULL, 'ACTIVO', 'Peugeot');
INSERT INTO marca_vehiculo (id_marca_vehiculo, fecha_creacion, fecha_ultima_modificacion, estatus, nombre) VALUES (13, NULL, NULL, 'ACTIVO', 'Renault');
INSERT INTO marca_vehiculo (id_marca_vehiculo, fecha_creacion, fecha_ultima_modificacion, estatus, nombre) VALUES (14, NULL, NULL, 'ACTIVO', 'Tata');
INSERT INTO marca_vehiculo (id_marca_vehiculo, fecha_creacion, fecha_ultima_modificacion, estatus, nombre) VALUES (15, NULL, NULL, 'ACTIVO', 'Toyota');
INSERT INTO marca_vehiculo (id_marca_vehiculo, fecha_creacion, fecha_ultima_modificacion, estatus, nombre) VALUES (16, NULL, NULL, 'ACTIVO', 'Volkswagen');


--
-- TOC entry 2160 (class 0 OID 0)
-- Dependencies: 187
-- Name: marca_vehiculo_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('marca_vehiculo_id_seq', 1, false);


--
-- TOC entry 2100 (class 0 OID 159450)
-- Dependencies: 158
-- Data for Name: menu; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO menu (id_menu, fecha_creacion, fecha_ultima_modificacion, actividad, icono, nombre, ruta, tipo, id_padre) VALUES (1, NULL, NULL, NULL, 'z-icon-shopping-cart', 'Cotizar', '/WEB-INF/views/sistema/funcionalidades/en_proceso/listaRequerimientosProveedor.zul', 3, NULL);
INSERT INTO menu (id_menu, fecha_creacion, fecha_ultima_modificacion, actividad, icono, nombre, ruta, tipo, id_padre) VALUES (3, NULL, NULL, NULL, 'z-icon-user', 'Perfil', '/WEB-INF/views/sistema/configuracion/editarPerfil.zul', 3, NULL);
INSERT INTO menu (id_menu, fecha_creacion, fecha_ultima_modificacion, actividad, icono, nombre, ruta, tipo, id_padre) VALUES (2, NULL, NULL, NULL, 'fa fa-newspaper-o', 'Ordenes de Compra', '/WEB-INF/views/sistema/funcionalidades/proveedor/listaOrdenesCompraProveedor.zul', 3, NULL);
INSERT INTO menu (id_menu, fecha_creacion, fecha_ultima_modificacion, actividad, icono, nombre, ruta, tipo, id_padre) VALUES (10, NULL, NULL, NULL, 'z-icon-book', 'Mis Requerimientos', NULL, 1, NULL);
INSERT INTO menu (id_menu, fecha_creacion, fecha_ultima_modificacion, actividad, icono, nombre, ruta, tipo, id_padre) VALUES (11, NULL, NULL, NULL, NULL, 'En Emision', '/WEB-INF/views/sistema/funcionalidades/emitidos/listaMisRequerimientosEmitidos.zul', 1, 10);
INSERT INTO menu (id_menu, fecha_creacion, fecha_ultima_modificacion, actividad, icono, nombre, ruta, tipo, id_padre) VALUES (12, NULL, NULL, NULL, NULL, 'En Proceso', '/WEB-INF/views/sistema/funcionalidades/en_proceso/listaMisRequerimientosProcesados.zul', 1, 10);
INSERT INTO menu (id_menu, fecha_creacion, fecha_ultima_modificacion, actividad, icono, nombre, ruta, tipo, id_padre) VALUES (13, NULL, NULL, NULL, NULL, 'Ofertados', '/WEB-INF/views/sistema/funcionalidades/ofertados/listaMisRequerimientosOfertados.zul', 1, 10);
INSERT INTO menu (id_menu, fecha_creacion, fecha_ultima_modificacion, actividad, icono, nombre, ruta, tipo, id_padre) VALUES (14, NULL, NULL, NULL, NULL, 'Todos', '/WEB-INF/views/sistema/funcionalidades/emitidos/listaRequerimientosGeneral.zul', 1, 10);
INSERT INTO menu (id_menu, fecha_creacion, fecha_ultima_modificacion, actividad, icono, nombre, ruta, tipo, id_padre) VALUES (15, NULL, NULL, NULL, 'z-icon-bold', 'Datos Basicos', NULL, 1, NULL);
INSERT INTO menu (id_menu, fecha_creacion, fecha_ultima_modificacion, actividad, icono, nombre, ruta, tipo, id_padre) VALUES (16, NULL, NULL, NULL, NULL, 'Configuracion Variables', '/WEB-INF/views/sistema/configuracion/confVariables.zul', 1, 15);
INSERT INTO menu (id_menu, fecha_creacion, fecha_ultima_modificacion, actividad, icono, nombre, ruta, tipo, id_padre) VALUES (17, NULL, NULL, NULL, 'z-icon-cog', 'Configuracion', NULL, 1, NULL);
INSERT INTO menu (id_menu, fecha_creacion, fecha_ultima_modificacion, actividad, icono, nombre, ruta, tipo, id_padre) VALUES (18, NULL, NULL, NULL, 'z-icon-lock', 'Seguridad', NULL, 1, 17);
INSERT INTO menu (id_menu, fecha_creacion, fecha_ultima_modificacion, actividad, icono, nombre, ruta, tipo, id_padre) VALUES (19, NULL, NULL, NULL, NULL, 'Usuarios', '/WEB-INF/views/sistema/seguridad/configuracion/usuarios/listaUsuarios.zul', 1, 18);
INSERT INTO menu (id_menu, fecha_creacion, fecha_ultima_modificacion, actividad, icono, nombre, ruta, tipo, id_padre) VALUES (201, NULL, NULL, NULL, 'z-icon-bold', 'Datos Basicos', NULL, 4, NULL);
INSERT INTO menu (id_menu, fecha_creacion, fecha_ultima_modificacion, actividad, icono, nombre, ruta, tipo, id_padre) VALUES (202, NULL, NULL, NULL, NULL, 'Vehiculo', '/WEB-INF/views/sistema/funcionalidades/usuario/listaMisVehiculos.zul', 4, 201);
INSERT INTO menu (id_menu, fecha_creacion, fecha_ultima_modificacion, actividad, icono, nombre, ruta, tipo, id_padre) VALUES (300, NULL, NULL, NULL, 'z-icon-book', 'Mis Requerimientos', NULL, 2, NULL);
INSERT INTO menu (id_menu, fecha_creacion, fecha_ultima_modificacion, actividad, icono, nombre, ruta, tipo, id_padre) VALUES (301, NULL, NULL, NULL, NULL, 'En Emision', '/WEB-INF/views/sistema/funcionalidades/emitidos/listaMisRequerimientosEmitidos.zul', 2, 300);
INSERT INTO menu (id_menu, fecha_creacion, fecha_ultima_modificacion, actividad, icono, nombre, ruta, tipo, id_padre) VALUES (302, NULL, NULL, NULL, NULL, 'En Proceso', '/WEB-INF/views/sistema/funcionalidades/en_proceso/listaMisRequerimientosProcesados.zul', 2, 300);
INSERT INTO menu (id_menu, fecha_creacion, fecha_ultima_modificacion, actividad, icono, nombre, ruta, tipo, id_padre) VALUES (303, NULL, NULL, NULL, NULL, 'Ofertados', '/WEB-INF/views/sistema/funcionalidades/ofertados/listaMisRequerimientosOfertados.zul', 2, 300);
INSERT INTO menu (id_menu, fecha_creacion, fecha_ultima_modificacion, actividad, icono, nombre, ruta, tipo, id_padre) VALUES (304, NULL, NULL, NULL, NULL, 'Pagos', NULL, 2, NULL);
INSERT INTO menu (id_menu, fecha_creacion, fecha_ultima_modificacion, actividad, icono, nombre, ruta, tipo, id_padre) VALUES (305, NULL, NULL, NULL, NULL, 'Consultar Pagos', '/WEB-INF/views/sistema/funcionalidades/ofertados/listaPagosDeClientes.zul', 2, 304);
INSERT INTO menu (id_menu, fecha_creacion, fecha_ultima_modificacion, actividad, icono, nombre, ruta, tipo, id_padre) VALUES (20, NULL, NULL, NULL, NULL, 'Proveedor', '/WEB-INF/views/sistema/maestros/listaProveedores.zul', 1, 15);
INSERT INTO menu (id_menu, fecha_creacion, fecha_ultima_modificacion, actividad, icono, nombre, ruta, tipo, id_padre) VALUES (21, NULL, NULL, NULL, NULL, 'Analista', '/WEB-INF/views/sistema/maestros/listaAnalistas.zul', 1, 15);
INSERT INTO menu (id_menu, fecha_creacion, fecha_ultima_modificacion, actividad, icono, nombre, ruta, tipo, id_padre) VALUES (22, NULL, NULL, NULL, NULL, 'Marcas de Vehiculo', '/WEB-INF/views/sistema/maestros/listaMarcas.zul', 1, 15);
INSERT INTO menu (id_menu, fecha_creacion, fecha_ultima_modificacion, actividad, icono, nombre, ruta, tipo, id_padre) VALUES (200, NULL, NULL, NULL, 'z-icon-book', 'Mis Requerimientos', '/WEB-INF/views/sistema/funcionalidades/usuario/listaMisRequerimientos.zul', 4, NULL);
INSERT INTO menu (id_menu, fecha_creacion, fecha_ultima_modificacion, actividad, icono, nombre, ruta, tipo, id_padre) VALUES (203, NULL, NULL, NULL, 'z-icon-user', 'Perfil', '/WEB-INF/views/sistema/configuracion/editarPerfil.zul', 4, NULL);


--
-- TOC entry 2161 (class 0 OID 0)
-- Dependencies: 188
-- Name: menu_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('menu_id_seq', 1, false);


--
-- TOC entry 2101 (class 0 OID 159458)
-- Dependencies: 159
-- Data for Name: moneda; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO moneda (id_moneda, fecha_creacion, fecha_ultima_modificacion, estatus, nombre, pais, simbolo) VALUES (1, NULL, NULL, 'ACTIVO', 'Bolivar', true, 'BsF');
INSERT INTO moneda (id_moneda, fecha_creacion, fecha_ultima_modificacion, estatus, nombre, pais, simbolo) VALUES (2, NULL, NULL, 'ACTIVO', 'Dolar', false, '$');
INSERT INTO moneda (id_moneda, fecha_creacion, fecha_ultima_modificacion, estatus, nombre, pais, simbolo) VALUES (3, NULL, NULL, 'ACTIVO', 'Euro', false, '€');


--
-- TOC entry 2162 (class 0 OID 0)
-- Dependencies: 189
-- Name: moneda_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('moneda_id_seq', 1, false);


--
-- TOC entry 2102 (class 0 OID 159466)
-- Dependencies: 160
-- Data for Name: motor; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2163 (class 0 OID 0)
-- Dependencies: 190
-- Name: motor_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('motor_id_seq', 1, false);


--
-- TOC entry 2103 (class 0 OID 159471)
-- Dependencies: 161
-- Data for Name: oferta; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2164 (class 0 OID 0)
-- Dependencies: 191
-- Name: oferta_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('oferta_id_seq', 1, false);


--
-- TOC entry 2104 (class 0 OID 159476)
-- Dependencies: 162
-- Data for Name: orden_compra; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2165 (class 0 OID 0)
-- Dependencies: 192
-- Name: orden_compra_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('orden_compra_id_seq', 1, false);


--
-- TOC entry 2105 (class 0 OID 159484)
-- Dependencies: 163
-- Data for Name: pago; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2106 (class 0 OID 159492)
-- Dependencies: 164
-- Data for Name: pago_cliente; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2166 (class 0 OID 0)
-- Dependencies: 193
-- Name: pago_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('pago_id_seq', 1, false);


--
-- TOC entry 2107 (class 0 OID 159497)
-- Dependencies: 165
-- Data for Name: pago_proveedor; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2108 (class 0 OID 159502)
-- Dependencies: 166
-- Data for Name: pais; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2167 (class 0 OID 0)
-- Dependencies: 194
-- Name: pais_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('pais_id_seq', 1, false);


--
-- TOC entry 2109 (class 0 OID 159507)
-- Dependencies: 167
-- Data for Name: persistent_logins; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2110 (class 0 OID 159512)
-- Dependencies: 168
-- Data for Name: persona; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO persona (id, fecha_creacion, fecha_ultima_modificacion, apellido, cedula, correo, direccion, estatus, nombre, telefono, tipo_menu, id_ciudad) VALUES (1, NULL, NULL, NULL, '123456789', 'admin@gmail.com', NULL, 'activo', 'Admin', NULL, 1, NULL);
INSERT INTO persona (id, fecha_creacion, fecha_ultima_modificacion, apellido, cedula, correo, direccion, estatus, nombre, telefono, tipo_menu, id_ciudad) VALUES (2, '2016-07-18 00:05:30.216', NULL, NULL, 'V123456789', 'maria@gmail.com', 'maria', 'activo', 'Maria', '123456789', NULL, 46);
INSERT INTO persona (id, fecha_creacion, fecha_ultima_modificacion, apellido, cedula, correo, direccion, estatus, nombre, telefono, tipo_menu, id_ciudad) VALUES (3, '2016-07-18 00:05:30.216', '2016-07-18 00:05:30.216', 'Caicedo', 'VV20186243', 'eugeniohernandez17@gmail.com', NULL, 'activo', 'Eugenio', '123456789', NULL, 113);
INSERT INTO persona (id, fecha_creacion, fecha_ultima_modificacion, apellido, cedula, correo, direccion, estatus, nombre, telefono, tipo_menu, id_ciudad) VALUES (4, '2016-07-18 00:05:30.216', NULL, NULL, '20186243', NULL, NULL, NULL, NULL, NULL, NULL, NULL);


--
-- TOC entry 2168 (class 0 OID 0)
-- Dependencies: 195
-- Name: persona_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('persona_id_seq', 4, true);


--
-- TOC entry 2111 (class 0 OID 159520)
-- Dependencies: 169
-- Data for Name: proveedor; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2112 (class 0 OID 159525)
-- Dependencies: 170
-- Data for Name: proveedor_clasificacion_repuesto; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2113 (class 0 OID 159528)
-- Dependencies: 171
-- Data for Name: proveedor_marca_vehiculo; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2114 (class 0 OID 159531)
-- Dependencies: 172
-- Data for Name: requerimiento; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2169 (class 0 OID 0)
-- Dependencies: 196
-- Name: requerimiento_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('requerimiento_id_seq', 1, false);


--
-- TOC entry 2115 (class 0 OID 159539)
-- Dependencies: 173
-- Data for Name: usuario; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO usuario (id, fecha_creacion, fecha_ultima_modificacion, activo, foto, pasword, username, persona_id) VALUES (1, NULL, NULL, true, NULL, '123', 'admin', 1);
INSERT INTO usuario (id, fecha_creacion, fecha_ultima_modificacion, activo, foto, pasword, username, persona_id) VALUES (2, '2016-07-18 00:05:30.216', NULL, true, NULL, '123', 'maria@gmail.com', 2);
INSERT INTO usuario (id, fecha_creacion, fecha_ultima_modificacion, activo, foto, pasword, username, persona_id) VALUES (5, '2016-07-18 00:05:30.216', NULL, true, NULL, '123', 'eugeniohernandez17@gmail.com', 4);


--
-- TOC entry 2170 (class 0 OID 0)
-- Dependencies: 197
-- Name: usuario_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('usuario_id_seq', 5, true);


--
-- TOC entry 2116 (class 0 OID 159549)
-- Dependencies: 174
-- Data for Name: vehiculo; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2171 (class 0 OID 0)
-- Dependencies: 198
-- Name: vehiculo_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('vehiculo_id_seq', 1, false);


--
-- TOC entry 1885 (class 2606 OID 159340)
-- Name: analista_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY analista
    ADD CONSTRAINT analista_pkey PRIMARY KEY (id_analista);


--
-- TOC entry 1887 (class 2606 OID 159345)
-- Name: banco_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY banco
    ADD CONSTRAINT banco_pkey PRIMARY KEY (id_banco);


--
-- TOC entry 1889 (class 2606 OID 159350)
-- Name: ciudad_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ciudad
    ADD CONSTRAINT ciudad_pkey PRIMARY KEY (id_ciudad);


--
-- TOC entry 1891 (class 2606 OID 159358)
-- Name: clasificacion_repuesto_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY clasificacion_repuesto
    ADD CONSTRAINT clasificacion_repuesto_pkey PRIMARY KEY (id_clasificacion_repuesto);


--
-- TOC entry 1893 (class 2606 OID 159363)
-- Name: cliente_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY cliente
    ADD CONSTRAINT cliente_pkey PRIMARY KEY (id_cliente);


--
-- TOC entry 1895 (class 2606 OID 159371)
-- Name: compra_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY compra
    ADD CONSTRAINT compra_pkey PRIMARY KEY (id_compra);


--
-- TOC entry 1897 (class 2606 OID 159376)
-- Name: configuracion_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY configuracion
    ADD CONSTRAINT configuracion_pkey PRIMARY KEY (id_configuracion);


--
-- TOC entry 1899 (class 2606 OID 159384)
-- Name: cotizacion_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY cotizacion
    ADD CONSTRAINT cotizacion_pkey PRIMARY KEY (id_cotizacion);


--
-- TOC entry 1901 (class 2606 OID 159392)
-- Name: deposito_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY deposito
    ADD CONSTRAINT deposito_pkey PRIMARY KEY (id_deposito);


--
-- TOC entry 1905 (class 2606 OID 159405)
-- Name: detalle_cotizacion_internacional_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY detalle_cotizacion_internacional
    ADD CONSTRAINT detalle_cotizacion_internacional_pkey PRIMARY KEY (id_detalle_cotizacion_internacional);


--
-- TOC entry 1903 (class 2606 OID 159400)
-- Name: detalle_cotizacion_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY detalle_cotizacion
    ADD CONSTRAINT detalle_cotizacion_pkey PRIMARY KEY (id_detalle_cotizacion);


--
-- TOC entry 1907 (class 2606 OID 159410)
-- Name: detalle_oferta_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY detalle_oferta
    ADD CONSTRAINT detalle_oferta_pkey PRIMARY KEY (id_detalle_oferta);


--
-- TOC entry 1909 (class 2606 OID 159418)
-- Name: detalle_requerimiento_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY detalle_requerimiento
    ADD CONSTRAINT detalle_requerimiento_pkey PRIMARY KEY (id_detalle_requerimiento);


--
-- TOC entry 1911 (class 2606 OID 159423)
-- Name: estado_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY estado
    ADD CONSTRAINT estado_pkey PRIMARY KEY (id_estado);


--
-- TOC entry 1913 (class 2606 OID 159431)
-- Name: forma_pago_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY forma_pago
    ADD CONSTRAINT forma_pago_pkey PRIMARY KEY (id_forma_pago);


--
-- TOC entry 1915 (class 2606 OID 159436)
-- Name: historico_moneda_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY historico_moneda
    ADD CONSTRAINT historico_moneda_pkey PRIMARY KEY (id_historia);


--
-- TOC entry 1917 (class 2606 OID 159441)
-- Name: history_logins_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY history_logins
    ADD CONSTRAINT history_logins_pkey PRIMARY KEY (id);


--
-- TOC entry 1919 (class 2606 OID 159449)
-- Name: marca_vehiculo_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY marca_vehiculo
    ADD CONSTRAINT marca_vehiculo_pkey PRIMARY KEY (id_marca_vehiculo);


--
-- TOC entry 1921 (class 2606 OID 159457)
-- Name: menu_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY menu
    ADD CONSTRAINT menu_pkey PRIMARY KEY (id_menu);


--
-- TOC entry 1923 (class 2606 OID 159465)
-- Name: moneda_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY moneda
    ADD CONSTRAINT moneda_pkey PRIMARY KEY (id_moneda);


--
-- TOC entry 1925 (class 2606 OID 159470)
-- Name: motor_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY motor
    ADD CONSTRAINT motor_pkey PRIMARY KEY (id_motor);


--
-- TOC entry 1927 (class 2606 OID 159475)
-- Name: oferta_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY oferta
    ADD CONSTRAINT oferta_pkey PRIMARY KEY (id_oferta);


--
-- TOC entry 1929 (class 2606 OID 159483)
-- Name: orden_compra_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY orden_compra
    ADD CONSTRAINT orden_compra_pkey PRIMARY KEY (id_orden_compra);


--
-- TOC entry 1933 (class 2606 OID 159496)
-- Name: pago_cliente_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY pago_cliente
    ADD CONSTRAINT pago_cliente_pkey PRIMARY KEY (id_pago_cliente);


--
-- TOC entry 1931 (class 2606 OID 159491)
-- Name: pago_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY pago
    ADD CONSTRAINT pago_pkey PRIMARY KEY (id);


--
-- TOC entry 1935 (class 2606 OID 159501)
-- Name: pago_proveedor_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY pago_proveedor
    ADD CONSTRAINT pago_proveedor_pkey PRIMARY KEY (id_pago_proveedor);


--
-- TOC entry 1937 (class 2606 OID 159506)
-- Name: pais_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY pais
    ADD CONSTRAINT pais_pkey PRIMARY KEY (id_pais);


--
-- TOC entry 1939 (class 2606 OID 159511)
-- Name: persistent_logins_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY persistent_logins
    ADD CONSTRAINT persistent_logins_pkey PRIMARY KEY (series);


--
-- TOC entry 1941 (class 2606 OID 159519)
-- Name: persona_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY persona
    ADD CONSTRAINT persona_pkey PRIMARY KEY (id);


--
-- TOC entry 1943 (class 2606 OID 159524)
-- Name: proveedor_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY proveedor
    ADD CONSTRAINT proveedor_pkey PRIMARY KEY (id_proveedor);


--
-- TOC entry 1945 (class 2606 OID 159538)
-- Name: requerimiento_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY requerimiento
    ADD CONSTRAINT requerimiento_pkey PRIMARY KEY (id_requerimiento);


--
-- TOC entry 1947 (class 2606 OID 159546)
-- Name: usuario_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY usuario
    ADD CONSTRAINT usuario_pkey PRIMARY KEY (id);


--
-- TOC entry 1949 (class 2606 OID 159548)
-- Name: usuario_username_key; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY usuario
    ADD CONSTRAINT usuario_username_key UNIQUE (username);


--
-- TOC entry 1951 (class 2606 OID 159556)
-- Name: vehiculo_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY vehiculo
    ADD CONSTRAINT vehiculo_pkey PRIMARY KEY (id);


--
-- TOC entry 1977 (class 2606 OID 159682)
-- Name: fk1b73e6547ca62c8c; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY pago_cliente
    ADD CONSTRAINT fk1b73e6547ca62c8c FOREIGN KEY (id_pago_cliente) REFERENCES pago(id);


--
-- TOC entry 1976 (class 2606 OID 159677)
-- Name: fk1b73e654f1f5c6db; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY pago_cliente
    ADD CONSTRAINT fk1b73e654f1f5c6db FOREIGN KEY (id_compra) REFERENCES compra(id_compra);


--
-- TOC entry 1986 (class 2606 OID 159727)
-- Name: fk1c4167898d35b97d; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY proveedor_marca_vehiculo
    ADD CONSTRAINT fk1c4167898d35b97d FOREIGN KEY (id_proveedor) REFERENCES proveedor(id_proveedor);


--
-- TOC entry 1987 (class 2606 OID 159732)
-- Name: fk1c416789e157ed2; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY proveedor_marca_vehiculo
    ADD CONSTRAINT fk1c416789e157ed2 FOREIGN KEY (id_marca_vehiculo) REFERENCES marca_vehiculo(id_marca_vehiculo);


--
-- TOC entry 1971 (class 2606 OID 159652)
-- Name: fk33155fb6af0e3c; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY menu
    ADD CONSTRAINT fk33155fb6af0e3c FOREIGN KEY (id_padre) REFERENCES menu(id_menu);


--
-- TOC entry 1954 (class 2606 OID 159567)
-- Name: fk334b85fae64f75a7; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cliente
    ADD CONSTRAINT fk334b85fae64f75a7 FOREIGN KEY (id_cliente) REFERENCES persona(id);


--
-- TOC entry 1975 (class 2606 OID 159672)
-- Name: fk3462994173adb7; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY pago
    ADD CONSTRAINT fk3462994173adb7 FOREIGN KEY (id_banco) REFERENCES banco(id_banco);


--
-- TOC entry 1974 (class 2606 OID 159667)
-- Name: fk3462996a94c15a; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY pago
    ADD CONSTRAINT fk3462996a94c15a FOREIGN KEY (id_forma_pago) REFERENCES forma_pago(id_forma_pago);


--
-- TOC entry 1979 (class 2606 OID 159692)
-- Name: fk3462db1417460b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY pais
    ADD CONSTRAINT fk3462db1417460b FOREIGN KEY (id_moneda) REFERENCES moneda(id_moneda);


--
-- TOC entry 1964 (class 2606 OID 159617)
-- Name: fk37d329f919e53841; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY detalle_oferta
    ADD CONSTRAINT fk37d329f919e53841 FOREIGN KEY (id_oferta) REFERENCES oferta(id_oferta);


--
-- TOC entry 1966 (class 2606 OID 159627)
-- Name: fk37d329f99df4ca42; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY detalle_oferta
    ADD CONSTRAINT fk37d329f99df4ca42 FOREIGN KEY (id_detalle_cotizacion) REFERENCES detalle_cotizacion(id_detalle_cotizacion);


--
-- TOC entry 1965 (class 2606 OID 159622)
-- Name: fk37d329f9a7a0aac4; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY detalle_oferta
    ADD CONSTRAINT fk37d329f9a7a0aac4 FOREIGN KEY (id_orden_compra) REFERENCES orden_compra(id_orden_compra);


--
-- TOC entry 1963 (class 2606 OID 159612)
-- Name: fk37d329f9f1f5c6db; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY detalle_oferta
    ADD CONSTRAINT fk37d329f9f1f5c6db FOREIGN KEY (id_compra) REFERENCES compra(id_compra);


--
-- TOC entry 1959 (class 2606 OID 159592)
-- Name: fk383bbd912b738cd1; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY deposito
    ADD CONSTRAINT fk383bbd912b738cd1 FOREIGN KEY (id_pago) REFERENCES pago(id);


--
-- TOC entry 1967 (class 2606 OID 159632)
-- Name: fk42c4ba5d13fee98b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY detalle_requerimiento
    ADD CONSTRAINT fk42c4ba5d13fee98b FOREIGN KEY (id_requerimiento) REFERENCES requerimiento(id_requerimiento);


--
-- TOC entry 1968 (class 2606 OID 159637)
-- Name: fk42c4ba5d5acb3c4a; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY detalle_requerimiento
    ADD CONSTRAINT fk42c4ba5d5acb3c4a FOREIGN KEY (id_clasificacion_repuesto) REFERENCES clasificacion_repuesto(id_clasificacion_repuesto);


--
-- TOC entry 1957 (class 2606 OID 159582)
-- Name: fk5c3e3f8d8d35b97d; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cotizacion
    ADD CONSTRAINT fk5c3e3f8d8d35b97d FOREIGN KEY (id_proveedor) REFERENCES proveedor(id_proveedor);


--
-- TOC entry 1958 (class 2606 OID 159587)
-- Name: fk5c3e3f8dba48e234; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cotizacion
    ADD CONSTRAINT fk5c3e3f8dba48e234 FOREIGN KEY (id_historico_moneda) REFERENCES historico_moneda(id_historia);


--
-- TOC entry 1978 (class 2606 OID 159687)
-- Name: fk5eb67cb8b2bb29f0; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY pago_proveedor
    ADD CONSTRAINT fk5eb67cb8b2bb29f0 FOREIGN KEY (id_pago_proveedor) REFERENCES pago(id);


--
-- TOC entry 1960 (class 2606 OID 159597)
-- Name: fk6d4757554cee9222; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY detalle_cotizacion
    ADD CONSTRAINT fk6d4757554cee9222 FOREIGN KEY (id_detalle_requerimiento) REFERENCES detalle_requerimiento(id_detalle_requerimiento);


--
-- TOC entry 1961 (class 2606 OID 159602)
-- Name: fk6d475755c714d379; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY detalle_cotizacion
    ADD CONSTRAINT fk6d475755c714d379 FOREIGN KEY (id_cotizacion) REFERENCES cotizacion(id_cotizacion);


--
-- TOC entry 1993 (class 2606 OID 159762)
-- Name: fk780e7965420b2bf5; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY vehiculo
    ADD CONSTRAINT fk780e7965420b2bf5 FOREIGN KEY (id_cliente) REFERENCES cliente(id_cliente);


--
-- TOC entry 1994 (class 2606 OID 159767)
-- Name: fk780e796542b69d2b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY vehiculo
    ADD CONSTRAINT fk780e796542b69d2b FOREIGN KEY (id_motor) REFERENCES motor(id_motor);


--
-- TOC entry 1995 (class 2606 OID 159772)
-- Name: fk780e7965d6ae7d3e; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY vehiculo
    ADD CONSTRAINT fk780e7965d6ae7d3e FOREIGN KEY (id_marca) REFERENCES marca_vehiculo(id_marca_vehiculo);


--
-- TOC entry 1973 (class 2606 OID 159662)
-- Name: fk82fec41358ca1006; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY orden_compra
    ADD CONSTRAINT fk82fec41358ca1006 FOREIGN KEY (id_pago_proveedor) REFERENCES pago_proveedor(id_pago_proveedor);


--
-- TOC entry 1985 (class 2606 OID 159722)
-- Name: fk97b0796d5acb3c4a; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY proveedor_clasificacion_repuesto
    ADD CONSTRAINT fk97b0796d5acb3c4a FOREIGN KEY (id_clasificacion_repuesto) REFERENCES clasificacion_repuesto(id_clasificacion_repuesto);


--
-- TOC entry 1984 (class 2606 OID 159717)
-- Name: fk97b0796d8d35b97d; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY proveedor_clasificacion_repuesto
    ADD CONSTRAINT fk97b0796d8d35b97d FOREIGN KEY (id_proveedor) REFERENCES proveedor(id_proveedor);


--
-- TOC entry 1969 (class 2606 OID 159642)
-- Name: fka11844451417460b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY historico_moneda
    ADD CONSTRAINT fka11844451417460b FOREIGN KEY (id_moneda) REFERENCES moneda(id_moneda);


--
-- TOC entry 1953 (class 2606 OID 159562)
-- Name: fkaeee1c58f93fceab; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ciudad
    ADD CONSTRAINT fkaeee1c58f93fceab FOREIGN KEY (id_estado) REFERENCES estado(id_estado);


--
-- TOC entry 1955 (class 2606 OID 159572)
-- Name: fkaf3f357e13fee98b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY compra
    ADD CONSTRAINT fkaf3f357e13fee98b FOREIGN KEY (id_requerimiento) REFERENCES requerimiento(id_requerimiento);


--
-- TOC entry 1956 (class 2606 OID 159577)
-- Name: fkaf3f357eba48e234; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY compra
    ADD CONSTRAINT fkaf3f357eba48e234 FOREIGN KEY (id_historico_moneda) REFERENCES historico_moneda(id_historia);


--
-- TOC entry 1962 (class 2606 OID 159607)
-- Name: fkbb5b3c95b6380082; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY detalle_cotizacion_internacional
    ADD CONSTRAINT fkbb5b3c95b6380082 FOREIGN KEY (id_detalle_cotizacion_internacional) REFERENCES detalle_cotizacion(id_detalle_cotizacion);


--
-- TOC entry 1980 (class 2606 OID 159697)
-- Name: fkbd224d21a483329; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY persistent_logins
    ADD CONSTRAINT fkbd224d21a483329 FOREIGN KEY (username) REFERENCES usuario(username);


--
-- TOC entry 1972 (class 2606 OID 159657)
-- Name: fkc336ee31a139073d; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY oferta
    ADD CONSTRAINT fkc336ee31a139073d FOREIGN KEY (id_re_cotizacion) REFERENCES cotizacion(id_cotizacion);


--
-- TOC entry 1952 (class 2606 OID 159557)
-- Name: fkc6c3524f506527a4; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY analista
    ADD CONSTRAINT fkc6c3524f506527a4 FOREIGN KEY (id_analista) REFERENCES persona(id);


--
-- TOC entry 1990 (class 2606 OID 159747)
-- Name: fkd19e472518e1bc7d; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY requerimiento
    ADD CONSTRAINT fkd19e472518e1bc7d FOREIGN KEY (id_analista) REFERENCES analista(id_analista);


--
-- TOC entry 1988 (class 2606 OID 159737)
-- Name: fkd19e4725420b2bf5; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY requerimiento
    ADD CONSTRAINT fkd19e4725420b2bf5 FOREIGN KEY (id_cliente) REFERENCES cliente(id_cliente);


--
-- TOC entry 1991 (class 2606 OID 159752)
-- Name: fkd19e4725488dd8e2; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY requerimiento
    ADD CONSTRAINT fkd19e4725488dd8e2 FOREIGN KEY (id_motor_v) REFERENCES motor(id_motor);


--
-- TOC entry 1989 (class 2606 OID 159742)
-- Name: fkd19e4725c4866335; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY requerimiento
    ADD CONSTRAINT fkd19e4725c4866335 FOREIGN KEY (id_marca_v) REFERENCES marca_vehiculo(id_marca_vehiculo);


--
-- TOC entry 1981 (class 2606 OID 159702)
-- Name: fkd78fcfacf153948f; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY persona
    ADD CONSTRAINT fkd78fcfacf153948f FOREIGN KEY (id_ciudad) REFERENCES ciudad(id_ciudad);


--
-- TOC entry 1983 (class 2606 OID 159712)
-- Name: fkdf24cade2b738d55; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY proveedor
    ADD CONSTRAINT fkdf24cade2b738d55 FOREIGN KEY (id_pais) REFERENCES pais(id_pais);


--
-- TOC entry 1982 (class 2606 OID 159707)
-- Name: fkdf24cadee131f38b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY proveedor
    ADD CONSTRAINT fkdf24cadee131f38b FOREIGN KEY (id_proveedor) REFERENCES persona(id);


--
-- TOC entry 1970 (class 2606 OID 159647)
-- Name: fkee0d88351a483329; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY history_logins
    ADD CONSTRAINT fkee0d88351a483329 FOREIGN KEY (username) REFERENCES usuario(username);


--
-- TOC entry 1992 (class 2606 OID 159757)
-- Name: fkf814f32e3212f95f; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY usuario
    ADD CONSTRAINT fkf814f32e3212f95f FOREIGN KEY (persona_id) REFERENCES persona(id);


--
-- TOC entry 2147 (class 0 OID 0)
-- Dependencies: 3
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


-- Completed on 2016-07-18 00:16:50

--
-- PostgreSQL database dump complete
--

