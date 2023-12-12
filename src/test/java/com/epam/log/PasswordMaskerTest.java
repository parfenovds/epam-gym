package com.epam.log;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class PasswordMaskerTest {

  @Test
  void testMaskSinglePassword() {
    // Подготовка данных
    PasswordMasker masker = new PasswordMasker();
    StringBuffer stringBuffer = new StringBuffer("Some text with password: abcd1234");

    // Вызов метода mask()
    masker.mask(stringBuffer, "*");

    // Проверка результатов
    assertEquals("Some text with password: ******", stringBuffer.toString());
  }

  @Test
  void testMaskMultiplePasswords() {
    // Подготовка данных
    PasswordMasker masker = new PasswordMasker();
    StringBuffer stringBuffer = new StringBuffer("pwd: 123456, password=pass123, some text");

    // Вызов метода mask()
    masker.mask(stringBuffer, "*");

    // Проверка результатов
    assertEquals("password: ******, password: ******, some text", stringBuffer.toString());
  }

  @Test
  void testNoPasswordsToMask() {
    // Подготовка данных
    PasswordMasker masker = new PasswordMasker();
    StringBuffer stringBuffer = new StringBuffer("No passwords here");

    // Вызов метода mask()
    masker.mask(stringBuffer, "*");

    // Проверка результатов
    assertEquals("No passwords here", stringBuffer.toString());
  }

  @Test
  void testMaskPasswordWithDifferentCases() {
    // Подготовка данных
    PasswordMasker masker = new PasswordMasker();
    StringBuffer stringBuffer = new StringBuffer("Password: ABCD1234, pwd=Pass5678");

    // Вызов метода mask()
    masker.mask(stringBuffer, "*");

    // Проверка результатов
    assertEquals("password: ******, password: ******", stringBuffer.toString());
  }

  @Test
  void testMaskPasswordWithSpecialCharacters() {
    // Подготовка данных
    PasswordMasker masker = new PasswordMasker();
    StringBuffer stringBuffer = new StringBuffer("Password: @bcd!23, pwd=Pass@456");

    // Вызов метода mask()
    masker.mask(stringBuffer, "*");

    // Проверка результатов
    assertEquals("password: ******, password: ******", stringBuffer.toString());
  }

  @Test
  void testMaskPasswordInLongText() {
    // Подготовка данных
    PasswordMasker masker = new PasswordMasker();
    StringBuffer stringBuffer = new StringBuffer("Lorem ipsum dolor sit amet, password=p@ssW0rD123, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.");

    // Вызов метода mask()
    masker.mask(stringBuffer, "*");

    // Проверка результатов
    assertEquals("Lorem ipsum dolor sit amet, password: ******, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.", stringBuffer.toString());
  }

  @Test
  void testMaskPasswordReplacement() {
    // Подготовка данных
    PasswordMasker masker = new PasswordMasker();
    StringBuffer stringBuffer = new StringBuffer("Some text with password: abcd1234");

    // Вызов метода mask()
    masker.mask(stringBuffer, "*");

    // Проверка результатов
    assertEquals("Some text with password: ******", stringBuffer.toString());
  }

  @Test
  void testMaskPasswords() {
    // Подготовка данных
    PasswordMasker masker = new PasswordMasker();
    StringBuffer stringBuffer = new StringBuffer("Some text with password: abcd1234 and pwd=pass5678");
    StringBuffer expectedBuffer = new StringBuffer("Some text with password: ****** and password: ******");

    // Вызов метода mask()
    masker.mask(stringBuffer, "*");

    // Проверка результатов
    assertEquals(expectedBuffer.toString(), stringBuffer.toString());
  }

}