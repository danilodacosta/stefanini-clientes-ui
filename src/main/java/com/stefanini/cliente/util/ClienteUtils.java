package com.stefanini.cliente.util;

public class ClienteUtils {

	public static String removeMaskCpf(String cpf) {
		return cpf.replaceAll("[^0-9]", "");
	}
	
	public static String addMaskCpf(String cpf) {
		return cpf.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})" ,"$1.$2.$3-$4");
		
	}
	
}
