# Range numbers are INCLUSIVE on both ends!
for my $n (1 .. 10){
	print "$n ";
}
print "\n";

# No error? but what is "..." ???
for my $n (1 ... 10){
	print "$n ";
}
print "\n";

# Range works on letters too.
for my $n ("A" .. "Z"){
	print "$n ";
}
print "\n";

# Range won't go backward!, this case prints "Z" only.
for my $n ("Z" .. "A"){
	print "$n ";
}
print "\n";

#range with steps have to be done in a ful for condition style.
for (my $i = 0; $i < 10; $i +=2){
	print "$i ";
}
print "\n";

