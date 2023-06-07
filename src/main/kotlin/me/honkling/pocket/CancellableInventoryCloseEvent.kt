package me.honkling.pocket

import org.bukkit.event.Cancellable
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.InventoryView

class CancellableInventoryCloseEvent(transaction: InventoryView) : InventoryCloseEvent(transaction), Cancellable {
    var isEventCancelled = false

    override fun isCancelled(): Boolean {
        return isEventCancelled
    }

    override fun setCancelled(cancel: Boolean) {
        isEventCancelled = cancel
    }
}