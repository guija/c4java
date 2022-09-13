package com.github.guija.c4java.model;

public class Service extends Component {

  public Service(String name) {
    super(name);
  }

  @Override
  public Type getType() {
    return Type.SERVICE;
  }

}
