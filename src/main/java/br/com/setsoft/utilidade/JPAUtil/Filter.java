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
	
	Object entity;
	
	Map<String, Object> parameters = new HashMap<String, Object>();
	
	StringBuilder JPQL = new StringBuilder();
	
	boolean enableLike = true;

	private Filter() {}
	
	private Filter(Object entity) {
		
		super();
		this.entity = entity;		
		this.init().generateRestriction(this.entity, "");
	}
	
	private Filter init() {
		
		this.JPQL.append("SELECT x FROM " + this.getClassName() + " x WHERE 1=1");
	
		return this;
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
		
		String fieldNameParam = fieldName.replace(".", "");
		
		if (this.isString(fieldValue) && this.isEnableLike()) {
			this.addQueryString(fieldName, fieldValue, operator);
			return this;
		}
		
		this.JPQL.append(" " + operator.getType() + " x." + fieldName + " =:" + fieldNameParam);
		this.parameters.put(fieldNameParam, fieldValue);
		
		return this;
	}
	
	public Filter addOrderBy(String fieldName) {
		
		this.JPQL.append(" Order By x." + fieldName);
		
		return this;
	}
	
	private void addQueryString(String fieldName, Object fieldValue, Operator operator) {
		
		String fieldNameParam = fieldName.replace(".", "");
		
		this.JPQL.append(" " + operator.getType() + " UPPER(x." + fieldName +")" + " Like :" + fieldNameParam);
		this.parameters.put(fieldNameParam, "%" + fieldValue.toString().trim().toUpperCase() + "%");
	}
	
	private boolean isString(Object object) {
		
		return object != null && object instanceof String;
	}
	
	private String getClassName() {
		
		return this.entity.getClass().getSimpleName();
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
		
		return new Filter(entity);
	}	
}