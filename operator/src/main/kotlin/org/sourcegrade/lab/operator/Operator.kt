package org.sourcegrade.lab.operator

import io.fabric8.kubernetes.api.model.batch.v1.JobBuilder
import io.fabric8.kubernetes.api.model.batch.v1.JobTemplateSpecBuilder
import io.fabric8.kubernetes.client.KubernetesClient
import io.fabric8.kubernetes.client.KubernetesClientBuilder
import kotlinx.coroutines.Job
import org.apache.logging.log4j.Logger
import java.util.Collections

class Operator(
    private val logger: Logger,
) {

    private lateinit var client: KubernetesClient

    fun initialize() {
        logger.info("Initializing operator...")
        client = KubernetesClientBuilder().build()
        logger.info("Connected to Kubernetes API server ${client.masterUrl} with API version ${client.apiVersion}.")
        logger.info("In namespace '${client.namespace}'.")
        logger.info("Getting pods")

        JobBuilder()
            .withNewSpec()
            .withNewTemplate()
            .withNewSpec()
            .addNewContainer()

        try {
            logger.info("Pods: ${client.pods().inNamespace(client.namespace).list()}")
        } catch (e: Exception) {
            logger.error("Failed to get pods", e)
        }

        JobBuilder()
            .withApiVersion("batch/v1")
            .withNewMetadata()
            .withName("pi")
//            .withLabels(Collections.singletonMap("label1", "maximum-length-of-63-characters"))
//            .withAnnotations(Collections.singletonMap("annotation1", "some-very-long-annotation"))
            .endMetadata()
            .withNewSpec()
            .withNewTemplate()
            .withNewSpec()
            .addNewContainer()
            .withName("pi")
            .withImage("perl")
            .withArgs("perl", "-Mbignum=bpi", "-wle", "print bpi(2000)")
            .endContainer()
            .withRestartPolicy("Never")
            .endSpec()
            .endTemplate()
            .endSpec()
            .build()


        logger.info("Got pods")
    }
}
