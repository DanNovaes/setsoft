package br.com.setsoft.enuns;

public enum EnumTipoPessoa {
	
	FISICA("F�sica","CPF", "Nome"),
	JURIDICA("Jur�dica","CNPJ", "Raz�o Social");
	
	private String tipo;
	private String sigla;
	private String descricao;
	
	private EnumTipoPessoa(String tipo, String sigla, String descricao) {
		this.tipo = tipo;
		this.sigla = sigla;
		this.descricao = descricao;
	}

	public String getTipo() {
		
		return tipo;
	}
	
	public String getSigla() {
		
		return sigla;
	}
	
	public String getDescricao() {
		
		return descricao;
	}
}