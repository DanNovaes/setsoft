package br.com.setsoft.utilidade.JPAUtil;

import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * Dynamic query builder using JPA.
 * 
 * @author Joel Marques 
 */
public class DynamicQueryBuilder implements IQueryBuilder {

	final EntityManager entityManager;
	
	final Filter filter;

	public DynamicQueryBuilder(Filter filter, EntityManager entityManager) {
		super();
		
		if (filter == null) {
			throw new IllegalArgumentException("Entity null.");
		}
		
		if (entityManager == null) {
			throw new IllegalArgumentException("EntityManager null.");
		}
		
		this.filter = filter;
		this.entityManager = entityManager;
	}
	
	@Override
	public Query getQuery() {
		
		final Query query = this.entityManager.createQuery(this.filter.getJPQL().toString());

		for (final String parameter : this.filter.getParameters().keySet()) {
			query.setParameter(parameter, this.filter.getParameters().get(parameter));
		}

		return query;		
	}
	
}