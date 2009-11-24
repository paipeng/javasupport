#!/usr/local/bin/perl
#
#####	Work produced by LEX Solutions, Inc.
#       http://www.lexsolutions.com
#
# FILE NAME:
#	blob
#
# DESCRIPTION:
#   Inserts or retrieves a BLOB from an Oracle database.
#   See usage for details.
#
#   This program requires Perl version 5.004_04, plus the 
#   DBI and DBD::Oracle libraries.  See www.cpan.org for details.
#
# HISTORY:
#	Date		Developer	Version	Description Of Changes Made
#	----		---------	-------	---------------------------
#   1/1999      Ed Hughes   1.0     Original
#

use DBI;

## Parameters
$doDebug = 0;
$| = 1; # set stdout to flush
$LONG_RAW_TYPE=24; # Oracle type id for blobs

# Get args
($operation, $connectString, $sqlStmt, $blobFileName) = @ARGV;

# Check args
if (
	(($operation ne "-select") && ($operation ne "-insert")) ||
	(($operation eq "-insert") && (! "$blobFileName")) ||
	(($operation eq "-insert") && (! grep(/\:blob/, $sqlStmt)))
	)
{
	print STDERR <<EOF;
Usage: 
	$0 -insert connectString "insert/update stmt" blobFileName
		(Where 'stmt' must contain one placeholder ":blob")

	$0 -select connectString "select stmt"  (output to stdout)
		(Note that multiple rows can be selected)

 eg:
  $0 ed/foo\@db -insert \\
	"insert into my_pics (name, pic) values ('Ed', :blob)" ./ed.jpg
  $0 ed/foo\@db -select \\
	"select pic from my_pics where name = 'Ed'" > ed2.jpg

EOF
	exit(-1);
}
	
# Parse connect string
($tmp, $dbName) = split(/\@/, $connectString);
($dbUser, $dbPasswd) = split(/\//, $tmp);
$doDebug && print "user/passwd\@db = $dbUser/$dbPasswd\@$dbName \n";

# Connect to DB
$dataSource = "dbi:Oracle:${dbName}"; # interface:driver:db_name
$doDebug && print "dataSource = $dataSource";

$db = DBI->connect($dataSource, $dbUser, $dbPasswd);
$db || 	die "Error connecting to db: $DBI::errstr\n";

&blobSelect() if ($operation eq "-select");
&blobInsert() if ($operation eq "-insert");

exit(0);


sub blobInsert()
{
	open(BLOB, "$blobFileName");
	$bytes = 0;
	$bytes = read(BLOB, $buf, 500000);

	print STDERR "Read $bytes bytes...\n";
	close(BLOB);
	
	$stmt = $db->prepare($sqlStmt) || die "\nPrepare error: $DBI::err .... $DBI::errstr\n";

	# Bind variable.  Note that long raw (blob) values must have their attrib explicitly specified
	$attrib{'ora_type'} = $LONG_RAW_TYPE;
	$stmt->bind_param(":blob", $buf, \%attrib);  

	$stmt->execute() || die "\nExecute error: $DBI::err .... $DBI::errstr\n";

	print STDERR "Complete.\n";
}


sub blobSelect()
{
	$db->{LongReadLen}=500000;  # Make sure buffer is big enough for BLOB

	$stmt = $db->prepare($sqlStmt) || die "\nPrepare error: $DBI::err .... $DBI::errstr\n";
	$stmt->execute() || die "\nExecute error: $DBI::err .... $DBI::errstr\n";

	$row = 0;
	while ($blob = $stmt->fetchrow)
	{
		printf STDERR "Fetching row %d \n", $row++;
		print STDOUT $blob;
	}
	$stmt->finish();

	if (!$row)
	{
		print STDERR "No data found.\n";
	}
	else
	{
		print STDERR "Complete.\n";
	}
}