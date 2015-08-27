
== notes for generating pot file 
sed "s/text=/presettext=/" < master_preset.xml > /tmp/master_preset.xml
cd /tmp
xml2po -o temp.pot master_preset.xml
sed "s/presettext/text/g" < temp.pot > preset.pot

