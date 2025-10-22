import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.internal.jvm.Jvm
import org.gradle.kotlin.dsl.property
import java.io.File

abstract class JavaRun() : JavaRunTask, AbstractJvmExec() {
    @get:Input
    override val mainClass: Property<String> = project.objects.property<String>()

    override fun Jvm.jvmExecutableForTask(): File = javaExecutable

    @TaskAction
    override fun exec() {
        args(
            "-cp", classpath.get().asPath,
            mainClass.get(),
        )
        super.exec()
    }
}
