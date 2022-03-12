COMMENT ON TABLE usuario_usrp IS '2019-10-07';
COMMENT ON TABLE usuariosesion_ussp IS '2019.10.07.00';

CREATE OR REPLACE VIEW valor_documento 
AS 
SELECT 
	cpvd_documento as documento, 
	mpvd_valorsubtotal as subtotal, 
	mpvd_valortotal as total, 
	mpvd_saldo as saldo,
	dpvd_fecha as fecha
   FROM pedidoventadinero_pvdp
  WHERE cpvd_estado = 'A';
  
update propiedad_ppdp set cppd_valor = '*' where cppd_key = 'FECHA_RANGO';