COMMENT ON TABLE usuario_usrp IS '2023-02-28';

update propiedad_ppdp pp 
set cppd_valor = replace (cppd_valor, 'group by cdpl_nombre order by 2 desc]', 'group by cdpl_nombre order by 2 desc limit 25]')
where cppd_propiedadvalor  = 'PROP_138'
and cppd_estado = 'A' and cppd_valor like '%group by cdpl_nombre order by 2 desc]%'