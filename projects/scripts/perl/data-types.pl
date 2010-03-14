# About numbers
$n0 = 1;
$n1 = 3;
$n2 = 0.33333;
$n3 = 1/3;
print("$n0 / $n1 ==> ", $n0 / $n1, "\n");
print("$n0 / $n1 * $n1 ==> ", $n0 / $n1 * $n1, "\n");
print("$n3 ==> ", $n3, "\n");
print("$n0 / $n1 == $n3 ==> ", $n0 / $n1 == $n3, "\n");
print("$n2 == $n3 ==> ", $n2 == $n3, "\n");

# Array of numbers
@n_ls = (0, 1, 2, 3, 4);
print(@n_ls . "\n");    # array size is passed to print func!
print("@n_ls" . "\n");  # interpretered array in string is passed to print func,  elements with SPACE. 
print(@n_ls, "\n");     # direct array is passed to print func, element without SPACE.
print("size: " . @n_ls . "\n");
print("size - 1: " . $#n_ls . "\n");
@n_ls2 = (0, 1, 2, 3, 4);

# About string
$s1 = "a";
$s1 .= "b";
$s1 .= "c";
print("$s1\n");

# Array of String
@s_ls = ('A'..'Z');
print(@s_ls, "\n");
print(@s_ls[0..2], "\n");  # => ABC
print(join("/", @s_ls), "\n");
print(split(/\//, join("/", @s_ls)), "\n");

# Hash
%h1 = ("a" => 1, "b" => 2, "c" => 3);
%h2 = (a => 1, b => 2, c => 3);
%fruit_color = ("apple", "red", "banana", "yellow");
print(%h, "\n");
print(%fruit_color, "\n");
print($h1{"a"}, "\n");
print($h1{"A"}, "\n");   # empty

# Debugging Array/Hash
use Data::Dumper;
print(Dumper(@s_ls));
print(Dumper(%h1));

# empty collection.
my %h = ();
print "Empty hash: ", Dumper(%h), "\n";
my @a = ();
print "Empty array: ", Dumper(@a), "\n";

# More on Array
my @animals = ("camel", "llama", "owl");
my @numbers = (23, 42, 69);
my @mixed   = ("camel", 42, 1.23);
print $animals[0];     # => camel
print "\n";
print $animals[1];     # => llama
print "\n";
print $mixed[$#mixed]; # => 1.23
print "\n";

# array slicing
print @animals[0..2];
print "\n";
print sort @animals;
print "\n";
print reverse sort @animals;
print "\n";

# More on Hash
my %fruit_color = (
        apple  => "red",
        banana => "yellow",
    );
print "More on Hashes\n";
print $fruit_color{"apple"};
print "\n";
my @fruits = keys %fruit_color;
my @colors = values %fruit_color;
print Dumper(@fruits);
print Dumper(@colors);

# nested hash and be accessed using "->"
my $variables = {
        scalar  =>  {
                     description => "single item",
                     sigil => '$',
                    },
        array   =>  {
                     description => "ordered list of items",
                     sigil => '@',
                    },
        hash    =>  {
                     description => "key/value pairs",
                     sigil => '%',
                    },
    };
print "Scalars begin with a $variables->{'scalar'}->{'sigil'}\n";

# Gotcha and notes
# * Array slicing variables needs to start with "@", not "$".
# * Don't depends on "@ary" output to be consistent.
# * @h and %h can co-exists with different namesapce!
# * Hash string key can be without quote!
