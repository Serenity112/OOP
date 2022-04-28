package laba3

fun main() {
    val newData = TinkoffDataService()

    val Ivan = Person("Ivan", "Ivanov")
    val Kirill1 = Person("Kiirill", "Irlandov")
    val Kiirill2 = Person("Kiirill", "Ivanov")
    val Maria = Person("Maria", "Cringe")
    val Jhon = Person("Jhon", "Jhonov")

    newData.addPerson(Ivan)
    newData.addPerson(Kirill1)
    newData.addPerson(Kiirill2)
    newData.addPerson(Maria)
    newData.addPerson(Jhon)

    newData.addContact(Ivan, Contact.Email("ivan@gmail.com"))
    newData.addEmail(Ivan, "vanya@mail.ru")
    newData.addPhone(Ivan, "+71219283515", PhoneType.Moblie)
    newData.removeContact(Ivan,  Contact.Email("ivan@gmail.com"))

    newData.addContact(Kirill1, Contact.Phone("7651090", PhoneType.Home))
    newData.addPhone(Kirill1, "+79219780536", PhoneType.Moblie)
    newData.addEmail(Kirill1, "Kirill@gmail.com")
    newData.addLink(Kirill1, "TG","tg.me/kirill")

    newData.addContact(Maria, Contact.Address("str. Nevsky, house 7"))
    newData.addPhone(Maria, "9073145", PhoneType.Work)

    newData.addContact(Jhon, Contact.Link("VK", "vk.com/jhon"))


    println("All persons in database:")
    println(newData.getAllPersons())

    println("All contacts: in database")
    println(newData.getAllContacts())

    println("All emails of Ivan")
    println(newData.getPersonEmails(Ivan))

    println("Phones of person Kirill1")
    println(newData.getPersonPhones(Kirill1))

    println("Searching persons with subname 'Ivan'")
    println(newData.getPersonByName("Ivan"))
}