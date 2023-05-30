
COMMENT ON TABLE usuario_usrp IS '2023-05-30';


--CAMPOS
update pedidoventacaracteristica_pvcp 
set cpvc_valortext = replace(cpvc_valortext, 'http://golyat.cloud', 'https://fs.softwareparati.com')
where cpvc_valortext like 'http://golyat.cloud%';

update pedidoventacaracteristica_pvcp 
set cpvc_valortext = replace(cpvc_valortext, 'https://golyat.cloud', 'https://fs.softwareparati.com')
where cpvc_valortext like 'https://golyat.cloud%';

--HISTORICOS
update z_pvc_pedidoventacaracteristica 
set cpvc_valortext = replace(cpvc_valortext, 'http://golyat.cloud', 'https://fs.softwareparati.com') 
where cpvc_valortext like 'http://golyat.cloud%';

update z_pvc_pedidoventacaracteristica 
set cpvc_valortext = replace(cpvc_valortext, 'https://golyat.cloud', 'https://fs.softwareparati.com') 
where cpvc_valortext like 'https://golyat.cloud%';

--ORGANIZACION
update organizacion_orgp 
set corg_imagen = replace(corg_imagen, 'http://golyat.cloud', 'https://fs.softwareparati.com') 
where corg_imagen like 'http://golyat.cloud%';

update organizacion_orgp 
set corg_imagen = replace(corg_imagen, 'https://golyat.cloud', 'https://fs.softwareparati.com') 
where corg_imagen like 'https://golyat.cloud%';

--USUARIO
update usuario_usrp 
set cusr_imagen = replace(cusr_imagen, 'http://golyat.cloud', 'https://fs.softwareparati.com') 
where cusr_imagen like 'http://golyat.cloud%';

update usuario_usrp 
set cusr_imagen = replace(cusr_imagen, 'https://golyat.cloud', 'https://fs.softwareparati.com') 
where cusr_imagen like 'https://golyat.cloud%';

-- DOCUMENTO PLANTILLA
update documentoplantilla_dplp 
set cdpl_imagen = replace(cdpl_imagen, 'http://golyat.cloud', 'https://fs.softwareparati.com')
where cdpl_imagen like 'http://golyat.cloud%';

update documentoplantilla_dplp 
set cdpl_imagen = replace(cdpl_imagen, 'https://golyat.cloud', 'https://fs.softwareparati.com')
where cdpl_imagen like 'https://golyat.cloud%';

--PROCESO
update proceso_prcp 
set cprc_imagen = replace(cprc_imagen, 'http://golyat.cloud', 'https://fs.softwareparati.com') 
where cprc_imagen like 'http://golyat.cloud%';

update proceso_prcp 
set cprc_imagen = replace(cprc_imagen, 'http://golyat.cloud', 'https://fs.softwareparati.com') 
where cprc_imagen like 'http://golyat.cloud%';

-- PRODUCTO
update producto_prop  
set cpro_imagen = replace(cpro_imagen, 'http://golyat.cloud', 'https://fs.softwareparati.com') 
where cpro_imagen like 'http://golyat.cloud%';

update producto_prop  
set cpro_imagen = replace(cpro_imagen, 'https://golyat.cloud', 'https://fs.softwareparati.com') 
where cpro_imagen like 'https://golyat.cloud%';

--CATEGORIA DE PRODUCTO
update categoriaproducto_cprp  
set ccpr_imagen = replace(ccpr_imagen, 'http://golyat.cloud', 'https://fs.softwareparati.com') 
where ccpr_imagen like 'http://golyat.cloud%';

update categoriaproducto_cprp  
set ccpr_imagen = replace(ccpr_imagen, 'https://golyat.cloud', 'https://fs.softwareparati.com') 
where ccpr_imagen like 'https://golyat.cloud%';
