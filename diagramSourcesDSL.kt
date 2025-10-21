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
    var sources: MutableList<Source> = mutableListOf()

    fun source(set: Source.() -> String): Source {
        val newSource = Source()
        newSource.filename = newSource.set()
        sources.add(newSource)
        return newSource
    }
}

class IncludedClass {
    lateinit var name: String
}

class Package(var name: String): MutableListAggregate() {
    var includedClasses: MutableList<IncludedClass> = mutableListOf()

    fun includedClass(set: IncludedClass.() -> String): IncludedClass {
        val newIncludedClass = IncludedClass()
        newIncludedClass.name = newIncludedClass.set()
        includedClasses.add(newIncludedClass)
        return newIncludedClass
    }
}

open class Diagram(name: String): MutableListAggregate() {
    var sourceDirs: MutableList<SourceDir> = mutableListOf()
    var packages: MutableList<Package> = mutableListOf()
    var sources: MutableList<Source> = mutableListOf()
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
        sources.add(newSource)
        return newSource
    }
}

class DiagramSources: MutableListAggregate() {
    var diagrams: MutableList<Diagram> = mutableListOf()
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




fun notmain() {
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