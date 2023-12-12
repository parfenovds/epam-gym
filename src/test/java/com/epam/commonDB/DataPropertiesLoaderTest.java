package com.epam.commonDB;

import com.epam.entity.Trainee;
import com.epam.entity.Trainer;
import com.epam.entity.Training;
import com.epam.entity.TrainingType;
import com.epam.entity.User;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.reflections.Reflections;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {DataPropertiesLoader.class})
public class DataPropertiesLoaderTest {

  private DataPropertiesLoader dataPropertiesLoader;

  @Mock
  private Storage storage;

  @Mock
  private Resource resource;

  @BeforeEach
  void setUp() throws IOException, NoSuchFieldException, IllegalAccessException {
    MockitoAnnotations.openMocks(this);
    dataPropertiesLoader = new DataPropertiesLoader();
    setField(dataPropertiesLoader, "dataPropertiesFile", resource);
    InputStream inputStream = getClass().getClassLoader().getResourceAsStream("data.properties");
    when(resource.getInputStream()).thenReturn(inputStream);
  }

  @Test
  void testPostProcessBeforeInitialization_WithStorageBean() {
    when(resource.getFilename()).thenReturn("data.properties");
    dataPropertiesLoader.postProcessBeforeInitialization(storage, "storage");
  }

  @Test
  void testPostProcessBeforeInitialization_WithoutStorageBean() {
    Object someOtherBean = new Object();
    Object processedBean = dataPropertiesLoader.postProcessBeforeInitialization(someOtherBean, "someOtherBean");
    assertSame(someOtherBean, processedBean);
  }

  @Test
  public void testGetCorrectFillOrderForMaps() throws Exception {

    Method method = DataPropertiesLoader.class.getDeclaredMethod("getCorrectFillOrderForMaps");
    method.setAccessible(true);


    List<String> result = (List<String>) method.invoke(dataPropertiesLoader);

    List<String> expected = List.of("traineeTests", "users", "trainingTests", "userTests", "trainerTests", "trainingTypes", "trainings", "trainingTypeTests", "trainers", "trainees");
    assertEquals(expected, result);
  }
  @Test
  public void testGetClassesToOrder() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    DataPropertiesLoader dataPropertiesLoader = new DataPropertiesLoader();

    Method method = DataPropertiesLoader.class.getDeclaredMethod("getClassesToOrder", Reflections.class);
    method.setAccessible(true);

    Class<?> class1 = Trainee.class;
    Class<?> class2 = Trainer.class;
    Class<?> class3 = Training.class;
    Class<?> class4 = TrainingType.class;
    Class<?> class5 = User.class;

    Reflections reflectionsMock = Mockito.mock(Reflections.class);
    Mockito.when(reflectionsMock.getSubTypesOf(Object.class))
        .thenReturn(Set.of(class1, class2, class3, class4, class5));

    List<Class<?>> result = (List<Class<?>>) method.invoke(dataPropertiesLoader, reflectionsMock);

    List<Class<?>> expected = Arrays.asList(class1, class2, class3, class4, class5);
    assertTrue(result.containsAll(expected) && expected.containsAll(result));
    assertEquals(expected.size(), result.size());
  }

  @Test
  public void testOrderClasses() throws Exception {
    DataPropertiesLoader dataPropertiesLoader = new DataPropertiesLoader();

    java.lang.reflect.Method method = DataPropertiesLoader.class.getDeclaredMethod("orderClasses", List.class, List.class);
    method.setAccessible(true);

    Class<?> class1 = Trainee.class;
    Class<?> class2 = Trainer.class;
    Class<?> class3 = Training.class;
    Class<?> class4 = TrainingType.class;
    Class<?> class5 = User.class;

    List<Class<?>> unsortedClasses = Arrays.asList(class3, class2, class5, class4, class1);
    List<Class<?>> expectedSortedClasses = Arrays.asList(class3, class5, class4, class2, class1);

    method.invoke(dataPropertiesLoader, unsortedClasses, Arrays.asList(class3, class2, class5, class4, class1));

    assertEquals(expectedSortedClasses, unsortedClasses);
  }

  @Test
  public void testMapClassesToMapNames() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    DataPropertiesLoader dataPropertiesLoader = new DataPropertiesLoader();

    List<Class<?>> sortedClasses = Arrays.asList(
        Trainee.class,
        Trainer.class,
        Training.class,
        TrainingType.class,
        User.class
    );

    List<String> expectedMapNames = Arrays.asList(
        "trainees",
        "trainers",
        "trainings",
        "trainingTypes",
        "users"
    );

    Method method = DataPropertiesLoader.class.getDeclaredMethod("mapClassesToMapNames", List.class);
    method.setAccessible(true);

    List<String> result = (List<String>) method.invoke(dataPropertiesLoader, sortedClasses);

    assertEquals(expectedMapNames, result);
  }

  @Test
  void testSetNextIdsInStorage_InvocationTargetException() throws NoSuchMethodException {
    DataPropertiesLoader dataPropertiesLoader = new DataPropertiesLoader();
    Storage storage = new Storage();

    Method method = DataPropertiesLoader.class.getDeclaredMethod(
        "setNextIdsInStorage", Storage.class, String.class, String.class);
    method.setAccessible(true);

    assertThrows(InvocationTargetException.class, () ->
        method.invoke(dataPropertiesLoader, storage, "fieldName", "value"));
  }
}
