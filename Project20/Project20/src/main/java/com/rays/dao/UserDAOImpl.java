package com.rays.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.rays.common.BaseDAOImpl;
import com.rays.dto.RoleDTO;
import com.rays.dto.UserDTO;

@Repository
public class UserDAOImpl extends BaseDAOImpl<UserDTO> implements UserDAOInt {

	@Autowired
	RoleDAOInt roleDao;

	@Override
	public Class<UserDTO> getDTOClass() {
		return UserDTO.class;
	}

	@Override
	public void populate(UserDTO dto) {
		if (dto.getRoleId() != null && dto.getRoleId() > 0) {
			RoleDTO roleDto = roleDao.findByPk(dto.getRoleId());
			dto.setRoleName(roleDto.getName());
		}
		if (dto.getId() != null && dto.getId() > 0) {
			UserDTO userData = findByPk(dto.getId());
			dto.setImageId(userData.getImageId());
		}
	}

	@Override
	protected List<Predicate> getWhereClause(UserDTO dto, CriteriaBuilder builder, Root<UserDTO> qRoot) {
		List<Predicate> where = new ArrayList<Predicate>();
		if (!isZeroNumber(dto.getRoleId())) {
			where.add(builder.equal(qRoot.get("roleId"), dto.getRoleId()));
		}
		if (!isEmptyString(dto.getFirstName())) {
			where.add(builder.like(qRoot.get("firstName"), dto.getFirstName() + "%"));
		}
		return where;
	}

}
