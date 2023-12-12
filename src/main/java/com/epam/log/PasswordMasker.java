package com.epam.log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordMasker implements LogMasker {
  private final Pattern passwordFindPattern = Pattern.compile("(?i)((?:password|pwd)(?::|=)(?:\\s*)[A-Za-z0-9@$!%*?&.;<>]+)");

  @Override
  public void mask(StringBuffer stringBuffer, String maskChar) {
    Matcher matcher = passwordFindPattern.matcher(stringBuffer);
    StringBuilder result = new StringBuilder();

    while (matcher.find()) {
      matcher.appendReplacement(result, "password: ******");
    }

    matcher.appendTail(result);
    stringBuffer.setLength(0);
    stringBuffer.append(result);
  }
}