COMMENT ON TABLE usuario_usrp IS '2019-07-02';

/*
CREATE OR REPLACE FUNCTION str_normalize(value text)
  RETURNS text AS
$BODY$
BEGIN
RETURN upper(translate(value, '���������������������������������ǻ?��[]`{},:;=&$#|!��<>', 'aaeeiioouuaeiouAAEEIIOOUUAEIOUnNcC '));
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;*/