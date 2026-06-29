package com.softtech.plateausuperapp

object PlateauNativeConstants {

    init {
        System.loadLibrary("plateau_native")
    }

    @JvmStatic
    external fun baseDomain(): String

    @JvmStatic
    external fun certificate(): String

    @JvmStatic
    external fun certificateId(): String

    @JvmStatic
    external fun serviceBaseUrl(): String

    @JvmStatic
    external fun jsonBaseUrl(): String
}
