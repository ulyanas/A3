#!/bin/bash
echo Testing System C

echo Temperature Controller
java -jar SystemC_TemperatureController.jar &

echo Humidity Controller
java -jar SystemC_HumidityController.jar &

echo Temperature Sensor
java -jar SystemC_TemperatureSensor.jar &

echo Humidity Sensor
java -jar SystemC_HumiditySensor.jar &

echo ECS Console
java -jar SystemC_ECSConsole.jar
