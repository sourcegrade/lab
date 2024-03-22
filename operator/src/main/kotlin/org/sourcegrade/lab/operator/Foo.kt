package org.sourcegrade.lab.operator

import io.fabric8.kubernetes.api.model.batch.v1.JobBuilder

fun foo() {
    JobBuilder()
        .withNewSpec()
        .withNewTemplate()
        .
}
