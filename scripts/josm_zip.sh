#!/bin/bash

sed "s=ICONPATH:==" < master_preset.xml | sed "s/ICONTYPE/png/" | xmlstarlet tr toJOSM.xslt > gen/josm_preset.xml
xmlstarlet val -s josm-preset-1.0.xlmns -e gen/josm_preset.xml || exit
cd gen
rm  josm.zip
zip josm.zip josm_preset.xml
cd ../icons/png
ls *.png | zip -@ ../../gen/josm.zip 
cd ../../i18n
ls *.po | zip -@ ../gen/josm.zip 

