=begin rdoc
* Both require statements are needed.
  Omiting the require jar and running this doesn't seem t work. 
  jruby -I'd:\data\dengz1\.m2\repository\commons-lang\commons-lang\2.4\commons-lang-2.4.jar' importJavaClass2.rb
=end

require 'java'
require 'd:\data\dengz1\.m2\repository\commons-lang\commons-lang\2.4\commons-lang-2.4.jar'
import org.apache.commons.lang.math.RandomUtils
3.times{ puts RandomUtils.nextInt }

