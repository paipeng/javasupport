=begin
require 'benchmark'

n = 1000000
Benchmark.bm(7) do |x|
	x.report("for:")   { for i in 1..n; a = "1"; end }
	x.report("times:") { n.times do   ; a = "1"; end }
	x.report("upto:")  { 1.upto(n) do ; a = "1"; end }
end
=end


=begin
Zemian@Zemian-PC /s
$ ruby -v
ruby 1.8.7 (2008-08-11 patchlevel 72) [i386-cygwin]

Zemian@Zemian-PC /apps
$ jruby -v
jruby 1.4.0 (ruby 1.8.7 patchlevel 174) (2009-11-02 69fbfa3) (Java HotSpot(TM) Client VM 1.6.0_17) [x86-java]

Zemian@Zemian-PC /s
$ ruby forCompare.rb 
             user     system      total        real
for:     0.015000   0.000000   0.015000 (  0.012000)
times:   0.000000   0.000000   0.000000 (  0.012000)
upto:    0.016000   0.000000   0.016000 (  0.011000)
>total:  0.031000   0.000000   0.031000 (  0.035000)
>avg:    0.010333   0.000000   0.010333 (  0.011667)

Zemian@Zemian-PC /s
$ jruby forCompare.rb
             user     system      total        real
for:     0.080000   0.000000   0.080000 (  0.031000)
times:   0.013000   0.000000   0.013000 (  0.013000)
upto:    0.010000   0.000000   0.010000 (  0.010000)
>total:  0.103000   0.000000   0.103000 (  0.054000)
>avg:    0.034333   0.000000   0.034333 (  0.018000)

=end
require 'benchmark'
include Benchmark          # we need the CAPTION and FMTSTR constants

n = 50000
Benchmark.benchmark(" "*7 + CAPTION, 7, FMTSTR, ">total:", ">avg:") do |x|
	tf = x.report("for:")   { for i in 1..n; a = "1"; end }
	tt = x.report("times:") { n.times do   ; a = "1"; end }
	tu = x.report("upto:")  { 1.upto(n) do ; a = "1"; end }
	[tf+tt+tu, (tf+tt+tu)/3]
end

