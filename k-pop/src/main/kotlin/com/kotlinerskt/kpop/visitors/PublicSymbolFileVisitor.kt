package com.kotlinerskt.kpop.visitors

import com.google.devtools.ksp.isPublic
import com.google.devtools.ksp.symbol.KSDeclaration
import com.google.devtools.ksp.symbol.KSFile
import com.google.devtools.ksp.symbol.KSNode
import com.google.devtools.ksp.symbol.KSVisitor
import com.google.devtools.ksp.visitor.KSDefaultVisitor
import com.kotlinerskt.kpop.ClassOffender
import com.kotlinerskt.kpop.FileOffender

class PublicSymbolFileVisitor(
    private val visitor: KSVisitor<Unit, ClassOffender?>,
    private val filterPredicate: (KSDeclaration) -> Boolean,
) : KSDefaultVisitor<Unit, FileOffender?>() {
    override fun defaultHandler(node: KSNode, data: Unit): FileOffender? = null

    override fun visitFile(file: KSFile, data: Unit): FileOffender =
        FileOffender(
            file,
            file.declarations
                .filter { it.isPublic() && filterPredicate(it) }
                .fold(listOf()) { acc, ksDeclaration ->
                    val resultDeclaration = ksDeclaration.accept(visitor, Unit)
                    if (resultDeclaration != null) acc + listOf(resultDeclaration) else acc
                }
        )
}
