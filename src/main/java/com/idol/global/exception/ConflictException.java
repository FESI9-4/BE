package com.idol.global.exception;

import lombok.Getter;

@Getter
public class ConflictException extends RuntimeException {

  private final ExceptionMessage exceptionMessage;

  public ConflictException(ExceptionMessage exceptionMessage) {
    super(exceptionMessage.getMessage());
    this.exceptionMessage = exceptionMessage;
  }

}
