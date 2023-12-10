package com.epam.log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.RegExUtils;

public class EmailMasker implements LogMasker {
  private final Pattern emailFindPattern = Pattern.compile("([a-zA-Z0-9._-]+@[a-zA-Z0-9._-]+\\.[a-zA-Z0-9_-]+)");
  private final Pattern emailMaskPattern = Pattern.compile("(?<=.)[^@](?=[^@]*?[^@]@)|(?:(?<=@.)|(?!^)\\G(?=[^@]*$)).(?=.*[^@]\\.)");

  public void mask(StringBuffer stringBuffer, String maskChar) {
    Matcher matcher =  emailFindPattern.matcher(stringBuffer);
    if (matcher.find()) {
      String email = matcher.group(1);
      String masked = RegExUtils.replaceAll(email, emailMaskPattern, maskChar);
      int idx = stringBuffer.indexOf(email);
      stringBuffer.replace(idx, idx + email.length(), masked);
    }
  }
}