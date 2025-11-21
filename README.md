# VFP ChatFilter Patch

**VFP (ViaFabricPlus) ChatFilter Patch** is a small modification that replaces the logic of chat filter code provided in ViaLegacy library (which ViaFabricPlus uses to connect to ancient mc version servers), allowing players to configure custom behavior of chat filter or even allow any characters to be sent.

This is practically useful when joining to servers with custom defined `font.txt`, a set of custom characters or with all characters allowed. A vivid example: Russian b1.7.3 servers with plugins and russian version of `font.txt`, which is unknown for an unpatched ViaLegacy.

![Example screenshot.](https://github.com/tracystacktrace/vfpchatfilterpatch/raw/main/docs/example_1.png)

However, when joining to the unsupported server (i.e. using vanilla `font.txt`), by writing other characters the player will be kicked out with the message "Illegal characters in chat" or similar. So don't try to use it everywhere, and make sure the server has allowed these characters.

As ViaFabricPlus, this is an only client-side mod.

## Configuration

This mod has a GUI configuration screen located next to the "ViaFabricPlus" button.

![Button location screenshot.](https://github.com/tracystacktrace/vfpchatfilterpatch/raw/main/docs/gui_1.png)

![Mod configuration screen.](https://github.com/tracystacktrace/vfpchatfilterpatch/raw/main/docs/gui_2.png)

Within the configuration screen, there are several buttons to allow you to change the behaviour of this mod.

### Modes (First Button)

There are currently two modes available - allowing only characters from the config font.txt (located `config/vfpchatfilterpatch/font.txt`) or allowing any characters.

### Force Reload `font.txt` (Second Button)

This button simply force reloads the font.txt, if the changes were made in the file and for some reason a reload is required without restarting the whole game.

### Replace Sequence

If the current mode is using the characters from `font.txt`, then any illegal characters will be replaced with the "replace sequence". By default, it's empty, so any illegal character is removed; however, you can make it replace with other characters or even strings if needed (i.e. `*`, `#`).

## License

This mod is licensed under Apache License 2.0.