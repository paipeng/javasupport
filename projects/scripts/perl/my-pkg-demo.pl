use MyPkg;
use MyPkg::CoolBean;
#require MyPkg;
use Data::Dumper;
print "I am current on ", __PACKAGE__, "\n";
print "Module dump? ", Dumper(MyPkg), "\n";
print "Use Module as reference? ", MyPkg->get_foo(), "\n";
print "Done\n";
