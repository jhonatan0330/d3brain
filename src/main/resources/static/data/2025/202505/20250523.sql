COMMENT ON TABLE usuario_usrp IS '2025-05-23';

ALTER TABLE account.registroauxiliar_rax DROP COLUMN crax_auxiliarcode;

ALTER TABLE account.registroauxiliar_rax DROP COLUMN crax_auxiliarnombre;

ALTER TABLE account.catalogo_ctg ALTER COLUMN dctg_fechafinal DROP NOT NULL;

delete from account.maparesultados_rmp where nrmp_cantidad = 0;

update account.maparesultados_rmp set mrmp_valor = abs(mrmp_positivo - mrmp_negativo);

update account.maparesultados_rmp set mrmp_saldosiguiente = mrmp_saldoanterior +(mrmp_positivo - mrmp_negativo) ;

ALTER TABLE account.cuenta_cue DROP COLUMN dcue_fechainicial;

ALTER TABLE account.cuenta_cue DROP COLUMN dcue_fechafinal;
