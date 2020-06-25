package engineering.b67.intellij_linter_base;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.vfs.VirtualFile;

import java.util.ArrayList;

public class Executor {
    protected static final Logger log = Logger.getInstance(Executor.class);

    private ArrayList<String> parameters;
    private String executable;
    private VirtualFile virtualFile;
    private String basePath;

    public Executor(String executable, VirtualFile virtualFile, String basePath) {
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

        // FIXME: If it have interpreter,
        // then needs to be splitted due to escaping
        if (executable.contains("|")) {
            String[] executableDetails = executable.split("\\|");
            commandLine.setExePath(executableDetails[0]);

            for (int i = 1; i < executableDetails.length; i++) {
                commandLine.addParameters(executableDetails[i]);
            }
        } else {
            commandLine.setExePath(executable);
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
