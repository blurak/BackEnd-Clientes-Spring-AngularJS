package com.bolsadeideas.springboot.backend.apirest.models.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.bolsadeideas.springboot.backend.apirest.models.entity.Cliente;


@Repository
public interface IClienteRepository extends JpaRepository<Cliente, Long>{
	public Cliente getByemail(String email);
}
