#print ant version
#java -cp D:\opt\apache-ant-1.7.1\lib\ant-launcher.jar;D:\opt\apache-ant-1.7.1\lib\ant.jar org.apache.tools.ant.Main -v

require 'java'

ant_home = 'D:\opt\apache-ant-1.7.1'
require "#{ant_home}/lib/ant-launcher.jar"
require "#{ant_home}/lib/ant.jar"

org.apache.tools.ant.Main.main(["-v"].to_java(:string))
