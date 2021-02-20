

insert into cambio_cmbp (ccmb_llave,ccmb_nombre,ccmb_motivo,dcmb_fecha,dcmb_fechaaplicacion,ccmb_sesionactiva,ccmb_estado) values ('16dbe1a9fbaf4fddacf0c581d871d70a','SC_100','Colores de los estados',TIMESTAMP '2020-09-04 11:11:30.643',NULL,'89f61840bb6245689b3f320b19ca085d','A');

-- -------------------
-- INSERTS for public.documentoplantilla_dplp
-- -------------------
insert into documentoplantilla_dplp (cdpl_llave,cdpl_objetivo,cdpl_codigo,cdpl_nombre,cdpl_imagen,cdpl_estado) values ('ca6c98e4de7c4614bfb2a10883b66eaa','VERIFICAR SEGUIMIENTO AUTOMATICAMENTE','F_81','VERIFICAR SEGUIMIENTO AUTOMATICAMENTE','http://colombiansofture.com/imagenes/modulo.png','A');

-- -------------------
-- INSERTS for public.documentoplantillacaracteristica_dpcp
-- -------------------
insert into documentoplantillacaracteristica_dpcp (cdpc_llave,cdpc_objetivo,cdpc_plantilla,cdpc_formato,cdpc_nombre,cdpc_codigo,ndpc_orden,cdpc_imagen,cdpc_estado) values ('6f8196308d3c46ebaf3aa2985ccd60e8','Contiene el proceso que se va a gestionar','ca6c98e4de7c4614bfb2a10883b66eaa','Z','PROCESO','PROCESO',1,NULL,'A');

-- -------------------
-- INSERTS for public.procesoestado_pesp
-- -------------------
insert into procesoestado_pesp (cpes_llave,cpes_estadodocumento,npes_avance,cpes_nombre,cpes_proceso,cpes_estado,cpes_tipo) values ('b0a84a129a2c429786dcce9af38ecc32','A',40,'EN RUTA 30','b389af4e10d0419b80a4acdd6d2d0dab','A','E');
insert into procesoestado_pesp (cpes_llave,cpes_estadodocumento,npes_avance,cpes_nombre,cpes_proceso,cpes_estado,cpes_tipo) values ('0d093eeaa3bf4b7691b7f419258d6690','A',50,'EN RUTA 60','b389af4e10d0419b80a4acdd6d2d0dab','A','E');
insert into procesoestado_pesp (cpes_llave,cpes_estadodocumento,npes_avance,cpes_nombre,cpes_proceso,cpes_estado,cpes_tipo) values ('bcd119c126c443548b7d65d107565950','A',70,'EN RUTA SIN SEGUIMIENTO','b389af4e10d0419b80a4acdd6d2d0dab','A','E');
insert into procesoestado_pesp (cpes_llave,cpes_estadodocumento,npes_avance,cpes_nombre,cpes_proceso,cpes_estado,cpes_tipo) values ('779aa219416a4d9da31db9ed6ff0c0d3','A',35,'TIEMPO EN RUTA SEGUIMIENTO?','b389af4e10d0419b80a4acdd6d2d0dab','A','D');

