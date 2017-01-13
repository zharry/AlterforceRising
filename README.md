# AlterforceRising
Grade 11, Computer Science Summative. This game is based around the mechanic of teleportation and developed from there. 

## How to play
1. WASD to move
2. Right Click to teleport
	- Press to display indicator
	- Ctrl to cancel teleport
	- Release to teleport to indicated location
3. Press F3 to toggle debug displays

## How to use F3 debug display
- The text in the top right is a just a display of the ingame variables
- The Pink boxes represents the bounding box of the sprite
- The Magenta boxes represents the collision box of that object
- The Yellow line represents the rotation calculation for the player
- The Red line represents the TP damage collision detector

### Developer Notes
#### All variables (measurements) should be in:
- Time: Ticks
- Distance: Pixels
- Angles: Degrees
- Prioritize Distance, Time then Angles

#### Location
- The x and y should always be located at the object's top left corner

#### Rotation
- The rotation should always happen about the center point of the object

#### Data Types
- Use ints for timers, counter and offsets
- Use doubles for everything else
- Cast doubles to int only when required to, do math in double