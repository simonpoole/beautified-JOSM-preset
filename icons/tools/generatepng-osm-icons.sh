#!/bin/bash

pushd . > /dev/null
cd `dirname $BASH_SOURCE` > /dev/null
BASEFOLDER=`pwd`;
popd  > /dev/null
BASEFOLDER=`dirname $BASEFOLDER`

TYPES=(             'accommodation' 'amenity' 'barrier' 'education' 'food'    'health'  'landuse' 'money'   'natural'	'place_of_worship' 'poi'	'power'    'shopping' 'sport'   'tourist' 'transport' 'water'	'highway')
FORGROUND_COLOURS=( '#0092DA'       '#734A08' '#666666' '#39AC39'   '#734A08' '#DA0092' '#999999' '#000000' '#999999'	'#000000'          '#000000'	'#8e7409'  '#AC39AC'  '#39AC39' '#734A08' '#0092DA'   '#0092DA' '#666666')

SIZES=( 32 )

SVGFOLDER=${BASEFOLDER}/svg-osm-icons/
OUTPUTFOLDER=${BASEFOLDER}/png/

if [ ! -d "${OUTPUTFOLDER}" ]; then
  mkdir ${OUTPUTFOLDER}
fi

for (( i = 0 ; i < ${#TYPES[@]} ; i++ )) do

  if  [ "$1" == "" -o "$1" == "${TYPES[i]}" ]; then

    echo "On: ${TYPES[i]}"

    for FILE in $SVGFOLDER${TYPES[i]}/*.svg; do

      BASENAME=${FILE##/*/}
      BASENAME=${OUTPUTFOLDER}${TYPES[i]}_${BASENAME%.*}

      for (( j = 0 ; j < ${#SIZES[@]} ; j++ )) do
        ${BASEFOLDER}/tools/recolourtopng.sh ${FILE} ${FORGROUND_COLOURS[i]} ${FORGROUND_COLOURS[i]} '#ffffff' ${SIZES[j]} ${BASENAME}
      done

    done

  fi

done
