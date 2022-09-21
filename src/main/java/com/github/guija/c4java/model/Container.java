package com.github.guija.c4java.model;

public class Container extends Component {

  public Container(Component parent, String name, String description) {
    super(name, description);
    parent.add(this);
    this.parent = parent;
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
