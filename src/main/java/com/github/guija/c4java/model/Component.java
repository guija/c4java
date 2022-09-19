package com.github.guija.c4java.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.val;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Set;

@Getter
@ToString(onlyExplicitlyIncluded = true)
public abstract class Component {

  @ToString.Include
  private final String name;
  private Set<Component> uses = new HashSet<>();
  private Set<Component> usedBy = new HashSet<>();
  private Set<Component> components = new HashSet<>();
  private Component parent;

  public Component(String name) {
    this.name = name;
  }

  public abstract String getTypeDescription();

  public void uses(Component component) {
    uses.add(component);
    component.usedBy.add(this);
  }

  public void add(Component... components) {
    for (val component : components) {
      assert component.parent == null : "Component already has a parent. Cannot be reassigned";
      this.components.add(component);
      component.parent = this;
    }
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
    return name.replace(" ", "_");
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

  @RequiredArgsConstructor
  @Getter
  public enum Type {

    CONTAINER("Container"),
    SYSTEM("Software System"),
    USER("User"),
    SERVICE("Service");

    private final String description;

  }

}
