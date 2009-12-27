require 'java'

module Ex2
	include_class 'deng.myjava.Utils'
	def self.run 
		p Utils.takeArray([1,2,3,4,5].to_java)
		p Utils.takeArray(['a','b', 'c'].to_java(:string))
		p Utils.takeList([1,2,3,4,5])
		p Utils.takeList(['a','b', 'c'])
		#p Utils.takeMap({ :a => 1, :b => 2, :c => 3 }) # D'oh, jruby wont' convert :Symbol into java string.
		p Utils.takeMap({ 'a' => 1, 'b'=> 2, 'c' => 3 })
		p Utils.takeMap({ 'aa' => 1, 'bb'=> 2, 'cc' => 3 })
		p Utils.takeMap({ "a" => 1, "b"=> 2, "c" => 3 })
	end
end

module Ex1
	include_class 'deng.myjava.Utils'
	def self.run 
		task = Proc.new{ def run() puts "hello" end }
		#task.call
		Utils.printObject(task)
	end
end


eval(ARGV.shift || "Ex1::run")
