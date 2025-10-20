import org.gradle.api.Task
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.FileCollection
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Classpath
import org.gradle.api.tasks.Exec
import org.gradle.internal.jvm.Jvm
import java.io.File

interface TaskWithClasspath : Task {
    val classpath: Property<FileCollection>
}

interface JavaCompileTask : TaskWithClasspath {
    val sources: Property<FileCollection>
    val destinationDir: DirectoryProperty
}

interface JavaRunTask : TaskWithClasspath {
    val mainClass: Property<String>
}

abstract class AbstractJvmExec : TaskWithClasspath, Exec() {
    @Classpath
    override val classpath: Property<FileCollection> = project.objects.property<FileCollection>()

    init {
        executable(Jvm.current().jvmExecutableForTask().absolutePath)
    }

    protected abstract fun Jvm.jvmExecutableForTask(): File
}

public inline fun <reified T> org.gradle.api.model.ObjectFactory.property(): org.gradle.api.provider.Property<T> =
    this.property(T::class.java)
