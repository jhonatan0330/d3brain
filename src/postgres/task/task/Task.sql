
CREATE TABLE task.task_tsk (
	ctsk_llave varchar(32) NOT NULL,
	ctsk_estado varchar(1) NOT NULL DEFAULT 'A'::character varying,
	CONSTRAINT pk_task_tsk PRIMARY KEY (ctsk_llave)
);

ALTER TABLE task.task_tsk ADD ctsk_user varchar(32) NOT NULL ;
ALTER TABLE task.task_tsk ADD ctsk_title varchar(100) NOT NULL ;
ALTER TABLE task.task_tsk ADD ctsk_notes varchar(4.000);
ALTER TABLE task.task_tsk ADD dtsk_completed timestamp with time zone;
ALTER TABLE task.task_tsk ADD dtsk_dueDate timestamp with time zone;
ALTER TABLE task.task_tsk ADD ntsk_priority int NOT NULL DEFAULT 0;
ALTER TABLE task.task_tsk ADD ntsk_order int NOT NULL DEFAULT 0;
