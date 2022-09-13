package com.github.guija.c4java.model;

import lombok.Getter;
import lombok.val;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Set;

@Getter
public abstract class Component {

  private final String name;
  private Set<Component> uses = new HashSet<>();
  private Set<Component> usedBy = new HashSet<>();
  private Set<Component> components = new HashSet<>();
  private Component parent;

  public Component(String name) {
    this.name = name;
  }

  public void uses(Component component) {
    uses.add(component);
    component.usedBy.add(this);
  }

  public void add(Component component) {
    assert parent == null : "Component already has a parent. Cannot be reassigned";
    components.add(component);
    component.parent = this;
  }

  public Set<Component> getAllChildComponentsWithRoot() {
    val components = new HashSet<Component>();
    val queue = new ArrayDeque<Component>();
    queue.add(this);
    while(!queue.isEmpty()) {
      val component = queue.pop();
      components.add(component);
      queue.addAll(component.getComponents());
    }
    return components;
  }

  public String getId() {
    return name;
  }

  public Component getAncestorWithType(Type type) {
    if (getType() == type) {
      return this;
    } else if (parent == null) {
      throw new IllegalStateException("Parent with type not found");
    } else {
      return parent.getAncestorWithType(type);
    }
  }

  public abstract Type getType();

  public enum Type {
    CONTAINER,
    SYSTEM,
    USER,
    SERVICE
  }

}
