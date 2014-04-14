#!/bin/bash
echo Starting ECS System A

echo Security Controller
java -jar SystemA_SecurityController.jar &

echo Starting Door Sensor Console
java -jar SystemA_DoorSensor.jar &

echo Starting Window Sensor Console
java -jar SystemA_WindowSensor.jar &

echo Starting Motion Sensor Console
java -jar SystemA_MotionSensor.jar &

echo Security Console
java -jar SystemA_SecurityConsole.jar