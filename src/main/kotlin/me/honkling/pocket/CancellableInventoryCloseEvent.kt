package me.honkling.pocket

import org.bukkit.event.Cancellable
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.InventoryView

class CancellableInventoryCloseEvent(transaction: InventoryView) : InventoryCloseEvent(transaction), Cancellable {
    var cancelled = false

    override fun isCancelled(): Boolean {
        return cancelled
    }

    override fun setCancelled(cancel: Boolean) {
        cancelled = cancel
    }
}