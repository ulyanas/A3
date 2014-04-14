#!/bin/bash
echo Testing System C

echo Temperature Controller
java -jar SystemC_TemperatureController.jar &

sleep 10 

echo Humidity Controller
java -jar SystemC_HumidityController.jar &

sleep 10

echo Temperature Sensor
java -jar SystemC_TemperatureSensor.jar &

sleep 10

echo Humidity Sensor
java -jar SystemC_HumiditySensor.jar &

sleep 10

echo ECS Console
java -jar SystemC_ECSConsole.jar
