package com.github.guija.c4java.model;

public class Topic extends Container {
  public Topic(String name) {
    super(name, "");
  }

  @Override
  public String getTypeDescription() {
    return "Topic";
  }
  
}
