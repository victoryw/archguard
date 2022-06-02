package org.archguard.scanner.ctl.loader

import org.archguard.meta.OutputType
import org.archguard.rule.core.Issue
import org.archguard.scanner.core.SlotSpec
import org.archguard.scanner.core.context.Context
import org.archguard.scanner.core.context.SlotHub
import org.slf4j.LoggerFactory

/**
 * TODO: in order to support for multiple feature collections, need to refactor to pub/sub mod
 * SlotHubImpl
 */
class SlotHubImpl(val context: Context) : SlotHub {
    private val slotInstanceByType: MutableMap<String, SourceCodeSlot> = mutableMapOf()
    private val logger = LoggerFactory.getLogger(this.javaClass)

    fun register(analyserSpecs: List<SlotSpec>) {
        analyserSpecs.filter {
            it.slotType == "rule"
        }.map {
            val slotInstance = AnalyserLoader.loadSlot(it)
            val coin = slotInstance.ticket()[0]

            slotInstanceByType[coin] = SourceCodeSlot(it, slotInstance)
        }
    }

    fun maybePlugSlot(data: Any?) {
        if (data == null) return

        // for handle old versions plugin
        if (data !is List<*>) return

        val items = data as List<Any>
        if (items.isEmpty()) return

        val outputType = items[0]::class.java.name
        logger.info("found output type: $outputType")

        val slot = slotInstanceByType[outputType] ?: return

        plugSlot(slot, items)
    }

    private fun plugSlot(slot: SourceCodeSlot, data: List<Any>) {
        logger.info("try plug slot for: ${slot.clz}")

        slot.clz.prepare(emptyList())
        val output = slot.clz.process(data)

        logger.info("done plug slot for: ${slot.clz}")

        // check is output in register
        checkOutputCanPlug(output, data)

        // todo: move api process in slot
        when (slot.define.slotType) {
            "rule" -> {
                context.client.saveRuleIssues(output as List<Issue>)
            }
        }
    }

    private fun checkOutputCanPlug(output: OutputType, data: List<Any>) {
        if (output.isNotEmpty()) {
            val isSameType = output[0].javaClass.name == data[0].javaClass.name
            if (!isSameType) {
                maybeOutputCanPlug(output)
            }
        }
    }

    private fun maybeOutputCanPlug(output: OutputType) {
        maybePlugSlot(output)
    }
}