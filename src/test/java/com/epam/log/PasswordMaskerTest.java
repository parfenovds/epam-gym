package com.epam.log;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class PasswordMaskerTest {

  @Test
  void testMaskSinglePassword() {
    PasswordMasker masker = new PasswordMasker();
    StringBuffer stringBuffer = new StringBuffer("Some text with password: abcd1234");

    masker.mask(stringBuffer, "*");

    assertEquals("Some text with password: ******", stringBuffer.toString());
  }

  @Test
  void testMaskMultiplePasswords() {
    PasswordMasker masker = new PasswordMasker();
    StringBuffer stringBuffer = new StringBuffer("pwd: 123456, password=pass123, some text");

    masker.mask(stringBuffer, "*");

    assertEquals("password: ******, password: ******, some text", stringBuffer.toString());
  }

  @Test
  void testNoPasswordsToMask() {
    PasswordMasker masker = new PasswordMasker();
    StringBuffer stringBuffer = new StringBuffer("No passwords here");

    masker.mask(stringBuffer, "*");

    assertEquals("No passwords here", stringBuffer.toString());
  }

  @Test
  void testMaskPasswordWithDifferentCases() {
    PasswordMasker masker = new PasswordMasker();
    StringBuffer stringBuffer = new StringBuffer("Password: ABCD1234, pwd=Pass5678");

    masker.mask(stringBuffer, "*");

    assertEquals("password: ******, password: ******", stringBuffer.toString());
  }

  @Test
  void testMaskPasswordWithSpecialCharacters() {
    PasswordMasker masker = new PasswordMasker();
    StringBuffer stringBuffer = new StringBuffer("Password: @bcd!23, pwd=Pass@456");

    masker.mask(stringBuffer, "*");

    assertEquals("password: ******, password: ******", stringBuffer.toString());
  }

  @Test
  void testMaskPasswordInLongText() {
    PasswordMasker masker = new PasswordMasker();
    StringBuffer stringBuffer = new StringBuffer("Lorem ipsum dolor sit amet, password=p@ssW0rD123, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.");

    masker.mask(stringBuffer, "*");

    assertEquals("Lorem ipsum dolor sit amet, password: ******, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.", stringBuffer.toString());
  }

  @Test
  void testMaskPasswordReplacement() {
    PasswordMasker masker = new PasswordMasker();
    StringBuffer stringBuffer = new StringBuffer("Some text with password: abcd1234");

    masker.mask(stringBuffer, "*");

    assertEquals("Some text with password: ******", stringBuffer.toString());
  }

  @Test
  void testMaskPasswords() {
    PasswordMasker masker = new PasswordMasker();
    StringBuffer stringBuffer = new StringBuffer("Some text with password: abcd1234 and pwd=pass5678");
    StringBuffer expectedBuffer = new StringBuffer("Some text with password: ****** and password: ******");

    masker.mask(stringBuffer, "*");

    assertEquals(expectedBuffer.toString(), stringBuffer.toString());
  }
}
