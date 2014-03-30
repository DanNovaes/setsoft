package br.com.setsoft.utilidade.JPAUtil;

public enum Operator {
	
	AND("AND"),
	OR("OR");
	
	private String type;
	
	private Operator(String type) {
		this.type = type;
	}
	
	public String getType() {
		
		return type;
	}
}
