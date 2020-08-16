package com.stefanini.cliente.enums;

public enum SexoEnum {
	
	FEMININO("Feminino"),
	MASCULINO("Masculino");
	
	private String value;
	
	private SexoEnum(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}