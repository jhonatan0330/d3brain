COMMENT ON TABLE usuario_usrp IS '2019-01-15';

update producto_prop set cpro_codigo = '2019-01-15' where cpro_llave = 'a03e251ff52d4f10bb955f19ba84a693';

update producto_prop set cpro_codigo = substring(cpro_codigo || '-' || cpro_llave, 0, 32)
	where cpro_estado = 'I' and cpro_codigo in (
	select cpro_codigo from producto_prop 
	group by cpro_codigo
	having count(*)>1);

update producto_prop set cpro_codigo = substring(cpro_codigo || '-' || cpro_llave, 0, 32)
	where cpro_codigo in (
	select cpro_codigo from producto_prop 
	where cpro_estado = 'A'
	group by cpro_codigo
	having count(*)>1);


	
ALTER TABLE producto_prop DROP CONSTRAINT IF EXISTS producto_prop_cpro_codigo_key;
ALTER TABLE producto_prop ADD CONSTRAINT producto_prop_cpro_codigo_key UNIQUE (cpro_codigo);
