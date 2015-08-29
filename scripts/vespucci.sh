#!/bin/bash


sed "s=ICONPATH:=icons/png/=" < master_preset.xml | sed "s/ICONTYPE/png/" > gen/vespucci_preset.xml
