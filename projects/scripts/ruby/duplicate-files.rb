require 'fileutils'
file = ARGV.shift
dest = ARGV.shift
n = (ARGV.shift || "1").to_i
n.times do |i|
	ext = File.extname(file)
	to = File.basename(file, ext) + "_" + i.to_s + ext
	destfile = File.join(dest, to)
	puts "Duplicating to #{destfile}"
	FileUtils.cp(file, destfile)
end
