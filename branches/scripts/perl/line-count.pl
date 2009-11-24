use Cwd; # module for finding the current working directory
$|=1;    # turn off I/O buffering

sub ScanDirectory {
    my ($workdir) = shift; 

    my($startdir) = &cwd; # keep track of where we began

    chdir($workdir) or die "Unable to enter dir $workdir:$!\n";

    opendir(DIR, ".") or die "Unable to open $workdir:$!\n";
    my @names = readdir(DIR);
    closedir(DIR);
 
    foreach my $name (@names){
        next if ($name eq "."); 
        next if ($name eq "..");
	
        if (-d $name){                     # is this a directory?
            &ScanDirectory($name);
            next;
        }
        unless (&CheckFile($name)){           
            print &cwd."/".$name."\n"; # print the bad filename
        }
    }
    chdir($startdir) or die "Unable to change to dir $startdir:$!\n";
}

$lines = 0;
sub CheckFile{
	my $filename = shift @_;
    print "Counting lines from $filename\n";
	open(FILE, $filename) or die "Can't open `$filename': $!";
	while (sysread FILE, $buffer, 4096) {
		$lines += ($buffer =~ tr/\n//);
	}
	close FILE;
}

ScanDirectory($ARGV[0]);
print "$lines lines.";