# simple subroutine/function definition
sub foo{ print "foo\n"; }
foo(); # invoking subroutine.
&foo;  # invoking without parenthensis

# function with Prototypes - arguments checking.
sub foo2($$){
	my ($p1, $p2) = @_;
	print "params: $p1, $p2\n";
}
#foo2("x"); # => will error out with "Not enough argumetns"
foo2("x", "y");

# this is actually a empty prototyped function - and it can be inlined by interpreter!
sub foo3(){ "foo3\n"; }
#foo3(33) # => error out with "Too many arguments"
print foo3

# pulling out function arguments and return value
sub square {
	my $num = shift;
	my $result = $num * $num;
	return $result;
}
print square(8), "\n";


