package com.bolsadeideas.springboot.backend.apirest.models.services.implement;
import java.util.ArrayList;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.bolsadeideas.springboot.backend.apirest.models.entity.Cliente;
import com.bolsadeideas.springboot.backend.apirest.models.repository.IClienteRepository;
import com.bolsadeideas.springboot.backend.apirest.models.services.IClienteService;




@Service
public class IClienteServiceImp implements IClienteService {
	@Autowired
	private IClienteRepository repository;
	@Override
	public Cliente crear(Cliente persona) {
		Cliente per = repository.getByemail(persona.getEmail());	
		if(per == null) {
			repository.save(persona);
			return persona;
		}
		else {
			return null;
		}	
	}

	@Override
	public void eliminar(Long id) {
		repository.deleteById(id);
	}

	@Override
	public List<Cliente> listarTodasPersonas() {
		List<Cliente> lista = new ArrayList<Cliente>();
		lista = repository.findAll();
		return lista;
	}
	@Override
	public Page<Cliente> findAll(Pageable pageable) {
		
		return repository.findAll(pageable);
	} 

	@Override
	public Cliente editar(Cliente entidad) {		
			
			return repository.save(entidad);
		
	}

	@Override
	public List<Cliente> findAll() {
		
		return (List<Cliente>) repository.findAll();
	}

	@Override
	
	public Cliente findById(Long id) {
		return repository.findById(id).orElse(null);
	}

	@Override
	public Cliente findByEmail(String email) {		
		return repository.getByemail(email);
	}

	

}
