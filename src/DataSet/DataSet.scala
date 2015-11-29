package DataSet

class DataSet(val input: List[Sample]) {
    val inputLayerSize: Int = input.head.input.activeSize
    val outputLayerSize: Int = input.head.target.activeSize


    override def toString = input.mkString("DataSet(\n\t", "\n\t", "\n)")
}
