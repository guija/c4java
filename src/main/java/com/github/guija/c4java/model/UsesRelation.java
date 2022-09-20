package com.github.guija.c4java.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UsesRelation {

  private final Component user;
  private final Component used;
  private final String comment;
  private final boolean async;

}
