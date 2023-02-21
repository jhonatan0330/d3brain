COMMENT ON TABLE usuario_usrp IS '2023-02-21';

CREATE TABLE task_task_tsk (
        ctsk_llave character varying(32) NOT NULL,
        ctsk_user character varying(32)  NOT NULL,
        ctsk_title character varying(200) NOT NULL,
        ctsk_notes character varying(4000),
        dtsk_completed timestamp with time zone,
        dtsk_duedate timestamp with time zone,
        ntsk_priority int4 NOT NULL DEFAULT 0,
        ntsk_order int4 NOT NULL DEFAULT 0,
        dtsk_createdAt timestamp with time zone  NOT NULL,
        dtsk_updatedAt timestamp with time zone,
        ctsk_state character varying(1) NOT NULL DEFAULT 'A',
        CONSTRAINT PK_task_task_tsk PRIMARY KEY (ctsk_llave)
    );
