
import net.dv8tion.jda.api.events.ShutdownEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader


class Ping : ListenerAdapter() {

    private var bool = false

    override fun onMessageReceived(event: MessageReceivedEvent) {
        event.author.isBot.not().apply {
            val message = event.message
            val msg = message.contentRaw.split(" ").toMutableList() // Might do something else with the list later on.
            if (msg.isNotEmpty()) {
                if(msg[0] == "auth"){
                    if(msg[1] == "tcp") {
                        if(!bool){
                            event.channel.sendMessage("Successfully authenticated!").queue()
                            bool = true
                        } else event.channel.sendMessage("Already authenticated!").queue()
                    } else event.channel.sendMessage("Bad password.").queue()
                }
                if (msg[0] == "sendmsg") {
                    if(!bool) {
                        event.channel.sendMessage("Please authenticate using auth <password>").queue()
                        return
                    }
                    val string = msg
                        .joinToString()
                        .substring(9)
                        .replace(",", "")
                    try {
                        sendTerminalMessage(string, event)
                    } catch (E : IOException) {
                        println("$string is invalid.")
                    } catch (Ex : IllegalArgumentException) {
                        println("error")
                    }

                }
            }
        }
    }

    override fun onShutdown(event: ShutdownEvent) {
        println(event.timeShutdown)
        super.onShutdown(event)
    }
    }

    private fun sendTerminalMessage(msg: String, event: MessageReceivedEvent) {

        val message = arrayOf("/bin/bash", "-c", msg)
        val exec = Runtime.getRuntime().exec(message)

        val reader = BufferedReader(InputStreamReader(exec.inputStream))

        while (reader.readLine() != null) {
            event.message.channel.sendMessage(reader.readLine()).queue()
        }
    }

