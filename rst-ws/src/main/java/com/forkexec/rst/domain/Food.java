package com.forkexec.rst.domain;

public class Food {
	
	String id;
	String entrada;
	String principal;
	String sobremesa;
	int preco;
	int tempoDeConfecao;
	
	
	public Food(String id, String entrada, String principal, String sobremesa,int preco, int tempoDeConfecao) {
		super();
		this.id = id;
		this.entrada = entrada;
		this.principal = principal;
		this.sobremesa = sobremesa;
		this.preco=preco;
		this.tempoDeConfecao = tempoDeConfecao;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getEntrada() {
		return entrada;
	}
	
	public void setEntrada(String entrada) {
		this.entrada = entrada;
	}
	
	public String getPrincipal() {
		return principal;
	}
	
	public void setPrincipal(String principal) {
		this.principal = principal;
	}
	
	public String getSobremesa() {
		return sobremesa;
	}
	
	public void setSobremesa(String sobremesa) {
		this.sobremesa = sobremesa;
	}
	
	public int getTempoDeConfecao() {
		return tempoDeConfecao;
	}
	
	public void setTempoDeConfecao(int tempoDeConfecao) {
		this.tempoDeConfecao = tempoDeConfecao;
	}
	
	public int getPreco() {
		return preco;
	}

	public void setPreco(int preco) {
		this.preco = preco;
	}

	@Override
	public String toString() {
		return "Menu [id=" + id + ", entrada=" + entrada + ", principal=" + principal + ", sobremesa=" + sobremesa
				+ ", tempoDeConfecao=" + tempoDeConfecao + "]";
	}
	

}
