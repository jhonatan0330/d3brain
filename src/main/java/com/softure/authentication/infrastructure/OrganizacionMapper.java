package com.softure.authentication.infrastructure;

import java.util.List;

import com.softure.SoftureSqlConnMapper;
import com.softure.authentication.domain.OrganizacionDTO;
import com.softure.authentication.domain.OrganizacionFilterDTO;
import com.softure.java.domain.IBasicMapper;

@SoftureSqlConnMapper(value = "OrganizacionMapper")
public interface OrganizacionMapper extends IBasicMapper<OrganizacionDTO, OrganizacionFilterDTO> {

	List<OrganizacionDTO> obtenerUsuario(String usuario);

	OrganizacionDTO obtenerPrincipal();
}