package com.github.guija.c4java.model;

public class Container extends Component {

  public Container(String name) {
    super(name);
  }

  @Override
  public Type getType() {
    return Type.CONTAINER;
  }

}
