package ru.otus.processor.homework;

import ru.otus.exceptions.EvenSecondException;
import ru.otus.model.Message;
import ru.otus.processor.Processor;

public class ExceptionOnEvenSecProcessor implements Processor {
  TimeProvider timeProvider;

  public ExceptionOnEvenSecProcessor(TimeProvider timeProvider) {
    this.timeProvider = timeProvider;
  }

  @Override
  public Message process(Message message) {
  if (timeProvider.getTime().getSecond() % 2 == 0 ) {
    throw new EvenSecondException();
  }
    return message;
  }
}
