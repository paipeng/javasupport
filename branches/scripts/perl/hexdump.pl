
# hdump from http://www.devx.com/DevX/Tip/17116
sub hdump {
    my $offset = 0;
    my(@array,$format);
    foreach my $data (unpack("a16"x(length($_[0])/16)."a*",$_[0])) {
        my($len)=length($data);
        if ($len == 16) {
            @array = unpack('N4', $data);
            $format="0x%08x (%05d)   %08x %08x %08x %08x   %s\n";
        } else {
            @array = unpack('C*', $data);
            $_ = sprintf "%2.2x", $_ for @array;
            push(@array, '  ') while $len++ < 16;
            $format="0x%08x (%05d)" .
               "   %s%s%s%s %s%s%s%s %s%s%s%s %s%s%s%s   %s\n";
        } 
        $data =~ tr/\0-\37\177-\377/./;
        printf $format,$offset,$offset,@array,$data;
        $offset += 16;
    }
}

# Read in a binary file and do a hdump.
my $filename = $ARGV[0];
my $buffer;
open(FH, $filename);
binmode FH;
while(read(FH, $buffer, 1024) != 0){
	hdump($buffer);
}
close(FH);

