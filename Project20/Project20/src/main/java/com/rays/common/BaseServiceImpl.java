package com.rays.common;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class BaseServiceImpl<T extends BaseDTO, D extends BaseDAOInt<T>> implements BaseServiceInt<T> {

	@Autowired
	protected D baseDao;

	@Override
	@Transactional(readOnly = false)
	public long add(T dto) {
		long pk = baseDao.add(dto);
		return pk;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void update(T dto) {
		baseDao.update(dto);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public long save(T dto) {
		Long id = dto.getId();
		if (id != null && id > 0) {
			update(dto);
		} else {
			id = add(dto);
		}
		return id;
	}

	@Override
	@Transactional(readOnly = true)
	public T findById(Long id) {
		T dto = baseDao.findByPk(id);
		return dto;
	}

	@Override
	public List search(T dto, int pageNo, int pageSize) {
		return baseDao.search(dto, pageNo, pageSize);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public T delete(long id) {
		T dto = findById(id);
		baseDao.delete(dto);
		return dto;
	}

}
