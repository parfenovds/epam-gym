package com.epam.commonDB;

//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import java.io.IOException;
//import java.io.InputStream;
//import lombok.extern.log4j.Log4j2;
//import org.springframework.beans.BeansException;
//import org.springframework.beans.factory.config.BeanPostProcessor;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.core.io.Resource;
//import org.springframework.stereotype.Component;
//
//@Component
//@Log4j2
//public class DataLoader implements BeanPostProcessor {
//  @Override
//  public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
//    if (beanName.equals("storage")) {
//      bean = fillStorage();
//    }
//    return bean;
//  }
//
//  private Storage fillStorage() {
//    Storage storage;
//    ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
//    Resource resource = new ClassPathResource("data.json");
//    try (InputStream inputStream = resource.getInputStream()) {
//      storage = objectMapper.readValue(inputStream, Storage.class);
//      log.info("Data loaded successfully into Storage.");
//    } catch (IOException e) {
//      log.error("Failed to load data from JSON.", e);
//      throw new RuntimeException(e);
//    }
//    return storage;
//  }
//}
