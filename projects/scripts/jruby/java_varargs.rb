require 'java'
import java.lang.System
import java.lang.Double
#System.out.printf("%6f", rand)
#System.out.printf("%6f", Double.new(rand))
#System.out.printf("%6f", [rand])
System.out.printf("%6f\n", [rand].to_java)
System.out.printf("%6f %6f %6f\n", [rand, rand, rand].to_java)
