COMMENT ON TABLE usuario_usrp IS '2024-02-01';

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo)
	select
	'PROP_252' , 'L', 'IMAGEN DEL DOCUMENTO', 'PLANTILLA_IMAGEN', 'REQUISITO'
	where not exists (select 1 from propiedadvalordefinido_pvdp where cpvd_llave  = 'PROP_252');
	
update propiedadvalordefinido_pvdp set bpvd_multiple =true where cpvd_llave = 'PROP_89';


CREATE OR REPLACE FUNCTION public.imagen(id_documento character varying, id_plantilla character varying, historico integer)
 RETURNS character varying
 LANGUAGE plpgsql
AS '
	declare plantilla_campo_imagen character varying;
	declare imagen character varying;
begin
    if id_documento is null then return  null; end if;
    select  cppd_valor into  plantilla_campo_imagen from propiedad_ppdp where cppd_campo = id_plantilla and cppd_estado = ''A'' and cppd_propiedadvalor = ''PROP_252'';
    if plantilla_campo_imagen is null then
		return (select cdpl_imagen from documentoplantilla_dplp where cdpl_llave = id_plantilla);
    else
    	select cpvc_valortext into imagen from campo4id(id_documento, plantilla_campo_imagen, historico);
		if imagen is null then
			return (select cdpl_imagen from documentoplantilla_dplp where cdpl_llave = id_plantilla);
		else
			return imagen;
		end if;
    end if;
end;';