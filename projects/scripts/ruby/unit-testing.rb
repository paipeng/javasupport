require 'test/unit'
class TC_MyTest < Test::Unit::TestCase
	def test_fail
		assert(false, "Asssertion was false.")
	end
end

require 'test/unit/ui/console/testrunner'
Test::Unit::UI::Console::TestRunner.run(TC_MyTest)
