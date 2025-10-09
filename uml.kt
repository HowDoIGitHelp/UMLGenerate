@file:JvmName("UmlKt")
@file:Suppress("NO_REFLECTION_IN_CLASS_PATH")

package umlGenerate

import java.io.File
import kotlin.reflect.KCallable
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty
import kotlin.reflect.KType

fun shortType(type: KType?): String {
    when (type) {
        null -> return ""
        else -> {
            val classifier = type.classifier.toString().split(".").last()
            val typeArgs = type.arguments
            val shortArgs = typeArgs.map {
                shortType(it.type)
            }

            val argument = when {
                shortArgs.isEmpty() -> ""
                else -> shortArgs.joinToString(",", "<", ">")
            }

            return "$classifier$argument"
        }
    }
}

fun asArgMember(param: KParameter): String =
    "${param.name}: ${shortType(param.type)}"

fun paramString(function: KCallable<*>): String =
    function.parameters.filter {
        it.name != null
    }.joinToString(separator = ", ", transform = ::asArgMember)

fun asUMLMember(member: KCallable<*>): String {
    val vPrefix = when (member.visibility?.name) {
        "PRIVATE" -> "-"
        "PUBLIC" -> "+"
        "PROTECTED" -> "#"
        else -> ""
    }
    val suffix = when(member) {
        is KFunction -> "(${paramString(member)})"
        else -> ""
    }
    val shortReturnType = shortType(member.returnType)
    val returnType = if (shortReturnType == "Unit") "" else ": $shortReturnType"
    return "$vPrefix ${member.name}$suffix$returnType"
}

fun umlMembers(clazz: KClass<*>): String {
    val declaredMembers = declaredProperties(clazz) + declaredMethods(clazz)
    val propertiesPlusMethods = declaredMembers.joinToString(
        separator = "\n    ",
        prefix = "    ",
        transform = ::asUMLMember
    )
    return listOf(constructors(clazz), propertiesPlusMethods)
        .filter(String::isNotEmpty)
        .joinToString("\n")
}

fun declaredMethods(clazz: KClass<*>): List<KFunction<*>> {
    val methodNames = clazz.java.declaredMethods.toList().map{ it.name }
    return clazz.members.filter {
        it.name in methodNames
    }.filterIsInstance<KFunction<*>>()
}

fun declaredProperties(clazz: KClass<*>): List<KProperty<*>> {
    val fieldNames = clazz.java.declaredFields.toList().map{ it.name }
    return clazz.members.filter {
        it.name in fieldNames
    }.filterIsInstance<KProperty<*>>()
}

fun constructors(clazz: KClass<*>): String =
    clazz.constructors.joinToString("\n") {
        "    ${shortType(clazz)}(${paramString(it)})"
    }

fun shortType(clazz: KClass<*>): String =
    clazz.simpleName ?: "InvalidName"

fun superTypedPrefix(clazz: KClass<*>): String =
    when {
        clazz.java.isInterface -> " ..|> ${shortType(clazz)}"
        else -> " --|> ${shortType(clazz)}"
    }

fun relationships(clazz: KClass<*>): String =
    clazz.supertypes.filter {
        shortType(it) != "Any"
    }.fold("") { acc, it ->
        when (val superClass = it.classifier) {
            is KClass<*> -> "$acc\n${shortType(clazz)}${superTypedPrefix(superClass)}"
            else -> "$acc\n${shortType(clazz)} --|> UnknownClassName"
        }
    }.drop(1)

fun header(clazz: KClass<*>): String =
    when {
        clazz.java.isInterface -> "interface ${shortType(clazz)}"
        clazz.isAbstract -> "abstract class ${shortType(clazz)}"
        else -> "class ${shortType(clazz)}"
    }

fun classDiagram(clazz: KClass<*>): String =
    "${header(clazz)}\n{\n${umlMembers(clazz)}\n}\n${relationships(clazz)}\n\n"

fun dependencySet(clazz: KClass<*>): Set<KClass<*>> {
    val dependencies: MutableSet<KClass<*>> = mutableSetOf()

    declaredMethods(clazz).forEach { method ->
        dependencies.addAll(method.parameters.map {
            it.type.classifier
        }.filterIsInstance<KClass<*>>())
        dependencies.addAll(listOf(method.returnType.classifier).filterIsInstance<KClass<*>>())
    }

    dependencies.addAll(declaredProperties(clazz)
        .map{ it.returnType.classifier }
        .filterIsInstance<KClass<*>>())

    return dependencies.toSet()
}

fun dependencyRelationships(clazz: KClass<*>, relevantClasses: Set<KClass<*>>) : String =
    dependencySet(clazz).intersect(relevantClasses).joinToString("\n") {
        "${shortType(clazz)} ..> ${shortType(it)}"
    }

fun uml(classes: List<KClass<*>>): String {
    val classSet = classes.toSet()
    val classDiagrams = classes.map(::classDiagram).fold("", String::plus)
    val dependencies = classes.map {
        dependencyRelationships(it, classSet.minusElement(it))
    }.filter(String::isNotEmpty).joinToString("\n")

    return "@startuml\n$classDiagrams$dependencies\n@enduml"
}

fun main(args: Array<String>) {
    val relevantClasses = args.dropLast(1).map {
        try {
            Class.forName(it).kotlin
        } catch (exception: ClassNotFoundException) {
            println("Class not found: $it, skipping.")
            return@map null
        }
    }.filterNotNull()

    val umlFile = File(args.last())
    umlFile.writeText(uml(relevantClasses))

}