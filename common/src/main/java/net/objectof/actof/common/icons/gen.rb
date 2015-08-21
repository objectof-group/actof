#!/usr/bin/ruby

#remove old pngs
`rm *.png`

#find all svgs
svgs = `find -name '*.svg'`.strip.split("\n")

svgs.each{|svg|
	puts "rendering " + svg
	`rsvg #{svg} #{svg[0..-5]}.png`
}


button = `find ./button -name '*.svg'`.strip.split("\n").map{|f| f[9..-5].gsub("-", "_").upcase}
toolbar = `find ./toolbar -name '*.svg'`.strip.split("\n").map{|f| f[10..-5].gsub("-", "_").upcase}

icons = (button & toolbar).sort

out = ""
out += "package net.objectof.actof.common.icons;\n"
out += "\n"
out += "public enum Icon {\n"
out += "\t" + icons.inject{|a, b| a + ",\n\t" + b}
out += "\n}"

File.open("Icon.java", 'w') { |file| file.write(out) }
