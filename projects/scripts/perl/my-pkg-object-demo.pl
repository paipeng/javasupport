use MyPkg::MyObject;
use MyPkg::MyService;

my $obj = MyPkg::MyObject->new();
print $obj, "\n";
MyPkg::MyObject->my_method();
$obj->my_method();

my $obj2 = MyPkg::MyService->new();
$obj2->my_method();
