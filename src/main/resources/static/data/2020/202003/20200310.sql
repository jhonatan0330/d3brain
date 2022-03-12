COMMENT ON TABLE usuario_usrp IS '2020-03-10';

UPDATE propiedadvalordefinido_pvdp SET bpvd_piderol = true where cpvd_llave in ('PROP_85','PROP_82',
'PROP_84','PROP_77','PROP_79','PROP_83','PROP_80','PROP_107','PROP_78','PROP_81','PROP_114','PROP_108');

UPDATE propiedadvalordefinido_pvdp SET bpvd_propiedadboolean = true where cpvd_llave = 'PROP_32';

--select * from propiedadvalordefinido_pvdp where cpvd_nombre like '%MULTIP%'