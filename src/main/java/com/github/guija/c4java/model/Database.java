package com.github.guija.c4java.model;

public class Database extends Container {

  public Database(String name) {
    super(name, "");
  }

  @Override
  public String getTypeDescription() {
    return "Database";
  }

}
