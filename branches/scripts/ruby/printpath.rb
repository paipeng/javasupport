# print Windows PATH elements per line
# @author Zemian Deng (dengz1) 12/11/2008

# work around to Windows case insenstive PATH var name.
key = ENV.keys.grep(/^path$/i).shift
puts ENV[key].split(File::PATH_SEPARATOR)