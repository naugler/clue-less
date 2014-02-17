***************************************************
* Clue-Less                                       *
*                                                 *
* A multiplayer Clue clone by BlakJack Games, Inc.*
***************************************************

** INTRO ******************************************
The project is divided into three modules:
 - clueless-common - Contains code common to both client and server
 - clueless-server - Required to host a game
 - clueless-client - You and your buddies will each use a client to connect to the server and play the game

** BUILDING ***************************************
The modules are designed to be built independently, but both client and server depend on common.
They can be build from the command line using ant <http://ant.apache.org/> or using your favorite IDE.

To build the project from the command line:
 1. Download the source code (duh).
 2. Make sure you have ant on your path! And Java!
 3. Navigate to the project directory, and then to the module subdirectory (client, common, or server).
 4. Type "ant jar".
 5. The jar file should be located in the dist/ subdirectory.

To build the project in NetBeans:
 1. Download the source code.
 2. In NetBeans, start a New Project.
 3. Select "Java Free-Form Project".
 4. Navigate to the project directory, and select the module you would like to build (client, common, or server).
 5. Change the build command from "compile" to "jar".
 6. If you are building client or server, don't forget to add the dependencies to the sources classpath, or Netbeans will complain.
 
To build the project in Eclipse:
 1. Download the source code.
 2. Figure out how to build the project in Eclipse.
 3. Update these instructions ;)
 
** RUNNING ****************************************
We have included handy run scripts for both client and server, but special
configuration may be required, and this part of the document should
describe how that can be accomplished.