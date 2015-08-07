
== notes for generating pot file 

 sed "s/text=/presettext=/" < master_preset.xml
 xml2po  -o preset.pot bla.xml
 sed "s/bla.xml/master_preset.xml/g" < preset.pot > temp.pot
  sed "s/presettext/text/g" < temp.pot > preset.pot

