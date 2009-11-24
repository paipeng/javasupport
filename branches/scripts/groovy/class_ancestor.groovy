HashMap.metaClass.ancestors = {
    ret = [delegate]
    ret << delegate.superclass
    ret << delegate.interfaces
}
HashMap.class.ancestors().each{ cls ->
    println(cls)
    cls.methods.findAll{ m -> m.name.startsWith("get") }.each{ m ->
        println(m.name)
    }
}
