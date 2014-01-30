package br.com.setsoft.enuns;

/**
 * Enumera��o que especifica o tipo de opera��o que se est� realizando
 * 
 * @author Joel Marques
 * @since Outubro 01, 2011
 * 
 */
public enum EnumOperacao {
	
	ADICIONAR("adicionar"),
	BUSCAR("buscar"),
	ALTERAR("alterar"),
	VISUALIZAR("visualizar"),
	EXCLUIR("escluir");
	
	private String tipo;	
	
	private EnumOperacao(String t) {
		tipo = t;
	}
	
	public String getTipo() {
		return tipo;
	}
}