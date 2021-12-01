package com.kotlinerskt.kpop

import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSNode
import com.kotlinerskt.kpop.visitors.ClassMethodVisitor
import com.kotlinerskt.kpop.visitors.PublicSymbolFileVisitor
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ksp.KotlinPoetKspPreview
import com.squareup.kotlinpoet.ksp.writeTo

typealias LoggerFun = (String, KSNode?) -> Unit

@KotlinPoetKspPreview
class KPopProcessor(
    kspLogger: KSPLogger,
    private val codeGenerator: CodeGenerator,
    private val options: Map<String, String>,
) : SymbolProcessor {

    private val logger: LoggerFun by lazy {
        val strict = options.getOrElse("strict") {
            kspLogger.warn(""""strict" option neither set to "true" or "false". Defaulting to "false" """)

            "false"
        }.toBooleanOr(false)

        if (strict) kspLogger::error else kspLogger::warn
    }

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val x = {
            val y = FileSpec.builder("com.kotlinerskt.kpop.generate", "Testeando")
                .addImport("org.junit.jupiter.api", "Assertions.assertEquals")
                .addType(
                    TypeSpec.classBuilder("Testeando")
                        .addFunction(
                            FunSpec.builder("x")
                                .addAnnotation(
                                    AnnotationSpec.builder(ClassName("org.junit.jupiter.api", "Test")).build()
                                )
                                .addStatement("assertEquals(2, 5)")
                                .build()
                        )
                        .build()
                )
                .build()
            y.writeTo(codeGenerator, Dependencies(false))
        }
        try {
            x()
        } catch (ignored: FileAlreadyExistsException) {

        }

        val fileVisitor = PublicSymbolFileVisitor(ClassMethodVisitor()) {
            it is KSClassDeclaration
        }

        val partitionedFiles = resolver.getAllFiles()
            .map { it.accept(fileVisitor, Unit) }
            .filterNotNull()
            .filter { it.offenderClasses.isNotEmpty() }
            .toList()

        partitionedFiles.forEach {
            logger(
                "File: `${it.fileDeclaration}` contains `Activity` with wrong `onCreate`: ${it.offenderClasses}",
                it.offenderClasses.first().offenderMethods.first()
            )
        }

        val genDoc = options.getOrElse("gen_doc") { "false" }
            .toBooleanOr(false)
        if (genDoc) {
            val doc = createHtmlDoc(partitionedFiles)
            val path = options.getOrElse("path") { TODO("Break or find local project path") }
            createFile(doc, path) { filePath ->
                logger("K-pop report write: $filePath", null)
            }
        }

        return emptyList()
    }

    private fun String.toBooleanOr(other: Boolean): Boolean = toBooleanStrictOrNull() ?: other
}
