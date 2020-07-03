package com.thoughtworks.archguard.module.domain

interface LogicModuleRepository {
    @Deprecated("Replace by getAllByShowStatus")
    fun getAllByShowStatusLegacy(isShow: Boolean): List<LogicModuleLegacy>
    fun getAllByShowStatus(isShow: Boolean): List<LogicModule>
    fun getAll(): List<LogicModule>
    fun update(id: String, logicModule: LogicModule)
    fun updateAll(logicModules: List<LogicModule>)
    fun create(logicModule: LogicModuleLegacy)
    fun delete(id: String)
    fun getDependence(caller: String, callee: String): List<ModuleDependency>
    fun deleteAll()
    fun saveAll(logicModules: List<LogicModule>)

    @Deprecated("Replace by getAllClassDependency")
    fun getAllClassDependencyLegacy(members: List<String>): List<DependencyLegacy>
    fun getAllClassDependency(members: List<ModuleMember>): List<Dependency<JClass>>
    fun getParentClassId(id: String): List<String>
}
