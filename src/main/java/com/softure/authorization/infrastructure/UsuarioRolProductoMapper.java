package com.softure.authorization.infrastructure;

import com.softure.SoftureSqlConnMapper;
import com.softure.authorization.domain.UsuarioRolProductoDTO;
import com.softure.authorization.domain.UsuarioRolProductoFilterDTO;
import com.softure.java.domain.IBasicMapper;

@SoftureSqlConnMapper(value = "UsuarioRolProductoMapper")
public interface UsuarioRolProductoMapper extends IBasicMapper<UsuarioRolProductoDTO, UsuarioRolProductoFilterDTO> {

}