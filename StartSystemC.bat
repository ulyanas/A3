%ECHO OFF
START "SYSTEM A SECURITY CONSOLE"  /NORMAL java -jar SystemA_SecurityConsole.jar
START "SYSTEM A SECURITY CONTROLLER" /MIN /NORMAL java -jar SystemA_SecurityController.jar
START "SYSTEM A DOOR SENSOR" /MIN /NORMAL java -jar SystemA_DoorSensor.jar
START "SYSTEM A WINDOW SENSOR" /MIN /NORMAL java -jar SystemA_WindowSensor.jar
START "SYSTEM A MOTION SENSOR" /MIN /NORMAL java -jar SystemA_MotionSensor.jar