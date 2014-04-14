For the testing purposes for each system we developed the set of executable jar files.
For the testing ease we provided several .bat and .sh files:

1) StartMessageManager - starts rmiregistry and MessageManager
2) StartSystemA - starts the execution of system A
3) StartSystemB - starts the execution of system B
4) StartSystemC - starts the execution of system C
5) TestSystemC - tool for launching devices for system C

NOTE:
Do not start the message manager twice, as RMI registry binds the port 1099.
Therefore you will not be able to launch two registries on the same port.

Step 1: Launch StartMessageManager.bat for Windows or StartMessageManager.sh for Linux/Mac OS

Step 2: 

For the system A:
Launch StartSystemA.bat for Windows or StartSystemA.sh for Linux/Mac OS
- To arm the system: input 1 at the security console.
- To disarm the system: input 2 at the security console.
- To stop the system: input X at the security console. 

- When the system is in armed mode you may model the behavior of the sensors.
Each time you trigger a sensor the information is passed to the monitoring console.
To reset the indicators you should manually arm and disarm the system.


For the system B:
Launch StartSystemB.bat for Windows or StartSystemB.sh for Linux/Mac OS
- To arm the system: input 1 at the security console.
- To disarm the system: input 2 at the security console.
- To prevent sprinklers from turning on: input 3 at the security console.
- To confirm sprinklers turning on: input 4 at the security console.
- To stop the system: input X at the security console. 

- When the system is in armed mode you may model the behavior of the smoke sensor.
When the smoke sensor detects smoke you have 10 seconds to turn off sprinklers via security console. Otherwise sprinklers will start sprinkling
To reset the indicators you should manually arm and disarm the system.


For the system C:
Launch StartSystemC.bat for Windows or StartSystemC.sh for Linux/Mac OS
By launching the system C you only get the empty maintenance console.
- To print the list of equipment in the system: input 1 at the maintenance console.
- To stop the maintenance console: input 2 at the maintenance console.

To test the system C there are 2 possible options:
Option 1) Run the testing script provided by our team.
For the ease of testing we have created the scripts that would start controllers, monitors and sensors in a specified order.
Each module will be started in 10 seconds after the previous one.
That allows to make requests to the maintenance console and see the change of number of devices in the system.

Option 2) Run the sensors / monitors / consoles manually.
Our team has provided the cross-platform runnable .jar files for each of the pluggable system component.
You may run the jar files for specific sensors / monitors / consoles and watch their status in maintenance console.

To simulate the not connected devices you may just exit from the device console.