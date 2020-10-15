
class later<T>(val block:()->T){
    fun test(){
        println(block)
        (block())
    }
    fun testFun(block: () -> Unit){
        println("testFun")
        block()
        println("test")
    }
}

fun main() {
    later<String>{
        "fklajklfksdjfkl"
    }.testFun {
        println("fjadsklfjsal;kf")
    }
}