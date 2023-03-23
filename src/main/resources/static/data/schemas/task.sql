
CREATE SCHEMA task AUTHORIZATION postgres;

CREATE TABLE task.task_tsk (
	ctsk_llave varchar(32) NOT NULL,
	ctsk_user varchar(32) NOT NULL,
	ctsk_title varchar(200) NOT NULL,
	ctsk_notes varchar(4000) NULL,
	dtsk_completed timestamptz NULL,
	dtsk_duedate timestamptz NULL,
	ntsk_priority int4 NOT NULL,
	ntsk_order int4 NOT NULL,
	dtsk_createdat timestamptz NOT NULL,
	ctsk_createuser varchar(32) NOT NULL,
	dtsk_updatedat timestamptz NULL,
	ctsk_updateuser varchar(32) NULL,
	ctsk_state varchar(1) NOT NULL DEFAULT 'A'::character varying,
	CONSTRAINT PK_task_task_tsk PRIMARY KEY (ctsk_llave)
);
