COMMENT ON TABLE usuario_usrp IS '2025-06-04';

ALTER TABLE account.registro_reg ADD creg_tipo varchar(1);

ALTER TABLE account.maparesultados_rmp DROP COLUMN IF EXISTS nrmp_promedio;

ALTER TABLE account.comprobante_cmp DROP COLUMN IF EXISTS dcmp_creacionfecha ;
ALTER TABLE account.comprobante_cmp DROP COLUMN IF EXISTS ccmp_creacionusuario;
ALTER TABLE account.comprobante_cmp DROP COLUMN IF EXISTS ccmp_creacionusuarionombre;
ALTER TABLE account.comprobante_cmp DROP COLUMN IF EXISTS dcmp_modificacionfecha;

ALTER TABLE account.tipocomprobante_tcm DROP COLUMN IF EXISTS dtcm_creacionfecha ;
ALTER TABLE account.tipocomprobante_tcm DROP COLUMN IF EXISTS ctcm_creacionusuario;
ALTER TABLE account.tipocomprobante_tcm DROP COLUMN IF EXISTS ctcm_creacionusuarionombre;
ALTER TABLE account.tipocomprobante_tcm DROP COLUMN IF EXISTS dtcm_modificacionfecha;

ALTER TABLE account.periodotiempo_ptm DROP COLUMN IF EXISTS dptm_creacionfecha ;
ALTER TABLE account.periodotiempo_ptm DROP COLUMN IF EXISTS cptm_creacionusuario;
ALTER TABLE account.periodotiempo_ptm DROP COLUMN IF EXISTS cptm_creacionusuarionombre;
ALTER TABLE account.periodotiempo_ptm DROP COLUMN IF EXISTS dptm_modificacionfecha;

ALTER TABLE task.task_tsk DROP COLUMN IF EXISTS ctsk_creacionusuario;
ALTER TABLE task.task_tsk DROP COLUMN IF EXISTS ctsk_creacionusuarionombre;
ALTER TABLE task.task_tsk DROP COLUMN IF EXISTS dtsk_modificacionfecha;

ALTER TABLE tarifario_trfp DROP COLUMN IF EXISTS dtrf_creacionfecha ;
ALTER TABLE tarifario_trfp DROP COLUMN IF EXISTS ctrf_creacionusuario;
ALTER TABLE tarifario_trfp DROP COLUMN IF EXISTS ctrf_creacionusuarionombre;
ALTER TABLE tarifario_trfp DROP COLUMN IF EXISTS dtrf_modificacionfecha;


ALTER TABLE account.pila_stk DROP COLUMN IF EXISTS cstk_creacionusuario;
ALTER TABLE account.pila_stk DROP COLUMN IF EXISTS cstk_creacionusuarionombre;
ALTER TABLE account.pila_stk DROP COLUMN IF EXISTS dstk_modificacionfecha;

ALTER TABLE account.maparesultados_rmp DROP COLUMN IF EXISTS drmp_creacionfecha ;
ALTER TABLE account.maparesultados_rmp DROP COLUMN IF EXISTS crmp_creacionusuario;
ALTER TABLE account.maparesultados_rmp DROP COLUMN IF EXISTS crmp_creacionusuarionombre;
ALTER TABLE account.maparesultados_rmp DROP COLUMN IF EXISTS drmp_modificacionfecha;

ALTER TABLE config.configtemplaterelation_ctr DROP COLUMN IF EXISTS dctr_creacionfecha ;
ALTER TABLE config.configtemplaterelation_ctr DROP COLUMN IF EXISTS cctr_creacionusuario;
ALTER TABLE config.configtemplaterelation_ctr DROP COLUMN IF EXISTS cctr_creacionusuarionombre;
ALTER TABLE config.configtemplaterelation_ctr DROP COLUMN IF EXISTS dctr_modificacionfecha;

ALTER TABLE account.catalogo_ctg DROP COLUMN IF EXISTS dctg_creacionfecha ;
ALTER TABLE account.catalogo_ctg DROP COLUMN IF EXISTS cctg_creacionusuario;
ALTER TABLE account.catalogo_ctg DROP COLUMN IF EXISTS cctg_creacionusuarionombre;
ALTER TABLE account.catalogo_ctg DROP COLUMN IF EXISTS dctg_modificacionfecha;

ALTER TABLE learning.article_art DROP COLUMN IF EXISTS dart_creacionfecha ;
ALTER TABLE learning.article_art DROP COLUMN IF EXISTS cart_creacionusuario;
ALTER TABLE learning.article_art DROP COLUMN IF EXISTS cart_creacionusuarionombre;
ALTER TABLE learning.article_art DROP COLUMN IF EXISTS dart_modificacionfecha;

ALTER TABLE account.registroauxiliar_rax DROP COLUMN IF EXISTS drax_creacionfecha ;
ALTER TABLE account.registroauxiliar_rax DROP COLUMN IF EXISTS crax_creacionusuario;
ALTER TABLE account.registroauxiliar_rax DROP COLUMN IF EXISTS crax_creacionusuarionombre;
ALTER TABLE account.registroauxiliar_rax DROP COLUMN IF EXISTS drax_modificacionfecha;

ALTER TABLE account.registro_reg DROP COLUMN IF EXISTS dreg_creacionfecha ;
ALTER TABLE account.registro_reg DROP COLUMN IF EXISTS creg_creacionusuario;
ALTER TABLE account.registro_reg DROP COLUMN IF EXISTS creg_creacionusuarionombre;
ALTER TABLE account.registro_reg DROP COLUMN IF EXISTS dreg_modificacionfecha;

ALTER TABLE account.cuenta_cue DROP COLUMN IF EXISTS dcue_creacionfecha ;
ALTER TABLE account.cuenta_cue DROP COLUMN IF EXISTS ccue_creacionusuario;
ALTER TABLE account.cuenta_cue DROP COLUMN IF EXISTS ccue_creacionusuarionombre;
ALTER TABLE account.cuenta_cue DROP COLUMN IF EXISTS dcue_modificacionfecha;

ALTER TABLE account.registro_reg DROP COLUMN IF EXISTS creg_tercero;
ALTER TABLE account.registro_reg DROP COLUMN IF EXISTS creg_terceroid;
ALTER TABLE account.registro_reg DROP COLUMN IF EXISTS creg_terceronombre;
ALTER TABLE account.registro_reg DROP COLUMN IF EXISTS creg_centrocosto;
ALTER TABLE account.registro_reg DROP COLUMN IF EXISTS creg_centrocostoid;
ALTER TABLE account.registro_reg DROP COLUMN IF EXISTS creg_centrocostonombre;
