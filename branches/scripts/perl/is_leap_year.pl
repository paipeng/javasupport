sub is_leap_year{
	my ($year) = @_;
	return (($year % 4 == 0) and ($year % 100 != 0)) or ($year % 400 == 0)
}

for my $year (2000 ... 2010){
	print "$year is leap year? ", is_leap_year($year), "\n";
}

