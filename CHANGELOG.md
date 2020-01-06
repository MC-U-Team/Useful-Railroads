# Changelog
All notable changes to this project will be documented in this file.

## [1.14.4-1.2.3.15] - 2020-01-06
### Added
 - Added tooltip information for track builder
 - Added tooltip information for teleport rail

## [1.14.4-1.2.2.14] - 2019-11-06
### Changed
 - Generate now all models (except the buffer stop)
 - Generate all other data files now (advancements moved place, but who cares about recipe book advancements?)
 - Fix bug, that buffer stop may not save the items inside
 - Fix bug, that the fuel shows negative values, if the values if above the short max value
 
### Added
 - Added german translation

## [1.14.4-1.2.2.13] - 2019-10-20
### Changed
 - Teleport rail now use the block middle
 - Buffer stop must now be powered to consume the minecarts
 - First 1.14.4 release

## [1.14.4-1.2.1.12] - 2019-10-13
### Changed
 - Track builder now places enough torches so mobs cannot spawn.

## [1.14.4-1.2.0.11] - 2019-09-27
### Changed
- Fixed crash on server side when opening guis with fuel slot

## [1.14.4-1.2.0.10] - 2019-09-27
### Changed
- Port to 1.14.4
- Teleport rail now works a bit better and fuel can now be added with datapacks 

### Added
- Added buffer stop (rail end). This block can consume and store the minecart if the minecart enters this block
- Added rail builder (single and double rail) that can easily build long distance rail roads.