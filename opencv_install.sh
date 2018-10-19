#!/bin/bash
set -eux -o pipefail

OPENCV_VERSION=${OPENCV_VERSION:-3.4.1}
OPENCV_SRC=$(pwd)/opencv
OPENCV_BUILD=$(pwd)/opencv/build
OPENCV_CONTRIB_ROOT=$(pwd)/opencv_contrib/modules
OPENCV_CONTRIB=$(pwd)/opencv_contrib/modules
INSTALL_FLAG=$HOME/usr/installed-version/$OPENCV_VERSION
INSTALL_PREFIX=$HOME/usr

if [[ ! -e $INSTALL_FLAG ]]; then
    TMP=$(mktemp -d)

    wget https://github.com/opencv/opencv/archive/$OPENCV_VERSION.tar.gz -O opencv-$OPENCV_VERSION.tar.gz
    wget https://github.com/opencv/opencv_contrib/archive/$OPENCV_VERSION.tar.gz -O opencv_contrib-$OPENCV_VERSION.tar.gz

    tar xzf opencv-$OPENCV_VERSION.tar.gz
    tar xzf opencv_contrib-$OPENCV_VERSION.tar.gz

    rm opencv-$OPENCV_VERSION.tar.gz
    mv opencv-$OPENCV_VERSION $OPENCV_SRC

    rm opencv_contrib-$OPENCV_VERSION.tar.gz
    mv opencv_contrib-$OPENCV_VERSION $OPENCV_CONTRIB_ROOT

    mkdir -p $OPENCV_BUILD

    pushd $OPENCV_BUILD
    cmake \
        -D WITH_CUDA=OFF \
        -D BUILD_EXAMPLES=OFF \
        -D BUILD_TESTS=OFF \
        -D BUILD_PERF_TESTS=OFF  \
        -D BUILD_opencv_java=OFF \
        -D BUILD_opencv_python=OFF \
        -D BUILD_opencv_python2=OFF \
        -D BUILD_opencv_python3=OFF \
        -D OPENCV_EXTRA_MODULES_PATH=$OPENCV_CONTRIB \
        -D CMAKE_INSTALL_PREFIX=$INSTALL_PREFIX \
        -D CMAKE_BUILD_TYPE=Release \
        ..
    make install && sudo mkdir -p "$(dirname "$INSTALL_FLAG")" && sudo touch "$INSTALL_FLAG";
    popd
    touch $HOME/fresh-cache
fi

sudo cp -r $HOME/usr/include/* /usr/local/include/
sudo cp -r $HOME/usr/lib/* /usr/local/lib/
sudo ldconfig