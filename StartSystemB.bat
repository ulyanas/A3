%ECHO OFF
START "SYSTEM B SECURITY CONTROLLER" /MIN /NORMAL java -jar SystemB_SecurityController.jar
START "SYSTEM B DOOR SENSOR" /MIN /NORMAL java -jar SystemB_SprinklerController.jar
START "SYSTEM B SMOKE SENSOR" /MIN /NORMAL java -jar SystemB_SmokeSensor.jar
START "SYSTEM B SECURITY CONSOLE" /NORMAL java -jar SystemB_SecurityConsole.jar

