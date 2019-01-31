package com.plot.finder.images.storage.impl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.plot.finder.exception.MyRestPreconditionsException;
import com.plot.finder.images.storage.StorageService;
import com.plot.finder.util.RestPreconditions;

@Service
public class StorageServiceImpl implements StorageService {

	private String fileStorageLocation;
	private ResourceLoader resourceLoader;
	
	@Autowired
	public StorageServiceImpl(ResourceLoader resourceLoader) throws MyRestPreconditionsException {
		this.resourceLoader = resourceLoader;
		this.fileStorageLocation = System.getProperty("user.dir")+File.separator+"images"+File.separator;
		
		try {
            Files.createDirectories(Paths.get(fileStorageLocation).toAbsolutePath().normalize());
        } catch (Exception ex) {
            throw new MyRestPreconditionsException("Image service initialization error", 
            		"Could not create the directory where the uploaded files will be stored.");
        }
	}

	
	public ResponseEntity<Resource> getImage(Resource resource, HttpServletRequest request){
		String contentType = null;
	    try {
	        contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
	    } catch (Exception e) {
	    	contentType = "application/octet-stream";
	    }
	    
	    return ResponseEntity.ok()
	            .contentType(MediaType.parseMediaType(contentType))
	            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
	            .body(resource);
	}
	
	public void saveImage(final MultipartFile mpf, Long id, boolean isUser) throws MyRestPreconditionsException {	
		RestPreconditions.assertTrue(null != mpf, "Image save error", 
				"You are attempting to upload a non-existing file.");
		RestPreconditions.assertTrue(!mpf.isEmpty(), "Image save error", 
				"You are attempting to upload an empty file.");
		RestPreconditions.assertTrue(!mpf.getOriginalFilename().contains(".."), "Image save error", 
				"Upload filename contains invalid path sequence");
		RestPreconditions.assertTrue(mpf.getSize() < (3 * 1024 * 1024), "Image save error", 
				"Max upload file size is 3MB");
		RestPreconditions.assertTrue(checkMpFileExtension(mpf.getOriginalFilename()), "Image save error", 
				"You are only allowed to upload files with extensions jpg, jpeg, png and bmp");
		RestPreconditions.assertTrue(id!=null && id>0, "Image save error", 
				"Uploading file for invalid plot id ("+id+")");
		
		// compose dir structure :
		String dir = buildDirPath(id);
		
		// check resulting dir :
		checkResultingDir(dir);
		
		// put files in dir :
		saveFileInDir(dir, mpf);
	}
	
