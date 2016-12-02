package util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import data.Direction;
import data.Tile;
import data.TileGrid;
import data.TileType;

public class MapManager {
	
	public static TileGrid loadMap(String name) {
		TileGrid map = new TileGrid();
		try {
			BufferedReader reader = new BufferedReader(new FileReader("src/maps/" + name + ".txt"));
			String[] startData = reader.readLine().split("\\s+");
			for (int i = 0; i < map.getHeight(); i++) {
				String data = reader.readLine();
				for (int j = 0; j < map.getWidth(); j++) {
					TileType type = toTileObject(data.substring(j, j + 1));
					map.setTile(j, i, type);
				}
			}
			reader.close();
			map.setStartTile(map.getTile(Integer.parseInt(startData[0]), Integer.parseInt(startData[1])));
			map.setStartDirection(Direction.values()[Integer.parseInt(startData[2])]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	public static void saveMap(String name, TileGrid map) {
		String mapData = "";
		for (int i = 0; i < map.getHeight(); i++) {
			for (int j = 0; j < map.getWidth(); j++) {
				mapData += toTileCode(map.getTile(j, i));
			}
			mapData += "\n";
		}
		try {
			File file = new File("src/maps/" + name + ".txt");
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write(mapData);
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static TileType toTileObject(String code) {
		TileType type = TileType.Blank;
		switch (code) {
		case "0":
			type = TileType.Blue;
			break;
		case "1":
			type = TileType.Gray;
			break;
		case "2":
			type = TileType.Red;
			break;
		case "3":
			type = TileType.Finish;
			break;
		}
		return type;
	}
	
	public static String toTileCode(Tile t) {
		String code = "";
		switch (t.getType()) {
		case Blue:
			code = "0";
			break;
		case Gray:
			code = "1";
			break;
		case Red:
			code = "2";
			break;
		case Finish:
			code = "3";
			break;
		default:
			code = "#";
			break;
		}
		return code;
	}
}
