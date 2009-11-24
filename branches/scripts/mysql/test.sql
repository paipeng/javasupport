CREATE DATABASE test;
USE test;

CREATE TABLE pet (name VARCHAR(20), owner VARCHAR(20), 
species VARCHAR(20), sex CHAR(1), birth DATE, death DATE);
LOAD DATA LOCAL INFILE "pet.txt" INTO TABLE pet;

CREATE TABLE event (name VARCHAR(20), date DATE,
 type VARCHAR(15), remark VARCHAR(255)); 
LOAD DATA LOCAL INFILE "event.txt" INTO TABLE pet;
