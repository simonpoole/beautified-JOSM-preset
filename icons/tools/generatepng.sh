#!/bin/bash

pushd . > /dev/null
cd `dirname $BASH_SOURCE` > /dev/null
BASEFOLDER=`pwd`;
popd  > /dev/null
BASEFOLDER=`dirname $BASEFOLDER`
SOURCEFOLDER=$1

TYPES=(             'accommodation' 'amenity' 'barrier'	'craft'		'education' 'emergency'	'food'    'health'  'highway'	'landuse' 'money'   'natural'	'man_made'	'military'	'office'	'osm'		'place_of_worship' 'poi'	'power'    'shopping' 'sport'   'tourist' 'transport' 'water')
FORGROUND_COLOURS=( '#0092DA'       '#734A08' '#666666' '#ff9933'	'#39AC39'   '#CE2029'	'#734A08' '#DA0092' '#666666'	'#999999' '#000000' '#999999'	'#8e7409'	'#556B2F'	'#ff3333'	'#000000'	'#000000'          '#000000'	'#8e7409'  '#AC39AC'  '#39AC39' '#734A08' '#0092DA'   '#0092DA' )

SIZES=( 48 )

SVGFOLDER=${BASEFOLDER}/${SOURCEFOLDER}/
OUTPUTFOLDER=${BASEFOLDER}/png/

if [ ! -d "${OUTPUTFOLDER}" ]; then
  mkdir ${OUTPUTFOLDER}
fi

for (( i = 0 ; i < ${#TYPES[@]} ; i++ )) do

    echo "On: ${TYPES[i]}"

    for FILE in $SVGFOLDER${TYPES[i]}/*.svg; do

      BASENAME=${FILE##/*/}
      BASENAME=${OUTPUTFOLDER}${TYPES[i]}_${BASENAME%.*}

      for (( j = 0 ; j < ${#SIZES[@]} ; j++ )) do
        ${BASEFOLDER}/tools/recolourtopng.sh ${FILE} ${FORGROUND_COLOURS[i]} ${FORGROUND_COLOURS[i]} '#ffffff' ${SIZES[j]} ${BASENAME}
      done

    done

done
