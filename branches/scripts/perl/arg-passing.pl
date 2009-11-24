
# NOTE: The easier way to create/read a perl subroutine signature to use style
# like "my ($p1, $p2 [...]) = @_" format

use Data::Dumper;

#create function that takes parameters by hash's key name
sub func{
	my (%params) = @_;
	print "params: ", $p1, Dumper(%params), "\n";	
}

func();
func("foo");
func(foo);
func(foo => "bar");
func("test", foo => "bar"); # ???
func({ foo => "bar" });     # ???

#a proper way to pass parameters.
func(foo => "bar", foo2 => 99);
