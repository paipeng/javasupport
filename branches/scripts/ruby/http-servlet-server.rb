# http://localhost:2000/hello
# http://localhost:2000/hello/again
require 'webrick'
include WEBrick

s = HTTPServer.new( :Port => 2000 )

# HTTPServer#mount(path, servletclass)
#   When a request referring "/hello" is received,
#   the HTTPServer get an instance of servletclass
#   and then call a method named do_"a HTTP method".

class HelloServlet < HTTPServlet::AbstractServlet
  def do_GET(req, res)
    res.body = "<HTML>hello, world.</HTML>"
    res['Content-Type'] = "text/html"
  end
end
s.mount("/hello", HelloServlet)


# HTTPServer#mount_proc(path){|req, res| ...}
#   You can mount also a block by `mount_proc'.
#   This block is called when GET or POST.

s.mount_proc("/hello/again"){|req, res|
  res.body = "<HTML>hello (again)</HTML>"
  res['Content-Type'] = "text/html"
}

trap("INT"){ s.shutdown }
s.start
