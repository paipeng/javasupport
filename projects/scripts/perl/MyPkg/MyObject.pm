package MyPkg::MyObject;

use Data::Dumper;

sub new {
	my $self  = shift;
	my $class = ref($self) || $self;
	return bless {}, $class;
}

sub my_method{
	print Dumper(\@_);	
}

1;
