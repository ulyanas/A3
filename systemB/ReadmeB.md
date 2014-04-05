A3
==
Several Steps to Start SystemA:

0. Go to A3/
1. Compile all .java file.
rum command

	a. javac -d ./bin  *.java
	
	b. javac -d ./bin  ./systemB/*.java
	
	c. javac -d ./bin  ./Configuration/*.java


2. Go to A3/bin/


3. Start MessageBus
run command

    a. rmiregistry
    
    b. java MessageManager


4. Start Security Controller
run command

    a. systemB/SecurityController
    
4. Start Sprinkler Controller
run command

    a. java systemB/SprinklerController
    
5. Start Smoke Sensor Console
run command 

    a. java systemB/SmokeSensor

6. start Security Fire Monitoring Console
	
	a. java systemB/SecurityConsole 
	
7. Arm system

////FOR MAC USE SHELL SCRIPTS/////

1. do 0-1

2. put files EMStart.sh and systemBStart.sh into A3/bin folder

3. go to A3/bin

4. sh EMStart.sh

5. Arm system
    
