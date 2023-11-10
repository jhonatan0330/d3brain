COMMENT ON TABLE usuario_usrp IS '2023-11-10';

select * from organizar_ultima_gestion();

update propiedadvalordefinido_pvdp set bpvd_multiple = true  where cpvd_llave  = 'PROP_82';