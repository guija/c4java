# c4java

![maven](https://github.com/guija/c4java/actions/workflows/maven.yml/badge.svg)

Library for generating C4 Architecture Diagrams using the programming language java.

Further documentation will be added.

## TODO

- container view: external system that uses this system

- Documentation
  - Examples
  - how to integrate (maven etc.)
  - Highlight / define architecture as code approach
  - Reference C4 conceptual model
- Think about customizations like custom types, custom shapes etc.
- Insert line breaks in description of components manually.
- Generate container diagram for every system.
- Generate system diagram for whole project.
- Write `plantuml` generator.
- Write tests:
    - There is an edge between two systems if a container within the system calls a container within another system
    - There are no edges in the system view from system A to system A (self references are not helpful).
- style tests for edges / nodes
- add tests for async / sync (dotted vs dashed)
- test comments on edges are being used as labels