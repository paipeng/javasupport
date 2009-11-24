require 'find'
dir = ARGV.shift || "."
search = ARGV.shift
# Seperate into two to have better performance.
if(search.nil?)
	Find.find(dir) do |path|
		puts path
	end
else
	search_re = Regexp.new(search)
	Find.find(dir) do |path|
		puts path if path =~ search_re
	end
end
