Beautified JOSM preset
======================

This started off as a project to rework the [original JOSM preset](https://josm.openstreetmap.de/browser/josm/trunk/data/defaultpresets.xml) to use icons that work better as UI elements for use in [vespucci](https://github.com/MarcusWolschon/osmeditor4android), but has since evolved to be improved in many other aspects.

The icons have been replaced with ones from http://www.sjjb.co.uk/mapicons/introduction ,  http://osm-icons.org, https://github.com/gmgeo/osmic  and a handful of ones created by myself. 

The links to original icons remain if they currently don't have a replacement, current count is roughly 90 icons that either have not been replaced or are missing. 

The preset file contained at last count over 880 individual object presets vs 724 in the original. Extensions to the original JOSM schema are documented on the [vespucci site](http://vespucci.io/tutorials/presets/).

Due to the touchy nature of the subject, presets for military objects have been moved out of the default preset to [military preset](http://simonpoole.github.io/military-preset/).

Building the actual preset files is done with gradle and should work on both unixy operations systems and windows, the "generateAllPresetTypes" task will generate the variants in the `gen` directory.

Building requires `xmlstarlet` installed and on your path. 

The icons are currently assumed to be in icons/png, generating the icons from SVG requires `rsvg-convert`to be installed and sh to be present (we will likely move this to gradle too).

The presets are being translated on transifex [here](https://www.transifex.com/openstreetmap/presets/).

Please follow us on [twitter](https://twitter.com/search?q=vespucci_editor) for updates.
