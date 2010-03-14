path = ARGV.shift || '.'
puts `dir #{path}| ruby slice.rb 7 -3 | ruby column.rb -1 -1`
