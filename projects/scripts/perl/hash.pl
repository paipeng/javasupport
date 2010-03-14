use Data::Dumper;

# Dumper prints Array/Hash reference is much more readable than the actual data
# variables because it prints without space!

# different way of creating a hash
my %a = (a, 1, b, 2, c, 3);
my %a2 = ("a", 1, "b", 2, "c", 3);
my %b = (a => 1, b => 2, c => 3);
print Dumper(\%a);
print Dumper(\%a2);
print "bareword vs string keys: equals ? ", $a{"a"} == $a2{"a"} ? "yes" : "no", "\n";
print Dumper(\%b);

# bad way to create hash, you MUST use $c, not %c!
my %c = {a => 1, b => 2, c => 3};
print Dumper(\%c);

# hash reference literal
my $hashref = {a => 1, b => 2, c => 3};
print Dumper($hashref);

sub mkHashRef{ return  {"foo"=>{"a"=> 11}}; }
print Dumper(mkHashRef);

print "{'a'} = ", $$hashref{'a'}, "\n";
print "{'a'} = ", $hashref->{'a'}, "\n";

# check exists key
my %h = ("a", "");
print "defined : ", defined $h{"a"}, "\n";
print "exists : ", exists $h{"a"}, "\n";
undef $h{"a"};
print Dumper(\%h);
print "defined : ", defined $h{"a"}, "\n";
print "exists : ", exists $h{"a"}, "\n";

# looping hash
my %hash = ();
$hash{'key1'} = 'value1';
$hash{'key2'} = 'value2';
$hash{'key3'} = 'value3';
while (my($key, $value) = each(%hash)){
	print $key . ' - 	' . $value . "\n";
}

# Can't do this!!! this only take fisrt TWO elements in the hash and ENDS!!!
foreach my $pair (each(%hash)) {
	print Dumper($pair);
} 

foreach my $key (keys %hash) { 
	print "$key = $hash{$key}\n"; 
} 
