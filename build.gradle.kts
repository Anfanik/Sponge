import org.spongepowered.gradle.dev.SpongeDevExtension

// TODO - Add mixin gradle when that comes around.
plugins {
    id("org.spongepowered.gradle.sponge.common")
    id("net.minecraftforge.gradle")
}

minecraft {
    mappings(project.properties["mcpType"]!! as String, project.properties["mcpMappings"]!! as String)
    runs {
        create("server") {
            workingDirectory( project.file("../run"))
            mods {
                create("sponge") {
                    source(project.sourceSets["main"])
                }
            }
        }
    }
    project.sourceSets["main"].resources
            .filter { it.name.endsWith("_at.cfg") }
            .files
            .forEach { accessTransformer(it) }
}



dependencies {
    // Minecraft... duh
    // TODO - migrate this to the plugin, from the gradle properties
    minecraft("net.minecraft:" + project.properties["minecraftDep"] + ":" + project.properties["minecraftVersion"])

    runtime("org.apache.logging.log4j:log4j-slf4j-impl:2.8.1")

    // Database stuffs... likely needs to be looked atå
    runtime("com.zaxxer:HikariCP:2.6.3")
    runtime("org.mariadb.jdbc:mariadb-java-client:2.0.3")
    runtime("com.h2database:h2:1.4.196")
    runtime("org.xerial:sqlite-jdbc:3.20.0")
}

tasks.test {
    systemProperties("lwts.tweaker" to "org.spongepowered.common.launch.TestTweaker")
    reports.html.setEnabled(false)
}

// Configure the TestPlugins project.
val api = spongeDev.api!!
val testPlugins = project(":testplugins") {
    apply(plugin = "org.spongepowered.gradle.sponge.dev")
    apply(plugin = "java-library")
    configure<SpongeDevExtension> {
        licenseProject = "Sponge"
    }
    dependencies {
        implementation(project(api.path))
    }
}
val launch by sourceSets.creating
val launchWrapper by sourceSets.creating {
    compileClasspath += launch.compileClasspath
}
sourceSets.main {
    compileClasspath += launch.compileClasspath
    runtimeClasspath += launch.runtimeClasspath
}

dependencies {
    // For unit testing... not really going to work.
    runtime(project(testPlugins.path)) {
        exclude(module="spongeapi")
    }
    "launchImplementation"("org.spongepowered:mixin:0.8")
    "launchImplementation"("org.ow2.asm:asm-tree:6.2")
    "launchImplementation"("org.ow2.asm:asm-util:6.2")
    "launchImplementation"("org.apache.logging.log4j:log4j-api:2.8.1")
    implementation(launch.output)

    "launchWrapperImplementation"(launch.output)
    "launchWrapperImplementation"("net.minecraft:launchwrapper:1.11") {
        exclude(module="lwjgl")
    }
    implementation(launchWrapper.output)

}