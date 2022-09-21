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
@RequiredArgsConstructor
public abstract class Component {

  @ToString.Include
  private final String name;
  private final String description;
  private Set<UsesRelation> usesRelations = new HashSet<>();
  private Set<UsesRelation> usedBy = new HashSet<>();
  private Set<Component> components = new HashSet<>();
  private Component parent;

  public abstract String getTypeDescription();

  public void uses(Component component, String comment) {
    uses(component, comment, false);
  }

  public void usesAsync(Component component, String comment) {
    uses(component, comment, true);
  }

  public void uses(Component component, String comment, boolean isAsync) {
    val usesRelation = new UsesRelation(this, component, comment, isAsync);
    usesRelations.add(usesRelation);
    component.usedBy.add(usesRelation);
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
