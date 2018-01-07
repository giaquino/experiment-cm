package com.giaquino.cm

import java.io.File

object TestUtil {

    fun loadFileFromResource(name: String): File {
        return File(javaClass.classLoader.getResource(name).path)
    }
}