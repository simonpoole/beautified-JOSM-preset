[![build status](https://github.com/simonpoole/beautified-JOSM-preset/actions/workflows/preset.yml/badge.svg)](https://github.com/simonpoole/beautified-JOSM-preset/actions) [![Donate with liberapay](https://img.shields.io/liberapay/patrons/SimonPoole.svg?logo=liberapay)](https://liberapay.com/SimonPoole/donate)

# Beautified JOSM preset

## Introduction

This started off as a project to rework the [original JOSM presets](https://josm.openstreetmap.de/browser/trunk/resources/data/defaultpresets.xml) to use icons that work better as UI elements for use in [Vespucci](https://github.com/MarcusWolschon/osmeditor4android), but has since evolved to be improved in many other aspects.

## Icons

The icons have been replaced with ones from http://www.sjjb.co.uk/mapicons/introduction, http://osm-icons.org, https://github.com/gmgeo/osmic and a handful of ones created by myself. The build process produces JOSM compatible versions of the preset file with references to the original SVG icons and one variant with the replacement icons.

The links to original icons remain if they currently don't have a replacement, current count is roughly 90 icons that either have not been replaced or are missing.

To make using the presets easier with the original JOSM icons we've added one level of indirection in that instead of directly containing a reference to an icon, we reference entries in _beautified_icons.mapping_ and _josm_icons.mapping_ that contain references to our and JOSM icons respectively.

## Count of object presets

The preset file contained at last count a good 1'000 individual object presets vs 724 in the original.

## Exclusion of military objects

Due to the touchy nature of the subject, presets for military objects have been moved out of the default preset to [military preset](http://simonpoole.github.io/military-preset/).

## Using in JOSM

You can link to the presets by using one of
 
https&#58;//github&#46;com/simonpoole/beautified-JOSM-preset/releases/latest/download/josm.zip 

https&#58;//github&#46;com/simonpoole/beautified-JOSM-preset/releases/latest/download/josm_orig_icons.zip (for a version that uses the JOSM icons) 

these will always return the current release of the specified file. 

## Information for developers

### Understanding the preset schema

Vespucci uses JOSM-style tagging presets defined in an XML config file (`master_preset.xml`). Read about the format here:

https://wiki.openstreetmap.org/wiki/Customising_JOSM_Presets

https://josm.openstreetmap.de/wiki/TaggingPresets

However, Vespucci ignores some settings and adds some of its own. Differences to the original JOSM schema are documented on the [Vespucci site](http://vespucci.io/tutorials/presets/). Note that the build process creates preset files that can be used both with Vespucci and JOSM.

### Building the preset files

Building the actual preset files is done with gradle and should work on both unixy operating systems and windows, the build task will generate the variants in the `gen` directory.

Building requires `xmlstarlet` and a perl installation, both should be on your path

The PNG icons are currently assumed to be in icons/png, generating them from SVG requires `rsvg-convert` to be installed. Currently the `recolour`and `generatePngs`tasks need to executed, when appropriate, manually.

### Translations

The presets are being translated on transifex [here](https://app.transifex.com/openstreetmap/presets), to retrieve the translations you will need to install and setup the [transifex command line client](https://docs.transifex.com/client/introduction). You only need to set up your transifex login, you can and should use the language mapping file from this repository.

## Information on updates

Please follow us on [mastodon](https://en.osm.town/@vespucci_editor) for updates.
