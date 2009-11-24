# Interesting score calculation 
# http://tech.groups.yahoo.com/group/perl-beginner/message/26442
#

my @numbers = (1,2,5,5,8,9,9,9,9,10);
my $next = 5;

my $pct = insert_and_get_percentile(\@numbers, $next); # pass @numbers by reference

print $pct, "\n";
print join(' ', @numbers), "\n";

# assumes that the input list is ALREADY SORTED
sub insert_and_get_percentile {
	my ($data, $n) = @_;
	my $score = 0;
	
	push @$data, $n; # $n, the new value, is now at the END of the array
	
	# an old-school for loop for going through the data
	for (my $i = 0; $i < @$data; $i++) {
	
		# if data[x] is less than the new value add 1
		$score++ if $data->[$i] < $n;
		
		# if data[x] is equal to the new value, add 1/2
		$score += 0.5 if $data->[$i] == $n;
		
		# if data[x] is greater than the new value:
		# a) add 1/2 (for the new value itself),
		# b) move the new value to right before data[x] (via pop() and splice()),
		# c) and stop looping through the array
		$score += 0.5, splice(@$data, $i, 0, pop @$data), last if $data->[$i] > $n;
	
	}
	
	return $score / @$data;
}

