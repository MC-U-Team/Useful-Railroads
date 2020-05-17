package info.u_team.useful_railroads.data.provider;

import static info.u_team.useful_railroads.init.UsefulRailroadsBlocks.*;
import static info.u_team.useful_railroads.init.UsefulRailroadsItemGroups.GROUP;
import static info.u_team.useful_railroads.init.UsefulRailroadsItems.*;

import info.u_team.u_team_core.data.*;
import net.minecraft.block.Block;
import net.minecraft.item.Item;

public class UsefulRailroadsLanguagesProvider extends CommonLanguagesProvider {
	
	public UsefulRailroadsLanguagesProvider(GenerationData data) {
		super(data);
	}
	
	@Override
	public void addTranslations() {
		final String teleportRailBlockKey = "block.usefulrailroads.teleport_rail";
		final String trackBuilderItemKey = "item.usefulrailroads.track_builder";
		
		final String teleportRailContainerKey = "container.usefulrailroads.teleport_rail";
		final String trackBuilderContainerKey = "container.usefulrailroads.track_builder";
		
		// English
		
		add(GROUP, "Useful Railroads");
		
		add(HIGHSPEED_RAIL, "High Speed Rail (5 Blocks/Tick)");
		add(SPEED_CLAMP_RAIL, "Speed Clamp Rail (0.25 Blocks/Tick)");
		add(DIRECTION_RAIL, "Direction Rail");
		add(INTERSECTION_RAIL, "Intersection Rail");
		add(TELEPORT_RAIL, "Teleport Rail");
		
		add(BUFFER_STOP, "Buffer Stop");
		add(teleportRailBlockKey + ".missing_setup", "Cannot place. Setup the rail first.");
		add(teleportRailBlockKey + ".how_to_setup", "Drop the rail and an enderpearl at a location and wait till a lightning stike appears.");
		add(teleportRailBlockKey + ".not_enough_fuel", "Not enough fuel. This teleportation costs %s fuel.");
		
		add(SINGLE_TRACK_BUILDER, "Single Track Builder");
		addTooltip(SINGLE_TRACK_BUILDER, 0, "%s to open settings gui");
		addTooltip(SINGLE_TRACK_BUILDER, 1, "%s to execute build operation");
		add(DOUBLE_TRACK_BUILDER, "Double Track Builder");
		addTooltip(DOUBLE_TRACK_BUILDER, 0, "%s to open settings gui");
		addTooltip(DOUBLE_TRACK_BUILDER, 1, "%s to execute build operation");
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
		
		add("usefulrailroads.tooltip.right_click", "Right click");
		add("usefulrailroads.tooltip.shift_right_click", "Shift + Right click");
		
		// German
		
		add("de_de", GROUP, "Nützliche Eisenbahn Erweiterungen");
		
		add("de_de", HIGHSPEED_RAIL, "Hochgeschwindigkeitsschiene (5 Blöcke/Tick)");
		add("de_de", DIRECTION_RAIL, "Richtungsschiene");
		add("de_de", INTERSECTION_RAIL, "Kreuzungsschiene");
		add("de_de", TELEPORT_RAIL, "Teleportationsschiene");
		
		add("de_de", BUFFER_STOP, "Prellbock");
		add("de_de", teleportRailBlockKey + ".missing_setup", "Kann nicht platziert werden. Richte zuerst die Schiene ein.");
		add("de_de", teleportRailBlockKey + ".how_to_setup", "Lege die Schiene und eine Enderperle auf den Boden und warte bis ein Blitz dort einschlägt.");
		add("de_de", teleportRailBlockKey + ".not_enough_fuel", "Nicht genug Treibstoff. Diese Teleportation kostet %s Treibstoff.");
		
		add("de_de", SINGLE_TRACK_BUILDER, "Einzelner Gleisbauer");
		addTooltip("de_de", SINGLE_TRACK_BUILDER, 0, "%s zum Öffnen des Einstellungsfensters");
		addTooltip("de_de", SINGLE_TRACK_BUILDER, 1, "%s zum Ausführen der Bauoperation");
		add("de_de", DOUBLE_TRACK_BUILDER, "Doppelter Gleisbauer");
		addTooltip("de_de", DOUBLE_TRACK_BUILDER, 0, "%s zum Öffnen des Einstellungsfensters");
		addTooltip("de_de", DOUBLE_TRACK_BUILDER, 1, "%s zum Ausführen der Bauoperation");
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
		
		add("de_de", "usefulrailroads.tooltip.right_click", "Rechtsklick");
		add("de_de", "usefulrailroads.tooltip.shift_right_click", "Umschalttaste + Rechtsklick");
	}
	
	protected void addTooltip(Block key, int line, String value) {
		addTooltip(key.asItem(), line, value);
	}
	
	protected void addTooltip(String locale, Block key, int line, String value) {
		addTooltip(locale, key.asItem(), line, value);
	}
	
	protected void addTooltip(Item key, int line, String value) {
		add(key.getTranslationKey() + ".tooltip." + line, value);
	}
	
	protected void addTooltip(String locale, Item key, int line, String value) {
		add(locale, key.getTranslationKey() + ".tooltip." + line, value);
	}
	
}