-- -------------------
-- INSERTS for public.procesotransicion_ptrp
-- -------------------
insert into procesotransicion_ptrp (cptr_llave,cptr_nombre,cptr_proceso,cptr_estadopartida,cptr_plantilla,bptr_documentador,cptr_afectasaldo,bptr_rapida,cptr_estadollegada,cptr_estado) values ('2f5ea14b97174296971970828114bb40','VERIFICAR SEGUIMIENTO AUTOMATICAMENTE','b389af4e10d0419b80a4acdd6d2d0dab','b8a065b9846c4cab9f3e59b44e6df28b','ca6c98e4de7c4614bfb2a10883b66eaa',false,NULL,false,'779aa219416a4d9da31db9ed6ff0c0d3','A');
insert into procesotransicion_ptrp (cptr_llave,cptr_nombre,cptr_proceso,cptr_estadopartida,cptr_plantilla,bptr_documentador,cptr_afectasaldo,bptr_rapida,cptr_estadollegada,cptr_estado) values ('b66e210fab5d472982ce72f584fc7020','> 30 MIN','b389af4e10d0419b80a4acdd6d2d0dab','779aa219416a4d9da31db9ed6ff0c0d3',NULL,false,NULL,false,'b0a84a129a2c429786dcce9af38ecc32','A');
insert into procesotransicion_ptrp (cptr_llave,cptr_nombre,cptr_proceso,cptr_estadopartida,cptr_plantilla,bptr_documentador,cptr_afectasaldo,bptr_rapida,cptr_estadollegada,cptr_estado) values ('a49fec75f0984e5ea84b090b0d5a2175','> 60 MIN','b389af4e10d0419b80a4acdd6d2d0dab','779aa219416a4d9da31db9ed6ff0c0d3',NULL,false,NULL,false,'0d093eeaa3bf4b7691b7f419258d6690','A');
insert into procesotransicion_ptrp (cptr_llave,cptr_nombre,cptr_proceso,cptr_estadopartida,cptr_plantilla,bptr_documentador,cptr_afectasaldo,bptr_rapida,cptr_estadollegada,cptr_estado) values ('9495051a59d6455f8be85810a56a0c57','> 90 MIN','b389af4e10d0419b80a4acdd6d2d0dab','779aa219416a4d9da31db9ed6ff0c0d3',NULL,false,NULL,false,'bcd119c126c443548b7d65d107565950','A');
insert into procesotransicion_ptrp (cptr_llave,cptr_nombre,cptr_proceso,cptr_estadopartida,cptr_plantilla,bptr_documentador,cptr_afectasaldo,bptr_rapida,cptr_estadollegada,cptr_estado) values ('f437df1854b24a9d8f9693d5f7c3f4d6','REGISTRAR SEGUIMIENTO','b389af4e10d0419b80a4acdd6d2d0dab','b0a84a129a2c429786dcce9af38ecc32','0a4a5a88379c4873af4506d8689e0027',true,NULL,false,'b8a065b9846c4cab9f3e59b44e6df28b','A');
insert into procesotransicion_ptrp (cptr_llave,cptr_nombre,cptr_proceso,cptr_estadopartida,cptr_plantilla,bptr_documentador,cptr_afectasaldo,bptr_rapida,cptr_estadollegada,cptr_estado) values ('eeacdaafe85348cb8893fddd0ec702b0','VERIFICAR SEGUIMIENTO','b389af4e10d0419b80a4acdd6d2d0dab','b0a84a129a2c429786dcce9af38ecc32','ca6c98e4de7c4614bfb2a10883b66eaa',false,NULL,false,'779aa219416a4d9da31db9ed6ff0c0d3','A');
insert into procesotransicion_ptrp (cptr_llave,cptr_nombre,cptr_proceso,cptr_estadopartida,cptr_plantilla,bptr_documentador,cptr_afectasaldo,bptr_rapida,cptr_estadollegada,cptr_estado) values ('d482eefc94b749fab3afdfea3daa2b95','VERIFICAR SEGUIMIENTO','b389af4e10d0419b80a4acdd6d2d0dab','0d093eeaa3bf4b7691b7f419258d6690','ca6c98e4de7c4614bfb2a10883b66eaa',false,NULL,false,'779aa219416a4d9da31db9ed6ff0c0d3','A');
insert into procesotransicion_ptrp (cptr_llave,cptr_nombre,cptr_proceso,cptr_estadopartida,cptr_plantilla,bptr_documentador,cptr_afectasaldo,bptr_rapida,cptr_estadollegada,cptr_estado) values ('6a91a6004dd1453baa7aca36b746d891','REGISTRAR SEGUIMIENTO','b389af4e10d0419b80a4acdd6d2d0dab','0d093eeaa3bf4b7691b7f419258d6690','0a4a5a88379c4873af4506d8689e0027',true,NULL,false,'b8a065b9846c4cab9f3e59b44e6df28b','A');
insert into procesotransicion_ptrp (cptr_llave,cptr_nombre,cptr_proceso,cptr_estadopartida,cptr_plantilla,bptr_documentador,cptr_afectasaldo,bptr_rapida,cptr_estadollegada,cptr_estado) values ('f03c88929c1f4fd6acb7b4c901bac210','REGISTRAR SEGUIMIENTO','b389af4e10d0419b80a4acdd6d2d0dab','bcd119c126c443548b7d65d107565950','0a4a5a88379c4873af4506d8689e0027',true,NULL,false,'b8a065b9846c4cab9f3e59b44e6df28b','A');

