package com.thoughtworks.archguard.module.domain.plugin

import com.thoughtworks.archguard.module.domain.model.Dependency
import com.thoughtworks.archguard.module.domain.model.JMethodVO

interface DependPlugin : Plugin {

    fun fixMethodDependencies(methodDependencies: List<Dependency<JMethodVO>>)
            : List<Dependency<JMethodVO>>
}