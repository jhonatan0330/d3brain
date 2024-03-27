COMMENT ON TABLE usuario_usrp IS '2024-03-27';

--Corregir los usuario con documentos con cambio de estado forzado
update usuariorol_erlp
set cerl_estado = 'I', derl_fechafinal = now()
where cerl_llave in (
	select ue.cerl_llave  from usuariorol_erlp ue
		inner join pedidoventa_pdvp pp on (pp.cpdv_llave = cerl_documento and pp.cpdv_estado!='A' )
	where cerl_estado ='A'
);

update usuario_usrp
set cusr_estado = 'I'
where cusr_llave in (
	select uu.cusr_llave from usuario_usrp uu 
	where 0 = (select count(*) from usuariorol_erlp ue where ue.cerl_usuario =cusr_llave and cerl_estado = 'A')
	and cusr_estado = 'A'
);