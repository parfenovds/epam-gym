package com.epam.log;

import java.util.List;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;
import org.apache.logging.log4j.message.SimpleMessage;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class MaskingConverterTest {

  @Test
  void testFormatLogs() {
    // Подготовка
    Configurator.setLevel("com.your.package", Level.INFO);
    Logger logger = (Logger) org.apache.logging.log4j.LogManager.getLogger("com.your.package");

    // Создание LogEvent
    LogEvent logEvent = Log4jLogEvent.newBuilder()
        .setLoggerName("com.your.package")
        .setLoggerFqcn(logger.getClass().getName())
        .setLevel(Level.INFO)
        .setMessage(new SimpleMessage("Email: test@example.com, password=pass123"))
        .build();

    // Создание MaskingConverter
    MaskingConverter converter = new MaskingConverter(List.of(new EmailMasker(), new PasswordMasker()));
    StringBuilder output = new StringBuilder();

    // Применение конвертера к LogEvent
    converter.format(logEvent, output);

    // Проверка результата
    assertEquals("Email: t**t@e*****e.com, password: ******", output.toString());
  }

  // Другие тесты
}