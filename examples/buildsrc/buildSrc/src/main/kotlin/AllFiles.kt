import org.gradle.api.Project
import java.io.File

data class Finder(val path: File) {
    fun withExtension(extension: String): List<File> =
        path.walkTopDown().filter { it.isFile && it.extension == extension }.toList()
}
fun Project.allFilesIn(path: String) = Finder(projectDir.resolve(path))
