# # Reading a text file.
# my $filename = $ARGV[0];
# open(FH, $filename);
# while(<FH>){
# 	print($_);
# }
# close(FH);

# Reading a text file.
my $filename = $ARGV[0];
open(FH, $filename);
while(my $ln = <FH>){
	print($ln);
}
close(FH);

