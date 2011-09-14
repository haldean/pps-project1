#!/usr/bin/env python

import random

print """<?xml version="1.0" encoding="UTF-8"?> 
<java version="1.6.0_26" class="java.beans.XMLDecoder"> 
"""

for i in range(300):
  dangerous = random.choice([True, False, False, False])
  filename = dangerous and "shark.png" or "treasure.png"
  happiness = 2
  print """ <object class="isnork.sim.SeaLifePrototype"> 
  <void property="dangerous"> 
   <boolean>%s</boolean> 
  </void> 
  <void property="filename"> 
   <string>%s</string> 
  </void> 
  <void property="happiness"> 
   <int>%d</int> 
  </void> 
  <void property="maxCount"> 
   <int>2</int> 
  </void> 
  <void property="minCount"> 
   <int>1</int> 
  </void> 
  <void property="name"> 
   <string>Thing %d</string> 
  </void> 
  <void property="speed"> 
   <int>1</int> 
  </void> 
 </object>""" % (dangerous and "true" or "false", filename, happiness, i)

print '</java>'
