package com.rays.common;

import java.util.List;

public interface BaseDAOInt<T extends BaseDTO> {

	public Long add(T dto);

	public T findByPk(Long pk);

	public T findByUniqueKey(String attribute, String value);

	public void update(T dto);

	public void delete(T dto);
	
	public List search(T dto, int pageNo, int pageSize);
	

}
