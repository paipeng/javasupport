# Create new hash
Hash["a", 100, "b", 200]       #=> {"a"=>100, "b"=>200}
Hash["a" => 100, "b" => 200]   #=> {"a"=>100, "b"=>200}
{ "a" => 100, "b" => 200 }     #=> {"a"=>100, "b"=>200}

# Array to Hash
irb(main):001:0> a = [1, 2, 3]
=> [1, 2, 3]
irb(main):002:0> a2 = a.map { |v| [v, v*2] }
=> [[1, 2], [2, 4], [3, 6]]
irb(main):007:0> Hash[*a2.flatten]            # You need to flatten a first!
=> {1=>2, 2=>4, 3=>6}                         # Hash.[] is same as as { key, value, ...}

