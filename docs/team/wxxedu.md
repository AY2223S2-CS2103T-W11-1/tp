---
layout: page
title: Wang Xiuxuan's Project Portfolio Page
---

### Project: AddressBook Level 3

Wingman is an application designed to help airline managers efficiently manage the allocation of their resources.
Given below are my contributions to the project.

* **Code Contribution**:
    * My code contribution to the Wingman project can be found at
      this [link](https://nus-cs2103-ay2223s2.github.io/tp-dashboard/?search=wxxedu&breakdown=true&sort=groupTitle&sortWithin=title&since=2023-02-17&timeframe=commit&mergegroup=&groupSelect=groupByRepos&checkedFileTypes=docs~functional-code~test-code~other).

1. Adapted `AddressBook`'s model layer such that we could easily extend it.
   Including ([#21](https://github.com/AY2223S2-CS2103T-W11-1/tp/pull/21), 
   [#22](https://github.com/AY2223S2-CS2103T-W11-1/tp/pull/22)):
    * Migrated `UniquePersonsList` to `UniqueList`, a list that's unique with
      the `Item`s contained inside it.
    * Migrated `ReadOnlyAddressBook` to `ReadOnlyItemManager`, an interface that
      managers the `Item`s inside it.
    * Migrated `AddressBook` to `ItemManager`. 
    * **What they do**: allows my teammates to work on independent features without writing much duplicate code. 
    * **Justification**: same as above. 

2. Handled general implementation for
   Pilot ([#22](https://github.com/AY2223S2-CS2103T-W11-1/tp/pull/22))
    * Responsible for implementing the Plane model in Wingman along with all the basic functions related. This includes:
      adding and deleting a Plane, linking or unlinking a Plane to or from a Flight, and handling the storage and the
      parsing of a Plane.

    * **What it does:** Allows for a Plane to be properly managed in the Wingman app.
    * **Justification:** It includes all the necessary commands which a manager would need to manage the planes in their
      fleet eg. they can add a new plane, and delete planes which are out of service.

3. Rewrote the parser for Wingman, abandoning the original parsing logic.
   ([#39](https://github.com/AY2223S2-CS2103T-W11-1/tp/pull/39)) 
    * Adopted a 2-level parsing scheme that can be adapted to a multi-level
      parsing scheme easily. 
    * **What it does**: parses the command put into Wingman. 
    * **Justification**: to reduce the boilerplate in the original AddressBook
      parser.

4. Implemented a `Link` class that represents a link between two entities. 
    * **What it does**: describes a link from one entity to another. 
    * **Justification**: reduce code duplication (did not work so well).

* **Project Management**:
    * Joined in discussions about where the project is going. 

