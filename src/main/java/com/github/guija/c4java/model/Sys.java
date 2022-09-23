package com.github.guija.c4java.model;

import lombok.Getter;

@Getter
public class Sys extends Component {

  private final boolean external;

  public Sys(Project project, String name, String description) {
    this(project, name, description, false);
  }

  @Override
  public String getTypeDescription() {
    return "Software System";
  }

  public Sys(Project project, String name, String description, boolean external) {
    super(name, description);
    this.external = external;
    project.getSystems().add(this);
  }

  @Override
  public Type getType() {
    return Type.SYSTEM;
  }

}
