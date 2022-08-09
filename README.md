# ByteWorld Discord Events Mod

This Minecraft Fabric mod watches the Fabric Allow Death event and sends the player death message to a discord web hook.

## Building the source
* Open the repository in Intellij
* Let the gradle processes run.
* Run the 'build' gradle task to create the jar file (should end up in build/libs)

## Setup

* Add the jar to your server's Fabric mods folder.
* Run the server.
* Stop the server.
* Edit the ByteWorldDiscordEvents.json file to set your bot name and your discord web hook URL. 
* Start the server

## License
My hacked together code (everything .java in src) in this simple mod is under an MIT License.

The original example mod that this is based on can be found at https://github.com/FabricMC/fabric-example-mod and is under a CC0 license. Mostly it provides the gradle build and IntelliJ integration.
