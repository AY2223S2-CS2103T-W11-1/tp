---
layout: page
title: Jishnu's Project Portfolio Page
---

# Project: Wingman
<br>

## Overview:
Wingman is an application designed to help airline managers efficiently manage the allocation of their resources.
<br>

## Summary of Contributions
Given below are my contributions to the project.

### Code contributed:
[Link to code dashboard](https://nus-cs2103-ay2223s2.github.io/tp-dashboard/?search=jishnu28&breakdown=true&sort=groupTitle&sortWithin=title&since=2023-02-17&timeframe=commit&mergegroup=&groupSelect=groupByRepos&checkedFileTypes=docs~functional-code~test-code~other)

### Enhancements implemented:
1. Implemented the **flight class** to model the flight resource 
   - This class is used to model the flight object in a way suitable for use in Wingman,
     from a storage and interaction perspective. Implemented in a similar way to AB3's person class,
     with separate files in Ui, logic, model, and storage packages.
   - This also included the implementation of the add and delete feature for the flight objects.
   
2. **Unlink Flight** feature for all resource types (crew, pilot, plane, location)
   - This was implemented in a similar way to the `linkflight` feature, with the main difference being that the
     `put/putRevolve` method call was replaced with a `delete` method call.
   - Aside from this, the implementation was primarily in the logic layer,
     with the storage layer being updated to ensure the links are stored in their updated states,
     under the correct classes.
   - The benefit of this feature is that it allows the user to remove a link between a resource and a flight
     when they have made a wrong link or when the link is no longer relevant.
   - This implementation was also used to guide the subsequent implementation of the `unlinklocation` feature.
   
3. **Link Flight** feature for the following resource types (crew, plane, location)
   - This was implemented in the same way that the `linkflight` feature was implemented for the pilot class,
     making use of a map to store the shape and contents of the links, in the respective classes.
   - The logic layer was updated to enable this feature and the model layer was updated to store the link as
     an attribute in the corresponding classes. The storage layer was also updated to allow for the link
     attributes to be saved accurately.
   - The benefit of this feature is that it allows the user to link a resource to a flight, which is useful
     for the user to keep track of the resources that are allocated to a flight.
   - This implementation was also used to guide the subsequent implementation of the `linklocation` feature.

### Contributions to the UG:
- Created the general structure of the UG to work on together with the group
- Drafted a format for the description of each feature and created an example version for each
- Created a glossary of key terms and added definitions for 'resource', 'resource entity' and 'prefix'
- Created descriptions for 'How to use this guide', 'Getting started' and 'Modal Editing' sections

### Contributions to the DG:
- Created the general structure of the DG to work on together with the group
- Edited the UML diagrams in the 'Architecture' section to reflect the changes from AB3 to Wingman
- Created the Sequence diagram in the 'Unlinking XYZ from a flight' section
- Drafted a format for the description of each feature in the DG
- Created the description for the 'Unlinking XYZ from a flight' feature

### Contributions to team-based-tasks:
- Recording of demo videos
- Formatting of the DG and UG
- Creating UML diagrams for the general section in the DG
- Contribute to meeting minutes during weekly meetings

### Review/mentoring contributions:
- Some of the PRs I've reviewed and merged:
  - [Fix issues raised in PED](https://github.com/AY2223S2-CS2103T-W11-1/tp/pull/224)
  - [Change zero-based index to one-based index](https://github.com/AY2223S2-CS2103T-W11-1/tp/pull/138)
  - [Link Implementation](https://github.com/AY2223S2-CS2103T-W11-1/tp/pull/81)


### Contributions beyond the project team:
- Found and reported [bugs](https://github.com/jishnu28/ped/tree/main/files) in other teams' project during PE dry run
