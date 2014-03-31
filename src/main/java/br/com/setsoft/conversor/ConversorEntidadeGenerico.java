package br.com.setsoft.conversor;

import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.setsoft.interfaces.IEntidadeBase;

@FacesConverter(value = "conversorEntidadeGenerico")
public class ConversorEntidadeGenerico implements Converter {
	
	@Override 
	public Object getAsObject(FacesContext ctx, UIComponent component, String primaryKey) {
		
        return this.getEntity(component, primaryKey); 
    }
	
	@Override    
	public String getAsString(FacesContext ctx, UIComponent component, Object entity) {
		
		return this.getPrimaryKey(component, entity);  
    }
	
	private Object getEntity(UIComponent component, String primaryKey) {
		
		if (this.isNotBlank(primaryKey)) {
			
			Map<String, Object> attributes = this.getAttributesFrom(component);
			
			if (this.isNotNull(attributes)) {
				return attributes.get(primaryKey);
			}
		}
		
        return null;
    }
	
	private String getPrimaryKey(UIComponent component, Object entity) {
		
		String primaryKey = this.getPrimaryKey(entity);
		
		//add primary key to component.
		this.addAttribute(component, primaryKey, entity);
		
		return primaryKey;
	}
	
	@SuppressWarnings("rawtypes")
	private String getPrimaryKey(Object entity) {
		
		if (this.isInstanceOfIEntidadeBase(entity)) {
			
			Object primaryKey = ((IEntidadeBase)entity).getPK();
			
			if (this.isNotNull(primaryKey)) {
				
				return primaryKey.toString();
			}
		}
		
		return null;
	}
	
	private boolean isInstanceOfIEntidadeBase(Object entity) {
		
		return this.isNotNull(entity) && entity instanceof IEntidadeBase;
	}
	
	private boolean isNotBlank(String value) {
		
		return this.isNotNull(value) && !value.trim().isEmpty();
	}
	
	private boolean isNotNull(Object object) {
		
		return object != null;
	}
	
	private void addAttribute(UIComponent component, String key, Object value) {
		
		if (this.isNotBlank(key) && this.isNotNull(value)) {
			
			Map<String, Object> attributes = this.getAttributesFrom(component);
			
			if (this.isNotNull(attributes)) {
				attributes.put(key, value);
			}
		}    	
    }  
  
    private Map<String, Object> getAttributesFrom(UIComponent component) {
    	
        return this.isNotNull(component) ? component.getAttributes() : null;  
    }
	
}