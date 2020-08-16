package com.stefanini.cliente.bean;

import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.primefaces.PrimeFaces;

import com.stefanini.cliente.enums.SexoEnum;
import com.stefanini.cliente.model.Cliente;
import com.stefanini.cliente.service.ClienteService;

@ManagedBean
@SessionScoped
public class ClienteBean implements Serializable {

	private static final long serialVersionUID = 1L;

	public ClienteService clienteService = new ClienteService();

	private List<Cliente> clientes;// = new ArrayList<>();

	private Cliente clienteEdicao = new Cliente();

	@PostConstruct
	private void buscarClientes() {
		this.clientes = this.clienteService.listar();
	}

	public String novoCliente() {
		clienteEdicao = new Cliente();
		return "CadastroCliente?faces-redirect=true";
	}

	private void updateComponents(String componente) {
		PrimeFaces current = primefacesCurrent();
		current.ajax().update(componente);
	}

	public void salvar() throws ParseException {

		Cliente cliente;
		String mensagem;

		if (this.clienteEdicao.getId() != null) {
			cliente = this.clienteService.editar(clienteEdicao);
			mensagem = "Cliente alterado com sucesso!";

		} else {
			cliente = this.clienteService.salvar(clienteEdicao);
			mensagem = "Cliente salvo com sucesso!";
		}

		if (cliente != null) {
			this.buscarClientes();
			this.clienteEdicao = cliente;
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(mensagem));
		}

	}

	public void editar(Cliente cliente) {

		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("CadastroCliente.xhtml");
			this.clienteEdicao = cliente;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(cliente);
	}

	public void excluir(Cliente cliente) {
		this.clienteEdicao = cliente;
		PrimeFaces current = primefacesCurrent();
		this.updateComponents("frm:dialogConfirm");
		current.executeScript("PF('cd').show();");
	}

	public void remove() {

		String response = this.clienteService.excluir(this.clienteEdicao.getId());

		if (response.equals("ok")) {
			PrimeFaces current = primefacesCurrent();
			current.ajax().update("frm");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Cliente excluido com sucesso!"));
			this.buscarClientes();
		}
	}

	public List<Cliente> getClientes() {

		if (this.clientes == null) {
			this.clientes = clienteService.listar();
		}

		return clientes;
	}

	public Cliente getClienteEdicao() {
		return clienteEdicao;
	}

	public void setClienteEdicao(Cliente clienteEdicao) {
		this.clienteEdicao = clienteEdicao;
	}

	public SexoEnum[] getSexos() {
		return SexoEnum.values();
	}

	private PrimeFaces primefacesCurrent() {
		PrimeFaces current = PrimeFaces.current();
		return current;
	}

}