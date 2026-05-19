<p align="center">
<img width="800" src="https://github.com/Sefiraat/Networks/blob/master/images/logo/logo.svg"><br><br>
</p>

# Networks

[![Build Status](https://Slimefun5.github.io/builds/Slimefun5/Networks/stable/badge.svg)](https://Slimefun5.github.io/builds/Slimefun5/Networks/stable)
![GitHub Downloads (all assets, all releases)](https://img.shields.io/github/downloads/Slimefun5/Networks/total)
[![GitHub Followers](https://img.shields.io/github/followers/Slimefun5?style=social)](https://github.com/Slimefun5)
[![GitHub Stars](https://img.shields.io/github/stars/Slimefun5/Networks?style=social)](https://github.com/Slimefun5/Networks)
[![bStats](https://bStats.org/signatures/bukkit/Networks.svg)](https://bStats.org/plugin/bukkit/Networks/31391)

A Slimefun addon that adds digital storage networks, crafting grids, and item transport.

## Requirements
- Java 25
- Paper 1.16.* - 26.1.*
- [Slimefun 5](https://github.com/Slimefun5/Slimefun5)

## About Networks

You can find a fuller guide to Networks including all items and blocks in
my [Documentation Pages](https://sefiraat.dev)

![](https://github.com/Sefiraat/Networks/blob/master/images/wiki/setup.png?raw=true)

## Network Grid / Crafting Grid

It can access every single item in the network and display it to you on a single GUI, you can pull out items one by one
or by stack. Items can be inserted directly through this grid and a special crafting grid can craft both vanilla AND
slimefun items using ingredients directly from the network.

![](https://github.com/Sefiraat/Networks/blob/master/images/wiki/grid.png?raw=true)

![](https://github.com/Sefiraat/Networks/blob/master/images/wiki/grid_crafting.png?raw=true)

## Network Bridge

The Network Bridge just acts as a block to help extend the network out cheaply

## Network Cells

Network Cells are a single block that can hold a double-chests worth of items within it. These items are exposed to the
network. They are designed for items that you either have few of or are non-stackable/unique.

## Network Quantum Storages

A Network Quantum Storage is a single block that can hold a massive amount of a single type of item within it. Starting
at 4k items upgradable all the way up to 2 billion at it's highest. Quantums should be thought as barrels for deep
storage of mass-produced items.

## Network Monitors

A Network Monitor is used to 'expose' the inventories of connected blocks. This is required for the network to see
inside of a Network Shell's card and also storage solutions from other plugins like Infinity Expansion's barrels.

## Import/Export

The Network Importer has a 9-slot inventory that Cargo can access and drop items into. The importer will periodically
try to move these items from the import slots into the network, should there be capacity to do so. The Network Exporter
takes a single item as a template and tries to withdraw matching items from the network into itself. It's internal
inventory is accessible by cargo. These two blocks act as a bridge between cargo/networks when needed

## Push and Pull

The Network Grabber will try to remove items from adjacent Slimefun machines and move them directly into the network if
possible. This works for any Slimefun machine that accepts cargo-out. The Network Pusher takes a single item as a
template and tries to withdraw the item form the network and push it into an adjacent Slimefun machine's input slot(s)
when possible.

## Power

The Network Capacitor will accept energy in from the EnergyNet and will store it. Any connected Network machine can use
this power when required. Total network power can be seen using the Network Power Display.

A Network Power Outlet can take power from within the network and output it to an adjacent EnergyNet machine.

## Wireless Item Transfer

The Network Transmitter will take matching items from the network and will send it to a linked Network Receiver. The
receiver will accept the item and will try to send it into its own Network.

## Autocrafting!

The Network Encoder will take a given recipe (Vanilla or Slimefun) and encode it into a blank blueprint. The Network
Autocrafter will take this blueprint and try to craft it periodically using items directly within the network assuming
enough items are found and enough power is provided. Items are output back into the network. The Withholding Autocrafter
does the same thing but keeps the crafted items inside itself, up to a single stack, and exposes this stack to the
network for withdrawal. This is useful to keep a stock of items without crafting too many or for items useful only as
crafting materials for other auto crafters.

## Remote Control

The Network Remote(s) are expensive crafts that allow you to access a bound-grid wirelessly. Different tiers of remotes
allow different access ranges from 150 blocks, 500, unlimited and then cross-dimensionally.

## Trash

The Network Purger takes a single item as a template and will find a matching item inside the network, withdraw it then
void it. Use with care.

## Other/Misc

- The Network Crayon allows you to turn on/off particles from machines showing you when and what they are doing without
  having to open un the GUI.
- The Network Configurator allows you to copy the settings of an appropriate node and then paste the settings back onto
  another node. Costly to craft but allows for quick setups.
- The Network Probe will show every block that's connected to a controller including a summary of their contents.

## Thanks!

As usual, a big thanks Boomer, Cai and Lucky for their help testing and refining networks

A big thanks to the owners of **mct.tantrum.org** who have tested nearly everything I have made and really given me
direction and drive to make these things including some monster test setups of Networks to really push it into the dirt
;)

Another big shoutout to **GentlemanCheesy** of **mc.talosmp.net** for being my first (and as of writing this, only!)
sponsor. A few coffee's a month to make me feel better about making these addons <3

## Developer API

You can easily depend on this project using [github-gradle](https://github.com/intisy/github-gradle).

In your `build.gradle.kts`:

```kotlin
plugins {
    id("io.github.intisy.github-gradle") version "1.8.2.1"
}

dependencies {
    "githubCompileOnly"("Slimefun5:Networks:v1.0.1")
}
```

## Wiki

[Read more on the Slimefun Wiki...](https://github.com/Slimefun5/Wiki/wiki/Networks)

## Discord

You can find Slimefun's community on Discord! Click the badge below to join the server for suggestions/questions or other discussions about this plugin.

<p align="center">
  <a href="https://discord.gg/fsD4Bkh">
    <img src="https://discordapp.com/api/guilds/738626600539160576/widget.png?style=banner2" alt="Discord"/>
  </a>
</p>

## License

This project is open-source and licensed under the MIT License.
