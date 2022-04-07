import laba1.address.*

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class AddressTest {

    @Test
    fun getAddress() {
        val address = Address(123456, "New York", "Main Street", 178)
        assertEquals(123456, address.index)
        assertEquals("New York", address.city)
        assertEquals("Main Street", address.street)
        assertEquals(178, address.house)
    }

    @Test
    fun parseAddresses() {
        val stringOfAddresses: String =
            "1. 189765, city Moscow, street Korablestroiteley, h. 3" + System.lineSeparator() +
                    "2. 123111, city SPB, street Bolshoy, h. 11" + System.lineSeparator() +
                    "3. 189765, city Moscow, street Leninsky, h. 2"

        val listOfAddresses = parseAddresses(stringOfAddresses)

        val address0 :Address = Address(189765, "Moscow", "Korablestroiteley", 3)
        val address1 :Address = Address(123111, "SPB", "Bolshoy", 11)
        val address2 :Address = Address(189765, "Moscow", "Leninsky", 2)

        assertEquals(address0, listOfAddresses[0])
        assertEquals(address1, listOfAddresses[1])
        assertEquals(address2, listOfAddresses[2])
    }
}