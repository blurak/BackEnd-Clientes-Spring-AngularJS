package com.bolsadeideas.springboot.backend.apirest.models.services;

import java.nio.file.Path;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface IUploadFileService {
	public Resource cargar(String nombreFoto) throws Exception;
	public String copiar(MultipartFile archivo) throws Exception;
	public boolean eliminar (String nombreFoto);
	public Path getPath(String nombreFoto);
}
