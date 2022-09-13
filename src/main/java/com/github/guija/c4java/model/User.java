package com.github.guija.c4java.model;

public class User extends Component {

  public User(String name) {
    super(name);
  }

  @Override
  public String getTypeDescription() {
    return "User";
  }

  @Override
  public Type getType() {
    return Type.USER;
  }

}
