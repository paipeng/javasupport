require 'benchmark'

s = "Ex1::test"
s2 = "Ex2"
n = 10000
Benchmark.bm(25) do |x|
	x.report("substring_cmp") do
		1.upto(n) do 
			a = s +"::test" unless s[-6 .. -1] == "::test"
			a2 = s2 +"::test" unless s[-6 .. -1] == "::test"
		end
	end
	x.report("regexp_cmp") do
		1.upto(n) do 
			a = s + "::test" unless s =~ /::test$/
			a2 = s2 + "::test" unless s =~ /::test$/
		end
	end
	x.report("include?") do
		1.upto(n) do 
			a = s + "::test" unless s.include? "::test" 
			a2 = s2 + "::test" unless s.include? "::test" 
		end
	end
end

=begin

## Ruby 1.9.1 testing:
Zemian@Zemian-PC /s
$ ruby -v
ruby 1.9.1p376 (2009-12-07 revision 26041) [i386-cygwin]

Zemian@Zemian-PC /s
$ time ruby -e 'puts "hello"'
hello

real    0m0.179s
user    0m0.031s
sys     0m0.093s

Zemian@Zemian-PC /tmp
$ time ruby -e 'File.readlines("nums.txt").sort { |a,b| a.to_i <=> b.to_i }.each { |e| puts e }'

real    0m0.242s
user    0m0.093s
sys     0m0.076s





## Startup time compare.

$ python -V
Python 2.5.2

$ perl -v

This is perl, v5.10.1 (*) built for i686-cygwin-thread-multi-64int

$ jruby -v
jruby 1.4.0 (ruby 1.8.7 patchlevel 174) (2009-11-02 69fbfa3) (Java HotSpot(TM) Client VM 1.6.0_17) [x86-java]

$ ruby -v
ruby 1.8.7 (2008-08-11 patchlevel 72) [i386-cygwin]

$ java -version
java version "1.6.0_17"
Java(TM) SE Runtime Environment (build 1.6.0_17-b04)
Java HotSpot(TM) Client VM (build 14.3-b01, mixed mode, sharing)


$ time python -c 'print("hello\n")'
hello

real    0m0.146s
user    0m0.031s
sys     0m0.061s

$ time perl -e 'print "hello\n";'
hello

real    0m0.076s
user    0m0.015s
sys     0m0.031s

$ time ruby -e 'puts "hello"'
hello

real    0m0.097s
user    0m0.016s
sys     0m0.015s

$ time jruby -e 'puts "hello"'
hello

real    0m2.104s
user    0m0.091s
sys     0m0.517s

$ time jruby --1.9 -e 'puts "hello"'
hello

real    0m2.184s
user    0m0.122s
sys     0m0.427s

Zemian@Zemian-PC /s/myjava
$ time java -cp target/classes deng.myjava.Hello
hello

real    0m0.184s
user    0m0.015s
sys     0m0.031s


## Compare numeric sorting from file

Zemian@Zemian-PC /tmp
$ time python -c '
f = file("nums.txt", "r")
lines = [int(line) for line in f]
f.close()
lines.sort()
for e in lines:
  print(e)
'

real    0m0.186s
user    0m0.030s
sys     0m0.109s

Zemian@Zemian-PC /tmp
$ time cat nums.txt |sort -g

real    0m0.127s
user    0m0.046s
sys     0m0.061s

Zemian@Zemian-PC /tmp
$ time perl -e 'open(FH, "nums.txt"); @lines = <FH>; @lines = sort { $a <=> $b } @lines; print(@lines); close(FH);'

real    0m0.154s
user    0m0.031s
sys     0m0.047s


Zemian@Zemian-PC /tmp
$ ruby -e '(1..5000).to_a.shuffle.each { |e| puts e }' > nums.txt
$ time ruby -e 'File.readlines("nums.txt").sort { |a,b| a.to_i <=> b.to_i }.each { |e| puts e }'

real    0m0.192s
user    0m0.078s
sys     0m0.030s

Zemian@Zemian-PC /tmp
$ time jruby -e 'File.readlines("nums.txt").sort { |a,b| a.to_i <=> b.to_i }.each { |e| puts e }'

real    0m2.356s
user    0m0.197s
sys     0m0.364s

Zemian@Zemian-PC /tmp
$ time jruby --1.9 -e 'File.readlines("nums.txt").sort { |a,b| a.to_i <=> b.to_i }.each { |e| puts e }'

real    0m2.476s
user    0m0.151s
sys     0m0.381s

Zemian@Zemian-PC /s/myjava
$ time java -cp target/classes deng.myjava.SortNumsFile $(wpath /tmp/nums.txt)

real    0m0.353s
user    0m0.030s
sys     0m0.076s

=end

