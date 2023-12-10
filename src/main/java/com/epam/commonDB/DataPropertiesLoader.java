package com.epam.commonDB;

import com.epam.commonDB.util.ObjectFactoryUtil;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import lombok.extern.log4j.Log4j2;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
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
  public static final String PATH_TO_ENTITIES = "com.epam.entity";
  public static final String STORAGE_BEAN_NAME = "storage";
  public static final String DATA_PROPERTIES = "data.properties";
  @Value(DATA_PROPERTIES)
  private Resource dataPropertiesFile;
  @Override
  public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
    if (beanName.equals(STORAGE_BEAN_NAME)) {
      log.info("Processing bean: {}", beanName);
      List<String> classNamesInOrderToLoad = getCorrectFillOrderForMaps((Storage) bean);
      log.info("Correct fill order for maps is acquired: {}", classNamesInOrderToLoad);
      fillStorage((Storage) bean, classNamesInOrderToLoad);
      log.info("Storage is filled with data.");
    }
    return bean;
  }

  private List<String> getCorrectFillOrderForMaps(Storage bean) {
    Reflections reflections = new Reflections(PATH_TO_ENTITIES, new SubTypesScanner(false));
    List<Class<?>> classes = getClassesToOrder(reflections);
    log.info("Classes to be ordered are acquired: {}", classes);
    List<Class<?>> sortedClasses = new ArrayList<>(classes);
    orderClasses(sortedClasses, classes);
    log.info("Classes are ordered: {}", sortedClasses);
    return mapClassesToMapNames(sortedClasses);
  }

  private List<Class<?>> getClassesToOrder(Reflections reflections) {
    return reflections.getSubTypesOf(Object.class).stream()
        .filter(clazz -> !clazz.isInterface())
        .filter(clazz -> !clazz.getName().endsWith("Builder"))
        .toList();
  }

  private void orderClasses(List<Class<?>> sortedClasses, List<Class<?>> classes) {
    sortedClasses.sort(Comparator.comparingInt(o -> {
      Field[] fields = o.getDeclaredFields();
      for (Field field : fields) {
        if (classes.contains(field.getType())) {
          return classes.indexOf(field.getType());
        }
      }
      return Integer.MIN_VALUE;
    }));
  }

  private List<String> mapClassesToMapNames(List<Class<?>> sortedClasses) {
    return sortedClasses.stream().map(c -> minimizeFirstLetterAndAddPlural(c.getName().substring(c.getName().lastIndexOf('.') + 1))).toList();
  }

  private void fillStorage(Storage storage, List<String> classNamesInOrderToLoad) {
    try (InputStream inputStream = dataPropertiesFile.getInputStream()) {
      Properties properties = new Properties();
      properties.load(inputStream);
      log.info("Properties are loaded from the file: {}", properties);
      List<String> sortedPropertyNames = getSortedPropertyNames(properties, classNamesInOrderToLoad);
      log.info("Property names are sorted: {}", sortedPropertyNames);
      fillStorageWithValues(storage, sortedPropertyNames, properties);
      log.info("Data loaded successfully into Storage.");
    } catch (IOException e) {
      log.info("Failed to load data from properties file {}", DATA_PROPERTIES, e);
      throw new RuntimeException(e);
    }
  }

  private void fillStorageWithValues(Storage storage, List<String> sortedPropertyNames, Properties properties) {
    int i = 0;
    while (i < sortedPropertyNames.size()) {
      String currentPropertyName = sortedPropertyNames.get(i);
      if (currentPropertyName.startsWith(NON_MAP_PREFIX)) {
        handleNonMapProperty(storage, currentPropertyName, properties);
        ++i;
      } else {
        i = handleMapProperty(i, sortedPropertyNames, properties, storage);
      }
    }
  }

  private void handleNonMapProperty(Storage storage, String propertyName, Properties properties) {
    String value = properties.getProperty(propertyName);
    setNextIdsInStorage(storage, propertyName, value);
  }

  private void setNextIdsInStorage(Storage storage, String fieldName, String value) {
    try {
      Class<?> type = storage.getClass().getDeclaredField(fieldName).getType();
      storage.getClass().getMethod(
          SET_METHOD_PREFIX + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1),
          type
      ).invoke(storage, ObjectFactoryUtil.getObjectFactory(type).create(value));
    } catch (IllegalAccessException e) {
      log.error("Failed to set next id in storage: illegal access.", e);
      throw new RuntimeException(e);
    } catch (InvocationTargetException e) {
      log.error("Failed to set next id in storage: invocation target.", e);
      throw new RuntimeException(e);
    } catch (NoSuchMethodException e) {
      log.error("Failed to set next id in storage: no such method.", e);
      throw new RuntimeException(e);
    } catch (NoSuchFieldException e) {
      log.error("Failed to set next id in storage: no such field.", e);
      throw new RuntimeException(e);
    }
    log.info("Next id for field {} is set", fieldName);
  }

  private int handleMapProperty(int index, List<String> sortedPropertyNames, Properties properties, Storage storage) {
    String currentPropertyName = sortedPropertyNames.get(index);
    String[] splitPropertyName = getSplitStringByDot(currentPropertyName);
    String mapName = splitPropertyName[0];
    Long id = Long.parseLong(splitPropertyName[1]);
    Class<?> entityClass = getEntityClass(mapName);
    Object objectToPutInMap = getObjectToPutInMap(entityClass);

    while (index < sortedPropertyNames.size() && sortedPropertyNames.get(index).startsWith(mapName + "." + id + ".")) {
      handleMapField(storage, index, sortedPropertyNames, properties, entityClass, objectToPutInMap);
      ++index;
    }

    getCertainMapFromStorage(storage, mapName).put(id, objectToPutInMap);
    return index;
  }

  private void handleMapField(Storage storage, int index, List<String> sortedPropertyNames, Properties properties,
      Class<?> entityClass, Object objectToPutInMap) {
    String currentPropertyName = sortedPropertyNames.get(index);
    String[] splitPropertyName = getSplitStringByDot(currentPropertyName);
    String fieldName = splitPropertyName[2];
    String value = properties.getProperty(sortedPropertyNames.get(index));
    Class<?> fieldType;
    try {
      fieldType = entityClass.getDeclaredField(fieldName).getType();
      setFieldInObjectToPutInMap(objectToPutInMap, fieldName, fieldType, value);
    } catch (NoSuchFieldException e) {
      mapNestedField(storage, entityClass, objectToPutInMap, fieldName, value);
    }
  }

  private void mapNestedField(Storage storage, Class<?> entityClass, Object objectToPutInMap, String fieldName, String value) {
    fieldName = fieldName.substring(0, fieldName.length() - 2);
    Class<?> fieldType;
    try {
      fieldType = entityClass.getDeclaredField(fieldName).getType();
    } catch (NoSuchFieldException ex) {
      throw new RuntimeException(ex);
    }
    String nestedObjectMapName = minimizeFirstLetterAndAddPlural(getFieldNameWithoutPackage(fieldType));
    Object o = getCertainMapFromStorage(storage, nestedObjectMapName).get(Long.parseLong(value));
    setFieldInObjectWithExistingObjectToPutInMap(objectToPutInMap, fieldName, fieldType, o);
  }

  private String getFieldNameWithoutPackage(Class<?> fieldType) {
    return fieldType.getName().substring(fieldType.getName().lastIndexOf('.') + 1);
  }

  private Class<?> getEntityClass(String mapName) {
    Class<?> clazz;
    try {
      clazz = Class.forName(PATH_TO_ENTITIES + "." + capitalizeFirstLetterAndRemoveLastLetter(mapName));
    } catch (ClassNotFoundException e) {
      log.error("Failed to get entity class.", e);
      throw new RuntimeException(e);
    }
    log.info("Entity class is successfully acquired: {}", clazz);
    return clazz;
  }

  private Object getObjectToPutInMap(Class<?> clazz) {
    Object objectToPutInMap;
    try {
      objectToPutInMap = clazz.getDeclaredConstructor().newInstance();
    } catch (InstantiationException e) {
      log.error("Failed to create object to put in map.", e);
      throw new RuntimeException(e);
    } catch (IllegalAccessException e) {
      log.error("Failed to create object to put in map: illegal access.", e);
      throw new RuntimeException(e);
    } catch (InvocationTargetException e) {
      log.error("Failed to create object to put in map: invocation target.", e);
      throw new RuntimeException(e);
    } catch (NoSuchMethodException e) {
      log.error("Failed to create object to put in map: no constructor.", e);
      throw new RuntimeException(e);
    }
    log.info("Object to put in map is created: {}", objectToPutInMap);
    return objectToPutInMap;
  }

  private void setFieldInObjectWithExistingObjectToPutInMap(Object objectToPutInMap, String fieldName, Class<?> fieldType, Object o) {
    try {
      objectToPutInMap.getClass().getMethod(SET_METHOD_PREFIX + capitalizeFirstLetter(fieldName), fieldType
      ).invoke(objectToPutInMap, o);
    } catch (IllegalAccessException e) {
      log.error("Failed to set field in object to put in map: illegal access.", e);
      throw new RuntimeException(e);
    } catch (InvocationTargetException e) {
      log.error("Failed to set field in object to put in map: invocation target.", e);
      throw new RuntimeException(e);
    } catch (NoSuchMethodException e) {
      log.error("Failed to set field in object to put in map: no such method.", e);
      throw new RuntimeException(e);
    }
    log.info("Field in object to put in map {} is set {} with object {}", objectToPutInMap, fieldType, o);
  }

  private void setFieldInObjectToPutInMap(Object objectToPutInMap, String fieldName, Class<?> fieldType, String value) {
    try {
      objectToPutInMap.getClass().getMethod(SET_METHOD_PREFIX + capitalizeFirstLetter(fieldName), fieldType
      ).invoke(objectToPutInMap, ObjectFactoryUtil.getObjectFactory(fieldType).create(value));
    } catch (IllegalAccessException e) {
      log.error("Failed to set field in object to put in map: illegal access.", e);
      throw new RuntimeException(e);
    } catch (InvocationTargetException e) {
      log.error("Failed to set field in object to put in map: invocation target.", e);
      throw new RuntimeException(e);
    } catch (NoSuchMethodException e) {
      log.error("Failed to set field in object to put in map: no such method.", e);
      throw new RuntimeException(e);
    }
    log.info("Field in object to put in map {} is set: {}={}", objectToPutInMap, fieldName, value);
  }

  private String capitalizeFirstLetterAndRemoveLastLetter(String mapName) {
    return mapName.substring(0, 1).toUpperCase() + mapName.substring(1, mapName.length() - 1);
  }

  private String[] getSplitStringByDot(String str) {
    return str.split("\\.");
  }

  private String capitalizeFirstLetter(String str) {
    return str.substring(0, 1).toUpperCase() + str.substring(1);
  }
  private String minimizeFirstLetterAndAddPlural(String str) {
    return str.substring(0, 1).toLowerCase() + str.substring(1) + "s";
  }

  private List<String> getSortedPropertyNames(Properties properties, List<String> classNamesInOrderToLoad) {
    Comparator<String> prefixComparator = Comparator.comparing(s -> s.split("\\.")[0], Comparator.comparingInt(classNamesInOrderToLoad::indexOf));
    Comparator<String> restComparator = Comparator.comparing(s -> s.split("\\.").length > 1 ? s.split("\\.")[1] : s.split("\\.")[0]);
    Comparator<String> customComparator = prefixComparator.thenComparing(restComparator);
    return properties.stringPropertyNames()
        .stream()
        .sorted(customComparator)
        .toList();
  }

  private <T> Map<Long, T> getCertainMapFromStorage(Storage storage, String mapName) {
    String capitalizedMapName = capitalizeFirstLetter(mapName);
    try {
      Map<Long, T> certainMap = (Map<Long, T>) storage.getClass().getMethod(GET_METHOD_PREFIX + capitalizedMapName).invoke(storage);
      log.info("Map {} is successfully acquired from the storage.", mapName);
      return certainMap;
    } catch (Exception e) {
      log.error("Failed to get map from storage.", e);
      throw new RuntimeException(e);
    }
  }
}
