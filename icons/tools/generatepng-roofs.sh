#!/bin/bash

pushd . > /dev/null
cd `dirname $BASH_SOURCE` > /dev/null
BASEFOLDER=`pwd`;
popd  > /dev/null
BASEFOLDER=`dirname $BASEFOLDER`

SIZES=( 48 )
SVGFOLDER=${BASEFOLDER}/svg-roofs/
OUTPUTFOLDER=${BASEFOLDER}/png/

if [ ! -d "${OUTPUTFOLDER}" ]; then
  mkdir ${OUTPUTFOLDER}
fi

for FILE in $SVGFOLDER/*.svg; do

      BASENAME=${FILE##/*/}
      BASENAME=${OUTPUTFOLDER}${BASENAME%.*}

      for (( j = 0 ; j < ${#SIZES[@]} ; j++ )) do
	echo  $FILE ${BASENAME}
	rsvg-convert -a -f png -h ${SIZES[j]} -o ${BASENAME}.png $FILE
      done

done
