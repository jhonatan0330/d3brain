COMMENT ON TABLE usuario_usrp IS '2019-01-31';

CREATE OR REPLACE FUNCTION public.dcs_saldo_cero(character varying)
  RETURNS character varying  LANGUAGE plpgsql AS '
BEGIN

	IF EXISTS (SELECT mpvd_saldo FROM pedidoventadinero_pvdp WHERE cpvd_documento  = $1 AND cpvd_estado = ''A'' AND mpvd_saldo !=0) THEN
		RETURN ''N'';
	ELSE
		RETURN ''S'';
	END IF;

END;
';