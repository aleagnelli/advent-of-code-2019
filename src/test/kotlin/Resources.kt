object Resources {
    fun read(day: Int, split: String): List<String> {
        return Resources::class.java.getResource("day$day.txt")
            .readText()
            .split(split)
    }
}
