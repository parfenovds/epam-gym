package com.epam.log;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class EmailMaskerTest {

  @Test
  public void testBasicEmailMasking() {
    String originalString = "Send an email to john.doe@example.com";
    StringBuffer stringBuffer = new StringBuffer(originalString);

    EmailMasker emailMasker = new EmailMasker();
    emailMasker.mask(stringBuffer, "*");

    String expectedString = "Send an email to j******e@e*****e.com";
    assertEquals(expectedString, stringBuffer.toString(), "Emails should be masked");
  }

  @Test
  public void testStringWithoutEmail() {
    String originalString = "This string does not contain an email address";
    StringBuffer stringBuffer = new StringBuffer(originalString);

    EmailMasker emailMasker = new EmailMasker();
    emailMasker.mask(stringBuffer, "*");

    assertEquals(originalString, stringBuffer.toString(), "String without email should remain unchanged");
  }
  @Test
  public void testEmailAtStartOfString() {
    String originalString = "john.doe@example.com is the email address";
    StringBuffer stringBuffer = new StringBuffer(originalString);

    EmailMasker emailMasker = new EmailMasker();
    emailMasker.mask(stringBuffer, "*");

    String expectedString = "j******e@e*****e.com is the email address";
    assertEquals(expectedString, stringBuffer.toString(), "Email at start of string should be masked");
  }

  @Test
  public void testEmailAtEndOfString() {
    String originalString = "The email address is jane.smith@example.com";
    StringBuffer stringBuffer = new StringBuffer(originalString);

    EmailMasker emailMasker = new EmailMasker();
    emailMasker.mask(stringBuffer, "*");

    String expectedString = "The email address is j********h@e*****e.com";
    assertEquals(expectedString, stringBuffer.toString(), "Email at end of string should be masked");
  }
}