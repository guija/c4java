package com.github.guija.c4java.model;

import lombok.Getter;

@Getter
public class Sys extends Component {

  private final boolean external;

  public Sys(String name, String description) {
    this(name, description, false);
  }

  @Override
  public String getTypeDescription() {
    return "Software System";
  }

  public Sys(String name, String description, boolean external) {
    super(name, description);
    this.external = external;
  }

  @Override
  public Type getType() {
    return Type.SYSTEM;
  }

}
