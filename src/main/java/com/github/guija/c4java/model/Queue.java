package com.github.guija.c4java.model;

public class Queue extends Container {
  public Queue(String name) {
    super(name, "");
  }

  @Override
  public String getTypeDescription() {
    return "Queue";
  }

}
