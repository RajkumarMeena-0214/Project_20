package com.rays.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

public class BaseCtl<F extends BaseForm, T extends BaseDTO, S extends BaseServiceInt<T>> {

	@Autowired
	protected S baseService;

	@Value("${page.size}")
	private int pageSize = 0;

	public ORSResponse validate(BindingResult bindingResult) {
		ORSResponse res = new ORSResponse(true);
		if (bindingResult.hasErrors()) {
			res.setSuccess(false);
			Map<String, String> errors = new HashMap<String, String>();
			List<FieldError> list = bindingResult.getFieldErrors();
			list.forEach(e -> {
				errors.put(e.getField(), e.getDefaultMessage());
			});
			res.addInputError(errors);
		}
		return res;
	}

	@RequestMapping(value = "/search/{pageNo}", method = { RequestMethod.GET, RequestMethod.POST })
	public ORSResponse search(@RequestBody F form, @PathVariable int pageNo) {

		ORSResponse res = new ORSResponse(true);

		pageNo = (pageNo < 0) ? 0 : pageNo;

		T dto = (T) form.getDTO();

		res.addData(baseService.search(dto, pageNo, pageSize));
		res.addNextData(baseService.search(dto, pageNo + 1, pageSize));

		return res;
	}

	@PostMapping("/deleteMany/{ids}")
	public ORSResponse deleteMany(@PathVariable String[] ids, @RequestParam("pageNo") String pageNo,
			@RequestBody F form) {
		ORSResponse res = new ORSResponse(true);
		try {
			for (String id : ids) {
				T dto = baseService.delete(Long.parseLong(id));
			}
			T dto = (T) form.getDTO();

			res.addData(baseService.search(dto, Integer.parseInt(pageNo), pageSize));
			res.setSuccess(true);
			res.addMessage("Records Deleted Successfully");
		} catch (Exception e) {
			res.setSuccess(false);
			res.addMessage(e.getMessage());
		}
		return res;
	}

	@GetMapping("/get/{id}")
	public ORSResponse get(@PathVariable Long id, HttpServletRequest req) {
		ORSResponse res = new ORSResponse(true);
		T dto = baseService.findById(id);
		if (dto != null) {
			res.addResult("id", dto);
		} else {
			res.addData(dto);
		}
		return res;
	}

	@PostMapping("save")
	public ORSResponse save(@RequestBody @Valid F form, BindingResult bindingResult, HttpServletRequest req) {
		ORSResponse res = validate(bindingResult);
		if (!res.isSuccess()) {
			return res;
		}
		T dto = (T) form.getDTO();
		if (dto.getId() != null && dto.getId() > 0) {
			baseService.update(dto);
		} else {
			baseService.add(dto);
		}
		res.addData(dto.getId());
		return res;
	}

}
