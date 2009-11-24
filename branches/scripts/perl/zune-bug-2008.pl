sub is_leap_year{
	my ($year) = @_;
	return (($year % 4 == 0) and ($year % 100 != 0)) or ($year % 400 == 0)
}
sub test_zune_bug{
	my ($days, $year) = shift @_;
	my $year = 2008;
	while ( $days > 365 ){
	   if ( is_leap_year($year) ) {
		   if ( $days > 366 ) {  # WHY check greater than 366???
			   $days -= 366;
			   $year += 1;
		   }
	   }
	   else {
		   $days -= 365;
		   $year += 1;
	   }
	}
	return ($days, $year);
}


my @res = test_zune_bug(366, 2008); #infinate loop!
print "$res[0], $res[1]\n";
	

