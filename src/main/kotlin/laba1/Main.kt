package laba1

import laba1.address.*

fun main() {
    val stringOfAddresses: String =
        "1. 189765, city Moscow, street Korablestroiteley, h. 3" + System.lineSeparator() +
                "2. 123111, city SPB, street Bolshoy, h. 11" + System.lineSeparator() +
                "3. 189765, city Moscow, street Leninsky, h. 2" + System.lineSeparator() +
                "4. 101789, city Chicago, street Cad, h. 67" + System.lineSeparator() +
                "5. 109991, city Omsk, street Teatralnaya, h. 7" + System.lineSeparator() +
                "6. 188646, city New York, street Wim, h. 677"

    val listOfAddresses = parseAddresses(stringOfAddresses)

    println("Addresses:")
    listOfAddresses.forEachIndexed { index, it -> println("${index + 1}. ${it.address}") }

    println("\nAddresses with maximum index:")
    val maxIndex = listOfAddresses.maxOfOrNull { it.index }
    listOfAddresses.forEachIndexed { index, it -> if (it.index == maxIndex) println("${index + 1}. ${it.address}") }

    println("\nAddresses with minimum index:")
    val minIndex = listOfAddresses.minOfOrNull { it.index }
    listOfAddresses.forEachIndexed { index, it -> if (it.index == minIndex) println("${index + 1}. ${it.address}") }

    println("\nAddress with shortest street name:")
    val minStreetName = listOfAddresses.minOfOrNull { it.street.length }
    listOfAddresses.forEachIndexed { index, it -> if (it.street.length == minStreetName) println("${index + 1}. ${it.address}") }

    println("\nAddress with longest street name:")
    val maxStreetName = listOfAddresses.maxOfOrNull { it.street.length }
    listOfAddresses.forEachIndexed { index, it -> if (it.street.length == maxStreetName) print("${index + 1}. ${it.address}") }
}