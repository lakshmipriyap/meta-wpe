SUMMARY = "WPE WebKit port pairs the WebKit engine with OpenGL-ES (OpenGL for Embedded Systems), \
           allowing embedders to create simple and performant systems based on Web platform technologies. \
           It is designed with hardware acceleration in mind, relying on EGL, the Wayland EGL platform, and OpenGL ES."
HOMEPAGE = "https://wpewebkit.org/"
LICENSE = "BSD & LGPLv2+"
LIC_FILES_CHKSUM = "file://Source/WebCore/LICENSE-LGPL-2.1;md5=a778a33ef338abbaf8b8a7c36b6eec80 "
PR = "r2"

DEPENDS += " \
    wpebackend \
    bison-native ccache-native glib-2.0-native gperf-native libxml2-native ninja-native ruby-native chrpath-replacement-native \
    cairo freetype glib-2.0 gnutls harfbuzz icu jpeg pcre sqlite3 zlib \
    libepoxy libpng libsoup-2.4 libwebp libxml2 libxslt \
    virtual/egl virtual/libgles2 \
"

PV = "2.20+git${SRCPV}"

# setup ccache-native
CCACHE = "${STAGING_DIR_NATIVE}${bindir}/ccache "

SRCREV ?= "d3c3f5df97ff93669877db3f1c5376bfcc16f3e8"
BASE_URI ?= "git://github.com/WebPlatformForEmbedded/WPEWebKit.git;protocol=git;branch=master"
SRC_URI = "${BASE_URI} \
           file://0001-Define-MESA_EGL_NO_X11_HEADERS-when-not-using-GLX.patch \
           file://0001-Fix-build-with-musl.patch \
           file://0002-include-GraphicsContext3D.h-for-DONT_CARE-definition.patch \
          "

S = "${WORKDIR}/git"

inherit cmake pkgconfig perlnative pythonnative

WPE_PLATFORM ?= "${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'westeros', '', d)}"
WPE_PLATFORM ?= "${@bb.utils.contains('virtual/egl', 'broadcom-refsw', 'nexus', '', d)}"
WPE_PLATFORM_x86 ?= "intelce"
WPE_PLATFORM ?= "egl"

PACKAGECONFIG ?= "2dcanvas deviceorientation fullscreenapi encryptedmedia fetchapi gamepad indexeddb logs mediasource mediastatistics notifications nativevideo sampling-profiler subtitle subtlecrypto video webaudio ${WPE_PLATFORM}"

# Mesa only offscreen target support for Westeros backend
# FIXME Needs to be moved to mesa backend
PACKAGECONFIG[westeros-mesa] = "-DUSE_WPEWEBKIT_BACKEND_WESTEROS_MESA=ON,,"

# WPE Platform specific switches
PACKAGECONFIG[intelce] = "-DUSE_WPEWEBKIT_PLATFORM_INTEL_CE=ON -DUSE_HOLE_PUNCH_GSTREAMER=ON,,"
PACKAGECONFIG[nexus] = "-DUSE_WPEWEBKIT_PLATFORM_BCM_NEXUS=ON -DUSE_HOLE_PUNCH_GSTREAMER=ON,,"
PACKAGECONFIG[qcomdb] = "-DUSE_WPEWEBKIT_PLATFORM_QCOM_DB=ON,,"
PACKAGECONFIG[egl] = "-DUSE_GSTREAMER_GL=ON,-DUSE_GSTREAMER_GL=OFF,virtual/egl"
PACKAGECONFIG[westeros] = "-DUSE_WPEWEBKIT_PLATFORM_WESTEROS=ON -DUSE_HOLE_PUNCH_GSTREAMER=ON -DUSE_WESTEROS_SINK=ON,,westeros"

