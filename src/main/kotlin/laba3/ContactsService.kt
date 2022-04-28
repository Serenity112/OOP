package laba3

interface ContactsService {
    fun addContact(person: Person, contact: Contact)
    fun removeContact(person: Person, contact: Contact)

    fun addPerson(person: Person)
    fun removePerson(person: Person)

    fun addPhone(person: Person, phone: String, phoneType: PhoneType)
    fun addEmail(person: Person, email: String)
    fun addAddress(person: Person, address: String)
    fun addLink(person: Person, name: String, url: String)

    fun getPersonContacts(person: Person) : List<Contact>
    fun getPersonPhones(person: Person) : List<Contact.Phone>
    fun getPersonEmails(person: Person) : List<Contact.Email>
    fun getPersonLinks(person: Person) : List<Contact.Link>

    fun getAllPersons(): List<Person>
    fun getAllContacts(): Map<Person, List<Contact>>

    fun getPersonByName(name: String): List<Person>
}
