COMMENT ON TABLE usuario_usrp IS '2026-09-07';

      	update propiedadvalordefinido_pvdp
        set cpvd_estado = 'I'
        where cpvd_llave in ('PROP_52','PROP_51');
        
        update propiedad_ppdp
        set cppd_estado = 'I', dppd_fechaeliminacion = now()
        where cppd_propiedadvalor in ('PROP_52','PROP_51');
        
        
         update propiedadvalordefinido_pvdp pp 
        set bpvd_propiedadboolean = true
        where pp.cpvd_llave in ('PROP_183','PROP_107');

		update propiedad_ppdp
        set cppd_texto = null, cppd_valor = '1'
        where cppd_propiedadvalor in ('PROP_183','PROP_107');

        DROP table if exists public.detallecaracteristicaproducto_dcpp_old;