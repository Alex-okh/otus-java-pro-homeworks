package ru.otus.processor;

import org.junit.jupiter.api.DisplayName;

import org.junit.jupiter.api.Test;
import ru.otus.exceptions.EvenSecondException;
import ru.otus.model.Message;
import ru.otus.processor.homework.ExceptionOnEvenSecProcessor;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class EvenSecondProcessorTest {

  @Test
  @DisplayName("Обработка четных секунд")
  void EvenSecondProcessorTestEven() {
    var message = new Message.Builder(2L).field1("Field1").field2("Field2").field12("test").build();
    var processor = new ExceptionOnEvenSecProcessor(() -> LocalTime.of(1,1,20));
    assertThrows(EvenSecondException.class, () -> processor.process(message));
  }

  @Test
  @DisplayName("Обработка нечетных секунд")
  void EvenSecondProcessorTestOdd() {
    var message = new Message.Builder(2L).field1("Field1").field2("Field2").field12("test").build();
    var processor = new ExceptionOnEvenSecProcessor(() -> LocalTime.of(1,1,21));
    assertEquals(processor.process(message),message);
  }
}
