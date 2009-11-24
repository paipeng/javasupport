require 'benchmark'; 
Benchmark.bm(1) do |bm| bm.report('test'){ system(ARGV) } end