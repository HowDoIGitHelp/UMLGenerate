package umlGenerate

abstract class MutableListAggregate {
    protected fun <T> addToMemberCollection(
        instance: T,
        memberCollection: MutableList<T>,
        set: T.() -> Unit
    ): T {
        instance.set()
        memberCollection.add(instance)
        return instance
    }
}

class Source {
    lateinit var filename: String
}

class SourceDir: MutableListAggregate() {
    lateinit var path: String
    lateinit var sources: MutableList<Source>

    fun source(set: Source.() -> String): Source {
        val newSource = Source()
        newSource.filename = newSource.set()
        return newSource
    }
}

class IncludedClass {
    lateinit var name: String
}

class Package(var name: String): MutableListAggregate() {
    lateinit var includedClasses: MutableList<IncludedClass>

    fun includedClass(set: IncludedClass.() -> String): IncludedClass {
        val newIncludedClass = IncludedClass()
        newIncludedClass.name = newIncludedClass.set()
        return newIncludedClass
    }
}

open class Diagram(name: String): MutableListAggregate() {
    lateinit var sourceDirs: MutableList<SourceDir>
    lateinit var packages: MutableList<Package>
    lateinit var umlOutputDir: String
    var umlOutput: String = "$name.uml"
    lateinit var svgOutputDir: String
    var svgOutputFilename: String = "$name.svg"
    var rengenerate = false

    fun packageSpec(name: String, set: Package.() -> Unit) = addToMemberCollection(Package(name), packages, set)
    fun sourceDir(set: SourceDir.() -> Unit) = addToMemberCollection(SourceDir(), sourceDirs, set)

    fun source(set: Source.() -> String): Source {
        val newSource = Source()
        newSource.filename = newSource.set()
        return newSource
    }
}

class DiagramSources: MutableListAggregate() {
    lateinit var diagrams: MutableList<Diagram>
    var umlOutputDir: String? = null
    var svgOutputDir: String? = null
    var regenerate = false
    fun diagram(name: String, set: Diagram.() -> Unit): Diagram {
        val newDiagram = Diagram(name)
        newDiagram.umlOutputDir = umlOutputDir ?: ""
        newDiagram.svgOutputDir = svgOutputDir ?: ""
        return addToMemberCollection(newDiagram, diagrams, set)
    }
}

fun diagramSources(set: DiagramSources.() -> Unit): DiagramSources {
    val newDiagramSources = DiagramSources()
    newDiagramSources.set()
    return newDiagramSources
}




fun main() {
    val diagramSources =
        diagramSources {
            regenerate = true
            diagram("Diagram Name") {
                packageSpec("package") {
                    includedClass { "Class1" }
                    includedClass { "Class2" }
                }
                sourceDir {
                    source { "source.kt" }
                    source { "source.kt" }
                }
            }
        }
}