COMMENT ON TABLE usuario_usrp IS '2023-12-20';

ALTER TABLE task.task_tsk RENAME COLUMN dtsk_createdat TO dtsk_creacionfecha;
ALTER TABLE task.task_tsk RENAME COLUMN dtsk_updatedat TO dtsk_modificacionfecha;
ALTER TABLE task.task_tsk RENAME COLUMN ctsk_createduser TO ctsk_creacionusuario;
ALTER TABLE task.task_tsk RENAME COLUMN ctsk_state TO ctsk_estado;

ALTER TABLE task.task_tsk ADD ctsk_creacionusuarionombre varchar(200);

ALTER TABLE task.task_tsk DROP COLUMN ctsk_updateduser;