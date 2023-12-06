package com.epam.commonDB;

import com.epam.commonDB.util.ObjectFactoryUtil;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class DataPropertiesLoader implements BeanPostProcessor {
  public static final String NON_MAP_PREFIX = "next";
  public static final String SET_METHOD_PREFIX = "set";
  public static final String GET_METHOD_PREFIX = "get";
  public static final String PATH_TO_ENTITIES = "com.epam.entity.";
  public static final String STORAGE_BEAN_NAME = "storage";
  @Value("data.properties")
  private Resource dataPropertiesFile;
  @Override
  public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
    if (beanName.equals(STORAGE_BEAN_NAME)) {
      fillStorage((Storage) bean);
    }
    return bean;
  }

  private void fillStorage(Storage storage) {
    try (InputStream inputStream = dataPropertiesFile.getInputStream()) {
      Properties properties = new Properties();
      properties.load(inputStream);
      List<String> sortedPropertyNames = getSortedPropertyNames(properties);
      fillStorageWithValues(storage, sortedPropertyNames, properties);
      log.info("Data loaded successfully into Storage.");
    } catch (IOException e) {
      log.error("Failed to load data from JSON.", e);
      throw new RuntimeException(e);
    } catch (NoSuchMethodException e) {
      throw new RuntimeException(e);
    } catch (InvocationTargetException e) {
      throw new RuntimeException(e);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    } catch (InstantiationException e) {
      throw new RuntimeException(e);
    } catch (NoSuchFieldException e) {
      throw new RuntimeException(e);
    }
  }

  private void fillStorageWithValues(
      Storage storage,
      List<String> sortedPropertyNames,
      Properties properties) throws NoSuchFieldException, IllegalAccessException, InvocationTargetException, NoSuchMethodException,
      ClassNotFoundException, InstantiationException {
    int i = 0;
    while (i < sortedPropertyNames.size()) {
      String currentPropertyName = sortedPropertyNames.get(i);
      if (currentPropertyName.startsWith(NON_MAP_PREFIX)) {
        String value = properties.getProperty(currentPropertyName);
        setNextIdsInStorage(storage, currentPropertyName, value);
        ++i;
      } else {
        String[] splitPropertyName = getSplitStringByDot(currentPropertyName);
        String mapName = splitPropertyName[0];
        Long id = Long.parseLong(splitPropertyName[1]);
        Class<?> clazz = Class.forName(PATH_TO_ENTITIES + capitalizeFirstLetterAndRemoveLastLetter(mapName));
        Object objectToPutInMap = clazz.getDeclaredConstructor().newInstance();
        while (i < sortedPropertyNames.size() && sortedPropertyNames.get(i).startsWith(mapName + "." + id + ".")) {
          currentPropertyName = sortedPropertyNames.get(i);
          splitPropertyName = getSplitStringByDot(currentPropertyName);
          String fieldName = splitPropertyName[2];
          String value = properties.getProperty(sortedPropertyNames.get(i));
          Class<?> fieldType = clazz.getDeclaredField(fieldName).getType();
          setFieldInObjectToPutInMap(objectToPutInMap, fieldName, fieldType, value);
          ++i;
        }
        getCertainMapFromStorage(storage, mapName).put(id, objectToPutInMap);
      }
    }
  }

  private static void setFieldInObjectToPutInMap(Object objectToPutInMap, String fieldName, Class<?> fieldType, String value) {
    try {
      objectToPutInMap.getClass().getMethod(SET_METHOD_PREFIX + capitalizeFirstLetter(fieldName), fieldType
      ).invoke(objectToPutInMap, ObjectFactoryUtil.getObjectFactory(fieldType).create(value));
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    } catch (InvocationTargetException e) {
      throw new RuntimeException(e);
    } catch (NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
  }

  private static String capitalizeFirstLetterAndRemoveLastLetter(String mapName) {
    return mapName.substring(0, 1).toUpperCase() + mapName.substring(1, mapName.length() - 1);
  }

  private static String[] getSplitStringByDot(String str) {
    return str.split("\\.");
  }

  private static String capitalizeFirstLetter(String str) {
    return str.substring(0, 1).toUpperCase() + str.substring(1);
  }

  private static void setNextIdsInStorage(Storage storage, String fieldName, String value) {
    try {
      Class<?> type = storage.getClass().getDeclaredField(fieldName).getType();
      storage.getClass().getMethod(
          SET_METHOD_PREFIX + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1),
          type
      ).invoke(storage, ObjectFactoryUtil.getObjectFactory(type).create(value));
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    } catch (InvocationTargetException e) {
      throw new RuntimeException(e);
    } catch (NoSuchMethodException e) {
      throw new RuntimeException(e);
    } catch (NoSuchFieldException e) {
      throw new RuntimeException(e);
    }
  }

  private static List<String> getSortedPropertyNames(Properties properties) {
    return properties.stringPropertyNames().stream().sorted().toList();
  }

  private <T> Map<Long, T> getCertainMapFromStorage(Storage storage, String mapName) {
    String capitalizedMapName = capitalizeFirstLetter(mapName);
    try {
      return (Map<Long, T>) storage.getClass().getMethod(GET_METHOD_PREFIX + capitalizedMapName).invoke(storage);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
