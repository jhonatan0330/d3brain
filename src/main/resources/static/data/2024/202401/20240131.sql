COMMENT ON TABLE usuario_usrp IS '2024-01-31';


INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo)
	select
	'PROP_253' , 'L', 'FECHA PLANEADA DE INICIO', 'PLANTILLA_FECHA_INICIO', 'REQUISITO'
	where not exists (select 1 from propiedadvalordefinido_pvdp where cpvd_llave  = 'PROP_253');
	
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo)
	select
	'PROP_254' , 'L', 'FECHA PLANEADA A FINALIZAR', 'PLANTILLA_FECHA_FINAL', 'REQUISITO'
	where not exists (select 1 from propiedadvalordefinido_pvdp where cpvd_llave  = 'PROP_254');

ALTER TABLE public.actividad_actp DROP COLUMN dact_fechalimite;
ALTER TABLE public.actividad_actp DROP COLUMN dact_fechaterminar;
ALTER TABLE public.actividad_actp DROP COLUMN dact_fechaarrancar;
ALTER TABLE public.actividad_actp DROP COLUMN nact_duracion;
ALTER TABLE public.actividad_actp DROP COLUMN cact_actividadprevia;
ALTER TABLE public.actividad_actp DROP COLUMN cact_actividadsiguiente;

CREATE OR REPLACE FUNCTION public.campo4documento(_documento character varying, _historico integer)
 RETURNS TABLE(cpvc_llave character varying, cpvc_documento character varying, dpvc_valorfecha timestamp with time zone, mpvc_valornumero numeric, cpvc_valortext character varying, cpvc_valoropcion character varying, cpvc_estado character varying, cpvc_campo character varying, cpvc_valorauxiliar character varying, cpvc_transaccionregistro character varying, cpvc_transaccioninactivo character varying)
 LANGUAGE plpgsql
AS '
begin
	if _historico = 0 then
		select npdv_historico into _historico from pedidoventa_pdvp where cpdv_llave = _documento;
	end if;
	if _historico is null then
		return query select
				t.cpvc_llave,
				t.cpvc_documento,
				t.dpvc_valorfecha,
				t.mpvc_valornumero,
				t.cpvc_valortext,
				t.cpvc_valoropcion,
				t.cpvc_estado,
				t.cpvc_campo,
				t.cpvc_valorauxiliar,
				t.cpvc_transaccionregistro,
				t.cpvc_transaccioninactivo
			from pedidoventacaracteristica_pvcp t where t.cpvc_documento = _documento and t.cpvc_estado = ''A'';
	else
		return query select
			z.cpvc_llave,
			z.cpvc_documento,
			z.dpvc_valorfecha,
			z.mpvc_valornumero,
			z.cpvc_valortext,
			z.cpvc_valoropcion,
			z.cpvc_estado,
			z.cpvc_campo,
			z.cpvc_valorauxiliar,
			z.cpvc_transaccionregistro,
			z.cpvc_transaccioninactivo
		from historic.z_pvc_pedidoventacaracteristica z where z.cpvc_documento = _documento and z.cpvc_estado = ''A'';
	end if;
END;'
;

CREATE OR REPLACE FUNCTION public.campo4id(_documento character varying, _id_campo character varying, _historico integer)
 RETURNS TABLE(cpvc_llave character varying, cpvc_documento character varying, dpvc_valorfecha timestamp with time zone, mpvc_valornumero numeric, cpvc_valortext character varying, cpvc_valoropcion character varying, cpvc_estado character varying, cpvc_campo character varying, cpvc_valorauxiliar character varying, cpvc_transaccionregistro character varying, cpvc_transaccioninactivo character varying)
 LANGUAGE plpgsql
AS '
begin
	if _historico = 0 then
		select npdv_historico into _historico from pedidoventa_pdvp where cpdv_llave = _documento;
	end if;
	if _historico is null then
		return query select
				t.cpvc_llave,
				t.cpvc_documento,
				t.dpvc_valorfecha,
				t.mpvc_valornumero,
				t.cpvc_valortext,
				t.cpvc_valoropcion,
				t.cpvc_estado,
				t.cpvc_campo,
				t.cpvc_valorauxiliar,
				t.cpvc_transaccionregistro,
				t.cpvc_transaccioninactivo
			from pedidoventacaracteristica_pvcp t where t.cpvc_documento = _documento and t.cpvc_campo = _id_campo and t.cpvc_estado = ''A'';
	else
		return query select
			z.cpvc_llave,
			z.cpvc_documento,
			z.dpvc_valorfecha,
			z.mpvc_valornumero,
			z.cpvc_valortext,
			z.cpvc_valoropcion,
			z.cpvc_estado,
			z.cpvc_campo,
			z.cpvc_valorauxiliar,
			z.cpvc_transaccionregistro,
			z.cpvc_transaccioninactivo
		from historic.z_pvc_pedidoventacaracteristica z where z.cpvc_documento = _documento and z.cpvc_campo = _id_campo and z.cpvc_estado = ''A'';
	end if;	
END;'
;

CREATE OR REPLACE FUNCTION public.saldo4documento(_documento character varying, _historico integer)
 RETURNS TABLE(cpvd_llave character varying, cpvd_documento character varying, mpvd_valortotal numeric, mpvd_saldo numeric, cpvd_estado character varying, dpvd_fecha timestamp with time zone)
 LANGUAGE plpgsql
AS '
begin
	if _historico = 0 then
		select npdv_historico into _historico from pedidoventa_pdvp where cpdv_llave = _documento;
	end if;
	if _historico is null then
		return query select
				t.cpvd_llave, 
				t.cpvd_documento, 
				t.mpvd_valortotal, 
				t.mpvd_saldo, 
				t.cpvd_estado, 
				t.dpvd_fecha 
			from pedidoventadinero_pvdp t where t.cpvd_documento = _documento and t.cpvd_estado = ''A'';
	else
		return query select
				z.cpvd_llave, 
				z.cpvd_documento, 
				z.mpvd_valortotal, 
				z.mpvd_saldo, 
				z.cpvd_estado, 
				z.dpvd_fecha 
			from historic.z_pvd_pedidoventadinero z where z.cpvd_documento = _documento and z.cpvd_estado = ''A'';
	end if;
END;'
;


