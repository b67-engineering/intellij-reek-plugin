package engineering.b67.intellij_reek_plugin;

import engineering.b67.intellij_linter_base.Result;
import engineering.b67.intellij_linter_base.Warning;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ReekResult extends Result {

    protected String pluginName = "Reek";

    public ReekResult(String output) {
        super(output);
    }

    @Override
    public List<Warning> getWarnings() {
        return parseOutput(this.output, new ArrayList<>());
    }

    private List<Warning> parseOutput(String output, List<Warning> warnings) {
        try {
            JSONArray jsonArray = new JSONArray(output);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject row = jsonArray.getJSONObject(i);

                warnings.add(createWarning(row));
            }
        } catch (JSONException ignored) { }

        return warnings;
    }

    private Warning createWarning(JSONObject jsonObject) throws JSONException {
        Warning warning = new Warning(
                jsonObject.getJSONArray("lines").getInt(0),
                0,
                jsonObject.getString("smell_type"),
                jsonObject.getString("message")
        );

        warning.setPlugin(this.pluginName);
        warning.setUrl(jsonObject.getString("documentation_link"));

        return warning;
    }

}
