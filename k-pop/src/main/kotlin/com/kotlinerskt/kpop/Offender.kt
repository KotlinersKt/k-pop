package com.kotlinerskt.kpop

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFile
import com.google.devtools.ksp.symbol.KSFunctionDeclaration

data class FileOffender(
    val fileDeclaration: KSFile,
    val offenderClasses: List<ClassOffender>,
)

data class ClassOffender(
    val declaration: KSClassDeclaration,
    val offenderMethods: List<KSFunctionDeclaration>
)