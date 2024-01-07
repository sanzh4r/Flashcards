fun main(args: Array<String>) {
    if (args.size < 3) {
    main(arrayOf<String>("first", "second", "third"))
    } else {
    val first = args[0]
    val second = args[1]
    val third = args[2]
    println(first)
    println(second)
    println(third)
}
}
