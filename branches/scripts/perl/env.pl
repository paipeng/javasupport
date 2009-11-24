if(not exists $ENV{'Path'}){
	print "You don't have PATH env defined.\n";
}
for my $k (keys %ENV){
	print $k, "\n" if $k =~ /Path/i;
}
