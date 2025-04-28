# AutoTrash

![Minecraft](https://img.shields.io/badge/Minecraft-1.14_--_1.20.4-brightgreen)
![License](https://img.shields.io/badge/License-CC0-blue.svg)
![Build Status](https://img.shields.io/badge/Build-Passing-brightgreen)

AutotTrash is a versatile and customizable Automatic Trash system for Minecraft 1.14 to 1.20.4 servers. It provides a simple to use approuch to permission based item removal!

For most updated build you can use the Jenkins Build.
https://jenkins.pizzalover.dev/job/AutoTrash/

## Features

- **Custom Item Support**: Since the plugin encodes the itemstack into Base64, any custom items will work and all it's data is saved. Making any custom item work!
- **Permission Based**: AutoTrash uses permissions to handle which items are deleted, making it extremely easy to prepare toggle's for it.
- **Low Performance Usage**: Due to how simple AutoTrash is, it doesn't affect the server's performance as other plugins would.
- **User-Friendly**: Easy to set up and configure.
- **Folia Support**: Can use this plugin with Folia! (Barely Tested)

## Getting Started

### Prerequisites

- Minecraft server running version 1.14 to 1.20.4
- Permission System

### Installation

1. **Download the Plugin:**
   Download the latest version of AutoTrash from the [Releases](https://github.com/PizzaThatCodes/AutoTrash/releases) page.

2. **Install the Plugin:**
   Place the downloaded `AutoTrash.jar` file in your server's `plugins` directory.

3. **Restart the Server:**
   Restart your Minecraft server to load the plugin.

### Configuration

AutoTrash provides a configuration file to customize the AFK modules and rewards.

1. **Locate the Configuration File:**
   After the first start, the configuration file will be generated in the `plugins/AutoTrash` directory.

2. **Edit the Configuration File:**
   Open the `config.yml` file with a text editor and configure the settings to your preference.

Below is an example configuration of `messages.yml`:

```
prefix: "&7[&6&lAutoTrash&7]&r"
messages:
  console-cannot-use-command: "{prefix} &cYou cannot use this command from the console."
  no-permission: "{prefix} &cYou do not have permission to use this command."

admin:
  commands:
    reload:
      success: "{prefix} &aSuccessfully reloaded the AutoTrash configuration."
    help:
    - "{prefix} &7AutoTrash Commands:"
    - "{prefix} &7- &6/autotrash add <permission>&7- Add the item in your hand to the AutoTrash list."
    - "{prefix} &7- &6/autotrash list &7- List all items in the AutoTrash list."
    - "{prefix} &7- &6/autotrash reload &7- Reload the AutoTrash configuration."
  messages:
    console-cannot-use-command: "{prefix} &cYou cannot use this command from the console."
  addItem:
    no-item-in-hand: "{prefix} &cYou must be holding an item to add it to the AutoTrash list."
    success: "{prefix} &aSuccessfully added item to the AutoTrash list."
    usage: "{prefix} &cUsage: /autotrash add <permission>"
```

Below is an example configuration of `config.yml`:

```
commands:
  admin:
    permission: 'autotrash.admin'

settings:
  confirm-delete: true
```

### Commands and Permissions

- `/autotrash <list:add:reload> <permission>` - Add and/or list the items & reload the plugin configuration.
  - Permission: `autotrash.admin`

### Default Permissions

- `autotrash.admin` - Allows players to add|list items, & reload the config, you are able to change this in `config.yml`.

## Contributing

We welcome contributions from the community! To contribute:

1. Fork the repository.
2. Create a new branch with your feature or bugfix.
3. Submit a pull request.

## License

This project is licensed under the CC0 License - see the [LICENSE](LICENSE) file for details.

## Contact

For support and inquiries, please join my [Discord server](https://discord.gg/pj8auQEPJU) or open an issue on GitHub.

---

Thank you for using AutoTrash! I hope it enhances your Minecraft server experience.
