import org.codehaus.groovy.runtime.*
use(TimeCategory){
    println 2.minutes.toMilliseconds()
}
println(2 * 60 * 1000)
