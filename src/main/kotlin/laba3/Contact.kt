package laba3

sealed class Contact {
    data class Phone(val phone: String, val type: PhoneType) : Contact() {
        init {
            if (type == PhoneType.Moblie) {
                if (phone[0] != '+' || phone.length != 12) {
                    throw IllegalArgumentException("Wrong mobile phone format!")
                }
            }

            if (type == PhoneType.Home) {
                if (phone.length != 7) {
                    throw IllegalArgumentException("Wrong home phone format!")
                }
            }

            if (type == PhoneType.Work) {
                if (phone.length != 7) {
                    throw IllegalArgumentException("Wrong work phone format!")
                }
            }
        }

        override fun toString(): String {
            return "$phone ${type.toString()}"
        }
    }

    data class Email(val email: String) : Contact() {
        init {
            if (!(email.contains('@')) || !(email.contains('.')) || email.indexOf('@') < email.indexOf('@')) {
                throw IllegalArgumentException("Wrong email format!")
            }
        }

        override fun toString(): String {
            return email
        }
    }

    data class Address(val address: String) : Contact() {
        override fun toString(): String {
            return address
        }
    }

    data class Link(val networkName: String, val url: String) : Contact() {
        override fun toString(): String {
            return "$networkName $url"
        }
    }
}