package com.github.guija.c4java.model;

public class Container extends Component {

  public Container(String name, String description) {
    super(name, description);
  }

  @Override
  public String getTypeDescription() {
    return "Container";
  }

  @Override
  public Type getType() {
    return Type.CONTAINER;
  }

}
