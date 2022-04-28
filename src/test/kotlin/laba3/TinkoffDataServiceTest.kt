package laba3

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class TinkoffDataServiceTest {

    @Test
    fun addContact() {
        val newData = TinkoffDataService()
        val vanya = Person("Vanya", "Ivanov")

        newData.addPerson(vanya)
        newData.addContact(vanya, Contact.Phone("1234556", PhoneType.Home))

        assertTrue(newData.getPersonContacts(vanya).contains(Contact.Phone("1234556", PhoneType.Home)))
    }

    @Test
    fun removeContact() {
        val newData = TinkoffDataService()
        val vanya = Person("Vanya", "Ivanov")

        newData.addPerson(vanya)
        newData.addContact(vanya, Contact.Phone("1234556", PhoneType.Home))
        newData.removeContact(vanya, Contact.Phone("1234556", PhoneType.Home))

        assertTrue(newData.getPersonContacts(vanya).isEmpty())
    }

    @Test
    fun addPerson() {
        val newData = TinkoffDataService()

        val vanya = Person("Vanya", "Ivanov")
        val katya = Person("Katya", "F")

        newData.addPerson(vanya)

        assertTrue(newData.getAllPersons().contains(vanya))
        assertFalse(newData.getAllPersons().contains(katya))
    }

    @Test
    fun removePerson() {
        val newData = TinkoffDataService()
        val vanya = Person("Vanya", "Ivanov")

        newData.addPerson(vanya)
        newData.removePerson(vanya)

        assertTrue(newData.getAllPersons().isEmpty())
    }

    @Test
    fun addPhone() {
        val newData = TinkoffDataService()
        val vanya = Person("Vanya", "Ivanov")

        newData.addPerson(vanya)
        newData.addPhone(vanya, "1234556", PhoneType.Home)

        assertTrue(newData.getPersonContacts(vanya).contains(Contact.Phone("1234556", PhoneType.Home)))
    }

    @Test
    fun addEmail() {
        val newData = TinkoffDataService()
        val vanya = Person("Vanya", "Ivanov")

        newData.addPerson(vanya)
        newData.addEmail(vanya, "vanya@yandex.ru")

        assertTrue(newData.getPersonContacts(vanya).contains(Contact.Email("yandex")))
    }

    @Test
    fun addAddress() {
        val newData = TinkoffDataService()
        val vanya = Person("Vanya", "Ivanov")

        newData.addPerson(vanya)
        newData.addAddress(vanya, "city Chicago")

        assertTrue(newData.getPersonContacts(vanya).contains(Contact.Address("city Chicago")))
    }

    @Test
    fun addLink() {
        val newData = TinkoffDataService()
        val vanya = Person("Vanya", "Ivanov")

        newData.addPerson(vanya)
        newData.addLink(vanya, "vk", "vk.com/vanya")

        assertTrue(newData.getPersonContacts(vanya).contains(Contact.Link("vk", "vk.com/vanya")))
    }

    @Test
    fun getPersonPhones() {
        val newData = TinkoffDataService()
        val vanya = Person("Vanya", "Ivanov")

        newData.addPerson(vanya)
        newData.addPhone(vanya, "+79046136405", PhoneType.Moblie)
        newData.addPhone(vanya, "+78005553535", PhoneType.Moblie)
        newData.addPhone(vanya, "1234567", PhoneType.Work)
        newData.addPhone(vanya, "9876543", PhoneType.Home)

        val list = mutableListOf<Contact.Phone>()
        list.add(Contact.Phone("+79046136405", PhoneType.Moblie))
        list.add(Contact.Phone("+78005553535", PhoneType.Moblie))
        list.add(Contact.Phone("1234567", PhoneType.Work))
        list.add(Contact.Phone("9876543", PhoneType.Home))

        assertEquals(list, newData.getPersonPhones(vanya))
    }

    @Test
    fun getPersonEmails() {
        val newData = TinkoffDataService()
        val vanya = Person("Vanya", "Ivanov")

        newData.addPerson(vanya)
        newData.addEmail(vanya, "vano@gmail.com")
        newData.addEmail(vanya, "master_van@mail.ru")

        val list = mutableListOf<Contact.Email>()
        list.add(Contact.Email("vano@gmail.com"))
        list.add(Contact.Email("master_van@mail.ru"))

        assertEquals(list, newData.getPersonEmails(vanya))
    }

    @Test
    fun getPersonLinks() {
        val newData = TinkoffDataService()
        val vanya = Person("Vanya", "Ivanov")

        newData.addPerson(vanya)
        newData.addLink(vanya, "vk", "vk.com/firstpage")
        newData.addLink(vanya, "vk", "vk.com/secondpage")

        val list = mutableListOf<Contact.Link>()
        list.add(Contact.Link("vk", "vk.com/firstpage"))
        list.add(Contact.Link("vk", "vk.com/secondpage"))

        assertEquals(list, newData.getPersonLinks(vanya))
    }

    @Test
    fun getPersonContacts() {
        val newData = TinkoffDataService()
        val vanya = Person("Vanya", "Ivanov")

        newData.addPerson(vanya)
        newData.addEmail(vanya, "vano@gmail.com")
        newData.addPhone(vanya, "+79046136405", PhoneType.Moblie)
        newData.addAddress(vanya, "street BigStreet")

        val list = mutableListOf<Contact>()
        list.add(Contact.Email("vano@gmail.com"))
        list.add(Contact.Phone("+79046136405", PhoneType.Moblie))
        list.add(Contact.Address("street BigStreet"))

        assertEquals(list, newData.getPersonContacts(vanya))
    }

    @Test
    fun getAllPersons() {
        val newData = TinkoffDataService()
        newData.addPerson(Person("Vanya", "Ivanov"))
        newData.addPerson(Person("Katya", "Kate"))
        newData.addPerson(Person("Nastya", "Nast"))

        val list = mutableListOf<Person>()
        list.add(Person("Vanya", "Ivanov"))
        list.add(Person("Katya", "Kate"))
        list.add(Person("Nastya", "Nast"))

        assertEquals(list, newData.getAllPersons())
    }

    @Test
    fun getAllContacts() {
        val newData = TinkoffDataService()
        newData.addPerson(Person("Vanya", "Ivanov"))
        newData.addPerson(Person("Katya", "Kate"))
        newData.addPerson(Person("Nastya", "Nast"))

        newData.addContact(Person("Vanya", "Ivanov"), Contact.Email("vanya@gmail.com"))
        newData.addContact(Person("Vanya", "Ivanov"), Contact.Email("ivan@gmail.com"))
        newData.addContact(Person("Katya", "Kate"), Contact.Email("katya@gmail.com"))
        newData.addContact(Person("Nastya", "Nast"), Contact.Email("nastya@gmail.com"))

        val list = mutableListOf<Person>()
        list.add(Person("Vanya", "Ivanov"))
        list.add(Person("Katya", "Kate"))
        list.add(Person("Nastya", "Nast"))

        val map = mutableMapOf<Person, List<Contact>>()
        map[Person("Vanya", "Ivanov")] =
            listOf<Contact>(Contact.Email("vanya@gmail.com"), Contact.Email("ivan@gmail.com"))
        map[Person("Katya", "Kate")] = listOf<Contact>(Contact.Email("katya@gmail.com"))
        map[Person("Nastya", "Nast")] = listOf<Contact>(Contact.Email("nastya@gmail.com"))

        assertEquals(map, newData.getAllContacts())
    }

    @Test
    fun getPersonByName() {
        val newData = TinkoffDataService()
        newData.addPerson(Person("Vanya", "Ivanov"))
        newData.addPerson(Person("Katya", "Ivanova"))
        newData.addPerson(Person("Nastya", "Nast"))

        val list = listOf(Person("Vanya", "Ivanov"), Person("Katya", "Ivanova"))

        assertEquals(list, newData.getPersonByName("Ivan"))
    }
}