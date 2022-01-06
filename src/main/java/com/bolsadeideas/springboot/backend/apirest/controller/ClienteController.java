package com.bolsadeideas.springboot.backend.apirest.controller;

import java.io.File;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import java.util.stream.Collectors;

import javax.management.RuntimeErrorException;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bolsadeideas.springboot.backend.apirest.models.entity.Cliente;
import com.bolsadeideas.springboot.backend.apirest.models.services.IClienteService;
import com.bolsadeideas.springboot.backend.apirest.models.services.IUploadFileService;





@Controller
@RestController
@RequestMapping("/clientes")
public class ClienteController {
	@Autowired
	IClienteService service;

	@Autowired
	IUploadFileService UpService;
	
	
	private final Logger log = LoggerFactory.getLogger(ClienteController.class);
	
	
	
	@CrossOrigin(origins = "*")
	@GetMapping("/consultarTodas")
	public ResponseEntity<List<Cliente>> consultarTodasPersonas(){
		return new ResponseEntity<List<Cliente>>(service.listarTodasPersonas(), HttpStatus.OK);
	}
	@CrossOrigin(origins = "*")
	@GetMapping("/consultar/page/{page}/{cant}")
	public ResponseEntity<Page<Cliente>> consultarPage(@PathVariable Integer page,@PathVariable Integer cant){
		return new ResponseEntity<Page<Cliente>>(service.findAll(PageRequest.of(page, cant)), HttpStatus.OK);
	}
	@CrossOrigin(origins = "*")
	@PostMapping("/crear")
	public ResponseEntity<?> crearPersona(@Valid @RequestBody Cliente persona,BindingResult result){		
		Map<String, Object> response = new HashMap<>();
		if(result.hasErrors()) {			
			List<String> errors = result.getFieldErrors().stream().map(err ->{
				return "El Campo "+ err.getField()+" "+err.getDefaultMessage();
				}).collect(Collectors.toList());

			response.put("error", errors);			
			return new ResponseEntity<Map<String, Object>>(response,HttpStatus.BAD_REQUEST);		
			
		}
			
		
		try {
		Cliente p = service.crear(persona);				
			if(p!= null) {
				response.put("Mensaje", "El cliente fue creado con exito");
				response.put("cliente",p);
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
				
			}
			else {
				 response.put("Mensaje", "El Email Que intenta registrar ya fue vinculado anteriormente");
				 return new ResponseEntity<Map<String, Object>>(response,HttpStatus.BAD_REQUEST);
			}
		}catch (Exception e) {
			response.put("Mensaje", "Compruebe Datos no diligenciados");
			response.put("error", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	@CrossOrigin(origins = "*")
	@DeleteMapping("/eliminar/{id}")
	public ResponseEntity<?> eliminarPersona(@PathVariable Long id){
		Map<String, Object> response = new HashMap<>();
		try{
			Cliente cliente = service.findById(id);
			String Fotoanterior = cliente.getFoto();
			UpService.eliminar(Fotoanterior);		
			service.eliminar(id);		
		}catch(Exception e) {
			response.put("Mensaje", "Error al realizar la consulta");
			response.put("error", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("Mensaje", "El Cliente Fue eliminado con exito");
		return new ResponseEntity<Map<String, Object>>( response,HttpStatus.OK);
	}
	
	@CrossOrigin(origins = "*")
	@GetMapping("/cliente/{id}")
	public ResponseEntity<?> clienteId(@PathVariable Long id){		
		Cliente cliente = null;
		Map<String, Object> response = new HashMap<>();
		try {
			 cliente = service.findById(id);
		}catch (DataAccessException e) {
			response.put("Mensaje", "Error al realizar la consulta");
			response.put("error", e.getMessage().concat(" : ").concat(e.getMostSpecificCause().getMessage()));
			 return new ResponseEntity<Map<String, Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
				 
		 if(cliente == null ) {
			 response.put("Mensaje", "el Cliente ID: ".concat(id.toString().concat(" No existe en la base de datos")));
			 return new ResponseEntity<Map<String, Object>>(response,HttpStatus.NOT_FOUND);
		 }
		return new ResponseEntity<Cliente>(cliente, HttpStatus.OK);
	}
	
	@CrossOrigin(origins = "*")
	@PutMapping("/editar")	
	public ResponseEntity<?> editarPst(@Valid @RequestBody Cliente persona ,BindingResult result){
		Cliente p = null;
		Cliente c = null;
		Map<String, Object> response = new HashMap<>();
		if(result.hasErrors()) {			
			List<String> errors = result.getFieldErrors().stream().map(err ->{
				return "El Campo "+ err.getField()+" "+err.getDefaultMessage();
				}).collect(Collectors.toList());

			response.put("error", errors);			
			return new ResponseEntity<Map<String, Object>>(response,HttpStatus.BAD_REQUEST);		
			
		}
		try {
			p=service.findById(persona.getId());	
			c=service.findByEmail(persona.getEmail());
			if(persona.getEmail().equals("")) {
				List<String> errors = new ArrayList(); 
				errors.add("el correo debe ser diligenciado");
				response.put("Mensaje", "el Cliente No se puede editar");
				response.put("error", errors);
				return new ResponseEntity<Map<String, Object>>(response,HttpStatus.BAD_REQUEST);
			}
			if(c != null) {
				if((persona.getId())== (c.getId()) && (p != null)) {			 
				 p = service.editar(persona);		
				 return new ResponseEntity<Cliente>(p,HttpStatus.OK);
			 }else {
				 response.put("Mensaje", "El correo que esta usando ya existe");
				 return new ResponseEntity<Map<String, Object>>(response,HttpStatus.BAD_REQUEST);
			 }}else {
				 if((p != null)) {
					 p = service.editar(persona);
					 return new ResponseEntity<Cliente>(p,HttpStatus.OK);
				 }
				 		
			 }		
			
		}catch (TransactionSystemException e) {
			response.put("Mensaje", "Error al actualizar");
			response.put("error", e.getMostSpecificCause().getMessage());
			 return new ResponseEntity<Map<String, Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("Mensaje", "El Cliente no existe");		
		return new ResponseEntity<Map<String, Object>>(response,HttpStatus.NOT_FOUND);
	}
	@CrossOrigin(origins = "*")
	@PostMapping("/upload")
	public ResponseEntity<?> upload(@RequestParam("archivo") MultipartFile archivo, @RequestParam("id") Long id){
		Map<String, Object> response = new HashMap<>();	
		Cliente cliente = service.findById(id);
		if(cliente != null) {
			if(!archivo.isEmpty()) {
				String nombreArchivo="";
				try {
					nombreArchivo=UpService.copiar(archivo);
				} catch (Exception e) {
					response.put("Mensaje", "Error al cargar la foto");					
					 return new ResponseEntity<Map<String, Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
					
				}
				String Fotoanterior = cliente.getFoto();
				UpService.eliminar(Fotoanterior);				
				cliente.setFoto(nombreArchivo);
				service.editar(cliente);
				response.put("mensaje","has subido correctamete la imagen:"+ nombreArchivo);
				response.put("cliente",cliente);
			}
		}
		return new ResponseEntity<Map<String, Object>>( response,HttpStatus.OK);
	}
	@CrossOrigin(origins = "*")
	@GetMapping("/uploads/img/{nombreFoto:.+}")
	public ResponseEntity<Resource> verFoto(@PathVariable String nombreFoto) {
		
		Resource recurso = null;
		try {
			recurso =UpService.cargar(nombreFoto);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		HttpHeaders cabecera = new HttpHeaders();
		cabecera.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+ recurso.getFilename()+"\"");
		
		return new ResponseEntity<Resource>(recurso,cabecera,HttpStatus.OK);
	}
}
