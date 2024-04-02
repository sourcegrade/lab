plugins {
    alias(libs.plugins.protobuf)
    alias(libs.plugins.ktor)
}

sourceSets {
    main {
        proto {
            srcDir("src/main/protobuf")
        }
    }
}

dependencies {
    api(libs.protobuf.java)
    implementation(kotlin("reflect"))
    implementation(libs.ktor.serialization.kotlinx)
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:${libs.protobuf.java.get().version}"
    }
}
