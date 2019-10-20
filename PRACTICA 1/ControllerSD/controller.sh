#! /bin/bash
export CLASSPATH=$CLASSPATH:$HOME/Escritorio/ControllerSD/clienteDeRMI.jar
javac ControllerSD.java
java -Djava.security.policy=registrar.policy ControllerSD $1 $2 $3
