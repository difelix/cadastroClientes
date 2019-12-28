package com.cadastroClientes.demo.utils;

public class Converters {
	
	public static boolean stringSomenteNumeros(String texto) {
		try {
			Integer.valueOf(texto);
		} catch (NumberFormatException ex) {
			return false;
		}
		return true;
	}

}
