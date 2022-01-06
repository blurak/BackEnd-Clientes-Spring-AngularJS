package com.bolsadeideas.springboot.backend.apirest.models.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.bolsadeideas.springboot.backend.apirest.models.entity.Cliente;

public interface IClienteService {
	public List<Cliente> findAll();
	
	public Page<Cliente> findAll(Pageable pageable);
	
	public Cliente crear(Cliente persona);
	
	public void eliminar(Long id);
	
	public List<Cliente> listarTodasPersonas();
	
	public Cliente editar(Cliente entidad);
	
	public Cliente findById(Long id);
	
	public Cliente findByEmail(String email);

}
