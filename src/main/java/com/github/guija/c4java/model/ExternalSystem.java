package com.github.guija.c4java.model;

public class ExternalSystem extends Sys {

  @Override
  public String getTypeDescription() {
    return "External Software System";
  }

  public ExternalSystem(String name, String description) {
    super(name, description, true);
  }

}
