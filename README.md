
# Expanded AE2 Fork for Cosmic Frontiers Graciously Provided by Kolja
Fixes many of the cross-mod issues between the two mods



> [!NOTE]
> The Entire Cosmolite Suite is used for Cosmic Frontiers, however may work with other packs if they adopt all the same mods.
> Feel free to suggest content and report issues at : [The Cosmic Frontiers Issue Tracker](https://github.com/Frontiers-PackForge/CosmicFrontiers/issues) firstly so we can verify this is an error of our own creation!
> 



# Credits and Original Description 

Expanded AE currently only adds a few new features.
## At first this mod was made to only add a bigger pattern provider, now it has turned into a full on project to port all of those feature you miss from old ae2 and its addons. I'm always open to further suggestions.
Currently porting features from:
- [GT New Horizons AE2](https://github.com/GTNewHorizons/Applied-Energistics-2-Unofficial)
- [AE2-UEL](https://github.com/AE2-UEL/Applied-Energistics-2)
- [Neeve's AE2](https://github.com/AE2-UEL/NAE2)

## What's included currently:

- Expanded Pattern Provider which has 72 slots for patterns so you don't have to build workarounds if you require more pattern slots for one machine, also added is the cable part version.

- Upgrade item, so you can upgrade existing pattern providers while keeping their patterns in them.

## Roadmap
- [x] Pattern Refiller Card 
  - Upgrade for Pattern Terminals, automatically restocks the terminal with blank patterns
- [ ] Sticky Card
  - Lets an item/fluid enter the network only on that storage bus.
Basically, if you had Storage Bus A and B, and one had a sticky card with the filter to cobblestone. Storage Bus A would be the only option even if Storage Bus B had a higher priority/open space etc. If an item can't be pushed/sent to where a sticky card is located for it, the item will never move through the network
- [ ] Advanced Blocking Card
  - Exposes the entire networks contents when placed in an interface, allowing blocking mode to be used on an entire subnet.
  
  - Basically, when you place the card inside of an interface/extended interface/etc, It exposes the entire contents of the connected network to itself, which makes it work with blocking mode
  
  - HOWEVER due to how Modern AE2 treats blocking mode in modern vs 1.7.10, what the card should realistically do on modern is disable insertion to the interface it's in, if the network contains items, as then it's effectively blocking mode 'regardless' of what pattern   provider pushes to that subnet, which would be extremely potent for larger automation setups
- [ ] ~~ME Filter Configuration terminal~~ -> ME Interface Terminal
  - ~~A terminal that allows you to view all filters that connected devices contain for ease of locating~~ -> Configure all Interfaces from a singular terminal

- [ ] Pattern optimization matrix
  - Basically gives you the ability to multiply all patterns from the crafting menu, really basic explanation but check gtnh ae2 if this interests you. Still debating if there's any actual usecase for this.
     
- [x] Pattern encoding optimization
  - Shift clicking when encoding will add the pattern to your inventory
  - Ability to multiple/divide processing patterns directly in the terminal (credits: [Ghostipedia](https://github.com/Ghostipedia) <3)
- [x] Pattern provider changes
  - Add button to multiply/divide all patterns inside

---
_Note: The Files [IDEAS.md](<https://github.com/ko-lja/expandedae/blob/cosmic-frontiers/IDEAS.md>) and [TODO.md](<https://github.com/ko-lja/expandedae/blob/cosmic-frontiers/TODO.md>) are licensed under All Rights Reserved_
