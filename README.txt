For the testing purposes for each system we developed the set of executable jar files.
For the testing ease we provided several .bat and .sh files:

1) StartMessageManager - starts rmiregistry and MessageManager
2) StartSystemA - starts the execution of system A
3) StartSystemB - starts the execution of system B
4) StartSystemC - starts the execution of system C

NOTE:
Do not start the message manager twice, as RMI registry binds the port 1099.
Therefore you will not be able to launch two registries on the same port.

For the system A:
- To arm the system: input 1 at the security console.
- To disarm the system: input 2 at the security console.
- To stop the system: input X at the security console. 

- When the system is in armed mode you may model the behavior of the sensors.
Each time you trigger a sensor the information is passed to the monitoring console.
To reset the indicators you should manually arm and disarm the system.

For the system B:
- To arm the system: input 1 at the security console.
- To disarm the system: input 2 at the security console.
- To stop the system: input X at the security console. 

- When the system is in armed mode you may model the behavior of the sensors.
Each time you trigger a sensor the information is passed to the monitoring console.
To reset the indicators you should manually arm and disarm the system.

