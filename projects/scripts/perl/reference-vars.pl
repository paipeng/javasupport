use Data::Dumper;

# Define data
my $foo = "foo";
my @ls = (1,2,3,4);
my %map = (one => 1, two=> 2, three => 3);
sub handler(){ return "I am a handler"; }

# Create references
my $scalarref = \$foo;
my $arrayref  = \@ls;
my $hashref   = \%map;
my $coderef   = \&handler;
my $globref   = \*foo;

# Use references
#print "ref ", Dumper($scalarref, $arrayref, $hashref, $coderef, $globref);
print '$scalaref ', $$scalarref, "\n";
print '$arrayref ', Dumper(@$arrayref), "\n";
print '$hashref ', Dumper(%$hashref), "\n";
print '$coderef ', &$coderef, "\n";

# More way to use reference, with braces
print '$scalaref ', ${$scalarref}, "\n";
print '$arrayref ', Dumper(@{$arrayref}), "\n";
print '$hashref ', Dumper(%{$hashref}), "\n";
print '$coderef ', &{$coderef}, "\n";

# More way to use reference, with right arrow (calling methods or operators?)
print '$arrayref [0] ', $arrayref->[0], "\n";
print '$hashref {three} ', $hashref->{'three'}, "\n";

# Creating reference directly from data literal
my $arrayref = [1, 2, ['a', 'b', 'c']];
my $hashref = {
	'Adam'  => 'Eve',
	'Clyde' => 'Bonnie',
};
my $coderef = sub { print "Boink!\n" };
print "ref ", Dumper($arrayref, $hashref, $coderef);


#Closure!
sub newprint {
	my $x = shift;
	return sub { my $y = shift; print "$x, $y!\n"; };
}
$h = newprint("Howdy");
$g = newprint("Greetings");
&$h("world");
&$g("earthlings");

# Nested referencing?
my $refrefref = \\\"howdy";
print $$$$refrefref;
