package com.rays.common;

public class BaseForm {

	private Long id = null;

	private int pageNo = 0;

	private int pageSize = 5;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BaseDTO getDTO() {
		return null;
	}

	public <T extends BaseDTO> T initDTO(T dto) {
		if (id != null && id > 0) {
			dto.setId(id);
		} else {
			dto.setId(null);
		}
		return dto;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

}
