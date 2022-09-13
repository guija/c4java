package com.github.guija.c4java.model;

import lombok.Getter;

@Getter
public class Sys extends Component {

  private final boolean external;

  public Sys(String name) {
    this(name, false);
  }

  public Sys(String name, boolean external) {
    super(name);
    this.external = external;
  }

  @Override
  public Type getType() {
    return Type.SYSTEM;
  }

}
