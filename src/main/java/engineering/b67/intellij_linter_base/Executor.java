package engineering.b67.intellij_linter_base;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.vfs.VirtualFile;

import java.util.ArrayList;

public class Executor {
    protected static final Logger log = Logger.getInstance(Executor.class);

    private ArrayList<String> parameters;
    private final CommandContext executable;
    private final VirtualFile virtualFile;
    private final String basePath;

    public Executor(CommandContext executable, VirtualFile virtualFile, String basePath) {
        this.executable = executable;
        this.virtualFile = virtualFile;
        this.basePath = basePath;
    }

    public void setParameters(ArrayList<String> parameters) {
        this.parameters = parameters;
    }

    public Process run() {
        log.info("Command: " + commandLine().getCommandLineString());

        try {
            return commandLine().createProcess();
        } catch (ExecutionException exception) {
            throw new LinterException(exception);
        }
    }

    private GeneralCommandLine commandLine() {
        final GeneralCommandLine commandLine = new GeneralCommandLine();

        if (executable.getInterpreter() == null) {
            commandLine.setExePath(executable.getExecutable());
        } else {
            commandLine.setExePath(executable.getInterpreter());
            commandLine.addParameters(executable.getExecutable());
        }

        commandLine.setWorkDirectory(basePath);
        commandLine.withEnvironment(System.getenv());
        commandLine.withParentEnvironmentType(GeneralCommandLine.ParentEnvironmentType.CONSOLE);

        for (String parameter : parameters) {
            commandLine.addParameters(parameter);
        }

        commandLine.addParameter(this.virtualFile.getPath());

        return commandLine;
    }
}