	public Resource readImage(final Long id, final String name) throws MyRestPreconditionsException{
		
		String imgDir = buildDirPath(id);
		
		if(!(new File(imgDir)).isDirectory()){
			return resourceLoader.getResource("classpath:images/no_image_found.jpeg");
		}
		
		try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(imgDir))){
			Path filePath = null;
			
			for (Path path : directoryStream) {
            	if(path.toString().contains(name)){
            		filePath = path;
            		break;
            	}
            }
			
			if(filePath!=null){
				return new UrlResource(filePath.toUri());
			} else {
            	return resourceLoader.getResource("classpath:images/no_image_found.jpeg");
            }
		} catch (Exception e){
			throw new MyRestPreconditionsException("Resource retreave error", "Oops ! Something went wrong in retreaving the image.");
		}
	}
	
	public void deleteImage(final Long id, String name) throws MyRestPreconditionsException {
		String dirPath = buildDirPath(id);
		
		checkResultingDir(dirPath);
		
		deletePreviousImage(dirPath, name);
		deletePreviousImage(dirPath, name+"_THUMBNAIL");
		
		deleteEmptyDirectoryTreeLeaf(dirPath);
	}
	
	private void deleteEmptyDirectoryTreeLeaf(String dirPath) throws MyRestPreconditionsException {
		File dir = new File(dirPath);
		// dirPath was already checked.
		
		// if directory is empty :
		if(dir.list().length == 0) {
			// delete empty dir and check :
			RestPreconditions.assertTrue(dir.delete(),"Image Storage Service error", "Failed to delete empty directory leaf "+dirPath);
			// make new dir path :
			String newDirPath = dirPath.split("[^0-9][0-9]+$")[0];
			// recursive call - check if parent dir is empty too and delete it accordingly
			deleteEmptyDirectoryTreeLeaf(newDirPath);
		}
	}
	
	// save file :
	private void saveFileInDir(String dir, MultipartFile mpf) throws MyRestPreconditionsException{

		String contentType = mpf.getOriginalFilename().split("\\.")[1];
		String[] parts = mpf.getOriginalFilename().split("[\\/]");
		String fileName = parts[parts.length-1];
		// delete previous :
		deletePreviousImage(dir, fileName);
		
		try {
			// save original :
			Path targetLocation = Paths.get(dir + fileName+"."+contentType).toAbsolutePath().normalize();
			Files.copy(mpf.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
			
			// save thumbnail :
			targetLocation = Paths.get(dir + fileName+"_THUMBNAIL."+contentType).toAbsolutePath().normalize();
			Files.copy(scaleImageInputstream(mpf, contentType, 100, 120), 
					targetLocation, StandardCopyOption.REPLACE_EXISTING);
			
		}catch (IllegalStateException | IOException e) {
			e.printStackTrace();
			throw new MyRestPreconditionsException("Image save error", "Failed to save image in "+dir);
		}
	}
	
	private void deletePreviousImage(String dir, String name) throws MyRestPreconditionsException{
		try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(dir))) {
            for (Path path : directoryStream) {
            	if(path.toString().contains(name)){
            		Files.delete(path);
            		break;
            	}
            }
        } catch (IOException ex) {
        	throw new MyRestPreconditionsException("Delete image error",ex.getMessage());
        }
	}
	
	private boolean checkMpFileExtension(final String fileName){
		return Pattern.compile("[.](jpg)|(JPG)|(png)|(PNG)|(bmp)|(BMP)|(jpeg)|(JPEG)$").matcher(fileName).find();
	}
	
	private InputStream scaleImageInputstream(final MultipartFile multipart,final String contentType,final int targetWidth,final int targetHeight) throws MyRestPreconditionsException {
		try{
	        BufferedImage originalImg = ImageIO.read(multipart.getInputStream());
	        
	        BufferedImage scaledImg = Scalr.resize(originalImg, Scalr.Method.QUALITY, Scalr.Mode.FIT_TO_WIDTH,
	                targetWidth, targetHeight, Scalr.OP_ANTIALIAS);
	        
	        ByteArrayOutputStream os = new ByteArrayOutputStream();
	        ImageIO.write(scaledImg, contentType, os);
	        os.flush();
	        return new ByteArrayInputStream(os.toByteArray());
		}
		catch(Exception e){
			throw new MyRestPreconditionsException("Image scaling error","Ooops - something went wrong !");
		}
    }
	
	// directory verification
	private void checkResultingDir(String path) throws MyRestPreconditionsException {
		
		File dir = new File(path);
		
		// if dir does not exist yet, make it :
		if(!dir.exists()){
			dir.mkdirs();
		}
		
		// check that it is a directory
		if(!dir.isDirectory()){
			throw new MyRestPreconditionsException("Directory creation error", "Invalid path to directory");
		}
		
		// check that it is writeable
		if(!java.nio.file.Files.isWritable(dir.toPath())){
			throw new MyRestPreconditionsException("Directory creation error", "File location is not writable");
		}
	}

	// create dir path in classpath: from userId / herbId
	private String buildDirPath(Long id) throws MyRestPreconditionsException{
		if (id==null || (id!=null && id<=0)){
			throw new MyRestPreconditionsException("Save Image File !","Id used for directory structure traverse is invalid");
		}
		
		char[] folders = (""+id).toCharArray();
		
		String dir = fileStorageLocation;
		
		for(int i=folders.length-1 ; i>=0 ; i--){
			dir += (folders[i]+File.separator);
		}
		
		return dir;
	}
}
