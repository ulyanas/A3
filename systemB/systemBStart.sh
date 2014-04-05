#!/bin/bash
echo Starting ECS System B


echo Security Controller
java bin/systemB/SecurityController &

echo Sprinkler Controller
java bin/systemB/SprinklerController &

echo Starting Smoke Sensor Console
java bin/systemB/SmokeSensor &


echo Security Fire Monitoring Console
java bin/systemB/SecurityConsole 

