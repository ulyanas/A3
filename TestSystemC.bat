%ECHO OFF
START "MUSEUM ENVIRONMENTAL CONTROL SYSTEM CONSOLE" /NORMAL java -jar SystemC_ECSConsole.jar
ping -n 10 127.0.0.1 > nul
START "TEMPERATURE CONTROLLER CONSOLE" /MIN /NORMAL java -jar SystemC_TemperatureController.jar
ping -n 10 127.0.0.1 > nul
START "HUMIDITY CONTROLLER CONSOLE" /MIN /NORMAL java -jar SystemC_HumidityController.jar
ping -n 10 127.0.0.1 > nul
START "TEMPERATURE SENSOR CONSOLE" /MIN /NORMAL java -jar SystemC_TemperatureSensor.jar
ping -n 10 127.0.0.1 > nul
START "HUMIDITY SENSOR CONSOLE" /MIN /NORMAL java -jar SystemC_HumiditySensor.jar
