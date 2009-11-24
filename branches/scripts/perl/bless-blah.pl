sub foo{ "foo"; }
$a = {'a' => 1};
$b = $a;
print "\$a is a ", ref($a), "\n";
print "\$b is a ", ref($b), "\n";
$c = bless $a, BLAH;
print "\$a is a ", ref($a), "\n";
print "\$b is a ", ref($b), "\n";
print "\$c is a ", ref($c), "\n";
print "\$c->{'a'} is a ", $c->{'a'}, "\n";
print "\$c->foo() is a ", $c->foo(), "\n";
