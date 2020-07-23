object Day4 {
    fun differentPassword(lowerBound: Int, upperBound: Int): Int {
        return (lowerBound..upperBound)
            .map { if (checkRules(it.toString())) 1 else 0 }
            .sum()
    }

    fun differentPassword2(lowerBound: Int, upperBound: Int): Int {
        return (lowerBound..upperBound)
            .map { if (checkRules(it.toString()) && newRule(it.toString())) 1 else 0 }
            .sum()
    }

    fun checkRules(password: String): Boolean {
        return password.length == 6 && containsDouble(password) && neverDecrease(password)
    }

    fun containsDouble(password: String): Boolean {
        return (0..4)
            .zip(1..5)
            .any { password[it.first] == password[it.second] }
    }

    fun neverDecrease(password: String): Boolean {
        return (0..4)
            .zip(1..5)
            .all { password[it.first] <= password[it.second] }
    }

    fun newRule(password: String): Boolean {
        return password.groupBy { it }.any { it.value.size == 2 }
    }

}

