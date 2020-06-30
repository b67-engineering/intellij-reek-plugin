package engineering.b67.intellij_linter_base;

public class CommandContext {
    private final String executable;
    private final String interpreter;

    public CommandContext(
        String executable,
        String interpreter
    ) {
        this.executable = executable;
        this.interpreter = interpreter;
    }

    public String getExecutable() {
        return executable;
    }

    public String getInterpreter() {
        return interpreter;
    }
}
