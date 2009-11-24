files = ARGV[0..-2]
dest = ARGV[-1]
files.each do |fn|
	cmd = "svn move #{fn} #{dest}"
	puts "Exec #{cmd}"
	puts `#{cmd}`
end