# WPE features
PACKAGECONFIG[2dcanvas] = "-DENABLE_ACCELERATED_2D_CANVAS=ON,-DENABLE_ACCELERATED_2D_CANVAS=OFF,"
PACKAGECONFIG[deviceorientation] = "-DENABLE_DEVICE_ORIENTATION=ON,-DENABLE_DEVICE_ORIENTATION=OFF,"
PACKAGECONFIG[encryptedmedia] = "-DENABLE_ENCRYPTED_MEDIA=ON,-DENABLE_ENCRYPTED_MEDIA=OFF,libgcrypt"
PACKAGECONFIG[fetchapi] = "-DENABLE_FETCH_API=ON,-DENABLE_FETCH_API=OFF,"
PACKAGECONFIG[fullscreenapi] = "-DENABLE_FULLSCREEN_API=ON,-DENABLE_FULLSCREEN_API=OFF,"
PACKAGECONFIG[fusion] = "-DUSE_FUSION_API_GSTREAMER=ON,-DUSE_FUSION_API_GSTREAMER=OFF,"
PACKAGECONFIG[gamepad] = "-DENABLE_GAMEPAD=ON,-DENABLE_GAMEPAD=OFF,"
PACKAGECONFIG[geolocation] = "-DENABLE_GEOLOCATION=ON,-DENABLE_GEOLOCATION=OFF,geoclue"
PACKAGECONFIG[indexeddb] = "-DENABLE_INDEXED_DATABASE=ON,-DENABLE_INDEXED_DATABASE=OFF,"
PACKAGECONFIG[logs] = "-DLOG_DISABLED=OFF,-DLOG_DISABLED=ON,"
PACKAGECONFIG[mediasource] = "-DENABLE_MEDIA_SOURCE=ON,-DENABLE_MEDIA_SOURCE=OFF,gstreamer1.0 gstreamer1.0-plugins-good,${RDEPS_MEDIASOURCE}"
PACKAGECONFIG[mediastream] = "-DENABLE_MEDIA_STREAM=ON,-DENABLE_MEDIA_STREAM=OFF,openwebrtc"
PACKAGECONFIG[mediastatistics] = "-DENABLE_MEDIA_STATISTICS=ON,-DENABLE_MEDIA_STATISTICS=OFF,"
PACKAGECONFIG[nativeaudio] = "-DENABLE_NATIVE_AUDIO=ON,-DENABLE_NATIVE_AUDIO=OFF,"
PACKAGECONFIG[nativevideo] = "-DENABLE_NATIVE_VIDEO=ON,-DENABLE_NATIVE_VIDEO=OFF,"
PACKAGECONFIG[notifications] = "-DENABLE_NOTIFICATIONS=ON,-DENABLE_NOTIFICATIONS=OFF,"
PACKAGECONFIG[sampling-profiler] = "-DENABLE_SAMPLING_PROFILER=ON,-DENABLE_SAMPLING_PROFILER=OFF,"
PACKAGECONFIG[subtlecrypto] = "-DENABLE_SUBTLE_CRYPTO=ON,-DENABLE_SUBTLE_CRYPTO=OFF,"
PACKAGECONFIG[subtitle] = "-DENABLE_TEXT_SINK=ON,-DENABLE_TEXT_SINK=OFF,"
PACKAGECONFIG[touch] = "-DENABLE_TOUCH_EVENTS=ON,,"
PACKAGECONFIG[udev] = "-DUSE_WPEWEBKIT_INPUT_UDEV=ON,,udev"
PACKAGECONFIG[video] = "-DENABLE_VIDEO=ON -DENABLE_VIDEO_TRACK=ON,-DENABLE_VIDEO=OFF -DENABLE_VIDEO_TRACK=OFF,gstreamer1.0 gstreamer1.0-plugins-base gstreamer1.0-plugins-good gstreamer1.0-plugins-bad,${RDEPS_VIDEO}"
PACKAGECONFIG[webaudio] = "-DENABLE_WEB_AUDIO=ON,-DENABLE_WEB_AUDIO=OFF,gstreamer1.0 gstreamer1.0-plugins-base gstreamer1.0-plugins-good,${RDEPS_WEBAUDIO}"
PACKAGECONFIG[webcrypto] = "-DENABLE_WEB_CRYPTO=ON,-DENABLE_WEB_CRYPTO=OFF,libgcrypt libtasn1"
PACKAGECONFIG[woff2] = "-DUSE_WOFF2=ON,-DUSE_WOFF2=OFF,woff2"

# DRM
PACKAGECONFIG[opencdm] = "-DENABLE_OPENCDM=ON,-DENABLE_OPENCDM=OFF,wpeframework"
PACKAGECONFIG[playready] = "-DENABLE_PLAYREADY=ON,-DENABLE_PLAYREADY=OFF,playready"

PACKAGECONFIG[gst_httpsrc] = "-DUSE_GSTREAMER_WEBKIT_HTTP_SRC=ON,,"
PACKAGECONFIG[gst_holepunch] = "-DUSE_HOLE_PUNCH_GSTREAMER=ON,,"

