package DataSet

object Loader {
    def fromCsv(fileName: String): DataSet = {
        new CsvLoader(fileName).load
    }
}
