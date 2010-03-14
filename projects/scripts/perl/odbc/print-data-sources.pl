#Check on command line:
#perl -MDBI -e "DBI->installed_versions"
#perl -MDBD::ODBC -e "print $DBD::ODBC::VERSION"

require DBI;

print "Checking ODBC driver\n";
my $d = join(",", @drivers);
print "DBD::ODBC";
print "not" if ($d !~ /ODBC/);
print " installed\n";

print "\nData sources\n";
my @dsns = DBI->data_sources('ODBC');
foreach my $d (@dsns){
  print "$d\n";
}

# Sample of output
# Data sources
# dbi:ODBC:MS Access Database
# dbi:ODBC:Excel Files
# dbi:ODBC:dBASE Files
