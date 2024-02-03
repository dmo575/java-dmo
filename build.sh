#!/usr/bin/bash

project=$1

if [ $# -ne 1 ]; then
    echo "Incorrect arg count"
fi

case $project in
    "learning1")
        javac -d learning-build learning/Main.java
        java -classpath learning-build learning.Main;;
    "learning2")
        javac -d learning-build learning/awt/Main2.java
        java -classpath learning-build learning.awt.Main2;;
    "ren")
        javac -d renderer-build renderer/Main.java
        java -classpath renderer-build renderer.Main;;
esac
