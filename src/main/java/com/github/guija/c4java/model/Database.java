package com.github.guija.c4java.model;

public class Database extends Container {

  public Database(Component parent, String name) {
    super(parent, name, "");
  }

  @Override
  public String getTypeDescription() {
    return "Database";
  }

}
