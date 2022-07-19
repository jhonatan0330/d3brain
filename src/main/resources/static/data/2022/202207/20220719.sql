
COMMENT ON TABLE usuario_usrp IS '2022-07-19';


--CAMPOS
update pedidoventacaracteristica_pvcp 
set cpvc_valortext = replace(cpvc_valortext, 'http://colombiansofture.com', 'https://golyat.cloud')
where cpvc_valortext like 'http://colombiansofture.com%';

update pedidoventacaracteristica_pvcp 
set cpvc_valortext = replace(cpvc_valortext, 'https://colombiansofture.com', 'https://golyat.cloud')
where cpvc_valortext like 'https://colombiansofture.com%';

--HISTORICOS
update z_pvc_pedidoventacaracteristica 
set cpvc_valortext = replace(cpvc_valortext, 'http://colombiansofture.com', 'https://golyat.cloud') 
where cpvc_valortext like 'http://colombiansofture.com%';

update z_pvc_pedidoventacaracteristica 
set cpvc_valortext = replace(cpvc_valortext, 'https://colombiansofture.com', 'https://golyat.cloud') 
where cpvc_valortext like 'https://colombiansofture.com%';

--ORGANIZACION
update organizacion_orgp 
set corg_imagen = replace(corg_imagen, 'http://colombiansofture.com', 'https://golyat.cloud') 
where corg_imagen like 'http://colombiansofture.com%';

update organizacion_orgp 
set corg_imagen = replace(corg_imagen, 'https://colombiansofture.com', 'https://golyat.cloud') 
where corg_imagen like 'https://colombiansofture.com%';

--USUARIO
update usuario_usrp 
set cusr_imagen = replace(cusr_imagen, 'http://colombiansofture.com', 'https://golyat.cloud') 
where cusr_imagen like 'http://colombiansofture.com%';

update usuario_usrp 
set cusr_imagen = replace(cusr_imagen, 'https://colombiansofture.com', 'https://golyat.cloud') 
where cusr_imagen like 'https://colombiansofture.com%';

-- DOCUMENTO PLANTILLA
update documentoplantilla_dplp 
set cdpl_imagen = replace(cdpl_imagen, 'http://colombiansofture.com', 'https://golyat.cloud')
where cdpl_imagen like 'http://colombiansofture.com%';

update documentoplantilla_dplp 
set cdpl_imagen = replace(cdpl_imagen, 'https://colombiansofture.com', 'https://golyat.cloud')
where cdpl_imagen like 'https://colombiansofture.com%';

--PROCESO
update proceso_prcp 
set cprc_imagen = replace(cprc_imagen, 'http://colombiansofture.com', 'https://golyat.cloud') 
where cprc_imagen like 'http://colombiansofture.com%';

update proceso_prcp 
set cprc_imagen = replace(cprc_imagen, 'http://colombiansofture.com', 'https://golyat.cloud') 
where cprc_imagen like 'http://colombiansofture.com%';

-- PRODUCTO
update producto_prop  
set cpro_imagen = replace(cpro_imagen, 'http://colombiansofture.com', 'https://golyat.cloud') 
where cpro_imagen like 'http://colombiansofture.com%';

update producto_prop  
set cpro_imagen = replace(cpro_imagen, 'https://colombiansofture.com', 'https://golyat.cloud') 
where cpro_imagen like 'https://colombiansofture.com%';

--CATEGORIA DE PRODUCTO
update categoriaproducto_cprp  
set ccpr_imagen = replace(ccpr_imagen, 'http://colombiansofture.com', 'https://golyat.cloud') 
where ccpr_imagen like 'http://colombiansofture.com%';

update categoriaproducto_cprp  
set ccpr_imagen = replace(ccpr_imagen, 'https://colombiansofture.com', 'https://golyat.cloud') 
where ccpr_imagen like 'https://colombiansofture.com%';
