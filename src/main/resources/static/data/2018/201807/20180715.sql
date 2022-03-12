COMMENT ON TABLE usuario_usrp IS '2018-07-15';

ALTER TABLE cuentapermisousuario_cpup
	ADD COLUMN ccpu_documentousuario character varying(32),
	ADD COLUMN ccpu_old character varying(32);

ALTER TABLE cuentapermisousuario_cpup ALTER COLUMN ccpu_usuario DROP NOT NULL;

INSERT INTO cuentapermisousuario_cpup (ccpu_llave, ccpu_usuario, ccpu_cuenta, bcpu_validarturno, mcpu_cierremaximo, ccpu_estado, ccpu_catalogo, ccpu_documentousuario, ccpu_old)
select replace(md5(random()::text || clock_timestamp()::text)::uuid::text, '-',''), null, ccpu_cuenta, bcpu_validarturno, mcpu_cierremaximo, ccpu_estado, ccpu_catalogo, cpdv_llave, ccpu_llave
from cuentapermisousuario_cpup, usuario_usrp , pedidoventa_pdvp
where cusr_llave = ccpu_usuario and cusr_llave  = ccpu_usuario and cpdv_nombre = cusr_identificacion;
--select * from cuentapermisousuario_cpup  
--select (select ccpu_llave from cuentapermisousuario_cpup nueva where ccpu_old = cmov_cuentapermisousuario limit 1),* from movimiento_movp order by 1;

update pedidoventacaracteristica_pvcp set cpvc_valoropcion = (select ccpu_llave from cuentapermisousuario_cpup nueva where ccpu_old = cpvc_valoropcion limit 1)  where cpvc_campo in (select cdpc_llave from documentoplantillacaracteristica_dpcp where cdpc_formato = 'C');
--select (select ccpu_llave from cuentapermisousuario_cpup where ccpu_llave = cpvc_valoropcion) ,* 
--from pedidoventacaracteristica_pvcp where cpvc_campo in (select cdpc_llave from documentoplantillacaracteristica_dpcp where cdpc_formato = 'C') order by 1  limit 1000; 

update movimiento_movp set cmov_cuentapermisousuario = (select ccpu_llave from cuentapermisousuario_cpup nueva where ccpu_old = cmov_cuentapermisousuario limit 1);
update movimiento_movp set cmov_cuentapermisousuariodestino = (select ccpu_llave from cuentapermisousuario_cpup nueva where ccpu_old = cmov_cuentapermisousuariodestino limit 1) where cmov_cuentapermisousuariodestino is not null;
update turno_turp set ctur_cuentapermiso = (select ccpu_llave from cuentapermisousuario_cpup nueva where ccpu_old = ctur_cuentapermiso limit 1);

delete from cuentapermisousuario_cpup where ccpu_documentousuario is null;

ALTER TABLE cuentapermisousuario_cpup ALTER COLUMN ccpu_documentousuario SET NOT NULL;

ALTER TABLE cuentapermisousuario_cpup
	DROP COLUMN ccpu_usuario;

ALTER TABLE cuentapermisousuario_cpup
	ALTER COLUMN ccpu_documentousuario SET NOT NULL;
