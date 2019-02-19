package com.plot.finder.images.storage;

import javax.servlet.http.HttpServletRequest;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import com.plot.finder.exception.MyRestPreconditionsException;

public interface StorageService {

	void saveImage(final MultipartFile multipartfile, String name, Long id) throws MyRestPreconditionsException;
	
	//Resource readImage(final Long Id, String name, final boolean thumbnail) throws MyRestPreconditionsException;
	
	void deleteImage(final Long id, final String name) throws MyRestPreconditionsException;
	
	ResponseEntity<Resource> getImage(Long id, String name, boolean isThumbnail, HttpServletRequest request) throws MyRestPreconditionsException;
	
	void checkFile(MultipartFile mpf) throws MyRestPreconditionsException;
}