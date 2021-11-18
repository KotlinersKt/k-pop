package com.kotlinerskt.kpop

import com.google.devtools.ksp.symbol.FileLocation
import kotlinx.html.*
import kotlinx.html.stream.appendHTML
import java.io.File

fun createFile(document: String, path: String, block: (String) -> Unit) {
    val file = File("$path/doc.html")
    file.printWriter()
        .use {
            it.append(document)
        }
    block("file://${file.path}")
}

fun createHtmlDoc(partitionedFiles: List<FileOffender>): String {
    return buildString {
        appendHTML().html {
            body {
                h1 {
                    +"Offender files"
                }

                ul {
                    partitionedFiles.forEach { offenderFile ->
                        val file = offenderFile.fileDeclaration
                        val offendersMethods = offenderFile.offenderClasses.first().offenderMethods
                        offendersMethods.forEach { method ->
                            li {
                                +"File ${file.fileName}"
                                br
                                val methodLocation = (method.location as FileLocation)
                                +"Method ${method.simpleName.asString()}: ${methodLocation.filePath}:${methodLocation.lineNumber}"
                            }
                        }
                    }
                }
            }
        }
    }
}