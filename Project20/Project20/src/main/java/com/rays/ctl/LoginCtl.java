package com.rays.ctl;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rays.common.BaseCtl;
import com.rays.common.ORSResponse;
import com.rays.dto.UserDTO;
import com.rays.form.LoginForm;
import com.rays.form.UserForm;
import com.rays.form.UserRegistrationForm;
import com.rays.service.UserServiceInt;

@RestController
@RequestMapping(value = "Auth")
public class LoginCtl extends BaseCtl<UserForm, UserDTO, UserServiceInt> {

	@PostMapping("signUp")
	public ORSResponse signUp(@RequestBody @Valid UserRegistrationForm form, BindingResult bindingResult,
			HttpServletRequest req) {
		ORSResponse res = validate(bindingResult);
		if (!res.isSuccess()) {
			return res;
		}
		UserDTO dto = new UserDTO();
		dto.setFirstName(form.getFirstName());
		dto.setLastName(form.getLastName());
		dto.setLoginId(form.getLoginId());
		dto.setPassword(form.getPassword());
		dto.setDob(form.getDob());
		dto.setRoleId(2L);

		baseService.register(dto);
		return res;
	}

	@PostMapping("login")
	public ORSResponse login(@RequestBody @Valid LoginForm form, BindingResult bindingResult, HttpServletRequest req) {
		ORSResponse res = validate(bindingResult);
		if (!res.isSuccess()) {
			return res;
		}
		UserDTO dto = baseService.authenticate(form.getLoginId(), form.getPassword());
		if (dto != null) {
			res.addData(dto);
		} else {
			res.addMessage("Invalid Login-Id or Password");
		}
		return res;
	}

}
