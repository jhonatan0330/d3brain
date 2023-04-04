COMMENT ON TABLE usuario_usrp IS '2023-04-04';


ALTER TABLE public.pedidoventadinero_pvdp ADD bpvd_controlarsaldo bool NOT NULL DEFAULT false;

ALTER TABLE public.z_pvd_pedidoventadinero ADD bpvd_controlarsaldo bool NOT NULL DEFAULT false;

update pedidoventadinero_pvdp set bpvd_controlarsaldo = true where mpvd_saldo != 0;

update z_pvd_pedidoventadinero set bpvd_controlarsaldo = true where mpvd_saldo != 0;