for ($i = 1; $i < 10; $i++) {
	print("$i ");
}
print("\n");

for $i (0 .. 3){
	print("$i ");
}
print("\n");

@ary = ("one", "two", "three");
for(@ary){
	print("$_ ");
}
print("\n");
for $x (@ary){
	print("$x ");
}
print("\n");

foreach $item (split(/;/, $ENV{PATH})) {
	print "Item: $item\n";
}
