#! /bin/bash

javac InterfazRemoto.java
export CLASSPATH=$CLASSPATH:$HOME/Escritorio/RMIServer/InterfazRemoto
javac ObjetoRemoto.java
rmic ObjetoRemoto

jar cvf clienteDeRMI.jar InterfazRemoto.class ObjetoRemoto_Stub.class

javac Registro.java

