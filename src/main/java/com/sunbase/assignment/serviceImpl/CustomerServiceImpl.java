package com.sunbase.assignment.serviceImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.google.common.base.Strings;
import com.sunbase.assignment.dto.CustomerFilterDto;
import com.sunbase.assignment.entity.Customer;
import com.sunbase.assignment.entity.Customer_;
import com.sunbase.assignment.model.CustomerModel;
import com.sunbase.assignment.repository.CustomerRepo;
import com.sunbase.assignment.service.CustomerService;
import com.sunbase.assignment.util.CustomerUtil;

@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	CustomerRepo repo;

	@Autowired
	ModelMapper mapper;

	@Autowired
	CustomerUtil util;

	@Autowired
	EntityManager entityManager;

	@Override
	public Customer saveOrUpdateCustomer(CustomerModel requestModel) {
		Customer savedObject = null;
		Customer customer = mapper.map(requestModel, Customer.class);

		if (requestModel.getId() != null) {
			Optional<Customer> customerById = repo.findById(requestModel.getId());
			if (customerById.isPresent()) {
				util.updateCustomerDetails(customerById.get(), customer);
				savedObject = repo.save(customerById.get());
			}
		} else
			savedObject = repo.save(customer);
		return savedObject;
	}

	@Override
	public List<Customer> getCustomer(@Valid CustomerFilterDto filterDto) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Customer> cq = cb.createQuery(Customer.class);
		Root<Customer> root = cq.from(Customer.class);
		
		List<Predicate> predicateList = new ArrayList<>();
		predicateList.addAll(Arrays
				.asList(filterDto.getId() != null ? cb.equal(root.get(Customer_.ID), filterDto.getId()) : null,
						!Strings.isNullOrEmpty(filterDto.getAddress())
								? cb.equal(root.get(Customer_.ADDRESS), filterDto.getAddress())
								: null,
						!Strings.isNullOrEmpty(filterDto.getCity())
								? cb.equal(root.get(Customer_.CITY), filterDto.getCity())
								: null,
						!Strings.isNullOrEmpty(filterDto.getEmail())
								? cb.equal(root.get(Customer_.EMAIL), filterDto.getEmail())
								: null,
						!Strings.isNullOrEmpty(filterDto.getFirstName())
								? cb.equal(root.get(Customer_.FIRST_NAME), filterDto.getFirstName())
								: null,
						!Strings.isNullOrEmpty(filterDto.getLastName())
								? cb.equal(root.get(Customer_.LAST_NAME), filterDto.getLastName())
								: null,
						!Strings.isNullOrEmpty(filterDto.getPhone())
								? cb.equal(root.get(Customer_.PHONE), filterDto.getPhone())
								: null,
						!Strings.isNullOrEmpty(filterDto.getState())
								? cb.equal(root.get(Customer_.STATE), filterDto.getState())
								: null,
						!Strings.isNullOrEmpty(filterDto.getStreet())
								? cb.equal(root.get(Customer_.STREET), filterDto.getStreet())
								: null,
						!Strings.isNullOrEmpty(filterDto.getCommonSearch()) ? cb.or(
								cb.like(root.get(Customer_.ID).as(String.class),
										"%" + filterDto.getCommonSearch() + "%"),
								cb.like(cb.lower(root.get(Customer_.ADDRESS)),
										"%" + filterDto.getCommonSearch().toLowerCase() + "%"),
								cb.like(cb.lower(root.get(Customer_.CITY)),
										"%" + filterDto.getCommonSearch().toLowerCase() + "%"),
								cb.like(cb.lower(root.get(Customer_.EMAIL)), "%" + filterDto.getCommonSearch() + "%"),
								cb.like(cb.lower(root.get(Customer_.FIRST_NAME)),
										"%" + filterDto.getCommonSearch().toLowerCase() + "%"),
								cb.like(cb.lower(root.get(Customer_.LAST_NAME)),
										"%" + filterDto.getCommonSearch().toLowerCase() + "%"),
								cb.like(cb.lower(root.get(Customer_.PHONE)),
										"%" + filterDto.getCommonSearch().toLowerCase() + "%"),
								cb.like(cb.lower(root.get(Customer_.STATE)),
										"%" + filterDto.getCommonSearch().toLowerCase() + "%"),
								cb.like(cb.lower(root.get(Customer_.STREET)),
										"%" + filterDto.getCommonSearch().toLowerCase() + "%"),
								cb.like(cb.concat(cb.lower(root.get(Customer_.FIRST_NAME)),
										cb.lower(root.get(Customer_.LAST_NAME))),
										"%" + filterDto.getCommonSearch() + "%"))
								: null)
				.stream().filter(Objects::nonNull).toList());
		// Sorting if sort and sortOrder are provided
		if (!Strings.isNullOrEmpty(filterDto.getSort()) && !Strings.isNullOrEmpty(filterDto.getSortOrder())) {
			sortResultList(cb, root, cq, filterDto.getSort(), filterDto.getSortOrder());
		}
		cq.select(root).where(cb.and(predicateList.toArray(new Predicate[predicateList.size()])));
		TypedQuery<Customer> createQuery = entityManager.createQuery(cq);

		// Pagination
		if (filterDto.getPage() != null && filterDto.getSize() != null) {
			int firstIndex = (filterDto.getPage() - 1) * filterDto.getSize();
			createQuery.setFirstResult(firstIndex).setMaxResults(filterDto.getSize()); //0-4
		}
		List<Customer> resultList = createQuery.getResultList();
		return resultList;
	}

	/**
	 * Helper method to sort the result list based on a field and sort order.
	 *
	 * @param cb        CriteriaBuilder instance.
	 * @param root      Root entity (Customer).
	 * @param cq        CriteriaQuery instance.
	 * @param sort      Sort field.
	 * @param sortOrder Sort order ("ASC" or "DESC").
	 */
	private void sortResultList(CriteriaBuilder cb, Root<Customer> root, CriteriaQuery<Customer> cq, String sort,
			String sortOrder) {
		Expression<?> orderByExpression = null;
		switch (sort) {
		case Customer_.ID:
			orderByExpression = root.get(Customer_.ID);
			break;
		case Customer_.FIRST_NAME:
			orderByExpression = root.get(Customer_.FIRST_NAME);
			break;
		case Customer_.LAST_NAME:
			orderByExpression = root.get(Customer_.LAST_NAME);
			break;
		case Customer_.ADDRESS:
			orderByExpression = root.get(Customer_.ADDRESS);
			break;
		case Customer_.CITY:
			orderByExpression = root.get(Customer_.CITY);
			break;
		case Customer_.EMAIL:
			orderByExpression = root.get(Customer_.EMAIL);
			break;
		case Customer_.PHONE:
			orderByExpression = root.get(Customer_.PHONE);
			break;
		case Customer_.STATE:
			orderByExpression = root.get(Customer_.STATE);
			break;
		case Customer_.STREET:
			orderByExpression = root.get(Customer_.STREET);
			break;
		default:
			break;
		}
		if (orderByExpression != null) {
			cq.orderBy(sortOrder.equalsIgnoreCase("DESC") ? cb.desc(orderByExpression) : cb.asc(orderByExpression));
		}
	}

	@Override
	public String deleteCustomer(Long id) {
		Optional<Customer> customerById = repo.findById(id);
		if (!customerById.isPresent())
			return "Customer details not found.";
		repo.deleteById(id);
		return "Deleted successfully";
	}
}
