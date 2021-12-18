[![build status](https://github.com/simonpoole/beautified-JOSM-preset/actions/workflows/javalib.yml/badge.svg)](https://github.com/simonpoole/beautified-JOSM-prest/actions) [![Donate with liberapay]( https://img.shields.io/liberapay/patrons/SimonPoole.svg?logo=liberapay)](https://liberapay.com/SimonPoole/donate)

# Beautified JOSM preset

## Introduction

This started off as a project to rework the [original JOSM preset](https://josm.openstreetmap.de/browser/josm/trunk/data/defaultpresets.xml) to use icons that work better as UI elements for use in [Vespucci](https://github.com/MarcusWolschon/osmeditor4android), but has since evolved to be improved in many other aspects.

## Icons

The icons have been replaced with ones from http://www.sjjb.co.uk/mapicons/introduction , http://osm-icons.org, https://github.com/gmgeo/osmic and a handful of ones created by myself. The build process a JOSM compatible version of the preset file with references to the original SVG icons and one with the new icons.

The links to original icons remain if they currently don't have a replacement, current count is roughly 90 icons that either have not been replaced or are missing.

## Count of object presets

The preset file contained at last count a good 1'000 individual object presets vs 724 in the original.

## Exclusion of military objects

Due to the touchy nature of the subject, presets for military objects have been moved out of the default preset to [military preset](http://simonpoole.github.io/military-preset/).

## Information for developers

### Understanding the preset schema

Vespucci uses JOSM-style tagging presets defined in an XML config file (`master_preset.xml`). Read about the format here:

- https://wiki.openstreetmap.org/wiki/Customising_JOSM_Presets
- https://josm.openstreetmap.de/wiki/TaggingPresets

However, Vespucci ignores some settings and adds some of its own. Differences to the original JOSM schema are documented on the [Vespucci site](http://vespucci.io/tutorials/presets/). Note that the build process creates preset files that can be used both with Vespucci and JOSM.

### Building the preset files

Building the actual preset files is done with gradle and should work on both unixy operating systems and windows, the build task will generate the variants in the `gen` directory.

Building requires `xmlstarlet` installed and on your path.

The PNG icons are currently assumed to be in icons/png, generating them from SVG requires `rsvg-convert` to be installed. Currently the `recolour`and `generatePngs`tasks need to executed, when appropriate, manually.

### Translations

The presets are being translated on transifex [here](https://www.transifex.com/openstreetmap/presets/), to retrieve the translations you will need to install and setup the [transifex command line client](https://docs.transifex.com/client/introduction). You only need to set up your transifex login, you can and should use the language mapping file from this repository.

### Area styling

Styling of areas as displayed in Vespucci is defined not in the presets, but by [Vespucci itself](https://github.com/MarcusWolschon/osmeditor4android/blob/master/src/main/assets/styles/Color-round-no-mp.xml#L62) - see `<feature type="way" tags="landuse=forest" minVisibleZoom="10" color="8800BE00" />` in that file.

## Information on updates

Please follow us on [twitter](https://twitter.com/search?q=vespucci_editor) for updates.
