package com.kotlinerskt.kpop

import com.google.devtools.ksp.getDeclaredFunctions
import com.google.devtools.ksp.symbol.*
import com.google.devtools.ksp.visitor.KSDefaultVisitor

class ClassMethodVisitor : KSDefaultVisitor<Unit, ClassOffender?>() {
    private val methodsVisitor = ValidateMethodVisitor {
        simpleName.asString().lowercase() == "oncreate"
                && isOverride()
                && parameters.size > 1
    }

    private fun KSFunctionDeclaration.isOverride(): Boolean =
        modifiers.contains(Modifier.OVERRIDE) || annotations.any {
            it.shortName.asString() == Override::class.simpleName
        }

    override fun defaultHandler(node: KSNode, data: Unit): ClassOffender? = null

    override fun visitClassDeclaration(
        classDeclaration: KSClassDeclaration,
        data: Unit
    ): ClassOffender? {
        if (classDeclaration.classKind != ClassKind.CLASS) return null

        if (classDeclaration.hasSuperType("android.app.Activity")) {
            val offenderOnCreateMethods =
                classDeclaration.getDeclaredFunctions().map { it.accept(methodsVisitor, Unit) }
                    .filterNotNull().toList()
            if (offenderOnCreateMethods.isNotEmpty()) return ClassOffender(
                classDeclaration,
                offenderOnCreateMethods
            )

        }

        return null
    }

    private fun KSClassDeclaration.hasSuperType(
        qualifiedName: String,
        kind: ClassKind = ClassKind.CLASS,
    ): Boolean =
        // class F : [...] <- supertypes
        superTypes.map { it.resolve() }
            .filter { it.declaration is KSClassDeclaration }
            .map { it.declaration as KSClassDeclaration }
            .filter { it.classKind == kind }
            .any {
                it.qualifiedName?.asString() == qualifiedName || it.hasSuperType(
                    qualifiedName,
                    kind
                )
            }

}
