package com.cs.common.utils

import java.io.File

/**
 *
 * @author  ChenSen
 * @date  2021/1/13
 * @desc
 **/

object Files {

    fun save(path: String, content: String) {
        File(path).run {
            if (!exists()) {
                createNewFile()
            }
            writeText(content)
        }
    }

    fun read(path: String): String {
        val file = File(path)
        return if (!file.exists()) {
            ""
        } else {
            file.readText()
        }
    }

}