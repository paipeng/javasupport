package MyPkg::MyService;
use base MyPkg::MyObject;

sub new {
	my $self  = shift;
	my $class = ref($self) || $self;
	return bless {}, $class;
}

sub my_method{
	my ($self)  = @_;
	$self->SUPER::my_method();
	print __PACKAGE__, " my_method is invoked.\n";
}

1;
