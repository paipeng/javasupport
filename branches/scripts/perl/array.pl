use Data::Dumper;

# Dumper prints Array/Hash reference is much more readable than the actual data
# variables because it prints without space!

# creating array
my @a = (a, 1, b, 2, c, 3);
print Dumper(\@a);

my $arrayref = [1,2,3,4,5];
print Dumper($arrayref);

print "[2] = ", @$arrayref[2], "\n";
print "[2] = ", $arrayref->[2], "\n";

# 2-D array
# assign to our array, an array of array references
my @AoA = (
	   [ "fred", "barney" ],
	   [ "george", "jane", "elroy" ],
	   [ "homer", "marge", "bart" ],
    );
print Dumper(\@AoA);
print $AoA[2][2], "\n";
