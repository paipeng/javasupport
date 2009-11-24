# usage: ruby slice.rb [start] [end] [step]
# extract fix number of rows from STDIN
# eg: dir | ruby slice.rb 7 -3
lines = []
$stdin.each{ |ln| lines << ln }
size = lines.size
num   = (ARGV.shift || "0").to_i
limit = (ARGV.shift || size.to_s).to_i
step  = (ARGV.shift || "1").to_i
if(num < 0) 
	num = size + num
end
if(limit < 0) 
	limit = size + limit
end
num.step(limit, step){ |i| puts lines[i] }
