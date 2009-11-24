
sub logger {
	my $logmessage = shift;
	open my $logfile, ">>", "my.log" or die "Could not open my.log: $!";
	print $logfile $logmessage;
	close $logfile or die "$logfile: $!";
}
logger("We have a logger subroutine!");
