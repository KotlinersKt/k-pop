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
        }.toBooleanStrictOrNull() ?: false

        if (strict) kspLogger::error else kspLogger::warn
    }

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val fileVisitor = PublicSymbolFileVisitor(ClassMethodVisitor()) {
            it is KSClassDeclaration
        }

        val partitionedFiles = resolver.getAllFiles().map {
            Pair(it, it.accept(fileVisitor, emptyList()))
        }.partition { it.second.isEmpty() }

        partitionedFiles.second.forEach {
            logger.invoke(
                "File: `${it.first}` contains `Activity` with wrong `onCreate`: ${it.second}",
                it.second.first()
            )
        }

        val docGenerator = createDoc(partitionedFiles)

        return emptyList()
    }
}

