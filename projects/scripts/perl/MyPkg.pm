# About Perl Package/Module information
# * Default package name is "main", $::sail is equivalent to $main::sail
# * Package name is NOT relative, must use full qualified name, or it refer to current only.
# * Package name is usually same as filename.
# * Function starts with _ mean private function in package(just a convention though)
# * Variable __PACKAGE__ is for current package.
# * A package symbols hash table is named %MyPkg::CoolBean::
#     use Data::Dumper;
#     print Dumper(\%::)
# * BEGIN block can apprears more than once and get executed once
# * Package may act as a Class, inheritance is done through @ISA array.
# * Nested/Sub Package is created using MyPkg.pm then MyPkg/NestedPkg.pm, and replace "/" with
#   "::" when referencing in code.
# * Module needs to return TRUE(1) at the end of library defination!
# * After a package/module is imported, it's bareword name can be used as a reference with right
#   arrow: MyPkg->my_function()
# * Module hooks can be defined using @INC or object->INC function. It gets invoked during a "require" call.
# * To use a package "use MyPkg;"

# Module function/keywords
#  do, import, no, package, require, use
#
#  do - is same as "eval" with a filename, but more consise and keep track of filename.
#       "require" function use this to execute/eval a module definition.
#  import - use to export names into modules. use Exporter module to add names into @EXPORT_OK array
#           as export in a modules, which means(importing). err... confusing!
#           Exporter - Implements default import method for modules
#
#  require - demands that a library file be included if it hasn't already been included. file
#            extension ".pm" is assumed.
#  use - perform "require" and "import" into one. => BEGIN { require MyPkg; MyPkg->import( LIST ); }

package MyPkg;
print "I am current on ", __PACKAGE__, "\n";
sub get_foo{ "I am a foo function"; }