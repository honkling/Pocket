package me.honkling.pocket

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin

class GUI(
    val plugin: Plugin,
    val template: String,
    val title: String,
    val type: InventoryType = InventoryType.CHEST
) {
    private val lines = template.trimIndent().split("\n")
    private val onClose = mutableMapOf<Inventory, CloseHandler>()
    private val substitutions = mutableMapOf<Char, Slot>()
    private val replacements = mutableMapOf<Int, Slot>()
    private val onClick = mutableMapOf<Int, ClickHandler>()

    init {
        if (lines.isEmpty())
            throw IllegalArgumentException("Received empty template")

        if (lines.find { it.length != lines[0].length } != null)
            throw IllegalArgumentException("Received template with inconsistent row length")

        val rows = lines.size
        val columns = lines[0].length

        if (type == InventoryType.CHEST) {
            if (rows < 3 || rows > 6)
                throw IllegalArgumentException("Received chest template with invalid amount of rows ($rows)")

            if (columns != 9)
                throw IllegalArgumentException("Received chest template with invalid amount of columns ($columns)")
        }

        if (type != InventoryType.CHEST) {
            if (lines.joinToString("").length != type.defaultSize)
                throw IllegalArgumentException("Received template with invalid size")
        }

        if (!type.isCreatable)
            throw IllegalArgumentException("Received template with invalid type")
    }

    fun put(char: Char, itemStack: ItemStack, onClick: ClickHandler = {}): GUI {
        substitutions[char] = Slot(itemStack, onClick)
        return this
    }

    fun put(index: Int, itemStack: ItemStack, onClick: ClickHandler = {}): GUI {
        replacements[index] = Slot(itemStack, onClick)
        return this
    }

    fun open(player: Player, onClose: CloseHandler = {}) {
        val rows = template
            .trimIndent()
            .split("\n")
            .size

        val inventory =
            if (type == InventoryType.CHEST) Bukkit.createInventory(null, rows * 9, title)
            else Bukkit.createInventory(null, type, title)

        val iterableTemplate = template.replace(Regex("\\s+"), "")
        iterableTemplate.forEachIndexed { index, char ->
            if (!substitutions.containsKey(char)) return@forEachIndexed

            val (itemStack, onClick) = substitutions[char]!!

            inventory.setItem(index, itemStack)
            this.onClick[index] = onClick
        }

        replacements.forEach { (index, slot) ->
            inventory.setItem(index, slot.itemStack)
            onClick[index] = slot.onClick
        }

        player.openInventory(inventory)
        this.onClose[inventory] = onClose
        registerListeners(inventory)
    }

    private fun registerListeners(immutableInventory: Inventory) {
        var inventory = immutableInventory
        val pluginManager = Bukkit.getPluginManager()

        pluginManager.registerEvents(object : Listener {
            @EventHandler
            fun onInventoryClick(e: InventoryClickEvent) {
                if (e.inventory != inventory)
                    return

                val slotIndex = e.slot

                if (!onClick.containsKey(slotIndex)) return

                e.isCancelled = true
                onClick[slotIndex]!!.invoke(e)
            }

            @EventHandler
            fun onInventoryClose(e: InventoryCloseEvent) {
                val cancellable = CancellableInventoryCloseEvent(e.view)

                if (cancellable.inventory != inventory)
                    return

                onClose[inventory]?.invoke(cancellable)

                if (!cancellable.isEventCancelled) {
                    InventoryClickEvent.getHandlerList().unregister(this)
                    InventoryCloseEvent.getHandlerList().unregister(this)
                    return
                }

                e.player.openInventory(inventory)
                inventory = e.player.openInventory.topInventory
            }
        }, plugin)
    }
}