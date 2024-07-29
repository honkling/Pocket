package me.honkling.pocket

import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent

typealias ClickHandler = (InventoryClickEvent) -> Unit
typealias CloseHandler = (CancellableInventoryCloseEvent) -> Unit
