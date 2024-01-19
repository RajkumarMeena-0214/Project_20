package com.rays.common;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public abstract class BaseDAOImpl<T extends BaseDTO> implements BaseDAOInt<T> {

	@PersistenceContext
	protected EntityManager entityManager;

	public abstract Class<T> getDTOClass();

	protected abstract List<Predicate> getWhereClause(T dto, CriteriaBuilder builder, Root<T> qRoot);

	public void populate(T dto) {
	}

	@Override
	public Long add(T dto) {
		populate(dto);
		entityManager.persist(dto);
		return dto.getId();
	}

	@Override
	public T findByPk(Long pk) {
		T dto = entityManager.find(getDTOClass(), pk);
		return dto;
	}

	public T findByUniqueKey(String attribute, String value) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<T> cq = builder.createQuery(getDTOClass());
		Root<T> qRoot = cq.from(getDTOClass());
		Predicate codition = builder.equal(qRoot.get(attribute), value);
		cq.where(codition);
		TypedQuery<T> tq = entityManager.createQuery(cq);
		List<T> list = tq.getResultList();
		T dto = null;
		if (list.size() > 0) {
			dto = list.get(0);
		}
		return dto;
	}

	@Override
	public void update(T dto) {
		populate(dto);
		entityManager.merge(dto);
	}

	public TypedQuery<T> createCriteria(T dto) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<T> cq = builder.createQuery(getDTOClass());
		Root<T> qRoot = cq.from(getDTOClass());
		List<Predicate> whereCondition = getWhereClause(dto, builder, qRoot);
		cq.where(whereCondition.toArray(new Predicate[whereCondition.size()]));
		TypedQuery<T> tq = entityManager.createQuery(cq);
		System.out.println("createCriteria==================BaseDAOImpl"+tq);
		return tq;
	}

	@Override
	public List search(T dto, int pageNo, int pageSize) {

		TypedQuery<T> tq = createCriteria(dto);
		if (pageSize > 0) {
			tq.setFirstResult(pageNo * pageSize);
			tq.setMaxResults(pageSize);
		}
		List<T> list = tq.getResultList();
		list.forEach(e -> {
			System.out.println("*****" + e);
		});
		return list;
	}

	protected boolean isEmptyString(String val) {
		return val == null || val.trim().length() == 0;
	}

	protected boolean isZeroNumber(Long val) {
		return val == null || val == 0;
	}
	
	@Override
	public void delete(T dto) {
		entityManager.remove(dto);
	}

}
