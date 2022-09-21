package com.github.guija.c4java.model;

public class Container extends Component {

  public Container(Component parent, String name, String description) {
    super(name, description);
    this.parent = parent;
    parent.add(this);
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
