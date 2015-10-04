package Layer

class OutputLayer(size: Int) extends HiddenLayer(size) {

    override def toString = neurons.mkString("Output\t\t[", ", ", "]")
}
