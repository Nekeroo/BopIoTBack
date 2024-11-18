import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.inject.se.SeContainerInitializer

@ApplicationScoped
class Main {

    fun main(args: Array<String>) {
        SeContainerInitializer.newInstance().initialize();
    }

}