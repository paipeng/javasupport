//closing BufferedStream also closes the FileStream!!!
try{
    String file = "c:\\test.txt"
    fr = new FileReader(file)
    br = new BufferedReader(fr)
    
    br.close()
    println(fr.read())
    println(fr.read())
}finally{
    br.close()
    fr.close()
}
