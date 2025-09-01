COMMENT ON TABLE usuario_usrp IS '2025-08-31';

ALTER TABLE public.detallecaracteristicaproducto_dcpp_old DROP CONSTRAINT IF EXISTS fk_detallecaracteristicaproductoentidad;

DROP TABLE IF EXISTS public.postcalificacion_pclp;

DROP TABLE IF EXISTS public.postrespuesta_prsp;

DROP TABLE IF EXISTS public.postpregunta_pprp;

DROP TABLE IF EXISTS public.encuestarespuesta_ersp;

DROP TABLE IF EXISTS public.encuestaopcionrespuesta_eorp;

DROP TABLE IF EXISTS public.encuestapregunta_eprp;

DROP TABLE IF EXISTS public.encuestagrupo_egrp;

DROP TABLE IF EXISTS public.encuesta_encp;
