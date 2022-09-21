package com.github.guija.c4java.model;

public class Queue extends Container {

  public Queue(Component parent, String name) {
    super(parent, name, "");
  }

  @Override
  public String getTypeDescription() {
    return "Queue";
  }

}
