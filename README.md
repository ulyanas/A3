A3
==
Several Steps to Start SystemA:

0. Go to A3/
1. Compile all .java file.
rum command

	a.javac -d ./bin  *.java
	
	b.javac -d ./bin  ./systemA/*.java
	
	c.javac -d ./bin  ./Configuration/*.java


2. Go to A3/bin/



3. Start MessageBus
run command

    a.rmiregistry
    
    b.java MessageManager


4. Start SecurityConsole
run command

    a.java systemA/SecurityConsole
    
5. Start SecuityController
run command 

    a.java systemA/SecurityController
    
6. Start Sensor
run command

    a.java systemA/DoorSensor
    
    b.java systemA/MotionSensor
    
    c.java systemA/WindowSensor
