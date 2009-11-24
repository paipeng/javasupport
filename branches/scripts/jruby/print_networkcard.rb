require 'java'
ni = java.net.NetworkInterface.networkInterfaces
ni.each{ |i| puts i; puts }

