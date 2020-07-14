package com.thoughtworks.archguard.clazz.controller

import com.thoughtworks.archguard.clazz.domain.service.ClassService
import com.thoughtworks.archguard.module.domain.model.Dependency
import com.thoughtworks.archguard.module.domain.model.JClass
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/classes")
class ClassController {

    @Autowired
    private lateinit var service: ClassService

    @GetMapping("/{name}/dependencies")
    fun getDependencies(@PathVariable("name") name: String,
                        @RequestParam(value = "module", required = false, defaultValue = "") module: String,
                        @RequestParam("deep", required = false, defaultValue = "3") deep: Int): Dependency<List<JClass>> {
        return service.findDependencies(module, name, deep)
    }

    @GetMapping("/{name}/invokes")
    fun getInvokes(@PathVariable("name") name: String,
                   @RequestParam(value = "module", required = false, defaultValue = "") module: String,
                   @RequestParam(value = "deep", required = false, defaultValue = "3") deep: Int,
                   @RequestParam(value = "callerDeep", required = false) callerDeep: Int?,
                   @RequestParam(value = "calleeDeep", required = false) calleeDeep: Int?,
                   @RequestParam(value = "needIncludeImpl", required = false, defaultValue = "true") needIncludeImpl: Boolean?) {
        service.findInvokes(module, name, callerDeep ?: deep, calleeDeep ?: deep, needIncludeImpl ?: true)
    }
}