-- -------------------
-- UPDATES for public.procesotransicion_ptrp
-- -------------------
update procesotransicion_ptrp set cptr_nombre = 'REGISTRAR SEGUIMIENTO' where cptr_llave = '8d232c1c97854349a1a32db7adfae61b';

-- -------------------
-- INSERTS for public.propiedad_ppdp
-- -------------------
insert into propiedad_ppdp (cppd_llave,cppd_motivo,cppd_propiedadvalor,cppd_tipo,cppd_campo,cppd_valor,cppd_texto,dppd_fechadefinicion,dppd_fechaimplementacion,cppd_cambiocreacion,cppd_cambioeliminacion,cppd_estado,cppd_rol,dppd_fechainicial,dppd_fechafinal,cppd_usuario,cppd_bloqueo) values ('c74b0b6bdfe04a329b4645164416b54a',NULL,'PROP_92','A','b8a065b9846c4cab9f3e59b44e6df28b','#FFFF00',NULL,TIMESTAMP '2020-03-17 17:52:55.258',TIMESTAMP '2020-09-04 11:11:31.754','16dbe1a9fbaf4fddacf0c581d871d70a',NULL,'A',NULL,NULL,NULL,NULL,NULL);
insert into propiedad_ppdp (cppd_llave,cppd_motivo,cppd_propiedadvalor,cppd_tipo,cppd_campo,cppd_valor,cppd_texto,dppd_fechadefinicion,dppd_fechaimplementacion,cppd_cambiocreacion,cppd_cambioeliminacion,cppd_estado,cppd_rol,dppd_fechainicial,dppd_fechafinal,cppd_usuario,cppd_bloqueo) values ('20565a4c0ce143e083d824652e8e4ddb',NULL,'PROP_92','A','b0a84a129a2c429786dcce9af38ecc32','#ffa500',NULL,TIMESTAMP '2020-09-04 11:12:30.386',TIMESTAMP '2020-09-04 11:12:30.302','16dbe1a9fbaf4fddacf0c581d871d70a',NULL,'A',NULL,NULL,NULL,NULL,NULL);
insert into propiedad_ppdp (cppd_llave,cppd_motivo,cppd_propiedadvalor,cppd_tipo,cppd_campo,cppd_valor,cppd_texto,dppd_fechadefinicion,dppd_fechaimplementacion,cppd_cambiocreacion,cppd_cambioeliminacion,cppd_estado,cppd_rol,dppd_fechainicial,dppd_fechafinal,cppd_usuario,cppd_bloqueo) values ('e2279012f7ea41e38f9f0c9360af7776',NULL,'PROP_92','A','0d093eeaa3bf4b7691b7f419258d6690','#FF0000',NULL,TIMESTAMP '2020-09-04 11:13:03.237',TIMESTAMP '2020-09-04 11:13:03.155','16dbe1a9fbaf4fddacf0c581d871d70a',NULL,'A',NULL,NULL,NULL,NULL,NULL);
insert into propiedad_ppdp (cppd_llave,cppd_motivo,cppd_propiedadvalor,cppd_tipo,cppd_campo,cppd_valor,cppd_texto,dppd_fechadefinicion,dppd_fechaimplementacion,cppd_cambiocreacion,cppd_cambioeliminacion,cppd_estado,cppd_rol,dppd_fechainicial,dppd_fechafinal,cppd_usuario,cppd_bloqueo) values ('9775cbb03bbb436c9b3bbd7fb1fe3554',NULL,'PROP_92','A','bcd119c126c443548b7d65d107565950','#572364',NULL,TIMESTAMP '2020-09-04 11:13:40.614',TIMESTAMP '2020-09-04 11:13:40.575','16dbe1a9fbaf4fddacf0c581d871d70a',NULL,'A',NULL,NULL,NULL,NULL,NULL);
insert into propiedad_ppdp (cppd_llave,cppd_motivo,cppd_propiedadvalor,cppd_tipo,cppd_campo,cppd_valor,cppd_texto,dppd_fechadefinicion,dppd_fechaimplementacion,cppd_cambiocreacion,cppd_cambioeliminacion,cppd_estado,cppd_rol,dppd_fechainicial,dppd_fechafinal,cppd_usuario,cppd_bloqueo) values ('9bbe2455682e4daaa4d2e35fcafa69c7',NULL,'PROP_37','C','6f8196308d3c46ebaf3aa2985ccd60e8','*','TODOS',TIMESTAMP '2020-09-04 11:19:48.343',TIMESTAMP '2020-09-04 11:19:48.343','16dbe1a9fbaf4fddacf0c581d871d70a',NULL,'A',NULL,NULL,NULL,NULL,NULL);
insert into propiedad_ppdp (cppd_llave,cppd_motivo,cppd_propiedadvalor,cppd_tipo,cppd_campo,cppd_valor,cppd_texto,dppd_fechadefinicion,dppd_fechaimplementacion,cppd_cambiocreacion,cppd_cambioeliminacion,cppd_estado,cppd_rol,dppd_fechainicial,dppd_fechafinal,cppd_usuario,cppd_bloqueo) values ('a800b54783f241b58547282ccdaa9bc6',NULL,'PROP_32','C','6f8196308d3c46ebaf3aa2985ccd60e8','1',NULL,TIMESTAMP '2020-09-04 11:44:21.004',TIMESTAMP '2020-09-04 11:44:21.004','16dbe1a9fbaf4fddacf0c581d871d70a',NULL,'A',NULL,NULL,NULL,NULL,NULL);

