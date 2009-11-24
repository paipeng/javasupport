# http://forums.devshed.com/perl-programming-6/perl-puzzle-sort-of-577137.html
my $var = 'foo';
my $str = 'bar';
for ($var = $str) {
   print "$var\n";
   s/bar/bonk/;
   print "$var\n";
}
