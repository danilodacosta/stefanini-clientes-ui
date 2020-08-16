package com.stefanini.cliente.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.stefanini.cliente.adapter.LocalDateAdapter;
import com.stefanini.cliente.adapter.LocalDateTimeAdapter;
import com.stefanini.cliente.model.Cliente;
import com.stefanini.cliente.service.exception.ExceptionResponse;
import com.stefanini.cliente.util.ClienteUtils;

public class ClienteService implements Serializable {

	private static final long serialVersionUID = 1L;

	private final String BASE_URL = "http://localhost:8081/stefanini/v1/clientes";

	private Gson gson;

	private ExceptionResponse exception; 

	private CloseableHttpClient httpclient = HttpClients.createDefault();

	public ClienteService() {
		gson = new GsonBuilder()
				.registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
				.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
				.create();
		exception = new ExceptionResponse();
	}

	public List<Cliente> listar() {

		List<Cliente> clientes = new ArrayList<Cliente>();

		try {

			HttpGet response = new HttpGet(this.BASE_URL);

			String jsonBody = httpclient.execute(response, this.responseHandler);

			clientes = gson.fromJson(jsonBody, new TypeToken<List<Cliente>>() {
			}.getType());

			clientes.stream().forEach(c -> c.setCpf(ClienteUtils.addMaskCpf(c.getCpf())));

			return clientes;

		} catch (IOException e) {

			this.ErroResponse(e);

		}

		return clientes;
	}

	public Cliente salvar(Cliente cliente) throws ParseException {

		HttpPost httpPost = new HttpPost(this.BASE_URL);
		httpPost.setHeader("Accept", "application/json");
		httpPost.setHeader("Content-type", "application/json");

		StringEntity stringEntity;

		try {

			cliente.setCpf(ClienteUtils.removeMaskCpf(cliente.getCpf()));

			stringEntity = new StringEntity(gson.toJson(cliente));

			httpPost.setEntity(stringEntity);

			String responseBody = httpclient.execute(httpPost, this.responseHandler);

			return gson.fromJson(responseBody, Cliente.class);

		} catch (IOException e) {

			this.ErroResponse(e);

		}

		return null;
	}

	public Cliente editar(Cliente cliente) throws ParseException {

		HttpPut httpPut = new HttpPut(this.BASE_URL + "/" + cliente.getId());
		httpPut.setHeader("Accept", "application/json");
		httpPut.setHeader("Content-type", "application/json");

		StringEntity stringEntity;

		try {

			cliente.setCpf(ClienteUtils.removeMaskCpf(cliente.getCpf()));

			stringEntity = new StringEntity(gson.toJson(cliente));

			httpPut.setEntity(stringEntity);

			String responseBody = httpclient.execute(httpPut, this.responseHandler);

			return gson.fromJson(responseBody, Cliente.class);

		} catch (IOException e) {

			this.ErroResponse(e);

		}

		return null;
	}

	public String excluir(Long id) {

		HttpDelete httpDelete = new HttpDelete(this.BASE_URL + "/" + id);

		try {

			httpclient.execute(httpDelete, this.responseHandler);

			return "ok";

		} catch (IOException e) {

			this.ErroResponse(e);

		}

		return "error";
	}

	private ResponseHandler<String> responseHandler = response -> {

		int status = response.getStatusLine().getStatusCode();
		InputStream is = response.getEntity().getContent();

		if (status >= 200 && status < 300) {
			HttpEntity entity = response.getEntity();
			return entity != null ? EntityUtils.toString(entity) : null;

		} else {

			String resultJson = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8)).lines().parallel()
					.collect(Collectors.joining("\n"));

			exception = gson.fromJson(resultJson, ExceptionResponse.class);

			throw new ClientProtocolException(exception.getTitulo());
		}
	};

	private void ErroResponse(IOException e) {

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error : " + e.getMessage(), ""));
		
		FacesContext context = FacesContext.getCurrentInstance();
		
		if (this.exception.getErrors() != null) {
			this.exception.getErrors().stream().forEach(erro -> {
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "* " + erro.getMensagem(), ""));
			});
		}

	}

}
