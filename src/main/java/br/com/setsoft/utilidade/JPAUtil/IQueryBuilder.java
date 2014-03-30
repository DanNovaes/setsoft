package br.com.setsoft.utilidade.JPAUtil;

import javax.persistence.Query;

/**
 * Dynamic query builder using JPA.
 * 
 * @author Joel Marques 
 */
public interface IQueryBuilder {
	
	Query getQuery();
}