package info.u_team.useful_railroads.data.provider;

import static info.u_team.useful_railroads.init.UsefulRailroadsBlocks.BUFFER_STOP;
import static info.u_team.useful_railroads.init.UsefulRailroadsBlocks.DIRECTION_RAIL;
import static info.u_team.useful_railroads.init.UsefulRailroadsBlocks.HIGHSPEED_RAIL;
import static info.u_team.useful_railroads.init.UsefulRailroadsBlocks.INTERSECTION_RAIL;
import static info.u_team.useful_railroads.init.UsefulRailroadsBlocks.SPEED_CLAMP_RAIL;
import static info.u_team.useful_railroads.init.UsefulRailroadsBlocks.TELEPORT_RAIL;
import static info.u_team.useful_railroads.init.UsefulRailroadsItems.DOUBLE_TRACK_BUILDER;
import static info.u_team.useful_railroads.init.UsefulRailroadsItems.SINGLE_TRACK_BUILDER;

import info.u_team.u_team_core.data.CommonLanguagesProvider;
import info.u_team.u_team_core.data.GenerationData;
import info.u_team.useful_railroads.init.UsefulRailroadsCreativeTabs;

public class UsefulRailroadsLanguagesProvider extends CommonLanguagesProvider {
	
	public UsefulRailroadsLanguagesProvider(GenerationData generationData) {
		super(generationData);
	}
	
