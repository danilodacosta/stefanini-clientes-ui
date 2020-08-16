package com.stefanini.cliente.service.exception;

import java.io.Serializable;
import java.util.List;

public class ExceptionResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	private String titulo;
	private List<Error> errors;

	public static class Error {

		private String mensagem;

		public Error(String mensagem) {
			super();

			this.mensagem = mensagem;
		}

		public String getMensagem() {
			return mensagem;
		}

		public void setMensagem(String mensagem) {
			this.mensagem = mensagem;
		}

	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public List<Error> getErrors() {
		return errors;
	}

	public void setErrors(List<Error> errors) {
		this.errors = errors;
	}

}