
use Data::Dumper;

# Perl variable assignment is passed as Reference ???
#   see http://perldoc.perl.org/perlsub.html#Temporary-Values-via-local()
#
# The behavior prooves that it "passed as value", which termed from language like
# Java. When you assign Perl variable, it not only creates a new reference, it also
# creates a new cloned/duplicated value to the assigned variable! This means changing
# to the value of the "copied" will NOT affect the original data.
#
# In Perl you can do the Java's "pass as reference" using Perl's explicit reference
# variable.

my $s = "foo";
my $s2 = $s;
print "\$s: ", $s, " \$s2: ", $s2, " ", \$s, " ", \$s2, "\n";
$s2 = "foo-bar";
print "\$s: ", $s, " \$s2: ", $s2, " ", \$s, " ", \$s2, "\n";

# Array value are also been cloned/duplicated!
my @a = (1,2,3);
my @a2 = @a;
print "\@a : ", Dumper(\@a), "\@a2 : ", Dumper(\@a2), \@a, " ", \@a2, "\n";
@a2 = (4,5,6);
print "\@a : ", Dumper(\@a), "\@a2 : ", Dumper(\@a2), \@a, " ", \@a2, "\n";

# Hash value are also been cloned/duplicated!
my %h = ("a", 1, "b", 2, "c", 3);
my %h2 = %h;
print "\%h : ", Dumper(\%h), "\%h2 : ", Dumper(\%h2), \%h, " ", \%h2, "\n";
%h2 = ("a", 4, "b", 5, "c", 6);
print "\%h : ", Dumper(\%h), "\%h2 : ", Dumper(\%h2), \%h, " ", \%h2, "\n";


# passing data into function are the same.
sub foo {
	print "\$_ ", $_[0], "\n";
	print "\\\$_ ", \$_[0], "\n";	
}

foo($s);
foo(\@a); #pass ref explicitly.