insert into propiedad_ppdp (cppd_llave,cppd_motivo,cppd_propiedadvalor,cppd_tipo,cppd_campo,cppd_valor,cppd_texto,dppd_fechadefinicion,dppd_fechaimplementacion,cppd_cambiocreacion,cppd_cambioeliminacion,cppd_estado,cppd_rol,dppd_fechainicial,dppd_fechafinal,cppd_usuario,cppd_bloqueo) values ('f1a4aed83a7b41d4b6bc4ea147b6f4e4','Consulta todos los viajes que se enecuntran en ruta, o en ruta 30 o en ruta 60','PROP_140','T','2f5ea14b97174296971970828114bb40','declare
	fecha_gestion timestamptz;
	viaje_ruta RECORD;
begin
	for viaje_ruta in select * from pedidoventa_pdvp viajes 		
		where viajes.cpdv_plantilla = ''db0c724b2c5142e39f0f5e2ac7f5e242'' 
		and viajes.cpdv_estadoexpediente in (''b8a065b9846c4cab9f3e59b44e6df28b'',''b0a84a129a2c429786dcce9af38ecc32'',''0d093eeaa3bf4b7691b7f419258d6690'') 
		order by cpdv_nombre
	loop		
		select ddrg_fecha into fecha_gestion from documentorelaciongestor_drgp 
			inner join pedidoventa_pdvp novedad on (novedad.cpdv_llave = cdrg_documentomodificador )
			where cdrg_documentoprincipal = viaje_ruta.cpdv_llave and cdrg_estado = ''A'' 
			and novedad.cpdv_plantilla in (''0a4a5a88379c4873af4506d8689e0027'', ''fe46cc32a1d348e4aec6867dc8d6b3a8'')
			order by ddrg_fecha desc limit 1;
		if (fecha_gestion is null or fecha_gestion < (now() - interval ''30'' minute)) then
			return next viaje_ruta;
		end if;
	end loop;
end;','00:00:00:00:10',TIMESTAMP '2020-09-04 11:42:35.564',TIMESTAMP '2020-09-04 14:47:41.390','16dbe1a9fbaf4fddacf0c581d871d70a',NULL,'A',NULL,TIMESTAMP '2020-09-04 00:00:00.000',NULL,NULL,NULL);

insert into propiedad_ppdp (cppd_llave,cppd_motivo,cppd_propiedadvalor,cppd_tipo,cppd_campo,cppd_valor,cppd_texto,dppd_fechadefinicion,dppd_fechaimplementacion,cppd_cambiocreacion,cppd_cambioeliminacion,cppd_estado,cppd_rol,dppd_fechainicial,dppd_fechafinal,cppd_usuario,cppd_bloqueo) values ('07522052d0a7464d92b8ced3d9563d57',NULL,'PROP_146','A','779aa219416a4d9da31db9ed6ff0c0d3','declare 
	fecha_gestion timestamptz;
begin 
	select ddrg_fecha into fecha_gestion from documentorelaciongestor_drgp 
			inner join pedidoventa_pdvp novedad on (novedad.cpdv_llave = cdrg_documentomodificador )
			where cdrg_documentoprincipal = documento and cdrg_estado = ''A'' 
			and novedad.cpdv_plantilla in (''0a4a5a88379c4873af4506d8689e0027'', ''fe46cc32a1d348e4aec6867dc8d6b3a8'')
			order by ddrg_fecha desc limit 1;
	
	if( fecha_gestion < (now() - interval ''90'' minute) ) then 
		return ''> 90 MIN'';
	else
		if(fecha_gestion < (now() - interval ''60'' minute)) then 
			return ''> 60 MIN'';
		else
			if(fecha_gestion < (now() - interval ''30'' minute)) then 
				return ''> 30 MIN'';
			end if;
		end if;
	end if;
	return ''< 30 MIN'';
end',NULL,TIMESTAMP '2020-09-04 12:00:20.346',TIMESTAMP '2020-09-04 14:53:16.871','16dbe1a9fbaf4fddacf0c581d871d70a',NULL,'A',NULL,NULL,NULL,NULL,NULL);

-- -------------------
update propiedad_ppdp set cppd_cambioeliminacion = '16dbe1a9fbaf4fddacf0c581d871d70a', cppd_estado = 'I' where cppd_llave = '116972a173b646a2940f6b53fd49b8e8';

-- -------------------
insert into relacioninterna_ritp (crit_llave,crit_propiedad,crit_plantilla,crit_campo,crit_estado) values ('d237c439df524f0c8806202ea80f38d5','f1a4aed83a7b41d4b6bc4ea147b6f4e4','ca6c98e4de7c4614bfb2a10883b66eaa','6f8196308d3c46ebaf3aa2985ccd60e8','A');

CREATE OR REPLACE FUNCTION public.decision_07522052d0a7464d92b8ced3d9563d57(documento character varying, modificador character varying)
  RETURNS character varying
  LANGUAGE plpgsql
AS
$body$
declare 
	fecha_gestion timestamptz;
begin 
	select ddrg_fecha into fecha_gestion from documentorelaciongestor_drgp 
			inner join pedidoventa_pdvp novedad on (novedad.cpdv_llave = cdrg_documentomodificador )
			where cdrg_documentoprincipal = documento and cdrg_estado = 'A' 
			and novedad.cpdv_plantilla in ('0a4a5a88379c4873af4506d8689e0027', 'fe46cc32a1d348e4aec6867dc8d6b3a8')
			order by ddrg_fecha desc limit 1;
	
	if( fecha_gestion < (now() - interval '90' minute) ) then 
		return '> 90 MIN';
	else
		if(fecha_gestion < (now() - interval '60' minute)) then 
			return '> 60 MIN';
		else
			if(fecha_gestion < (now() - interval '30' minute)) then 
				return '> 30 MIN';
			end if;
		end if;
	end if;
	return '< 30 MIN';
end;
$body$
  VOLATILE
  COST 100;

CREATE OR REPLACE FUNCTION public.propiedad_f1a4aed83a7b41d4b6bc4ea147b6f4e4(documento character varying, cant integer, pagina integer, fechaminima timestamp with time zone, fechamaxima timestamp with time zone, filtro character varying, codigo_exacto character varying, token character varying)
  RETURNS SETOF pedidoventa_pdvp
  LANGUAGE plpgsql
AS
$body$
declare
	fecha_gestion timestamptz;
	viaje_ruta RECORD;
begin
	for viaje_ruta in select * from pedidoventa_pdvp viajes 		
		where viajes.cpdv_plantilla = 'db0c724b2c5142e39f0f5e2ac7f5e242' 
		and viajes.cpdv_estadoexpediente in ('b8a065b9846c4cab9f3e59b44e6df28b','b0a84a129a2c429786dcce9af38ecc32','0d093eeaa3bf4b7691b7f419258d6690') 
		order by cpdv_nombre
	loop		
		select ddrg_fecha into fecha_gestion from documentorelaciongestor_drgp 
			inner join pedidoventa_pdvp novedad on (novedad.cpdv_llave = cdrg_documentomodificador )
			where cdrg_documentoprincipal = viaje_ruta.cpdv_llave and cdrg_estado = 'A' 
			and novedad.cpdv_plantilla in ('0a4a5a88379c4873af4506d8689e0027', 'fe46cc32a1d348e4aec6867dc8d6b3a8')
			order by ddrg_fecha desc limit 1;
		if (fecha_gestion is null or fecha_gestion < (now() - interval '30' minute)) then
			return next viaje_ruta;
		end if;
	end loop;
end;
$body$
  VOLATILE
  COST 100
  ROWS 1000;

