#!/bin/env ruby
h = Hash.new
sep = (ENV["OS"].include? "Windows") ? "\\" : "/"
lines = `svn st`
lines.grep(/^\?/).each { |ln|
  status = ln.split(/\s+/)
  #puts status
  words = status[1].split(sep)
  dir = words[0..-2].join(sep)
  ignore_name = words[-1]
  #puts dir
  if (! h.has_key? dir)
    h[dir] = []
  end
  h[dir] << ignore_name
}
h.each { |k, v|
  cmd = "svn ps svn:ignore '#{v.map{ |it| it.strip }.join("\n")}' '#{k}'"
  #puts cmd
  puts system(cmd)                                        
}                                          


##!/bin/bash
#svn st | grep '?' | ruby -e '
#h = Hash.new
#sep = (ENV["OS"].include? "Windows") ? "\\" : "/"
#$stdin.each { |ln|
#  status = ln.split(/\s+./)
#  words = status[1].split(sep)
#  dir = words[0..-2].join(sep)
#  ignore_name = words[-1]
#  #puts dir
#  if (! h.has_key? dir)
#    h[dir] = []
#  end
#  h[dir] << ignore_name
#}
##puts h
#h.each { |k, v|
#  nl = "\n"
#  cmd = "svn ps svn:ignore \"#{v.join()}\" \"#{k}\""
#  #puts cmd
#  puts system(cmd)                                        
#}
#'                                                                 

