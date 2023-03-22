COMMENT ON TABLE usuario_usrp IS '2023-03-20';

CREATE SCHEMA task AUTHORIZATION postgres;

CREATE TABLE task.task_tsk (
        ctsk_llave character varying(32) NOT NULL,
        ctsk_user character varying(32)  NOT NULL,
        ctsk_title character varying(200) NOT NULL,
        ctsk_notes character varying(4000),
        dtsk_completed timestamp with time zone,
        dtsk_duedate timestamp with time zone,
        ntsk_priority int4 NOT NULL DEFAULT 0,
        ntsk_order int4 NOT NULL DEFAULT 0,
        dtsk_createdat timestamp with time zone  NOT NULL,
        ctsk_createduser character varying(32)  NOT NULL,
        dtsk_updatedat timestamp with time zone,
        ctsk_updateuser character varying(32) NULL,
        ctsk_state character varying(1) NOT NULL DEFAULT 'A',
        CONSTRAINT PK_task_task_tsk PRIMARY KEY (ctsk_llave)
    );

INSERT INTO task.task_tsk
(ctsk_llave, ctsk_user, ctsk_title, ctsk_notes, dtsk_completed, dtsk_duedate, ntsk_priority, ntsk_order, dtsk_createdat, ctsk_createduser, dtsk_updatedat, ctsk_state)
SELECT ctsk_llave, ctsk_user, ctsk_title, ctsk_notes, dtsk_completed, dtsk_duedate, ntsk_priority, ntsk_order, dtsk_createdat, ctsk_user, dtsk_updatedat, ctsk_state
FROM public.task_task_tsk;

DROP TABLE public.task_task_tsk;