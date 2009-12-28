@start javaw -cp "h2-1.2.125.jar;%H2DRIVERS%;%CLASSPATH%" org.h2.tools.Console %*
@if errorlevel 1 pause