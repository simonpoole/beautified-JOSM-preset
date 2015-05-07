#!/bin/bash


sed "s=ICONPATH:==" < master_preset.xml | sed "s/ICONTYPE/png/" > vespucci_zip_preset.xml
rm  vespucci_zip.zip
zip vespucci_zip.zip vespucci_zip_preset.xml
cd icons/png
ls *.png | zip -@ ../../vespucci_zip.zip 
