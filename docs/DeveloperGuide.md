# Developer Guide

## Table of Contents
- **[Acknowledgements](#acknowledgements)**
- **[Setting up, getting started](#setting-up-getting-started)**
- **[Architecture](#architecture)**
    * [UI Component](#ui-component)
    * [Logic Component](#logic-component)
    * [Model Component](#model-component)
    * [Storage Component](#storage-component)
- **[Implementation](#implementation)**
    * [Adding locations](#adding-locations--seq-diagram-focussed-on-ui-layer----ai-bo)
        * [Motivation](#motivation-for-unique-id)
        * [Implementation](#implementation-of-unique-id)
- **[Appendix: Requirements](#appendix--requirements)**

## Acknowledgements
Coming soon

## Setting up, Getting started
Coming soon

## Architecture
<img src="images/WingmanArchitectureDiagram.png" width="276" alt="Architecture diagram">

Description coming soon


### UI Component
<img src="images/WingmanUiClassDiagram.png" width="1021" alt="UI Class diagram">

Description coming soon


### Logic Component
<img src="images/WingmanLogicClassDiagram.png" width="608" alt="UI Class diagram">

Description coming soon


### Model Component
<img src="images/WingmanModelClassDiagram.png" width="478" alt="UI Class diagram">

Description coming soon


### Storage Component
<img src="images/WingmanStorageClassDiagram.png" width="616" alt="UI Class diagram">

Description coming soon

### Overall Sequence
<img src="images/WingmanArchitectureSequenceDiagram.png" width="1005" alt="Architecture Sequence diagram">

This sequence diagram provides an overview of the different layers involved in executing an example command.
The example used here is the command to add a plane of the following specifications - (Model: A380, Age: 12).
This sequence is similar for most commands and the subsequent descriptions of Wingman's features include more detailed
diagrams to depict the processes at each layer in greater detail.

### Example Activity Diagram
<img src="images/WingmanLinkFlightActivity.png" width="231" alt="Link Flight activity diagram">

Description coming soon

## Implementation

### Adding XYZ (seq. diagram focussed on UI layer) - Ai Bo

### Deleting XYZ (seq. diagram focussed on Logic layer) - Celeste Cheah

### Linking XYZ to a flight (seq. diagram focussed on Model layer) 

**Rationale**

The rationale behind creating a `Link` class is that only such affords us the ability to describe a relationship between two objects in away that's safe. 

**How is this feature implemented?**

This linking feature is implemented a very similar way (if not the same) for unlinking crews, locations, pilots, and planes from flights. Hence, in this description the general term XYZ is used instead.

### Unlinking XYZ from a flight

**How is this feature implemented?**

This unlinking feature is implemented in the same way for unlinking crews, locations, pilots and planes from flights.
Hence, in this description the general term XYZ is used instead.

This feature is enabled by the following classes in particular:
- `UnlinkXYZCommand` - The command that unlinks a crew from a flight
- `UnlinkXYZCommandFactory` - The factory class that creates an {@code UnlinkCrewCommand}
- `Link` - The class defining a link to a target
- `Flight` - The class defining a flight object in Wingman

When a user enters the command:
> unlink /XYZprefix {XYZ identifier} /fl {flight identifier}

this command is passed from the UI layer to the logic layer similar to the way described above, in the
'Adding XYZ' section.

At the logic layer, while the sequence of method calls is similar to what is described in the 'Deleting XYZ' section,
the `UnlinkXYZCommand.execute(model)` method is called instead of the `DeleteXYZCommand.execute(model)` method.

This method then calls the `flight.XYZLink.delete(entry.getKey(), entry.getValue())` method where `entry` refers to
one key-value pairing in a mapping of FlightXYZType keys to XYZ values.
At this point, the process is at the model layer and continues with method calls similar to the ones described in the
'Linking XYZ to a flight' section until the control is passed back to the logic layer.

Subsequently, the control is passed to the storage layer through the `logicManager.save()` method.
This method calls `storage.saveXYZManager(model.getXYZManager())` and
`storage.saveFlightManager(model.getFlightManager());`, to save the updated flight and XYZ objects in storage. Since
these 2 method calls work in the same way, we shall focus on just the latter, to be succinct.

<img src="images/WingmanUnlinkXYZDiagram.png" width="966" alt="Sequence diagram at Storage layer">

After `model.getFlightManager()` returns the model, the `saveFlightManager` method calls the
`saveFlightManager(flightManager, flightStorage.getPath())` method in the same class.
`flightStorage` is an `ItemStorage<Flight>` object and flightManager is an `ReadOnlyItemManager<Flight>` object.
This method call uses the imported json package to store 'JsonIdentifiableObject' versions of the flightManager
which in turn contains the JsonAdaptedFlights, including the flight with the updated link represented as a
`Map<FlightXYZType, Deque<String>>` object.

**Why this way?**

In this way, we are able to make the unlink feature work in a very similar way to the link feature, simply swapping
some methods to perform the opposite operation (particularly the `execute` function of the `UnlinkXYZCommand` class).

**Alternatives that were considered:**

One alternative implementation that was considered was to set the link as an attribute in the flight class and update
it directly with every change. However, this approach had a few limitations as discussed in the previous section.

### Displaying flights across all modes
Initially, there is only one `ItemListPanel` that displays an item list specific to each mode. 
However, in order to link an object (pilot/crew/location/plane) to a flight, a separate list panel displaying flights is
necessary for ease of selecting and linking to a specific flight.

**Implementation of display of flight list**

In the implementation as seen in the image below, the `MainWindow` can be filled by `ItemListPanel` as well as `FlightListPanel`:

`ItemListPanel`: displays information about each `Item` using an `ItemCard` in a `ListView`.

`FlightListPanel`: displays information about each `Flight` using a `FlightCard` in a `ListView`.

<img src="images/WingmanUI.png" width="400px">

By having separate list panels, it will be easier to customise the display of different Item types if required by ui improvements.

In each `ItemCard` as seen in the image below, the item’s name will be shown together with its id and respective attributes. 

In each `FlightCard`, the structure is similar to that of `ItemCard` but specific to flight objects.

By modifying the layout and dividing into a left section which shows the resources, and a right section which shows the flights, 
we can keep the information displayed organised and clear to the user.

**Alternatives considered for display of flights:**
* Alternative 1 (current choice): Has two panels to display items and flights in one display window
* Pros: Easy to implement and can view all the information simultaneously after a command is executed.
    * Cons: Too cramped, which may lead to information overload.
* Alternative 2: Has one display window for items and a separate display window for flights
    * Pros: More organised and visually pleasant.
    * Cons: Hard to implement and unable to view 2 panels simultaneously without switching between windows

## Appendix: Requirements

### Product scope

**Target user profile**:

* has a need to manage a significant number of flights, pilots and crews
* prefer desktop apps over other types
* can type fast
* prefers typing to mouse interactions
* is reasonably comfortable using CLI apps

**Value proposition**: Airline managers will be able to take labor, welfare, and
resource optimization
into consideration such that they can assign tasks to the most appropriate crew
based on their location
and availability while optimizing their staff’s physical well-being.

### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (
unlikely to have) - `*`

| Priority | As a …​         | I want to …​                                                    | So that I can…​                                                        |
| -------- |-----------------|-----------------------------------------------------------------| ---------------------------------------------------------------------- |
| `* * *`  | airline manager | add new locations to the list of locations where we operate     | I can assign new departing locations and arrival locations |
| `* * *`  | airline manager | list old locations from the locations where we were operating   |    I can view all the locations    |
| `* * *`  | airline manager | remove old locations from the locations where we were operating | I can update departing locations and arrival locations |
| `* * *`  | airline manager | add crew to our workforce                                       | I can assign them to flights |
| `* * *`  | airline manager | list crew from our workforce                                    | I can view all the crew |
| `* * *`  | airline manager | delete crew from our workforce                                  | I can remove them from flights |
| `* * *`  | airline manager | add new planes to our fleet                                     | I can assign them to flights                               |
| `* * *`  | airline manager | list planes from our fleet                                      | I can view all the planes in our fleet                     |
| `* * *`  | airline manager | remove old planes from our fleet                                | I can update planes which can be used for flights          |
| `* * *`  | airline manager | add new pilots to the crew list                                 | I can assign flights to pilots                             |
| `* * *`  | airline manager | remove pilots from the locations                                | I can retire some pilots                                   |
| `* * *`  | airline manager | add a new flight to the scheduled flights                       | know what flights are upcoming                 |
| `* * *`  | airline manager | remove a flight from the scheduled flights                      | know what flights are upcoming                 |
| `* * *`  | airline manager | list the scheduled flights                                      | view all the upcoming flights                  |

*{More to be added}*

### Use cases

(For all use cases below, the **System** is the `Wingman` and the **Actor**
is the `user`, unless specified otherwise)

**Use case: Delete a location**

**MSS**

1.  User requests to list locations
2.  Wingman shows a list of locations
3. User requests to delete a specific location in the list
4. Wingman deletes the location
5. User request to add a location
6. Wingman adds the location

   Use case ends.

**Extensions**

* 3a. The given location is invalid.
    * 3a1. AddressBook shows an error message.
      Use case resumes at step 2.

* 5b. The given location is a duplicate of an existing one
    * 5b1. Wingman shows an error message.
      Use case resumes at step 4.

**Use case: Delete a crew**

**MSS**

1.  User requests to list crew
2.  Wingman shows a list of crew
3.  User requests to delete a specific crew in the list
4.  Wingman deletes the crew
5.  User request to add a crew
6.  Wingman adds the crew

    Use case ends.

**Extensions**

* 2a. The list is empty.
  Use case ends.

* 3a. The given crew is invalid.
    * 3a1. AddressBook shows an error message.
      Use case resumes at step 2.

* 5b. The given crew is a duplicate of an existing one
    *  5b1. Wingman shows an error message.
       Use case resumes at step 4.
    *  5b1. Wingman shows an error message.
       Use case resumes at step 4.

**Use case: Delete a plane**

**MSS**

1.  User requests to list planes
2.  Wingman shows a list of planes
3.  User requests to delete a specific plane in the list
4.  Wingman deletes the plane
5.  User request to add a plane
6.  Wingman adds the plane

    Use case ends.

**Extensions**

* 2a. The list is empty.
  Use case ends.

* 3a. The given plane is invalid.
    * 3a1. The AddressBook shows an error message. Use case resumes at step 2.

* 5b. The given plane is a duplicate of an existing plane.
    * 5b1. Wingman shows an error message. Use case resumes at step 4.

**Use case: Add a pilot**

**MSS**

1. User requests to add a pilot
2. User specifies the basic information of the pilot
3. Wingman adds the pilot

   Use case ends.

**Extensions**

* 2a. The given pilot is invalid.
    * 2a1. Wingman shows an error message.
      Use case resumes at step 2.
* 3a. The given pilot is a duplicate of an existing one
    * 3a1. Wingman shows an error message.
      Use case resumes at step 2.

**Use case: Delete a pilot**

**MSS**

1. User requests to delete a specific pilot in the list
2. Wingman deletes the pilot
3. User request to add a pilot
4. Wingman adds the pilot

   Use case ends.

**Use case: Add a flight**

**MSS:**

1.  User chooses 'flight' mode
2.  Wingman shows a list of scheduled flights
3.  User enters details of the new flight to add to schedule
4.  Wingman adds the details of the new flight to list of scheduled flights
    Use case ends.

**Extensions:**

* 3a. The given flight details are invalid.

    * 3a1. Wingman shows an error message and an example of a correct command.

      Use case resumes at step 2.
      *{More to be added}*

### Non-Functional Requirements

1.  Should work on any _mainstream OS_ as long as it has Java `11` or above installed.
2.  Should be able to hold up to 100 flights, crews, and pilots without a noticeable sluggishness in performance for typical usage.
3.  A user with above average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.
4.  The system should respond within a second.
5.  The data should be stored locally and should be in a human editable text file.
6. Should work without requiring an installer or a remote server
7. Should be for a single user and should not have any shared file storage mechanism

*{More to be added}*

### Glossary

* **Location**: A unit place that flights may depart from or arrive at.
* **Crew**: A unit person who can be added to or deleted from a flight.
* **Pilot**: Someone that is certified to fly an aircraft.
* **Plane**: A unit plane which can be assigned to flights.
* **Flight**: An activity with start and end locations, to which pilots, planes and crew can be assigned.

