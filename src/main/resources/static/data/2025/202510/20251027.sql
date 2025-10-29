COMMENT ON TABLE usuario_usrp IS '2025-10-27';

update propiedadvalordefinido_pvdp
	set bpvd_piderol = true, bpvd_pideusuario = true
where cpvd_llave = 'PROP_82';
