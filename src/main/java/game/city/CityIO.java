package game.city;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class CityIO {

    public static void saveCity(String path, City city) {
        JSONObject cityDetails = new JSONObject();
        JSONObject cityDimensions = new JSONObject();
        cityDimensions.put("x", city.getDimensions().getX());
        cityDimensions.put("y", city.getDimensions().getY());
        cityDetails.put("dimensions", cityDimensions);
        JSONArray cells = new JSONArray();
        for (CityCell c : city.getCells()) {
            JSONObject cellDetails = new JSONObject();
            cellDetails.put("id", c.getId());
            JSONObject cellPos = new JSONObject();
            cellPos.put("x", c.getPos().getX());
            cellPos.put("y", c.getPos().getY());
            cellDetails.put("position", cellPos);
            cellDetails.put("id", c.getId());
            cellDetails.put("imgIndex", c.getImgIndex());
            cellDetails.put("isRoad", c.isRoad());
            cellDetails.put("isBuilding", c.isBuilding());
            cellDetails.put("isSolid", c.isSolid());
            cells.add(cellDetails);
        }
        cityDetails.put("cells", cells);

        try (FileWriter file = new FileWriter(path)) {
            file.write(cityDetails.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static City loadCity(String path) {
        City city = null;
        JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader(path)) {

            JSONObject obj = (JSONObject) jsonParser.parse(reader);

            JSONObject cityDimensions = (JSONObject) obj.get("dimensions");
            long numCellsX = (Long) cityDimensions.get("x");
            long numCellsY = (Long) cityDimensions.get("y");
            city = new City((int) numCellsX, (int) numCellsY);

            JSONArray cells = (JSONArray) obj.get("cells");
            for (Object c : cells) {
                JSONObject cell = (JSONObject) c;
                JSONObject cellPosition = (JSONObject) cell.get("position");
                long posX = 0;
                long posY = 0;
                if (cell != null) {
                    posX = (Long) cellPosition.get("x");
                    posY = (Long) cellPosition.get("y");
                }
                long imgIndex = (Long) cell.get("imgIndex");
                long id = (Long) cell.get("id");
                Boolean isRoad = (Boolean) cell.get("isRoad");
                Boolean isBuilding = (Boolean) cell.get("isBuilding");
                Boolean isSolid = (Boolean) cell.get("isSolid");

                CityCell cityCell = new CityCell((int) id, (int) posX, (int) posY);
                cityCell.setImgIndex((int) imgIndex);
                cityCell.setRoad(isRoad);
                cityCell.setBuilding(isBuilding);
                cityCell.setSolid(isSolid);

                city.setCell((int) posX, (int) posY, cityCell);

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return city;
    }

}
