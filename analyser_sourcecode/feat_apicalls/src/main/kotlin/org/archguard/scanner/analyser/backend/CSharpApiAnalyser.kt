package org.archguard.scanner.analyser.backend

import chapi.domain.core.CodeDataStruct
import chapi.domain.core.CodeFunction
import org.archguard.scanner.core.sourcecode.ContainerSupply
import org.archguard.scanner.core.sourcecode.ContainerService

class CSharpApiAnalyser {
    var resources: List<ContainerSupply> = listOf()

    fun analysisByNode(node: CodeDataStruct, _workspace: String) {
        val routeAnnotation = node.filterAnnotations("RoutePrefix", "Route")
        if (routeAnnotation.isNotEmpty() || node.NodeName.endsWith("Controller")) {
            var baseUrl = ""
            if (routeAnnotation.isNotEmpty()) {
                baseUrl = routeAnnotation[0].KeyValues[0].Value
            }
            node.Functions.forEach { createResource(it, baseUrl, node) }
        }
    }

    private fun createResource(func: CodeFunction, baseUrl: String, node: CodeDataStruct) {
        var httpMethod = ""
        var route = ""
        for (annotation in func.Annotations) {
            when (annotation.Name) {
                "HttpGet" -> httpMethod = "Get"
                "HttpPost" -> httpMethod = "Post"
                "HttpDelete" -> httpMethod = "Delete"
                "HttpPut" -> httpMethod = "Put"
            }

            if (annotation.Name == "Route") {
                route = baseUrl + "/" + annotation.KeyValues[0].Value
            }
        }

        if (route.isNotEmpty() && httpMethod.isNotEmpty()) {
            if (!route.startsWith("/")) {
                route = "/${route}"
            }

            resources = resources + ContainerSupply(
                sourceUrl = route,
                sourceHttpMethod = httpMethod,
                packageName = node.Package,
                className = node.NodeName,
                methodName = func.Name
            )
        }
    }

    fun toContainerServices(): List<ContainerService> {
        val componentCalls: MutableList<ContainerService> = mutableListOf()

        val componentRef = ContainerService(name = "")
        componentRef.resources = this.resources

        componentCalls += componentRef
        return componentCalls
    }
}
