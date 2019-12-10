SUMMARY = "Generators for the Web Platform for Embedded Framework"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=1fe8768cbb5fd322f7d50656133549de"

PR = "r0"
PV = "3.0+git${SRCPV}"
S = "${WORKDIR}/git"

SRC_URI = "git://github.com/WebPlatformForEmbedded/WPEFramework.git;protocol=git;branch=development/bram/tools-host-package"
SRC_URI[md5sum] = "42b518b9ccd6852d1d709749bc96cb70"
SRC_URI[sha256sum] = "f3c45b121cf6257eafabdc3a8008763aed1cd7da06dbabc59a9e4d2a5e4e6697"

# This should be hard linked to the same version as wpeframework
SRCREV = "c77e79bf119ce6dca3b9dd6eb45a43bb87c5d5cb"

inherit cmake pkgconfig native pythonnative

DEPENDS = "\
    python-native \
    python-jsonref-native \
"

OECMAKE_SOURCEPATH = "${WORKDIR}/git/Tools"

FILES_${PN} += "${datadir}/*/Modules/*.cmake"
