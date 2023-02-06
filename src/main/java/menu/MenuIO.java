package menu;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class MenuIO {

    private static JSONObject getMenuObjectDetails(MenuObject mo) {
        JSONObject menuDetails = new JSONObject();
        menuDetails.put("id", mo.getId());
        menuDetails.put("name", mo.getName());
        menuDetails.put("isEnabled", mo.isEnabled());
        JSONObject menuTable = new JSONObject();
        menuTable.put("rows", mo.getCellTable().getX());
        menuTable.put("cols", mo.getCellTable().getY());
        menuDetails.put("cellTable", menuTable);
        if (mo.hasChildren()) {
            JSONArray items = new JSONArray();
            for (MenuObject item : mo.getItems()) {
                items.add(getMenuObjectDetails(item));
                menuDetails.put("items", items);
            }
        }
        return menuDetails;
    }

    public static void saveMenu(String path, MenuObject mo) {
        JSONObject menuDetails = getMenuObjectDetails(mo);
        try (FileWriter file = new FileWriter(path)) {
            file.write(menuDetails.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static MenuObject loadMenuObject(JSONObject obj) {
        MenuObject mo = new MenuObject();

        long id = (Long) obj.get("id");
        String name = (String) obj.get("name");
        boolean isEnabled = (Boolean) obj.get("isEnabled");
        JSONObject cellTable = (JSONObject) obj.get("cellTable");
        long rows = (Long) cellTable.get("rows");
        long cols = (Long) cellTable.get("cols");

        mo.setId((int)id);
        mo.setName(name);
        mo.setEnabled(isEnabled);
        mo.setTable((int)rows, (int)cols);

        if (obj.containsKey("items")) {
            JSONArray items = (JSONArray) obj.get("items");
            for (Object o : items) {
                MenuObject item = loadMenuObject((JSONObject) o);
                mo.add(item);
            }
        }

        return mo;
    }

    public static MenuObject loadMenu(String path) {
        MenuObject mo = null;
        JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader(path)) {
            JSONObject obj = (JSONObject) jsonParser.parse(reader);
            mo = loadMenuObject(obj);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return mo;
    }

}
