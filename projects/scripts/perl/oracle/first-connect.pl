use strict;
use DBI;
my $dbh = DBI->connect( 'dbi:Oracle:orcl',
                        'scott',
                        'tiger',
                        { RaiseError => 1, AutoCommit => 0 }
                      ) || die "Database connection not made: $DBI::errstr";


my $sql = qq{ SELECT TNAME, TABTYPE FROM TAB };    # Prepare and execute SELECT
my $sth = $dbh->prepare($sql);
$sth->execute();

my($tname, $tabtype);                     # Declare columns
$sth->bind_columns(undef, \$tname, \$tabtype);
print "List of tables:\n\n";              # Fetch rows from DB
while( $sth->fetch() ) {
    print "Object: $tname, type: $tabtype\n";
}
$sth->finish();     

$dbh->disconnect;