	@Override
	public void register() {
		final String teleportRailBlockKey = "block.usefulrailroads.teleport_rail";
		final String trackBuilderItemKey = "item.usefulrailroads.track_builder";
		
		final String teleportRailContainerKey = "container.usefulrailroads.teleport_rail";
		final String trackBuilderContainerKey = "container.usefulrailroads.track_builder";
		
		// English
		
		add(UsefulRailroadsCreativeTabs.TAB.get(), "Useful Railroads");
		
		addBlock(HIGHSPEED_RAIL, "High Speed Rail (5 Blocks/Tick)");
		addBlock(SPEED_CLAMP_RAIL, "Speed Clamp Rail (0.25 Blocks/Tick)");
		addBlock(DIRECTION_RAIL, "Direction Rail");
		addBlock(INTERSECTION_RAIL, "Intersection Rail");
		addBlock(TELEPORT_RAIL, "Teleport Rail");
		
		addBlock(BUFFER_STOP, "Buffer Stop");
		add(teleportRailBlockKey + ".missing_setup", "Cannot place. Setup the rail first.");
		add(teleportRailBlockKey + ".how_to_setup", "Drop the rail and an enderpearl at a location and wait till a lightning stike appears.");
		add(teleportRailBlockKey + ".not_enough_fuel", "Not enough fuel. This teleportation costs %s fuel.");
		
		addItem(SINGLE_TRACK_BUILDER, "Single Track Builder");
		addItemTooltip(SINGLE_TRACK_BUILDER, "", 0, "%s to open settings gui");
		addItemTooltip(SINGLE_TRACK_BUILDER, "", 1, "%s to execute build operation");
		addItem(DOUBLE_TRACK_BUILDER, "Double Track Builder");
		addItemTooltip(DOUBLE_TRACK_BUILDER, "", 0, "%s to open settings gui");
		addItemTooltip(DOUBLE_TRACK_BUILDER, "", 1, "%s to execute build operation");
		add(trackBuilderItemKey + ".not_enough_fuel", "Not enough fuel. This operation costs %s fuel.");
		add(trackBuilderItemKey + ".not_enough_blocks", "Not enough blocks.");
		
		add(teleportRailContainerKey, "Teleport Rail");
		add(teleportRailContainerKey + ".dimension", "Dimension");
		add(teleportRailContainerKey + ".x", "X");
		add(teleportRailContainerKey + ".y", "Y");
		add(teleportRailContainerKey + ".z", "Z");
		add(teleportRailContainerKey + ".fuel", "Fuel");
		add(teleportRailContainerKey + ".consumption", "Consumption");
		add(trackBuilderContainerKey, "Track Builder");
		add(trackBuilderContainerKey + ".rails", "Rails");
		add(trackBuilderContainerKey + ".ground_blocks", "Ground Blocks");
		add(trackBuilderContainerKey + ".tunnel_blocks", "Tunnel Blocks");
		add(trackBuilderContainerKey + ".redstone_torches", "Redstone Torches");
		add(trackBuilderContainerKey + ".torches", "Torches (light)");
		add(trackBuilderContainerKey + ".fuel", "Fuel");
		add(trackBuilderContainerKey + ".mode", "Mode");
		add(trackBuilderContainerKey + ".mode.noair", "No extra tunnel");
		add(trackBuilderContainerKey + ".mode.3x3", "3x3 air tunnel");
		add(trackBuilderContainerKey + ".mode.5x5", "5x5 air tunnel");
		add(trackBuilderContainerKey + ".mode.tunnel", "Tunnel with blocks & light");
		
		addTooltip("click", "right_click", 0, "Right click");
		addTooltip("click", "shift_right_click", 0, "Shift + Right click");
		
		// German
		
		add("de_de", UsefulRailroadsCreativeTabs.TAB.get(), "Nützliche Eisenbahn Erweiterungen");
		
		addBlock("de_de", HIGHSPEED_RAIL, "Hochgeschwindigkeitsschiene (5 Blöcke/Tick)");
		addBlock("de_de", SPEED_CLAMP_RAIL, "Bremsschiene (0.25 Blöcke/Tick)");
		addBlock("de_de", DIRECTION_RAIL, "Richtungsschiene");
		addBlock("de_de", INTERSECTION_RAIL, "Kreuzungsschiene");
		addBlock("de_de", TELEPORT_RAIL, "Teleportationsschiene");
		
		addBlock("de_de", BUFFER_STOP, "Prellbock");
		add("de_de", teleportRailBlockKey + ".missing_setup", "Kann nicht platziert werden. Richte zuerst die Schiene ein.");
		add("de_de", teleportRailBlockKey + ".how_to_setup", "Lege die Schiene und eine Enderperle auf den Boden und warte bis ein Blitz dort einschlägt.");
		add("de_de", teleportRailBlockKey + ".not_enough_fuel", "Nicht genug Treibstoff. Diese Teleportation kostet %s Treibstoff.");
		
		addItem("de_de", SINGLE_TRACK_BUILDER, "Einzelner Gleisbauer");
		addItemTooltip("de_de", SINGLE_TRACK_BUILDER, "", 0, "%s zum Öffnen des Einstellungsfensters");
		addItemTooltip("de_de", SINGLE_TRACK_BUILDER, "", 1, "%s zum Ausführen der Bauoperation");
		addItem("de_de", DOUBLE_TRACK_BUILDER, "Doppelter Gleisbauer");
		addItemTooltip("de_de", DOUBLE_TRACK_BUILDER, "", 0, "%s zum Öffnen des Einstellungsfensters");
		addItemTooltip("de_de", DOUBLE_TRACK_BUILDER, "", 1, "%s zum Ausführen der Bauoperation");
		add("de_de", trackBuilderItemKey + ".not_enough_fuel", "Nicht genug Treibstoff. Dieser Vorgang kostet %s Treibstoff.");
		add("de_de", trackBuilderItemKey + ".not_enough_blocks", "Nicht genügend Blöcke.");
		
		add("de_de", teleportRailContainerKey, "Teleportationsschiene");
		add("de_de", teleportRailContainerKey + ".dimension", "Dimension");
		add("de_de", teleportRailContainerKey + ".x", "X");
		add("de_de", teleportRailContainerKey + ".y", "Y");
		add("de_de", teleportRailContainerKey + ".z", "Z");
		add("de_de", teleportRailContainerKey + ".fuel", "Treibstoff");
		add("de_de", teleportRailContainerKey + ".consumption", "Verbrauch");
		add("de_de", trackBuilderContainerKey, "Gleisbauer");
		add("de_de", trackBuilderContainerKey + ".rails", "Schienen");
		add("de_de", trackBuilderContainerKey + ".ground_blocks", "Schotterblöcke");
		add("de_de", trackBuilderContainerKey + ".tunnel_blocks", "Tunnelblöcke");
		add("de_de", trackBuilderContainerKey + ".redstone_torches", "Redstone Fackeln");
		add("de_de", trackBuilderContainerKey + ".torches", "Fackeln (Licht)");
		add("de_de", trackBuilderContainerKey + ".fuel", "Treibstoff");
		add("de_de", trackBuilderContainerKey + ".mode", "Modus");
		add("de_de", trackBuilderContainerKey + ".mode.noair", "Kein extra Tunnel");
		add("de_de", trackBuilderContainerKey + ".mode.3x3", "3x3 Lufttunnel");
		add("de_de", trackBuilderContainerKey + ".mode.5x5", "5x5 Lufttunnel");
		add("de_de", trackBuilderContainerKey + ".mode.tunnel", "Tunnel mit Blöcken & Licht");
		
		addTooltip("de_de", "click", "right_click", 0, "Rechtsklick");
		addTooltip("de_de", "click", "shift_right_click", 0, "Umschalttaste + Rechtsklick");
	}
}
