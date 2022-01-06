package com.bolsadeideas.springboot.backend.apirest.models.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
@Entity
@Table(name="clientes")
public class Cliente implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotNull(message = "nombre es campo obligatorio")	
	@Size(min = 4,  max = 12, message = "direccion entre 4 y 12 carácteres")
	private String nombre;
	private String apellido;
	@NotNull(message = "email es campo obligatorio")
	@Column(name = "email")
	@Email
	private String email;
	@NotNull(message = "La fecha  es campo obligatorio")
 	@Column(name="creat_at")
 	@Temporal(TemporalType.DATE)
	private Date creatAt;
	private String foto;
	
 	/*@PrePersist
 	public void prePresist(){
 		creatAt = new Date();
 	}*/
 	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getcreatAt() {
		return creatAt;
	}
	

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	public void setcreatAt(Date creatAt) {
		this.creatAt = creatAt;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
