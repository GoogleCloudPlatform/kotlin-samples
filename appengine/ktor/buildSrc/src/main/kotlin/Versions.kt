import kotlin.String

/**
 * Find which updates are available by running
 *     `$ ./gradlew buildSrcVersions`
 * This will only update the comments.
 *
 * YOU are responsible for updating manually the dependency version. */
object Versions {
    const val appengine: String = "1.9.71"

    const val appengine_gradle_plugin: String = "1.3.5"

    const val google_cloud_logging_logback: String = "0.60.0-alpha" 

    const val de_fayard_buildsrcversions_gradle_plugin: String = "0.3.2" 

    const val io_ktor: String = "1.1.1"

    const val org_jetbrains_kotlin: String = "1.3.11"

    /**
     *
     *   To update Gradle, edit the wrapper file at path:
     *      ./gradle/wrapper/gradle-wrapper.properties
     */
    object Gradle {
        const val runningVersion: String = "5.1.1"

        const val currentVersion: String = "5.1.1"

        const val nightlyVersion: String = "5.2-20190115022437+0000"

        const val releaseCandidate: String = ""
    }
}
