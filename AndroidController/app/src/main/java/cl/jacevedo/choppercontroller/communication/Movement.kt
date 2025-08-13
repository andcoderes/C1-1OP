package cl.jacevedo.choppercontroller.communication


data class Movement(val s:Int, val l: Array<Int>, val r: Array<Int>, val lr: Array<Int>) : ICommunication {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Movement

        if (s != other.s) return false
        if (!l.contentEquals(other.l)) return false
        if (!r.contentEquals(other.r)) return false
        if (!lr.contentEquals(other.lr)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = s
        result = 31 * result + l.contentHashCode()
        result = 31 * result + r.contentHashCode()
        result = 31 * result + lr.contentHashCode()
        return result
    }

}