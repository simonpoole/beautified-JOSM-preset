#!/bin/bash

pushd . > /dev/null
cd `dirname $BASH_SOURCE` > /dev/null
BASEFOLDER=`pwd`;
popd  > /dev/null
BASEFOLDER=`dirname $BASEFOLDER`

${BASEFOLDER}/tools/generatepng.sh svg-osm-icons  
${BASEFOLDER}/tools/generatepng.sh svg-sjjb
${BASEFOLDER}/tools/generatepng.sh svg-osmic
${BASEFOLDER}/tools/generatepng-signs.sh

