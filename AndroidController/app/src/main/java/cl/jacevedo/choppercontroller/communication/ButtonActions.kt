package cl.jacevedo.choppercontroller.communication

data class ButtonActions(val s:Int, val p:Array<String>, val m: Array<Int>) : ICommunication {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ButtonActions

        if (s != other.s) return false
        if (!p.contentEquals(other.p)) return false
        if (!m.contentEquals(other.m)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = s.hashCode()
        result = 31 * result + p.contentHashCode()
        result = 31 * result + m.contentHashCode()
        return result
    }
}