package com.kotlinerskt.kpop

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSNode

typealias LoggerFun = (String, KSNode?) -> Unit

class KPopProcessor(
    kspLogger: KSPLogger,
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
