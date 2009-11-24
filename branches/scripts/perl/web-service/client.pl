#!perl -w
use SOAP::Lite;
print SOAP::Lite
	-> uri('http://localhost:8080/web-service-webapp-demo/helloWorld')
	-> sayHi()
	-> result;
