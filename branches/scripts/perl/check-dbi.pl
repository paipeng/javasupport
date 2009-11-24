#Check on command line:
#perl -MDBI -e "DBI->installed_versions"
#perl -MDBD::ODBC -e "print $DBD::ODBC::VERSION"

require DBI;

print "Installed drivers\n";
my @drivers = DBI->available_drivers;
print join(", ", @drivers), "\n";


print "\n\nChecking ODBC driver\n";
my $d = join(",", @drivers);
print "DBD::ODBC";
print "not" if ($d !~ /ODBC/);
print " installed\n";
