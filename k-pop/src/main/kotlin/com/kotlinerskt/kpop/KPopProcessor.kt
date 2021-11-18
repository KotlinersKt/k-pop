package com.kotlinerskt.kpop

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSNode
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.PrintWriter

typealias LoggerFun = (String, KSNode?) -> Unit

class KPopProcessor(
    kspLogger: KSPLogger,
    private val options: Map<String, String>,
) : SymbolProcessor {

    private val logger: LoggerFun by lazy {
        val strict = options.getOrElse("strict") {
            kspLogger.warn(""""strict" option neither set to "true" or "false". Defaulting to "false" """)

            "false"
        }.toBooleanStrictOrNull() ?: false

        if (strict) kspLogger::error else kspLogger::warn
    }

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val fileVisitor = PublicSymbolFileVisitor(ClassMethodVisitor()) {
            it is KSClassDeclaration
        }

        val partitionedFiles = resolver.getAllFiles().map {
            it.accept(fileVisitor, Unit)
        }
            .filterNotNull()
            .filter { it.offenderClasses.isNotEmpty() }

        partitionedFiles.forEach {
            logger.invoke(
                "File: `${it.fileDeclaration}` contains `Activity` with wrong `onCreate`: ${it.offenderClasses}",
                it.offenderClasses.first().offenderMethods.first()
            )
        }

        val doc = createHtmlDoc(partitionedFiles.toList())

        val path = options.getOrElse("path") { "/Users" }

        logger(doc, null)
        val file = File("$path/doc.html")
        file.printWriter()
            .use {
                it.append(doc)
            }

        logger(file.path, null)

        return emptyList()
    }
}

