require 'find'
require 'fileutils'
dir = ARGV[0] || "."
Find.find(dir) do |path|
	if FileTest.directory?(path) && File.basename(path) == ".svn"
		FileUtils::rm_rf(path, :verbose=>true)
		Find.prune
	end
end