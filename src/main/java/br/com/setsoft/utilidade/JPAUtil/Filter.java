package br.com.setsoft.utilidade.JPAUtil;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;

/**
 * JPA query filter.
 * 
 * @author Joel Marques 
 */
public class Filter {
	
	Map<String, Object> parameters = new HashMap<String, Object>();
	
	StringBuilder JPQL = new StringBuilder();
	
	static final String ALIAS = "x";
	
	boolean enableLike = true;

	private Filter() {}
	
	@SuppressWarnings("rawtypes")
	private Filter init(Class entityClass) {
	
		return this.addRestriction(this.initQuery(entityClass));
	}
	
	@SuppressWarnings("rawtypes")
	private String initQuery(Class entityClass) {
		
		return "SELECT " + Filter.ALIAS + " FROM " + entityClass.getSimpleName() + " " + Filter.ALIAS + " WHERE 1=1";
	}
	
	private Filter generateRestriction(Object entity, String embeddedPath) {
		
		List<Field> fields = ReflectionUtil.fields(entity.getClass());
		
		for (Field field : fields) {
			
			if (ReflectionUtil.isInvalid(field, entity)) {
				continue;
			}
			
			String fieldName = field.getName();
			
			Object fieldValue = ReflectionUtil.value(field, entity);
			
			
			if (field.isAnnotationPresent(Embedded.class) || field.isAnnotationPresent(EmbeddedId.class)) {
				
				this.generateRestriction(fieldValue, fieldName + ".");
				continue;
			}
			
			this.addRestriction(embeddedPath + fieldName, fieldValue, Operator.AND);			
		}
		
		return this;
	}
	
	/**
	 * Add a restriction to constrain the results to be retrieved.
	 * 
	 * @param fieldName
	 * @param fieldValue
	 * @param {@link Operator}
	 * @return {@link Filter}
	 */
	public Filter addRestriction(String fieldName, Object fieldValue, Operator operator) {
		
		
		if (this.isString(fieldValue) && this.isEnableLike()) {
			return this.addQueryString(fieldName, fieldValue, operator);
		}
		
		String parameterName = fieldName.replace(".", "");
		
		this.addRestriction(operator.getType() + " " + Filter.ALIAS + "." + fieldName + " " + "=:" +  parameterName);
		
		this.addParameter(parameterName, fieldValue);
		
		return this;
	}
	
	public Filter addRestriction(String restriction) {
		
		JPQL.append(" " + restriction);
		
		return this;
	}
	
	public Filter addParameter(String parameterName, Object parameterValue) {
		
		parameters.put(parameterName, parameterValue);
		
		return this;
	}
	
	public Filter addOrderBy(String fieldName) {
		
		return this.addRestriction("Order By " + Filter.ALIAS + "." + fieldName);
	}
	
	private Filter addQueryString(String fieldName, Object fieldValue, Operator operator) {
		
		String parameterName = fieldName.replace(".", "");
		
		this.addRestriction(operator.getType() + " " + "UPPER" + "(" + Filter.ALIAS + "." + fieldName + ")" + " " + "Like :" + parameterName);
		
		this.addParameter(parameterName, "%" + fieldValue.toString().trim().toUpperCase() + "%");
		
		return this;
	}
	
	private boolean isString(Object object) {
		
		return object != null && object instanceof String;
	}
	
	/**
	 * Disable the "like" operator for all string-valued properties.
	 */
	public Filter disableLike() {
		
		this.enableLike = false;
		
		return this;
	}
	
	public Map<String, Object> getParameters() {
		
		return parameters;
	}

	public StringBuilder getJPQL() {
		
		return JPQL;
	}
	
	public boolean isEnableLike() {
		
		return enableLike;
	}
	
	/**
	 * the query JPQL.
	 * @return a string representation of the query JPQL.
	 */
	@Override
	public String toString() {

		return this.getJPQL().toString();
	}
	
	/**
	 * Create a new instance, which includes all non-null properties
	 * by default
	 * 
	 * @param entity
	 * @return a new instance of {@link Filter}
	 * @author Joel Marques
	 */
	public static Filter create(Object entity) {
		
		if (entity == null) {
			throw new IllegalArgumentException("Entity null.");
		}
		
		return new Filter().init(entity.getClass()).generateRestriction(entity, "");
	}
	
	/**
	 * Create a new instance without restriction.
	 * 
	 * @param entityClass
	 * @return a new instance of {@link Filter}
	 * @author Joel Marques
	 */
	@SuppressWarnings("rawtypes")
	public static Filter create(Class entityClass) {
		
		if (entityClass == null) {
			throw new IllegalArgumentException("Entity null.");
		}
		
		return new Filter().init(entityClass);
	}
}