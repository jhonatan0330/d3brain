COMMENT ON TABLE usuario_usrp IS '2024-11-26';

ALTER TABLE propiedadvalordefinido_pvdp
	ADD COLUMN bpvd_privada boolean DEFAULT false NOT NULL;
	
update propiedadvalordefinido_pvdp set bpvd_privada = true
where cpvd_llave in ('PROP_219','PROP_182');