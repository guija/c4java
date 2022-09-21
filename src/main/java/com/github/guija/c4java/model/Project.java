package com.github.guija.c4java.model;

import lombok.Data;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class Project {

  private String name;

  private Set<Sys> systems = new HashSet<>();

  public void add(Sys system) {
    this.systems.add(system);
  }

  public void addAll(Sys... systems) {
    this.systems.addAll(Arrays.asList(systems));
  }

  public Set<Component> getComponents() {
    return systems.stream().flatMap(sys -> sys.getAllChildComponentsWithRoot().stream()).collect(Collectors.toSet());
  }

}
