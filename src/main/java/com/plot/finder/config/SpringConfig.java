package com.plot.finder.config;

import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@Configuration
@ComponentScan(basePackages={"com.plot.finder.*"})
@EnableTransactionManagement
public class SpringConfig {
	
    public static final String ENCODING_UTF_8 = "UTF-8";

    public static final long MAX_UPLOAD_FILE_SIZE = 52428807;

    public static final long MAX_UPLOAD_PER_FILE_SIZE = 5242880;

	@Bean
    @Primary
    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
        // this makes sure that datetime in string format in input 
		// is automatically converted to LocalDateTime object
        ObjectMapper objectMapper = builder.createXmlMapper(false).build();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        
        objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        return objectMapper;
    }
	
	@Bean(name = "multipartResolver")
	public CommonsMultipartResolver multipartResolver() {
		CommonsMultipartResolver ret = new CommonsMultipartResolver();

        ret.setMaxUploadSize(MAX_UPLOAD_FILE_SIZE);

        ret.setMaxUploadSizePerFile(MAX_UPLOAD_PER_FILE_SIZE);

        ret.setDefaultEncoding(ENCODING_UTF_8);

        return ret;
	}
	
	@Bean
	public HttpMessageConverters customConverters() {
		// this makes json nicely formated
	    HttpMessageConverter<?> additional = new FastJsonHttpMessageConverter();
	    return new HttpMessageConverters(additional);
	}
}
