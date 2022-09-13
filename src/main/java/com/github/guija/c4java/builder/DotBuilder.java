package com.github.guija.c4java.builder;

import com.github.guija.c4java.model.Component;
import com.github.guija.c4java.model.Project;
import com.github.guija.c4java.model.Sys;
import lombok.RequiredArgsConstructor;
import lombok.val;

@RequiredArgsConstructor
public class DotBuilder {

  public String generateSystemViewDot(Project project) {
    val viewType = Component.Type.SYSTEM;
    String dot = "";
    for (val system : project.getSystems()) {
      for (val used : system.getUses()) {
        val targetSystem = used.getAncestorWithType(viewType);
        dot = addEdge(dot, system, targetSystem);
      }
    }
    return dot;
  }

  public String generateContainerViewDot(Project project, Sys system) {
    // Gehe ueber System und Container und finde alle anderen Systeme oder container die diese Systeme / Container verwenden.
    String dot = "";
    for (val container : system.getComponents()) {
      for (val used : container.getUses()) {
        // If the used component is from another system then we want to reference the other system, not the container of that system.
        val usedSystem = used.getAncestorWithType(Component.Type.SYSTEM);
        if (usedSystem != system) {
          dot = addEdge(dot, container, usedSystem);
        } else {
          dot = addEdge(dot, container, used);
        }
      }
    }
    return dot;
  }

  private String addEdge(String dot, Component from, Component to) {
    String newDot = new String(dot);
    newDot += String.format("%s -> %s\n", from.getId(), to.getId());
    return newDot;
  }

}
