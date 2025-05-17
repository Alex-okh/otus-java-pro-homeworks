package ru.otus.processor.homework;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.exceptions.EvenSecondException;
import ru.otus.model.Message;
import ru.otus.processor.Processor;

import java.time.LocalTime;

public class ExceptionOnEvenSecProcessor implements Processor {
  TimeProvider timeProvider;
  private static final Logger logger = LoggerFactory.getLogger(ExceptionOnEvenSecProcessor.class);

  public ExceptionOnEvenSecProcessor(TimeProvider timeProvider) {
    this.timeProvider = timeProvider;
  }

  @Override
  public Message process(Message message) {
    LocalTime time = timeProvider.getTime();
    logger.info("Current time: {}", time);
    if (time.getSecond() % 2 == 0 ) {
    logger.info("Even second - throwing an exception");
      throw new EvenSecondException();
  }

    return message;
  }
}
