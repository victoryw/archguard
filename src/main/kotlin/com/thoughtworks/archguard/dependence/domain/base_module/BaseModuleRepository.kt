package com.thoughtworks.archguard.dependence.domain.base_module

interface BaseModuleRepository {
    fun getBaseModules(): List<String>

    fun getJClassesHasModules(): List<JClass>

    fun getJClassesById(id: String): JClass
}
