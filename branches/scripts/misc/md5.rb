
# One line example:
# ruyb -r'digest/md5' -e 'Digest::MD5.digest(File.read(ARGV[0])).each_byte { |b| printf("%x", b) }' /source/ruby/ruby-1.8.7-p248.zip

require 'digest/md5'
Digest::MD5.digest(File.read(ARGV[0])).each_byte { |b| printf("%x", b) }

