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

	
	public ResponseEntity<Resource> getImage(Long id, String name, boolean isThumbnail, HttpServletRequest request) 
			throws MyRestPreconditionsException{
		Resource resource = readImage(id, name, isThumbnail);
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
	
	public void saveImage(final MultipartFile mpf, String name, Long id) throws MyRestPreconditionsException {
		// compose dir structure :
		String path = buildDirPath(id);
		File dir = new File(path);
		
		// if dir does not exist yet, make it :
		if(!dir.exists()){
			try {
				dir.mkdirs();
			} catch (Exception e) {
				throw new MyRestPreconditionsException("Save image error", e.getMessage());
			}
		}
		
		// check that it is writeable
		RestPreconditions.assertTrue(java.nio.file.Files.isWritable(dir.toPath()), 
				"Directory creation error", "File location is not writable");
		
		// put files in dir :
		saveFileInDir(path, name, mpf);
	}
	
	private Resource readImage(final Long id, String name, boolean thumbnail) throws MyRestPreconditionsException{
		
		// build dir path :
		String imgDir = buildDirPath(id);
		
		{
			File tmp = new File(imgDir);
			RestPreconditions.assertTrue(tmp.exists(), 
					"Image read error","File path "+imgDir+" does not exist");
			RestPreconditions.assertTrue(java.nio.file.Files.isReadable(tmp.toPath()), 
					"Image read error", "File path "+imgDir+" is not readable");
		} // tmp ceases to exist here.
		
		try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(imgDir))){
			Path filePath = null;
			
			if(thumbnail) {
				name = thumbnailName(name);
			}
			
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
	
	private String thumbnailName(String name) {
		String[] parts = name.split("[.]");
		name = parts[0]+"_THUMBNAIL";
		if(parts.length>1) {
			name += ("."+parts[1]);
		}
		return name;
	}
	
	public void deleteImage(final Long id, String name) throws MyRestPreconditionsException {
		String dirPath = buildDirPath(id);
		
		{
			File tmp = new File(dirPath);
			RestPreconditions.assertTrue(tmp.exists(), 
					"Image delete error","File path "+dirPath+" does not exist");
			RestPreconditions.assertTrue(java.nio.file.Files.isWritable(tmp.toPath()), 
					"Image delete error", "File path "+dirPath+" is not writable");
		} // tmp ceases to exist here.
		
		deletePreviousImage(dirPath, name);
		deletePreviousImage(dirPath, thumbnailName(name));
		
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
			String newDirPath = dirPath.substring(0, dirPath.length() - 2);
			// recursive call - check if parent dir is empty too and delete it accordingly
			if(!newDirPath.endsWith("images\\")){
				deleteEmptyDirectoryTreeLeaf(newDirPath);
			}
		}
	}
	
	public void checkFile(MultipartFile mpf) throws MyRestPreconditionsException{
		if(mpf!=null){
			RestPreconditions.assertTrue(!mpf.isEmpty(), "Image save error", 
					"You are attempting to upload an empty file - "+mpf.getOriginalFilename());
			RestPreconditions.assertTrue(!mpf.getOriginalFilename().contains(".."), "Image save error", 
					"Upload filename contains invalid path sequence"+mpf.getOriginalFilename());
			RestPreconditions.assertTrue(mpf.getSize() < (5 * 1024 * 1024), "Image save error", 
					"Max upload file size is 3MB"+mpf.getOriginalFilename());
			RestPreconditions.assertTrue(checkMpFileExtension(mpf.getOriginalFilename()), "Image save error", 
					"You are only allowed to upload files with extensions jpg, jpeg, png and bmp"+mpf.getOriginalFilename());
		}
	}
	
	// save file :
	private void saveFileInDir(String dir, String name, MultipartFile mpf) throws MyRestPreconditionsException{
		
		String[] parts = mpf.getOriginalFilename().split("[.]");
		String ext = parts[parts.length-1];
		
		try {
			// save original :
			Path targetLocation = Paths.get(dir + name + "."+ext).toAbsolutePath().normalize();
			Files.copy(mpf.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
			
			// save thumbnail :
			targetLocation = Paths.get(dir + name +"_THUMBNAIL." + ext).toAbsolutePath().normalize();
			Files.copy(scaleImageInputstream(mpf, ext, 80, 100), 
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

	// create dir path in classpath: from userId / herbId
	private String buildDirPath(Long id) throws MyRestPreconditionsException{
		RestPreconditions.checkId(id); // got getImage
		
		char[] folders = (""+id).toCharArray();
		
		String dir = fileStorageLocation;
		
		for(int i=folders.length-1 ; i>=0 ; i--){
			dir += (folders[i]+File.separator);
		}
		
		return dir;
	}
}
