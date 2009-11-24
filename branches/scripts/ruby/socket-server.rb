# to test this server, do:
# telnet localhost 2000
require 'webrick'
port = ARGV.shift || 2000
s = WEBrick::GenericServer.new( :Port => port )
trap("INT"){ s.shutdown }
s.start{|sock|
	sock.print("Socket server is serving you time : " + Time.now.to_s + "\r\n")
}
