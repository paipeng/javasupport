#!/bin/bash

# Created on 11/13/2009 by Zemian Deng
#
# Shell script to start up a JVM with options needed for java-demo

SCRIPT_DIR=`dirname $0`
APP_DIR=`cd $SCRIPT_DIR/.. && pwd`
DEP=`find $APP_DIR/lib | ruby -pe 'gsub(/\n/, ":")'`
java -cp target/classes:$DEP deng.javademo.LoggerDemo
