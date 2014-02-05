# The Walking Braid
This is a simple 2D cross-platform game written using [libgdx](https://github.com/libgdx/libgdx) engine.
The player is animated as [Braid](http://braid-game.com/) character. Your goal is to collect keys in the maze within time limit of one minute.
* the player controlled via touchpad
* each collected key adds 10 seconds to the time limit
* hitting monsters slows player down for 5 seconds

## Implementation
The touchpad handling and implementation was taken from [Touchpad Example](http://www.bigerstaff.com/libgdx-touchpad-example/).
The project template was taken from [libgdx Gradle Template](https://github.com/libgdx/libgdx-gradle-template).
There is collision detection with walls based on map tiles. The collision between player, mimics and keys is done via geometrical rectangles overlaps.
There are three coordinate systems: _Screen_, _World_ and _Camera_
* Screen is a device coordinate system
* World is tiled map system
* Camera is visible World part on the Screen

## Media
The main character animation has two sprite sheets: _idle_ and _running_.
The monsters are hopping _Mimics_ form the Braid game. The font in the game is [AmazDooM](http://www.dafont.com/amazdoom.font) font.
The game level was created using [Tiled](http://www.mapeditor.org/) map editor (Sewer tileset).
The sprite sheets where created in Gimp and [Sprite Sheet](http://registry.gimp.org/node/20943) plugin.

## How to run
```
./gradlew build
./gradlew desktop:run
```
to create eclipse project
```
./gradlew eclipse
```
more info at https://github.com/libgdx/libgdx-gradle-template

## Links
* libgdx https://github.com/libgdx/libgdx
* libgdx Gradle template https://github.com/libgdx/libgdx-gradle-template
* Braid Graphics Briefcase http://www.davidhellman.net/braidbrief.htm
* AmazDooM font http://www.dafont.com/amazdoom.font. Conversion from **TTF** into **fnt** done via Hiero tool from libgdx-tools
* Hourglass icon https://www.iconfinder.com/icons/34227/clock_history_hourglass_pending_time_waiting_icon
* Tiled map editor http://www.mapeditor.org/
* Touchpad example www.bigerstaff.com/libgdx-touchpad-example/‎. Source code https://github.com/biggz/TouchPadTest
* Sprite Sheet Gimp plugin http://registry.gimp.org/node/20943