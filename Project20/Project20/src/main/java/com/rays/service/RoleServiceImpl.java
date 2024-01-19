package com.rays.service;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.rays.common.BaseServiceImpl;
import com.rays.dao.RoleDAOInt;
import com.rays.dto.RoleDTO;

@Service
@Transactional
public class RoleServiceImpl extends BaseServiceImpl<RoleDTO, RoleDAOInt> implements RoleServiceInt {

}
