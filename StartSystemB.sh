#!/bin/bash
echo Starting ECS System B

echo Security Controller
java -jar SystemB_SecurityController.jar &

echo Sprinkler Controller
java -jar SystemB_SprinklerController.jar &

echo Starting Smoke Sensor Console
java -jar SystemB_SmokeSensor.jar &

echo Security Fire Monitoring Console
java -jar SystemB_SecurityConsole.jar 

