package laba3

class TinkoffDataService : ContactsService {

    private val users = mutableMapOf<Person, MutableList<Contact>>()

    override fun addContact(person: Person, contact: Contact) {
        if (users.containsKey(person) && users[person]!!.contains((contact)))
            throw IllegalArgumentException("Contact already exists")
        else
            users[person]!!.add(contact)
    }

    override fun removeContact(person: Person, contact: Contact) {
        if (users.containsKey(person)) {
            if (users[person]!!.contains((contact))) {
                users[person]!!.remove(contact)
            } else {
                throw IllegalArgumentException("Contact does not exists")
            }
        } else {
            throw IllegalArgumentException("Person does not exists")
        }
    }

    override fun addPerson(person: Person) {
        if (users.containsKey(person))
            throw IllegalArgumentException("Person already exists")
        else
            users[person] = mutableListOf<Contact>()
    }

    override fun removePerson(person: Person) {
        if (users.containsKey(person))
            users.remove(person)
        else
            throw IllegalArgumentException("Person does not exists")
    }

    override fun addPhone(person: Person, phone: String, phoneType: PhoneType) {
        if (users.containsKey(person)) {
            if (users[person]!!.contains(Contact.Phone(phone, phoneType))) {
                throw IllegalArgumentException("Phone already exists")
            } else {
                users[person]!!.add(Contact.Phone(phone, phoneType))
            }
        } else {
            throw IllegalArgumentException("Person does not exist!")
        }
    }

    override fun addEmail(person: Person, email: String) {
        if (users.containsKey(person)) {
            if (users[person]!!.contains(Contact.Email(email))) {
                throw IllegalArgumentException("Email already exists")
            } else {
                users[person]!!.add(Contact.Email(email))
            }
        } else {
            throw IllegalArgumentException("Person does not exist!")
        }
    }

    override fun addAddress(person: Person, address: String) {
        if (users.containsKey(person)) {
            if (users[person]!!.contains(Contact.Address(address))) {
                throw IllegalArgumentException("Address already exists")
            } else {
                users[person]!!.add(Contact.Address(address))
            }
        } else {
            throw IllegalArgumentException("Person does not exist!")
        }
    }

    override fun addLink(person: Person, name: String, url: String) {
        if (users.containsKey(person)) {
            if (users[person]!!.contains(Contact.Link(name, url))) {
                throw IllegalArgumentException("Link already exists")
            } else {
                users[person]!!.add(Contact.Link(name, url))
            }
        } else {
            throw IllegalArgumentException("Person does not exist!")
        }
    }

    // filterIsInstance подсмотрел, т.к. через inline как в прошлой лабе не вышло
    override fun getPersonPhones(person: Person): List<Contact.Phone> {
        if (users.containsKey(person)) {
            return users[person]!!.filterIsInstance<Contact.Phone>()
        } else {
            throw IllegalArgumentException("Person does not exist!")
        }
    }

    override fun getPersonEmails(person: Person): List<Contact.Email> {
        if (users.containsKey(person)) {
            return users[person]!!.filterIsInstance<Contact.Email>()
        } else {
            throw IllegalArgumentException("Person does not exist!")
        }
    }

    override fun getPersonLinks(person: Person): List<Contact.Link> {
        if (users.containsKey(person)) {
            return users[person]!!.filterIsInstance<Contact.Link>()
        } else {
            throw IllegalArgumentException("Person does not exist!")
        }
    }

    override fun getPersonContacts(person: Person): List<Contact> {
        if (users.containsKey(person)) {
            return users[person]!!.toList()
        } else {
            throw IllegalArgumentException("Person does not exist!")
        }
    }

    override fun getAllPersons(): List<Person> {
        return users.toList().map { it.first }
    }

    override fun getAllContacts(): Map<Person, List<Contact>> {
        return users
    }

    override fun getPersonByName(name: String): List<Person> {
        return getAllPersons().filter { it.firstName.contains(name) || it.secondName.contains(name) }
    }
}