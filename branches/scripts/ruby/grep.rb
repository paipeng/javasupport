require 'find'
search = ARGV.shift
dir = ARGV.shift || "."

search_re = Regexp.new(search)
Find.find(dir) do |path|
	if(File.file?(path))
		File.open(path) do |f|
			f.grep(search_re) do |line|
				print("#{path} : #{line}")
			end
		end
	end
end

