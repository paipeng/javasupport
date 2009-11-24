# keyword/functions relate to name scoping:
# caller, import, local, my, our, state, package, use
# 
# local - A local modifies its listed variables to be "local" to the enclosing block, eval, or 
#         do FILE --and to any subroutine called from within that block.
#
#         A local just gives temporary values to global (meaning package) variables. It does 
#         not create a local variable.
#
#         In general, you should be using my instead of local, because it's faster and safer.
#
#         local is mostly used when the current value of a variable must be visible to 
#         called subroutines. (extending the current name scope to the called functions.)
#
# my -    A my declares the listed variables to be local (lexically) to the enclosing block, 
#         file, or eval. 
#
# our -   associates a simple name with a package variable in the current package 
#         for use within the current scope.

## Testing my scope
#use strict;
{
	my $foo = "foo";
	sub test{
		my $foo = "foox";
		print "Inside function \$foo ", $foo, "\n";
	}
	test();
	print "Outside function \$foo ", $foo, "\n";
}
print "Outside BLOCK \$foo ", $foo, "\n"; # => <empty>!!! will not error unless "use strict;" is enabled.

## Testing without local scope
$_ = "before ltest\n";
print;
sub ltest{
	$_ = "ltest\n";
}
ltest();
print;

## Testing with local scope
local $_ = "before ltest2\n";
print;
sub ltest2{
	#my $_ = "ltest2\n";	# NOPE, my can't be used on global var like $_
	local $_ = "ltest2\n";
}
ltest2();
print; # => before ltest2
       # notice that $_ inside function will not affect outside!!!
