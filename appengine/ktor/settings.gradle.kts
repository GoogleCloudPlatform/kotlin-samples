pluginManagement {
    // resolutionStrategy enables using plugins { id("com.google.cloud.tools.appengine") } syntax
    // It won't be needed when the plugin publishes
    // com.google.cloud.tools.appengine:com.google.cloud.tools.appengine.gradle.plugin
    // artifact to Sonatype OSSRH
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "com.google.cloud.tools.appengine") {
                useModule("com.google.cloud.tools:appengine-gradle-plugin:${requested.version}")
            }
        }
    }
}

rootProject.name = "appengine-ktor"
