fun main(args: Array<String>) {
    if(args.size < 3 || args.size > 3) {
        println("Invalid number of program arguments")
    } else {
        val arg1 = args[0]
        val arg1length = arg1.length
        val arg2 = args[1]
        val arg2length = arg2.length
        val arg3 = args[2]
        val arg3length = arg3.length
        println("Argument 1: $arg1. It has $arg1length characters")
        println("Argument 2: $arg2. It has $arg2length characters")
        println("Argument 3: $arg3. It has $arg3length characters")
    }
}
