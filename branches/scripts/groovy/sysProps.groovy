def props = System.properties
if (args.length == 0) {
  props.each{ k, v -> println("$k : $v") }
} else {
  props.keySet().findAll{ it.contains(args[0]) }.each{ k -> println("$k : ${props[k]}") }
}
