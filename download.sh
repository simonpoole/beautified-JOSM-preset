#!/bin/bash


sed "s=ICONPATH:=https://raw.githubusercontent.com/simonpoole/beautified-JOSM-preset/master/icons/png/=" < master_preset.xml | sed "s/ICONTYPE/png/" > download_preset.xml
