COMMENT ON TABLE usuario_usrp IS '2025-02-27';

select * from load_descripcion();


CREATE OR REPLACE FUNCTION public.load_descripcion()
 RETURNS void
 LANGUAGE plpgsql
AS '
DECLARE 
	_propiedad record;
BEGIN
    for _propiedad in select *, (select count(*) from pedidoventa_pdvp where cpdv_plantilla = pp.cppd_campo) as cantidad from propiedad_ppdp pp
				where pp.cppd_propiedadvalor = ''PROP_44'' and pp.cppd_estado = ''A'' and pp.cppd_motivo is null
				order by cantidad desc
    loop
		update pedidoventa_pdvp 
		set cpdv_descripcion = (select cpvc_valortext from pedidoventacaracteristica_pvcp where cpvc_documento = cpdv_llave and cpvc_estado = ''A'' and cpvc_campo = _propiedad.cppd_valor limit 1)
		where cpdv_plantilla = _propiedad.cppd_campo and npdv_historico is null;
		update pedidoventa_pdvp 
		set cpdv_descripcion = (select cpvc_valortext from historic.z_pvc_pedidoventacaracteristica where cpvc_documento = cpdv_llave and cpvc_estado = ''A'' and cpvc_campo = _propiedad.cppd_valor limit 1)
		where cpdv_plantilla = _propiedad.cppd_campo and npdv_historico is not null;
		update propiedad_ppdp set cppd_motivo = ''OK'' where cppd_llave = _propiedad.cppd_llave;
    end loop;
END; 
';


select * from load_descripcion();

DROP FUNCTION public.load_descripcion();