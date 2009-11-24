import sweet.helper.Benchmark

Benchmark.measure{ (1 to 5000).foreach{ i => Math.log(i) } }
println("="*79)

Benchmark.bm{ bm => 
	(1 to 20).foreach{ i =>
		bm.measure("Math.log"){ (1 to 5000).foreach{ i => Math.log(i) } }
	}
	bm.printtotal
	bm.printavg
}
println("="*79)

Benchmark.bm{ bm =>  
	bm.measure("while:"){ var sum = 0; var i = 0; while(i < 100){ sum += i; i+=1 } }
	bm.measure("foreach:"){ var sum = 0; (1 to 100).foreach{ i => sum += i } }
	bm.measure("foldRight:"){ val sum = (1 to 100).foldRight(0)(_+_); () }
	bm.measure("for-comp:"){ var sum = 0; for(i <- 1 to 100){ sum += i } }
	bm.printtotal
	bm.printavg
}
println("="*79)