Dir['D:/source/journals/*'].entries.each { |name|
  if File.basename(name) =~ /^tech.*/
    new_name = name.sub(/tech-/, '')
    puts "Renaming #{name} to #{new_name}"
    File.rename(name, new_name)
  end
}
