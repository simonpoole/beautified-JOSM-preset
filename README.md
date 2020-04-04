[![Build Status](https://travis-ci.org/simonpoole/beautified-JOSM-preset.svg?branch=master)](https://travis-ci.org/simonpoole/beautified-JOSM-preset)


Beautified JOSM preset
======================

This started off as a project to rework the [original JOSM preset](https://josm.openstreetmap.de/browser/josm/trunk/data/defaultpresets.xml) to use icons that work better as UI elements for use in [vespucci](https://github.com/MarcusWolschon/osmeditor4android), but has since evolved to be improved in many other aspects.

The icons have been replaced with ones from http://www.sjjb.co.uk/mapicons/introduction ,  http://osm-icons.org, https://github.com/gmgeo/osmic  and a handful of ones created by myself. 

The links to original icons remain if they currently don't have a replacement, current count is roughly 90 icons that either have not been replaced or are missing. 

The preset file contained at last count a good 1'000 individual object presets vs 724 in the original. Extensions to the original JOSM schema are documented on the [vespucci site](http://vespucci.io/tutorials/presets/).

Due to the touchy nature of the subject, presets for military objects have been moved out of the default preset to [military preset](http://simonpoole.github.io/military-preset/).

Building the actual preset files is done with gradle and should work on both unixy operating systems and windows, the build task will generate the variants in the `gen` directory.

Building requires `xmlstarlet` installed and on your path.

The PNG icons are currently assumed to be in icons/png, generating them from SVG requires `rsvg-convert` to be installed. Currently the `recolour`and `generatePngs`tasks need to executed, when appropriate, manually.

The presets are being translated on transifex [here](https://www.transifex.com/openstreetmap/presets/), to retrieve the translations you will need to install and setup the [transifex command line client](https://docs.transifex.com/client/introduction). You only need the to set up the login, you can and should use the language mapping file from this repository.

Please follow us on [twitter](https://twitter.com/search?q=vespucci_editor) for updates.
