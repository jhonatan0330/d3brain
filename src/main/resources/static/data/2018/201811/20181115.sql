COMMENT ON TABLE usuario_usrp IS '2018-11-15';

update pedidoventacaracteristica_pvcp set cpvc_valoropcion = (select cdex_expedientedetalle from documentorelacionexpediente_dexp where cdex_campomaestro  = cpvc_llave)
where cpvc_campo in (select cdpc_llave from documentoplantillacaracteristica_dpcp where cdpc_formato = 'Z'
	and cdpc_llave in (select cptr_campo from procesotransicion_ptrp )
	and cdpc_llave not in (select cpcp_campo from plantillacampoparametro_pcpp  where cpcp_key ='MULTIPLE' ))
and cpvc_valoropcion is null;

update reportebase_rpbp set crpb_jaspertext = 
	replace(crpb_jaspertext,
	'(select array_to_string(array(select cpdv_nombre from pedidoventa_pdvp where cpdv_llave in ( select cdex_expedientedetalle from documentorelacionexpediente_dexp
	where cdex_campomaestro in (select cpvc_llave from campo_documento where cdrc_documento  =(select cdpr_documento from deduccionproducto_dprp where cdpr_llave  = ctpi_deduccionproducto)))), '''', '''')) as documento_relacionado,'
	,'select cpdv_nombre from pedidoventa_pdvp  where cpdv_llave  = 
	(select cpvc_valoropcion from campo_documento where cdrc_documento = (select cdpr_documento from deduccionproducto_dprp where cdpr_llave  = ctpi_deduccionproducto) and cpvc_campo in (select cptr_campo from procesotransicion_ptrp))) as documento_relacionado,');

update reportebase_rpbp set crpb_variables = 
	replace(crpb_variables, 'JASPERTIPO', 'P_JASPERTIPO');
/*
select
    dtpi_fecha,
    (select cpro_nombre from producto_prop where cpro_llave =ctpi_producto) as ctpi_producto,
    (select cpdv_nombre from pedidoventa_pdvp where cpdv_llave =
	(select cdpr_documento from deduccionproducto_dprp where cdpr_llave  = ctpi_deduccionproducto)) as ctpi_concepto,
    (select cpdv_nombre from pedidoventa_pdvp  where cpdv_llave  = 
	(select cpvc_valoropcion from campo_documento where cdrc_documento = (select cdpr_documento from deduccionproducto_dprp where cdpr_llave  = ctpi_deduccionproducto) and cpvc_campo in (select cptr_campo from procesotransicion_ptrp))) as documento_relacionado,
    mtpi_cantidadinicial,
    mtpi_cantidadfinal,
    mtpi_cantidad,
    (select cbod_nombre from bodega_bodp where cbod_llave = ctpi_bodega) as bodega
FROM
    trazabilidadproductoinventario_tpip
WHERE
    --ctpi_producto = 'bb0d4534e9824f639b35c3b52b1d48bc' and 
    dtpi_fecha >= '2018-11-15 09:00' --and dtpi_fecha < $P{P_FECHA_FIN}
order by
    ctpi_bodega, ctpi_producto, dtpi_fecha


    select * from deduccionproducto_dprp, pedidoventa_pdvp where cpdv_llave =  cdpr_documento
    order by ddpr_fecha desc limit 10 
    --where cdpr_llave  = ctpi_deduccionproducto

        (select array_to_string(array(select cpdv_nombre from pedidoventa_pdvp where cpdv_llave in ( select cdex_expedientedetalle from documentorelacionexpediente_dexp
	where cdex_campomaestro in (select * from campo_documento where cdrc_documento  =(select cdpr_documento from deduccionproducto_dprp where cdpr_llave  = 'bba25fb8f8d64979b4d046bfd673f47c')))), '', '')) as documento_relacionado,


select * from campo_documento 
where cdrc_documento = (select cdpr_documento from deduccionproducto_dprp where cdpr_llave  = '27807746a18b46c991413a7e0c91e094');

select * from documentorelacionexpediente_dexp where cdex_campomaestro in ('b0b47aa71f6940949a7e93f74eb12721','b61774c8e51b4e7EXP-DESPACHO');

select * from campo_documento 
where cdrc_documento = (select cdpr_documento from deduccionproducto_dprp where cdpr_llave  = '0a2c3a34280348648e8398d71568b95c');

'918c9abc547b47a8a2e5585a797a7b71'
'b61774c8e51b4e76b11f1c04d083c930'

select * from campo_documento 
where cdrc_documento = (select cdpr_documento from deduccionproducto_dprp where cdpr_llave  = '27807746a18b46c991413a7e0c91e094');

'918c9abc547b47a8a2e5585a797a7b71'
'b61774c8e51b4e76b11f1c04d083c930'

select * from pedidoventa_pdvp  where cpdv_llave  = '918c9abc547b47a8a2e5585a797a7b71';
select * from pedidoventa_pdvp  where cpdv_llave  = 'b61774c8e51b4e76b11f1c04d083c930';

select * from pedidoventa_pdvp  where cpdv_llave  = '636191271f914b5ea7a3efe982434731'
select * from documentoplantilla_dplp  where cdpl_llave  = '07c7033734a74da2a21cf9a696af5435'

select * from campo_documento where cdrc_documento = '918c9abc547b47a8a2e5585a797a7b71';
select * from campo_documento where cdrc_documento = 'b61774c8e51b4e76b11f1c04d083c930';

select cpdv_nombre from pedidoventa_pdvp  where cpdv_llave  = (
select cpvc_valoropcion from campo_documento where cdrc_documento = 'b61774c8e51b4e76b11f1c04d083c930' and cpvc_campo in (select cptr_campo from procesotransicion_ptrp));

-- 
select (select cdex_expedientedetalle from documentorelacionexpediente_dexp where cdex_campomaestro  = cpvc_llave),
* from documentoplantillacaracteristica_dpcp, pedidoventacaracteristica_pvcp 
where cdpc_llave = cpvc_campo and cdpc_formato = 'Z' 
and cdpc_llave in (select cptr_campo from procesotransicion_ptrp )
and cdpc_llave not in (select cpcp_campo from plantillacampoparametro_pcpp  where cpcp_key ='MULTIPLE' )
and cpvc_valoropcion is null
order by cdpc_llave;

select * from documentorelacionexpediente_dexp where cdex_campomaestro  = '4b8363feab3b42cEXP-07c7033734a'
*/
