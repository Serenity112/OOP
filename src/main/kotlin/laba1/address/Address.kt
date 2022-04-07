package laba1.address

data class Address(val index: Int, val city: String, val street: String, val house: Int) {
    val address: String
        get() = "$index, city: $city, street: $street, house: $house"
}

fun parseAddresses(stringOfAddresses: String): List<Address> {
    val addressesList = mutableListOf<Address>()

    val particularAddresses = stringOfAddresses.split(System.lineSeparator()).toTypedArray()

    for (particularAddress: String in particularAddresses) {
        val addressData = particularAddress.split(',').toTypedArray()

        addressData[0] = addressData[0].substring(addressData[0].indexOf('.') + 2)

        val index: Int = addressData[0].toInt()

        val city: String = addressData[1]
            .replace("city", "")
            .trimStart()

        val street: String = addressData[2]
            .replace("street", "")
            .trimStart()

        val house: Int = addressData[3]
            .replace("h.", "")
            .trimStart()
            .toInt()

        addressesList.add(Address(index, city, street, house))

    }
    return addressesList
}