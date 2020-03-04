package engineering.b67.intellij_linter_base;

import java.util.List;

public abstract class Result {

    protected String output;
    protected String pluginName = "Undefined";
    protected List<Warning> warnings;

    public Result(String output) {
        this.output = output;
    }

    public abstract List<Warning> getWarnings();
}