EXTRA_OECMAKE += " \
    -DCMAKE_BUILD_TYPE=Release \
    -DCMAKE_COLOR_MAKEFILE=OFF \
    -DEXPORT_DEPRECATED_WEBKIT2_C_API=ON \
    -DBUILD_SHARED_LIBS=ON \
    -DPORT=WPE \
    -G Ninja \
"
EXTRANATIVEPATH += "chrpath-native"

# don't build debug
FULL_OPTIMIZATION_remove = "-g"

# JSC JIT on ARMv7 is better supported with Thumb2 instruction set.
ARM_INSTRUCTION_SET_armv7a = "thumb"
ARM_INSTRUCTION_SET_armv7r = "thumb"
ARM_INSTRUCTION_SET_armv7m = "thumb"
ARM_INSTRUCTION_SET_armv7ve = "thumb"

do_compile() {
    ${STAGING_BINDIR_NATIVE}/ninja ${PARALLEL_MAKE} -C ${B} libWPEWebKit.so libWPEWebInspectorResources.so WPEWebProcess WPENetworkProcess WPEStorageProcess WPEWebDriver
}

do_compile[progress] = "outof:^\[(\d+)/(\d+)\]\s+"

LEAD_SONAME = "libWPEWebKit.so"

PACKAGES =+ "${PN}-web-inspector-plugin"

FILES_${PN}-web-inspector-plugin += "${libdir}/libWPEWebInspectorResources.so"
INSANE_SKIP_${PN}-web-inspector-plugin = "dev-so"

PACKAGES =+ "${PN}-platform-plugin"

RDEPS_MEDIASOURCE = " \
    gstreamer1.0-plugins-good-isomp4 \
"

RDEPS_VIDEO = " \
    gstreamer1.0-plugins-base-app \
    gstreamer1.0-plugins-base-playback \
    gstreamer1.0-plugins-good-souphttpsrc \
"

RDEPS_WEBAUDIO = " \
    gstreamer1.0-plugins-good-wavparse \
"

# plugins-bad config option 'dash' -> gstreamer1.0-plugins-bad-dashdemux
# plugins-bad config option 'videoparsers' -> gstreamer1.0-plugins-bad-videoparsersbad

RDEPS_EXTRA = " \
    gstreamer1.0-plugins-base-audioconvert \
    gstreamer1.0-plugins-base-audioresample \
    gstreamer1.0-plugins-base-gio \
    gstreamer1.0-plugins-base-opus \
    gstreamer1.0-plugins-base-videoconvert \
    gstreamer1.0-plugins-base-videoscale \
    gstreamer1.0-plugins-base-volume \
    gstreamer1.0-plugins-base-typefindfunctions \
    gstreamer1.0-plugins-good-audiofx \
    gstreamer1.0-plugins-good-audioparsers \
    gstreamer1.0-plugins-good-autodetect \
    gstreamer1.0-plugins-good-avi \
    gstreamer1.0-plugins-good-deinterlace \
    gstreamer1.0-plugins-good-interleave \
    gstreamer1.0-plugins-good-matroska \
    gstreamer1.0-plugins-good-mpg123 \
    gstreamer1.0-plugins-bad-dashdemux \
    gstreamer1.0-plugins-bad-hls \
    gstreamer1.0-plugins-bad-mpegtsdemux \
    gstreamer1.0-plugins-bad-opusparse \
    gstreamer1.0-plugins-bad-smoothstreaming \
    gstreamer1.0-plugins-bad-videoparsersbad \
"

RDEPS_EXTRA_append_rpi = " \
    gstreamer1.0-omx \
    gstreamer1.0-plugins-bad-faad \
    gstreamer1.0-plugins-base-opengl \
"

# The RDEPS_EXTRA plugins are all required for certain media playback use cases,
# but have not yet been classified as being specific dependencies for video,
# webaudio or mediasource. Until that classification is done, add them all to
# each of the three groups...

RDEPS_MEDIASOURCE += "${RDEPS_EXTRA}"
RDEPS_VIDEO += "${RDEPS_EXTRA}"
RDEPS_WEBAUDIO += "${RDEPS_EXTRA}"

RRECOMMENDS_${PN} += " \
    ca-certificates \
    ttf-bitstream-vera \
    shared-mime-info \
    ${PN}-platform-plugin \
"
