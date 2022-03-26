# Dynamic Skins

Welcome to the **Dynamic Skins** mod repository!

This mod allows you to use online URLs to dynamically swap your skin in-game. It is made for **1.12.2**.

The idea is simple : You upload your skins somewhere (**imgur**, **discord**, etc...) 
then create and wear your new skins with commands by binding them to URLs.

With this mod, you can manage multiple skins easily and even wear **HD Skins**.

No need to worry about the model of the skin (alex or steve), it will auto-adjust your player's model in-game!

You can download the mod on [curseforge](https://www.curseforge.com/minecraft/mc-mods/dynamicskins).

This mod needs to be installed on **client-side** and on **server-side**.

**Dynamic Skins** is a **coremod** which uses [mixins](https://github.com/SpongePowered/Mixin) (by [SpongePowered](https://github.com/SpongePowered))

For more information about the libraries used by the mod, please check the **LICENSE-MIXINS** file.

## Hosts whitelist

For security purposes, to not load data from any kind of URLs,
the allowed hosts (websites from where you can download your skins) are managed through a whitelist.

By default, there are some popular hosts already loaded but then, you can add new ones **if you have the required rights**.

Launch your server / game once to initialize this whitelist.

You'll find the whitelist file in `config\dynamicskins\dynamic_skins_hosts_whitelist.txt`

### Whitelist commands

There are some commands you can use to manage the whitelist (with `/dynamicskinswhitelist` or `/dskinsw`)

You can directly add new hosts manually in the whitelist file and then run this command in-game :

```
/dskinsw reload
```

You can also just manage the whitelist directly in game.

Check all the whitelisted hosts :

```
/dskinsw
```

Add a new host :

```
/dskinsw add <hostname>
```

Remove a host :

```
/dskinsw remove <hostname>
```

## Manage your skins

**Every players** can create, store, wear and manage their own dynamic skins 
(with a limit of 30, by default, which can be changed on the server side).

There are some commands you can use to manage your skins (with `/dynamicskins` or `/dskins`)

To **create** a new skin, just use :

```
/dskins set <skinName> <URL>
```

It will also work if you want to **update** an existing skin.

Make sures the URL ends with **.png** or **.jpg** or **.bmp**.

Files must not be larger than 3 megabytes and can have a **FULL HD** resolution.

If an error message tells you the host is not whitelisted, you can maybe submit 
a request to the administrators of your server to ask if they can add it.

If you have enough permissions, the mod will tells you want command to run to add the host.

Now, to start **wearing** a skin (to display it on you) you can simply use :

```
/dskins wear <skinName>
```

You can execute it again to swap the skin, or, if you just want to stop wearing the dynamic skin, you can use :

```
/dskins unwear
```

You and the other players will be able to see your dynamic skin!

If you want to **remove** one of your skins, you can use :

```
/dskins remove <skinName>
```

If you need to check what URL is bound to a skin of yours, you can check it with :

```
/dskins checkurl <skinName>
```

At last, you can check all the dynamic skins you registered with :

```
/dskins list
```

## Configuration

For now, the mod allows you to configure two things

### Client-side

On client-side, you can enable or disable dynamic skins loading. If this option is disabled, 
you won't be able to see custom skins anymore or load them from remote URLs.

### Server-side

On server-side, you can change the maximum amount of skins that a player can store. By default, 
the limit is set on 30 (that's basically just URLs, so not too much data).

You can directly edit this parameter in the configuration file or through this command :

```
/dskinsc skinslimit <amount>
```

You can also use `/dynamicskinsconfig` instead of `/dskinsc`