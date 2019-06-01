
import net.dv8tion.jda.api.JDABuilder

fun main() {
        println("Hello World!")
        var JDA = JDABuilder("token").build().also {
            it.addEventListener(Ping())
        }
}
