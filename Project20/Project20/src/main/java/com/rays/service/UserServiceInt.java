package com.rays.service;

import com.rays.common.BaseServiceInt;
import com.rays.dto.UserDTO;

public interface UserServiceInt extends BaseServiceInt<UserDTO> {

	public UserDTO register(UserDTO dto);
	
	public UserDTO authenticate(String loginId, String password);

}
