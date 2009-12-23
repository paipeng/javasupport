## CASE 1: Explicitly loading jar file in source.
#=begin rdoc
#* Both require statements are needed.
# dengz1@ORLIW7MNDC91 /s $ jruby importJavaClass2.rb
#658350811
#589694937
#490753300
#=end
#require 'java'
#require 'd:\data\dengz1\.m2\repository\commons-lang\commons-lang\2.4\commons-lang-2.4.jar'
#import org.apache.commons.lang.math.RandomUtils
#3.times{ puts RandomUtils.nextInt }

# CASE 2: loading jar outside of the source file.
=begin rdoc
 dengz1@ORLIW7MNDC91 /s $ jruby importJavaClass2.rb
importJavaClass2.rb:17:in `require': no such file to load -- commons-lang-2.4 (LoadError)
        from importJavaClass2.rb:17
 dengz1@ORLIW7MNDC91 /s $ jruby -I'd:\data\dengz1\.m2\repository\commons-lang\commons-lang\2.4' importJavaClass2.rb
1322307787
1167704230
1195579364

NOTE This only works because package starts with "org", if you have custom package name it
doesn't work! See importJavaClass3.rb
=end
require 'java'
require 'commons-lang-2.4.jar'
import org.apache.commons.lang.math.RandomUtils
3.times{ puts RandomUtils.nextInt }

