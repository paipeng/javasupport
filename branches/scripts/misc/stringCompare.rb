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
