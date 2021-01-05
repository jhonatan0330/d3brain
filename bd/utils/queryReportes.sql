select  
	regexp_replace(replace(replace(replace(replace(replace(
		substring(lower(cppd_valor) from '(select\s[A-Za-z0-9\s_,\(\)\+\=\{\-\}\$''\/\!\.\|\*\<\>\:]*)')
	,'$p{p_fecha_inicio}', 'now()')
	,'$p{p_fecha_fin}', 'now()')
	,'$p{p_key} as int', '''0'' as int')
	,'$p{p_ano}', '''2019''')
	,'$p{p_numero}  as int', '''0'' as int')
	,'\$p\{[a-z0-9\_]*\}', '''''', 'g')
	|| ';--' || crpb_llave	
from reportebase_rpbp, propiedad_ppdp 
where crpb_llave  = cppd_campo and cppd_propiedadvalor = 'PROP_138' and cppd_estado = 'A' and crpb_estado = 'A'
order by crpb_llave