package com.forkexec.hub.domain;

public class FoodHub {
	
	String id;
	String entrada;
	String principal;
	String sobremesa;
	int preco;
	int tempoDeConfecao;
	int quantidade;
	
	public FoodHub() {
		super();
	}
	
	public FoodHub(String id, String entrada, String principal, String sobremesa,int preco, int tempoDeConfecao,int quantidade) {
		super();
		this.id = id;
		this.entrada = entrada;
		this.principal = principal;
		this.sobremesa = sobremesa;
		this.preco=preco;
		this.tempoDeConfecao = tempoDeConfecao;
		this.quantidade = quantidade;
	}
	
	public int getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
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
