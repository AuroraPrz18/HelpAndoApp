package com.codepath.aurora.helpandoapp.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Theme {
    public static List<String> allThemes = new ArrayList<>();
    public static Map<String, String> themeKey = new HashMap<>();
    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /***
     * Build a list of Theme objects from a JSON Array, and save it in a static variable
     * @param jsonArray
     * @return
     * @throws JSONException
     */
    public static void jsonArrayToObjects(JSONArray jsonArray) throws JSONException {
        List<String> allThemesAux = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            allThemesAux.add(jsonToObject(jsonArray.getJSONObject(i)).getName());
        }
        allThemes.clear();
        allThemes.addAll(allThemesAux);
    }

    /**
     * Builds a Theme with the information provided inside a JSON Object
     *
     * @param json
     * @return
     * @throws JSONException
     */
    public static Theme jsonToObject(JSONObject json) throws JSONException {
        Theme theme = new Theme();
        theme.setId(json.getString("id"));
        theme.setName(json.getString("name"));
        return theme;
    }
}
