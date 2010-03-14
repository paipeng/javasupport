__END__

REST vs SOAP vs XML-RPC 
=======================

About REST (Representational State Transfer)
http://www.onlamp.com/pub/a/onlamp/2008/02/19/developing-restful-web-services-in-perl.html
http://en.wikipedia.org/wiki/Representational_State_Transfer#REST_versus_RPC

About XML-RPC vs SOAP
http://weblog.masukomi.org/writings/xml-rpc_vs_soap.htm

XML-RPC is 
"...a spec ( http://www.xmlrpc.com/spec ) and a set of implementations that allow software running on disparate operating systems, running in different environments to make procedure calls over the Internet. 

It's remote procedure calling using HTTP as the transport and XML as the encoding. XML-RPC is designed to be as simple as possible, while allowing complex data structures to be transmitted, processed and returned." - xmlrpc.com

XML-RPC's Goals
XML-RPC is very humble in its goals. It doesn't set out to be the solution to every problem. Instead it seeks to be a simple and effective means to request and receive information.

SOAP 1.1 http://www.w3.org/TR/SOAP/

"SOAP is a lightweight protocol for exchange of information in a decentralized, distributed environment. It is an XML based protocol that consists of three parts: an envelope that defines a framework for describing what is in a message and how to process it, a set of encoding rules for expressing instances of application-defined datatypes, and a convention for representing remote procedure calls and responses." - the SOAP spec.

SOAP makes extensive use of namespacing and attribute specification tags in almost every element of a message. For example when mixing data types within an array you have to set the SOAP-ENC:arrayType to indicate mixed data types within the array in addition to specifying the type of each element of the array.

SOAP's Goals
SOAP tries to pick up where XML-RPC left off by implementing user defined data types, the ability to specify the recipient, message specific processing control, and other features.

