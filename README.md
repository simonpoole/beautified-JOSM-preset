Beautified JOSM preset
======================

This started off as a project to rework the [original JOSM preset](https://josm.openstreetmap.de/browser/josm/trunk/data/defaultpresets.xml) to use icons that work better as UI elements for use in [vespucci](https://github.com/MarcusWolschon/osmeditor4android), but has since evolved to be improved in many other aspects.

The icons have been replaced with ones from http://www.sjjb.co.uk/mapicons/introduction ,  http://osm-icons.org, https://github.com/gmgeo/osmic  and a handful of ones created by myself. 

The links to original icons remain if they currently don't have a replacement, current count is roughly 90 icons that either have not been replaced or are missing. 

The preset file contained at last count 880 individual object presets vs 724 in the original. Extensions to the original JOSM schema are documented on the [vespucci site](http://vespucci.io/tutorials/presets/).

The scripts

 * vespucci.sh will produce a version (vespucci_preset.xml) of the file suitable for inclusion in a build
 * download.sh wil produce a version with URLs to this site for downloading
 * vespucci_zip.sh will produce a zip (vespucci_zip.zip) suitable for downloading and installing
 * josm_zip.sh will produce a zip (josm.zip) suitable for downloading and installing with extensions to the JOSM xml schema removed

The scripts require `xmlstarlet` (and you will need `rsvg-convert`to generate the icons) and don't have any error checking and should be executed form the top level directory. Output is written to the `gen` directory.

The presets are being translated on transifex [here](https://www.transifex.com/openstreetmap/presets/).

Please follow us on [twitter](https://twitter.com/search?q=vespucci_editor) for updates.
