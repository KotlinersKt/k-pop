package com.kotlinerskt.kpop

import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSNode
import com.google.devtools.ksp.symbol.Modifier
import com.google.devtools.ksp.visitor.KSDefaultVisitor

class ValidateMethodVisitor(
    private val predicate: KSFunctionDeclaration.() -> Boolean
) : KSDefaultVisitor<Unit, Boolean>() {
    override fun defaultHandler(node: KSNode, data: Unit): Boolean = false

    override fun visitFunctionDeclaration(function: KSFunctionDeclaration, data: Unit): Boolean {
        return function.predicate()
    }
}
