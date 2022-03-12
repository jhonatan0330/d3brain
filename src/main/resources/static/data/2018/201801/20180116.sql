/*
Elimino la tabla reporte colocando permisos a los formularios de listable segun esta tabla
Elimino el campo pagado de los estados, no funcionaba
Actualizo los valores del id de catalogo en los parametros de un campo
Elimino las columans de tercero y descripcion de movimiento, tercero no se usaba y descripcion sale de la nueva funcion
Funcion para obtener la descripcion de un documento
Actualizar el nuevo nombre de parametros de los campos cuentas de formularios
Retire la tabla pedidoventamovimiento, esto se debe hacer por un documento y se maneja con las cuentas
Coloque la opcion de saldo a todos los procesos que manejaban valores
*/
COMMENT ON TABLE usuario_usrp IS '2018-01-16';

COMMENT ON TABLE usuariosesion_ussp IS '2018.01.16.06';

update documentoplantillarol_dprp set bdpr_listable = false, bdpr_vertodos = false where cdpr_plantilla  in (select cdpl_llave from documentoplantilla_dplp  where cdpl_tipo  = 'R');
update documentoplantillarol_dprp set bdpr_listable = true where cdpr_llave in (select cdpr_llave from documentoplantillarol_dprp  where cdpr_plantilla  in ( 
select cdpl_llave from documentoplantilla_dplp  where cdpl_tipo  = 'R') and cdpr_plantilla not in (select crep_base from reporte_repp where crep_estado = 'A'));
update documentoplantillarol_dprp set bdpr_listable = true where cdpr_llave in (select cdpr_llave from reporte_repp, documentoplantillarol_dprp where crep_estado = 'A' and crep_rol = cdpr_rol and cdpr_plantilla = crep_base);

DROP TABLE reporte_repp;

ALTER TABLE expedienteestado_exep
	DROP COLUMN bexe_pagado;

update pedidoventacaracteristica_pvcp set cpvc_valorauxiliar = (select cpvm_movimiento from pedidoventamovimiento_pvmp where cpvm_llave = cpvc_valorauxiliar ) where cpvc_campo in (
	select cdpc_llave from documentoplantillacaracteristica_dpcp  where cdpc_formato  = 'C'
) and (select cpvm_llave from pedidoventamovimiento_pvmp where cpvm_llave = cpvc_valorauxiliar ) is not null;


ALTER TABLE movimiento_movp
	DROP COLUMN cmov_descripcion,
	DROP COLUMN cmov_tercero;

DROP TABLE pedidoventamovimiento_pvmp;


CREATE OR REPLACE FUNCTION movimiento_descripcion(id_documento character varying)
  RETURNS character varying AS
$BODY$ 
DECLARE plantilla_campo_descripcion character varying;
DECLARE id_documento_principal character varying;
DECLARE descripcion_anidada character varying;
BEGIN 
    IF id_documento IS NULL THEN 
        RETURN NULL;
    END IF;
    SELECT cdpl_descripcion INTO plantilla_campo_descripcion FROM documentoplantilla_dplp, pedidoventa_pdvp where cpdv_plantilla = cdpl_llave and cpdv_llave = id_documento;
    CASE WHEN plantilla_campo_descripcion IS NOT NULL THEN 
	RETURN (select cpvc_valortext from campo_documento where cdrc_documento = id_documento and cpvc_campo = plantilla_campo_descripcion);
    ELSE
        SELECT cdrg_documentoprincipal INTO id_documento_principal FROM documentorelaciongestor_drgp WHERE cdrg_documentomodificador = id_documento;
	CASE WHEN id_documento_principal IS  NULL THEN 
	    RETURN NULL;
        ELSE
            SELECT movimiento_descripcion(cdrg_documentoprincipal) INTO descripcion_anidada;
            IF descripcion_anidada IS NULL THEN
		RETURN NULL;
            ELSE
		RETURN '(' || (select cpdv_nombre from pedidoventa_pdvp pcd where cpdv_llave = id_documento_principal) ||') '|| descripcion_anidada();
	    END IF;
        END CASE;
    END CASE;
END; 
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION upsert_reporte(character, character, character, character, character, character)
  OWNER TO postgres;

update plantillacampoparametro_pcpp set cpcp_key = 'CUENTA_CATALOGO_FILTRO' where cpcp_campo  in (select cdpc_llave from documentoplantillacaracteristica_dpcp  where cdpc_formato  = 'C');

INSERT INTO plantillacampoparametro_pcpp (cpcp_llave, cpcp_campo, cpcp_key, cpcp_valor)
	select SUBSTRING('2-'||cpcp_llave, 0,32), cpcp_campo, 'CUENTA_CATALOGO_MOVIMIENTO', cpcp_valor from plantillacampoparametro_pcpp where cpcp_campo  in (
	select cdpc_llave from documentoplantillacaracteristica_dpcp  where cdpc_formato  = 'C');

update plantillacampoparametro_pcpp set cpcp_valor = (select ccat_llave from catalogo_catp where ccat_codigo = cpcp_valor) where cpcp_campo  in (select cdpc_llave from documentoplantillacaracteristica_dpcp  where cdpc_formato  = 'C');

ALTER TABLE documentoplantillacosto_dpcp
	DROP CONSTRAINT fk_documentoplantillacostotipomovimientoabono;

ALTER TABLE documentoplantillacosto_dpcp
	DROP CONSTRAINT fk_documentoplantillacostotipomovimientopago;

ALTER TABLE documentoplantillacosto_dpcp
	DROP COLUMN cdpc_tipomovimientopago,
	DROP COLUMN cdpc_tipomovimientoabono,
	DROP COLUMN bdpc_solicitafecha,
	ADD COLUMN bdpc_saldo boolean DEFAULT false NOT NULL;

update documentoplantillacosto_dpcp set bdpc_saldo = true where cdpc_plantilla  in (
select cdpl_llave from documentoplantilla_dplp  where cdpl_maquinaestados  is not null);