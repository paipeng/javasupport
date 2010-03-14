# Created on 11/12/2009 by Zemian Deng
#
# Parse the JBOSS_HOME/jar-versions.xml and show jars listing
# by it's specTitle attr (components)

require 'rexml/document'

jarsMap = {}

xml = REXML::Document.new(File.open("/opt/jboss/jar-versions.xml"))
jarsElements = xml.root.elements
jarsElements.each do |jar|
  specTitle = jar.attributes['specTitle']
  jarName = jar.attributes['name']
  
  if not jarsMap.has_key?(specTitle)
    jarsMap[specTitle] = []
  end
  jarsMap[specTitle] << jarName
end

jarsMap.each do |k,v|
  puts "#{k} (#{v.size} jars):"
  puts "  #{v}"
end

puts "Total #{jarsElements.size} jars"
