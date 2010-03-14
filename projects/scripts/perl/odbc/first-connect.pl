
# Attempt to connect to Window's Access DB: Northwind
# You will need to setup ODBC first to the 
# "C:\Program Files\Microsoft Office\OFFICE11\SAMPLES\Northwind.mdb"
# "C:/Program Files/Microsoft Office/OFFICE11/SAMPLES/Northwind.mdb"

use strict;
use DBI;
use Data::Dumper;

my $dbh = DBI-> connect('dbi:ODBC:Northwind');
if(!$dbh){ print "$DBI::err\n$DBI::errstr\n$DBI::state"; }

#my $sql = "SELECT * FROM Categories"; #Picture field will error out!

# MS BOMB: SPACE in MS Access MUST be IGNORED in SQL!!!
# SQL BOMB: desc is a reserved keyword and need to be escaped with backtick!
my $sql = <<SQL_STR;
SELECT 
	CategoryID as id,
	CategoryName as name,
	Description as `desc`
FROM 
	Categories
SQL_STR

my $sth = $dbh->prepare($sql); $sth->execute();
my $count = 0;
while(my $row_hash_ref = $sth->fetchrow_hashref){
	print "--- Record #$count ---\n";	
	#print Dumper($row_hash_ref);
	while(my ($k, $v) = each %$row_hash_ref) {
		print "$k : $v\n";	
	}
	$count++;
}
$dbh->disconnect if ($dbh);
