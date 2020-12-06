import io.farewell12345.github.faqbot.AppConfig

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
    AppConfig.getInstance()
    later<String>{
        "fklajklfksdjfkl"
    }.testFun {
        println("fjadsklfjsal;kf")
    }
}