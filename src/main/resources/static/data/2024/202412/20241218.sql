COMMENT ON TABLE usuario_usrp IS '2024-12-18';

update usuariosesion_ussp set cuss_estado = 'I', duss_fechacierre = now() 
where cuss_estado  = 'A';