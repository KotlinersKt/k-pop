package com.kotlinerskt.kpop

import com.google.devtools.ksp.isPublic
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSDeclaration
import com.google.devtools.ksp.symbol.KSFile
import com.google.devtools.ksp.symbol.KSNode
import com.google.devtools.ksp.symbol.KSVisitor
import com.google.devtools.ksp.visitor.KSDefaultVisitor

typealias KSNodesList = List<KSNode>

class PublicSymbolFileVisitor(
    private val visitor: KSVisitor<KSNodesList, KSNodesList>,
    private val filterPredicate: (KSDeclaration) -> Boolean,
) : KSDefaultVisitor<KSNodesList, KSNodesList>() {
    override fun defaultHandler(node: KSNode, data: KSNodesList) = data

    override fun visitFile(file: KSFile, data: KSNodesList): KSNodesList {
        return file.declarations
            .filter { it.isPublic() && filterPredicate(it) }
            .fold(data) { acc, ksDeclaration -> ksDeclaration.accept(visitor, acc) }
    }
}
