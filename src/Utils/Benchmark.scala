package Utils

object Benchmark {
    def apply[R](block: => R): R = {
        val t0 = System.nanoTime()
        val result = block // call-by-name
        val t1 = System.nanoTime()
        println("Elapsed time: \t" + (t1 - t0) / 1E9 + "s")
        result
    }
}
