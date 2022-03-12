
COMMENT ON TABLE usuario_usrp IS '2018-09-03';

ALTER TABLE documentoplantillarol_dprp
	ADD COLUMN bdpr_sinrangofechas boolean DEFAULT false NOT NULL;

update documentoplantillarol_dprp set bdpr_sinrangofechas = NOT bdpr_rangofiltro;

ALTER TABLE documentoplantillarol_dprp
	DROP COLUMN bdpr_rangofiltro;
