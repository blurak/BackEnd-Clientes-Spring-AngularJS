package com.bolsadeideas.springboot.backend.apirest.models.services.implement;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import com.bolsadeideas.springboot.backend.apirest.models.services.IUploadFileService;

@Service
public class IUploadServiceImp implements IUploadFileService{
	private final Logger log = LoggerFactory.getLogger(IUploadServiceImp.class);
	@Override
	public Resource cargar(String nombreFoto) throws Exception {
		Path rutaArchivo = getPath(nombreFoto);
		log.info(rutaArchivo.toString());
		Resource recurso = new UrlResource(rutaArchivo.toUri());		
		if(!recurso.exists()&& !recurso.isReadable()) {
			rutaArchivo = Paths.get("src/main/resources/static/imges").resolve("no-usuario.png").toAbsolutePath();
			try {
				recurso = new UrlResource(rutaArchivo.toUri());
			} catch (Exception e) {
				e.printStackTrace();
			}
			log.error("Error no se puede cargar la imagen" + nombreFoto);
		}
		return recurso;
	}

	@Override
	public String copiar(MultipartFile archivo) throws Exception {
		String nombreArchivo = UUID.randomUUID().toString()+"_"+ archivo.getOriginalFilename().replace(" ", "");
		Path rutaArchivo = getPath(nombreArchivo);
		log.info(rutaArchivo.toString());
		
		Files.copy(archivo.getInputStream(), rutaArchivo);
		
		return nombreArchivo;
	}

	@Override
	public boolean eliminar(String nombreFoto) {
		if(nombreFoto!= null && nombreFoto.length()>0) {
			Path rutaArchivoAnterior = Paths.get("upload").resolve(nombreFoto).toAbsolutePath();
			File ArchivoFotoAnterior =  rutaArchivoAnterior.toFile();
			if(ArchivoFotoAnterior.exists()&&ArchivoFotoAnterior.canRead()) {
				ArchivoFotoAnterior.delete();
				return true;
			}
		}
		return false;
	}

	@Override
	public Path getPath(String nombreFoto) {
		
		return Paths.get("upload").resolve(nombreFoto).toAbsolutePath();
	}

}
