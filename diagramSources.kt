package umlGenerate

val umlSources = diagramSources {

    umlOutputDir = "umlOutputs/"
    svgOutputDir = umlOutputDir

    diagram("BooksBorrowing") {
        source { "/home/rub/Projects/lecturenotes/CMSC 23/solutions_and_tests/Kotlin/BooksBorrowing/solution.kt" }
        packageSpec("booksBorrowing") {
            includedClass { "LibraryCard" }
            includedClass { "BorrowableItem" }
            includedClass { "Periodical" }
            includedClass { "Book" }
            includedClass { "PC" }
        }
    }

    diagram("Shipment") {
        source { "/home/rub/Projects/lecturenotes/CMSC 23/solutions_and_tests/Kotlin/FactoryMethodPattern/shipment/solution.kt" }
        packageSpec("shipment") {
            includedClass { "Delivery" }
            includedClass { "StandardDelivery" }
            includedClass { "Shipment" }
            includedClass { "ExpressShipment" }
            includedClass { "ExpressDelivery" }
        }
    }

    diagram("ShipmentNoFactory") {
        source { "/home/rub/Projects/lecturenotes/CMSC 23/solutions_and_tests/Kotlin/FactoryMethodPattern/requestedFiles/requestedFile.kt" }
        packageSpec("requestedFile") {
            includedClass { "StandardDelivery" }
            includedClass { "Shipment" }
        }
    }

    diagram("TextBasedZeldaNoFactory") {
        source { "/home/rub/Projects/lecturenotes/CMSC 23/solutions_and_tests/Kotlin/AbstractFactoryPattern/requestedFiles/requestedFile.kt" }
        packageSpec("requestedFile") {
            includedClass { "Monster" }
            includedClass { "NormalBokoblin" }
            includedClass { "NormalMoblin" }
            includedClass { "NormalLizalflos" }
        }
    }

    diagram("TextBasedZelda") {
        source { "/home/rub/Projects/lecturenotes/CMSC 23/solutions_and_tests/Kotlin/AbstractFactoryPattern/tbzelda/solution.kt" }
        packageSpec("tbzelda") {
            includedClass { "Monster" }
            includedClass { "Bokoblin" }
            includedClass { "Moblin" }
            includedClass { "Lizalflos" }
            includedClass { "Dungeon" }
            includedClass { "NormalBokoblin" }
            includedClass { "NormalMoblin" }
            includedClass { "NormalLizalflos" }
            includedClass { "EasyDungeon" }
            includedClass { "BlueBokoblin" }
            includedClass { "BlueMoblin" }
            includedClass { "BlueLizalflos" }
            includedClass { "MediumDungeon" }
            includedClass { "SilverBokoblin" }
            includedClass { "SilverMoblin" }
            includedClass { "SilverLizalflos" }
            includedClass { "HardDungeon" }
        }
    }

    diagram("FactoryMethod") {
        source { "/home/rub/Projects/CMSC23CoursePack/Code/Kotlin Code/FactoryMethodPattern/factoryMethod/factoryMethod.kt" }
        packageSpec("factoryMethod") {
            includedClass { "Product" }
            includedClass { "DefaultProduct" }
            includedClass { "SpecialProductA" }
            includedClass { "SpecialProductB" }
            includedClass { "Factory" }
            includedClass { "SpecialFactoryA" }
            includedClass { "SpecialFactoryB" }
        }
    }

    diagram("AbstractFactory") {
        source { "/home/rub/Projects/CMSC23CoursePack/Code/Kotlin Code/AbstractFactoryPattern/abstractFactory/abstractFactory.kt" }
        packageSpec("abstractFactory") {
            includedClass { "ProductA" }
            includedClass { "ProductB" }
            includedClass { "ProductAVariant1" }
            includedClass { "ProductBVariant1" }
            includedClass { "ProductAVariant2" }
            includedClass { "ProductBVariant2" }
            includedClass { "AbstractFactory" }
            includedClass { "FactoryVariant1" }
            includedClass { "FactoryVariant2" }
        }
    }

    diagram("FormattedSentence") {
        source { "/home/rub/Projects/lecturenotes/CMSC 23/solutions_and_tests/Kotlin/DecoratorPattern/formattedSentence/solution.kt" }
        packageSpec("formattedSentence") {
            includedClass { "Sentence" }
            includedClass { "FormattedSentence" }
            includedClass { "BorderedSentence" }
            includedClass { "FancySentence" }
            includedClass { "UpperCaseSentence" }
        }
    }

    diagram("PrintableShipment") {
        source { "/home/rub/Projects/lecturenotes/CMSC 23/solutions_and_tests/Kotlin/AdapterPattern/printableShipment/solution.kt" }
        packageSpec("printableShipment") {
            includedClass { "Printable" }
            includedClass { "PrintableShipment" }
            includedClass { "Shipment" }
            includedClass { "ExpressShipment" }
        }
    }

    diagram("Adapter") {
        source { "/home/rub/Projects/CMSC23CoursePack/Code/Kotlin Code/AdapterPattern/adapter/adapter.kt" }
        packageSpec("adapter") {
            includedClass { "AbstractDependency" }
            includedClass { "RealDependency" }
            includedClass { "RequiredInterface" }
            includedClass { "Adapter" }
        }
    }

    diagram("Decorator") {
        source { "/home/rub/Projects/CMSC23CoursePack/Code/Kotlin Code/DecoratorPattern/decorator/decorator.kt" }
        packageSpec("decorator") {
            includedClass { "SimpleClass" }
            includedClass { "BaseDecorator" }
            includedClass { "Decorator1" }
            includedClass { "Decorator2" }
        }
    }

    diagram("FractionCalculator") {
        source { "/home/rub/Projects/lecturenotes/CMSC 23/solutions_and_tests/Kotlin/StrategyPattern/fractionCalculator/solution.kt" }
        packageSpec("fractionCalculator") {
            includedClass { "Calculation" }
            includedClass { "Addition" }
            includedClass { "Subtraction" }
            includedClass { "Multiplication" }
            includedClass { "Division" }
            includedClass { "Operation" }
        }
    }

}