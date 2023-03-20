package me.honkling.pocket

import org.bukkit.inventory.ItemStack

data class Slot(
    val itemStack: ItemStack,
    val onClick: ClickHandler
)