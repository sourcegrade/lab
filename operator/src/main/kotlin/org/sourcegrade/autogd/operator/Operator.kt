package org.sourcegrade.autogd.operator

import io.fabric8.kubernetes.client.KubernetesClient
import io.fabric8.kubernetes.client.KubernetesClientBuilder
import org.apache.logging.log4j.Logger

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

        try {
            logger.info("Pods: ${client.pods().inNamespace(client.namespace).list()}")
        } catch (e: Exception) {
            logger.error("Failed to get pods", e)
        }
        logger.info("Got pods")
//        client.namespaces().list().items.forEach {
//            logger.info("Found namespace: ${it.metadata.name}")
//        }
    }
}
