package com.rodmontiel.ec.ex;

public class GenericExceptionHandler extends RuntimeException {
  private static final long serialVersionUID = 3L;

  public GenericExceptionHandler(int code) {
    this.code = code;
  }

  public int code;
}
