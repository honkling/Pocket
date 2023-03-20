# Pocket

A lightweight inventory management library for Spigot.

# Creating GUIs

Creating GUIs is very simple. All you need to do is specify your plugin, the template, and a title.

```kt
var gui = GUI(
    plugin,
    """
    xxxxxxxxx
    x00abc00x
    xxxxxxxxx
    """,
    "Example GUI"
)
```

# Different Inventory Types

You can specify the type of inventory after the title.

```kt
var gui = GUI(
    plugin,
    """
    xabcx
    """,
    "Example GUI",
    InventoryType.HOPPER
)
```

# Placing Items

You can dictate the format of your GUI with the template specified in your constructor.<br>
All you need to do after constructing, is specify what item is which character.<br>
Note that any characters in the template will be filled in with air if a substite is not specified.

```kt
gui = gui
    .put('x', ItemStack(Material.BLACK_STAINED_GLASS_PANE)) // Place black stained glass panes in place of 'x'
    .put('a', ItemStack(Material.DIRT))
    .put('b', ItemStack(Material.IRON_INGOT))
    .put('c', ItemStack(Material.DIAMOND))
```

You may also place items at specific indexes, without need of the template.<br>
This allows for you to place items in GUIs dynamically, or if you just need a lot of unique items.

```kt
gui = gui
    .put(0, ItemStack(Material.WHITE_STAINED_GLASS_PANE)) // Place white stained glass pane at index 0
```

# Opening the GUI

You can open the GUI to a player using the `open` method.
```kt
gui.open(player)
```

# Click Events

You can add a click handler for an item just by specifying a Runnable after the item.

```kt
gui = gui
    .put('c', ItemStack(Material.DIAMOND)) { event: InventoryClickEvent ->
        event.player.sendMessage("Shiny diamond!")
    }
```

# Close Event

You can listen for the closing of the GUI by specifying a Runnable after the `open` method.
```kt
gui.open(player) { event: InventoryCloseEvent ->
    event.player.sendMessage("why leave now? :c")
}
```

# License

MIT License

Copyright (c) 2023 honkling

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
