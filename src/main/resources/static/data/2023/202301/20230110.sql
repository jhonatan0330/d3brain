COMMENT ON TABLE usuario_usrp IS '2023-01-10';

ALTER TABLE webservice_wbsp
	ADD COLUMN cwbs_url character varying(2000);

update webservice_wbsp set cwbs_url = (select cser_url from servidor_serp where cser_llave = cwbs_servidor);

ALTER TABLE webservice_wbsp
	DROP COLUMN cwbs_servidor,
	ALTER COLUMN cwbs_url SET NOT NULL;