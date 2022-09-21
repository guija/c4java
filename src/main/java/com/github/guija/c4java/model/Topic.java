package com.github.guija.c4java.model;

public class Topic extends Container {

  public Topic(Component parent, String name) {
    super(parent, name, "");
  }

  @Override
  public String getTypeDescription() {
    return "Topic";
  }
  
}
