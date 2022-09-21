package com.github.guija.c4java.model;

public class User extends Component {

  public User(String name, String description) {
    super(name, description);
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
