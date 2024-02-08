COMMENT ON TABLE usuario_usrp IS '2024-02-06';

ALTER TABLE tarifario_trfp ADD dtrf_creacionfecha timestamptz NOT NULL DEFAULT NOW();
ALTER TABLE tarifario_trfp ADD ctrf_creacionusuario varchar(32);
ALTER TABLE tarifario_trfp ADD ctrf_creacionusuarionombre varchar(200);
ALTER TABLE tarifario_trfp ADD dtrf_modificacionfecha timestamptz;

ALTER TABLE tarifario_trfp ADD ctrf_tiporecursonombre varchar(100);
ALTER TABLE tarifario_trfp ADD ctrf_tipodimension2nombre varchar(100);
ALTER TABLE tarifario_trfp ADD ctrf_tipodimension3nombre varchar(100);
ALTER TABLE tarifario_trfp ADD ctrf_tipodimension4nombre varchar(100);

update tarifario_trfp set ctrf_tiporecursonombre = (select cdpl_nombre from documentoplantilla_dplp where cdpl_llave = ctrf_tiporecurso) where ctrf_tiporecurso is not null; 
update tarifario_trfp set ctrf_tipodimension2nombre = (select cdpl_nombre from documentoplantilla_dplp where cdpl_llave = ctrf_tipodimension2) where ctrf_tipodimension2 is not null; 
update tarifario_trfp set ctrf_tipodimension3nombre = (select cdpl_nombre from documentoplantilla_dplp where cdpl_llave = ctrf_tipodimension3) where ctrf_tipodimension3 is not null; 
update tarifario_trfp set ctrf_tipodimension4nombre = (select cdpl_nombre from documentoplantilla_dplp where cdpl_llave = ctrf_tipodimension4) where ctrf_tipodimension4 is not null; 