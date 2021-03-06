# ForkExec

**Fork**: delicious menus for **Exec**utives

Distributed Systems 2018-2019, 2nd semester project

## Authors

Group A65

Ricardo Nunes 71015 git-ricnunes Leade Developer
Ricardo Nunes 71015 git-ricnunes Tester

For each module, the README file must identify the lead developer and the contributors.
The leads should be evenly divided among the group members.

## Getting Started

The overall system is composed of multiple services and clients.
The main service is the _hub_ service that is aided by the _pts_ service. 
There are also multiple _rst_ services, one for each participating restaurant.

See the project statement for a full description of the domain and the system.



### Prerequisites

Java Developer Kit 8 is required running on Linux, Windows or Mac.
Maven 3 is also required.

To confirm that you have them installed, open a terminal and type:

```
javac -version

mvn -version
```


### Installing

To compile and install all modules:

```
mvn clean install -DskipTests
```

The tests are skipped because they require each server to be running.

### Testing

First before testing all the modules make sure that all the serveres are connected.

mvn verify


