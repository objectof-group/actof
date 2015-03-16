#!/usr/bin/ruby

#remove old pngs
`rm *.png`

#find all svgs
svgs = `find -name '*.svg'`.strip.split("\n")

svgs.each{|svg|
	`rsvg #{svg} #{svg[0..-5]}.png`
}

