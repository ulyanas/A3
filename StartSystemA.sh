#!/bin/bash
echo Starting ECS System A

echo Security Controller
java -jar SystemA_SecurityController.jar &

echo Door Sensor
java -jar SystemA_DoorSensor.jar &

echo Window Sensor
java -jar SystemA_WindowSensor.jar &

echo Motion Sensor
java -jar SystemA_MotionSensor.jar &

echo Security Console
java -jar SystemA_SecurityConsole.jar