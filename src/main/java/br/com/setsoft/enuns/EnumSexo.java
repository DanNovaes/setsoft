package br.com.setsoft.enuns;


/**
 * Enumera��o que especifica o Sexo de uma Pessoa F�sica
 * 
 * @author Joel Marques
 * @since Setembro 21, 2011
 * 
 */

public enum EnumSexo {
	
	M("Masculino"),
	F("Feminino");
	
	private String tipo;	
	
	private EnumSexo(String t) {
		tipo = t;
	}

	public String getTipo() {
		return tipo;
	}
	
	@Override
	public String toString(){
		return tipo;
	}
}