package com.kotlinerskt.kpop

import com.google.devtools.ksp.symbol.KSFile
import kotlinx.html.dom.document


typealias NonCareFiles = List<Pair<KSFile, KSNodesList>>
typealias OffenderFiles = List<Pair<KSFile, KSNodesList>>

fun createDoc(partitionedFiles: Pair<NonCareFiles, OffenderFiles>) {

    val documentFile = document {

    }
}