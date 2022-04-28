package laba3

data class Person(val firstName: String, val secondName: String) {
    init {
        if(firstName.isEmpty() || secondName.isEmpty())
            throw IllegalArgumentException("Incorrect person data")
    }

    override fun toString(): String {
        return "$firstName $secondName"
    }
}