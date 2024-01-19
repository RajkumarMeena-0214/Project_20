package com.rays.common;

import java.util.List;

public interface BaseServiceInt<T extends BaseDTO> {

	public long add(T dto);

	public void update(T dto);

	public long save(T dto);

	public T findById(Long id);
	
	public T delete(long id);

	public List search(T dto, int pageNo, int pageSize);
	

}
