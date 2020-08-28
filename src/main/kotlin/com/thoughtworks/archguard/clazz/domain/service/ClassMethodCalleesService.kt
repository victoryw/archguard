package com.thoughtworks.archguard.clazz.domain.service

import com.thoughtworks.archguard.clazz.domain.JClass
import com.thoughtworks.archguard.clazz.domain.JClassRepository
import com.thoughtworks.archguard.method.domain.JMethodRepository
import com.thoughtworks.archguard.method.domain.service.MethodCalleesService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ClassMethodCalleesService {
    @Autowired
    private lateinit var methodRepo: JMethodRepository

    @Autowired
    private lateinit var classRepo: JClassRepository

    @Autowired
    private lateinit var methodCalleesService: MethodCalleesService

    fun findClassMethodsCallees(projectId: Long, target: JClass, calleeDeep: Int, needIncludeImpl: Boolean,
                                needParents: Boolean): JClass {
        target.methods = methodRepo.findMethodsByModuleAndClass(projectId, target.module, target.name)
        methodCalleesService.buildMethodCallees(target.methods, calleeDeep, needIncludeImpl)
        if (needParents) {
            (target.parents as MutableList).addAll(classRepo.findClassParents(projectId, target.module, target.name))
            target.parents.forEach { findClassMethodsCallees(projectId, it, calleeDeep, needIncludeImpl, needParents) }
        }
        return target
    }

}
