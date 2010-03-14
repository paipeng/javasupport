# delete file/dir recursively.
require 'fileutils'
FileUtils::rm_rf(ARGV, :verbose => true)