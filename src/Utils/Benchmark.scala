package Utils

object Benchmark {
    def apply[R](block: => R, caption: String): R = {
        println(Console.GREEN + caption + " starting" + Console.RESET)

        val t0 = System.nanoTime()
        val result = block // call-by-name
        val t1 = System.nanoTime()
        println("Elapsed time: \t" + (t1 - t0) / 1E9 + "s")
        println(Console.YELLOW + caption + " done" + Console.RESET)
        result
    }
}
