// Map
ls = ['one': [1,2,3], 'two': [7,8], 'three': [4,5,6]]
println ls.collect{ it.value.size() > 2 ? it : null }
println ls.findAll{ it.value.size() > 2 }
println ls.find{ it.value.size() > 2 }
println ls.every{ it.value.size() > 2 }
println("=" * 35)

// List
ls = (1..5).collect{ 'first_' + it }
ls += (6..10).collect{ 'second_' + it }
println ls.findAll{ it.split('_')[1].toInteger() > 8 }
println ls.findIndexOf{ it.split('_')[1].toInteger() > 8 }
println ls.findIndexValues{ it.split('_')[1].toInteger() > 8 }
println ls.grep(~/first_.*/)
