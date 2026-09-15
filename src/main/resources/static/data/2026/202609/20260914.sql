COMMENT ON TABLE usuario_usrp IS '2026-09-14';

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_propiedadboolean)
    SELECT 'PROP_239' , 'C', 'MONITOREAR', 'PLANTILLA_MONITOR', 'REQUISITO', true
    where not exists (select 1 from propiedadvalordefinido_pvdp where cpvd_llave  = 'PROP_239');

update propiedadvalordefinido_pvdp set cpvd_origen = 'L', bpvd_propiedadboolean = true where cpvd_llave = 'PROP_239';

ALTER TABLE account.cuenta_cue ALTER COLUMN ccue_catalogo DROP NOT NULL;

ALTER TABLE account.comprobante_cmp ALTER COLUMN ccmp_catalogo DROP NOT NULL;