package com.kotlinerskt.kpop

import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSNode
import com.google.devtools.ksp.visitor.KSDefaultVisitor

class ValidateMethodVisitor(
    private val predicate: KSFunctionDeclaration.() -> Boolean
) : KSDefaultVisitor<Unit, KSFunctionDeclaration?>() {
    override fun defaultHandler(node: KSNode, data: Unit): KSFunctionDeclaration? = null

    override fun visitFunctionDeclaration(
        function: KSFunctionDeclaration,
        data: Unit
    ): KSFunctionDeclaration? {
        return if (function.predicate()) function else null
    }
}
