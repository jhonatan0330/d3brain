COMMENT ON TABLE usuario_usrp IS '2020-04-04';

update propiedadvalordefinido_pvdp set cpvd_motivo = 'Toma el valor del (saldo)total de XXXXXX' where cpvd_llave = 'PROP_39';

insert into cambio_cmbp (ccmb_llave,ccmb_nombre,ccmb_motivo,dcmb_fecha) values ('SC_20200404','SC_20200404','Reorganizar los valores de total',now());

INSERT INTO propiedad_ppdp 
	(cppd_llave, cppd_campo, cppd_valor, cppd_propiedadvalor, dppd_fechadefinicion, cppd_motivo, cppd_cambiocreacion, cppd_tipo)
select
	substring('P39_'|| cdpc_llave,0,32), cdpc_llave, 2, 'PROP_39', now(), 'Toma el valor del saldo del documento', 'SC_20200404', 'C' 
from documentoplantillacaracteristica_dpcp 
where 
	cdpc_formato = 'Z'
	and 
	cdpc_llave in (select cppd_valor from propiedad_ppdp where cppd_propiedadvalor = 'PROP_47' and cppd_estado = 'A')
	and
	cdpc_llave not in (select cppd_campo from propiedad_ppdp where cppd_propiedadvalor = 'PROP_39' and cppd_estado = 'A');

delete from propiedad_ppdp where cppd_propiedadvalor = 'PROP_46';

delete from propiedadvalordefinido_pvdp where cpvd_llave = 'PROP_46';

drop view vi_valores;

drop view valor_documento;

ALTER TABLE pedidoventadinero_pvdp
	DROP COLUMN mpvd_valorsubtotal;

CREATE OR REPLACE VIEW vi_valores
AS SELECT pedidoventadinero_pvdp.cpvd_documento AS vi_vlr_documento,
    pedidoventadinero_pvdp.mpvd_valortotal AS vi_vlr_total,
    pedidoventadinero_pvdp.mpvd_saldo AS vi_vlr_saldo,
    pedidoventadinero_pvdp.dpvd_fecha AS vi_vlr_fecha
   FROM pedidoventadinero_pvdp
  WHERE pedidoventadinero_pvdp.cpvd_estado::text = 'A'::text;
 
update reportebase_rpbp set crpb_jaspertext = replace (crpb_jaspertext , 'valor_documento', 'vi_valores') where crpb_jaspertext like 'valor_documento'; 

INSERT INTO propiedad_ppdp 
	(cppd_llave, cppd_campo, cppd_valor, cppd_propiedadvalor, dppd_fechadefinicion, cppd_motivo, cppd_cambiocreacion, cppd_tipo)
select
	substring('P39_'|| ndpc_orden || cdpc_llave,0,32), cdpc_llave, 2, 'PROP_39', now(), 'Toma el valor del saldo del documento', 'SC_20200404', 'C' 
from documentoplantillacaracteristica_dpcp 
where 
	cdpc_formato = 'Z'
	and 
	cdpc_llave in (select cppd_valor from propiedad_ppdp where cppd_propiedadvalor = 'PROP_96' and cppd_estado = 'A')
	and
	cdpc_llave not in (select cppd_campo from propiedad_ppdp where cppd_propiedadvalor = 'PROP_39' and cppd_estado = 'A');

ALTER TABLE documentorelaciongestor_drgp
	ALTER COLUMN cdrg_documentomodificador DROP NOT NULL;
