#!/bin/sh
echo "create database if not exists ${artifactId}_dev" | mysql -u root
echo "grant all on ${artifactId}_dev.* to 'devuser'@'localhost' identified by 'devuser123'" | mysql -u root
echo "flush privileges" | mysql -u root
mvn hibernate3:hbm2ddl