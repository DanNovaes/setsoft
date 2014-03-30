package br.com.setsoft.utilidade.JPAUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.persistence.Id;
import javax.persistence.JoinColumn;

public final class ReflectionUtil {
	
	@SuppressWarnings("rawtypes")
	public static List<Field> fields(Class classe) {
		
		List<Field> fields = new ArrayList<Field>();
		
		Class classePai = classe.getSuperclass();
		
		if (classePai != Object.class) {
			
			fields.addAll(ReflectionUtil.fields(classePai));
		}
		
		fields.addAll(Arrays.asList(classe.getDeclaredFields()));
		
		return fields;
	}
	
	public static Object value(Field field, Object entity) {
		
		field.setAccessible(true);
		
		try {
			return field.get(entity);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Object getPrimaryKey(Object entity) {
		
		List<Field> fields = ReflectionUtil.fields(entity.getClass());
		
		for (Field field : fields) {
			
			if (field.isAnnotationPresent(Id.class)) {
				
				Object fieldValue = ReflectionUtil.value(field, entity);
				
				return fieldValue;
			}
		}
		
		return null;		
	}
	
	public static boolean isPrimaryKey(Object entity) {
		
		Object primaryKey = ReflectionUtil.getPrimaryKey(entity);
		
		if (primaryKey == null) {
			return false;
		}
		
		if (primaryKey instanceof String && primaryKey.toString().trim().isEmpty()) {
			return false;
		}
		
		return true;
	}
	
	public static boolean isInvalid(Field field, Object entity) {
		
		String fieldName = field.getName();
		
		if (fieldName.equalsIgnoreCase("serialVersionUID")) {
			return true;
		}
		
		Object fieldValue = ReflectionUtil.value(field, entity);
		
		if (fieldValue == null) {
			return true;
		}
		
		if (fieldValue instanceof Collection) {
			return true;
		}
		
		if (fieldValue instanceof String) {
			return fieldValue.toString().trim().isEmpty();
		}
		
		if (field.isAnnotationPresent(JoinColumn.class) && !ReflectionUtil.isPrimaryKey(fieldValue)) {
			return true;
		}
		
		return false;
	}

}