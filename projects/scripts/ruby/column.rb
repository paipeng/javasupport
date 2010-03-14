# usage: ruby column.rb [start] [end] [step]
# extract fix number of columns from STDIN
# eg: dir | ruby slice.rb 7 -3 | ruby column.rb -1 -1
lines = []
$stdin.each{ |ln| lines << ln.split(/\s+/) }
size = lines[0].size
num   = (ARGV.shift || "0").to_i
limit = (ARGV.shift || size.to_s).to_i
step  = (ARGV.shift || "1").to_i
if(num < 0) 
	num = size + num
end
if(limit < 0) 
	limit = size + limit
end
lines.each{ |cols|
	res = []
	num.step(limit, step){ |i| res << cols[i] }
	puts res.join(" ")
}
