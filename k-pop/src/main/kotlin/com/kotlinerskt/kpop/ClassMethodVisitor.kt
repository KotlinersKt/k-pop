package com.kotlinerskt.kpop

import com.google.devtools.ksp.getDeclaredFunctions
import com.google.devtools.ksp.symbol.*
import com.google.devtools.ksp.visitor.KSDefaultVisitor

class ClassMethodVisitor : KSDefaultVisitor<KSNodesList, KSNodesList>() {
    private val methodsVisitor = ValidateMethodVisitor {
        simpleName.asString().lowercase() == "oncreate"
                && isOverride()
                && parameters.size > 1
    }

    private fun KSFunctionDeclaration.isOverride(): Boolean =
        modifiers.contains(Modifier.OVERRIDE) || annotations.any {
            it.shortName.asString() == Override::class.simpleName
        }

    override fun defaultHandler(node: KSNode, data: KSNodesList) = data

    override fun visitClassDeclaration(
        classDeclaration: KSClassDeclaration,
        data: KSNodesList
    ): KSNodesList {
        if (classDeclaration.classKind != ClassKind.CLASS) return data

        if (classDeclaration.hasSuperType("android.app.Activity")) {
            val hasWrongOnCreateMethod =
                classDeclaration.getDeclaredFunctions().any { it.accept(methodsVisitor, Unit) }

            if (hasWrongOnCreateMethod) return data + listOf(classDeclaration)
        }

        return data
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
