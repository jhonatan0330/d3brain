COMMENT ON TABLE usuario_usrp IS '2026-11-25';

update propiedadvalordefinido_pvdp 
set cpvd_estado = 'I' 
where cpvd_llave = 'PROP_239'; 

update propiedadvalordefinido_pvdp
set cpvd_uso_motivo = 'Indica que valida'
where cpvd_llave in ('PROP_191');

INSERT INTO account.catalogo_ctg (cctg_llave, cctg_nombre, cctg_codigo, dctg_fechainicial, dctg_fechafinal) VALUES('D3',  'CATALOGO INTERNO DE INDICADORES', 'D3', '2026-01-01', '2040-12-31');

ALTER TABLE account.registro_reg DROP CONSTRAINT fk_registrocuenta;

ALTER TABLE account.maparesultados_rmp DROP CONSTRAINT fk_maparesultadoscuenta;
