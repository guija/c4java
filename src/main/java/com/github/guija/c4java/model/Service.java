package com.github.guija.c4java.model;

public class Service extends Component {

  public Service(String name, String description) {
    super(name, description);
  }

  @Override
  public String getTypeDescription() {
    return "Service";
  }

  @Override
  public Type getType() {
    return Type.SERVICE;
  }

}
