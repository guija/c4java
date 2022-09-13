package com.github.guija.c4java.builder;

import com.github.guija.c4java.model.Component;
import com.github.guija.c4java.model.Project;
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
        dot += String.format("%s -> %s\n", system.getId(), targetSystem.getId());
      }
    }
    return dot;
  }

  // public String generateContainerViewDot(Project project, Sys system) {
  //   // Gehe ueber System und Container und finde alle anderen Systeme oder container die diese Systeme / Container verwenden.
  //
  //   for (val used : system.getUses()) {
  //     val targetSystem = u
  //   }
  //
  //   return "";
  // }

}
