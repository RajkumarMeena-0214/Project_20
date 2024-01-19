package com.rays.service;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.rays.common.BaseServiceImpl;
import com.rays.dao.UserDAOInt;
import com.rays.dto.UserDTO;

@Service
@Transactional
public class UserServiceImpl extends BaseServiceImpl<UserDTO, UserDAOInt> implements UserServiceInt {

	@Override
	public UserDTO register(UserDTO dto) {
		baseDao.add(dto);
		return dto;
	}

	public UserDTO findByLoginId(String attribute, String value) {
		UserDTO dto = baseDao.findByUniqueKey(attribute, value);
		return dto;
	}

	@Override
	public UserDTO authenticate(String loginId, String password) {
		UserDTO dto = findByLoginId("loginId", loginId);
		if (dto != null) {
			if (password.equals(dto.getPassword())) {
				return dto;
			}
		}
		return null;
	}

}
