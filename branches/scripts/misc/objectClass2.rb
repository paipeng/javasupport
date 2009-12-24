module RRunnable
	def run
	end
end
module Ex7
  class Task
    include RRunnable
    def run
      puts "A Ruby module method"
    end
  end
  def self.test
    puts Task.ancestors.join(", ")

    task2 = Object.new
    class << task2
      include RRunnable
      def run
        puts "A Ruby module method - with anonymous class definition"
      end
    end
    puts task2.class.ancestors.join(", ")
  end
end

method = ARGV.shift || "Ex1::test"
eval method

