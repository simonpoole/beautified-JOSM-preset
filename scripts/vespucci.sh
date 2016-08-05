#!/bin/bash


sed "s=ICONPATH:=icons/png/=" < master_preset.xml | sed "s/ICONTYPE/png/" > gen/vespucci_preset.xml
cd ../icons/png
ls *.png | zip -@ ../../gen/vespucci_icons.zip 
