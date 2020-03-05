package engineering.b67.intellij_linter_base;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.openapi.vfs.VirtualFile;

import java.util.ArrayList;

public class Executor {

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
        try {
            return commandLine().createProcess();
        } catch (ExecutionException exception) {
            throw new LinterException(exception);
        }
    }

    private GeneralCommandLine commandLine() {
        final GeneralCommandLine commandLine = new GeneralCommandLine();

        commandLine.setExePath(executable);
        commandLine.setWorkDirectory(basePath);
        commandLine.withEnvironment(System.getenv());

        for (String parameter : parameters) {
            commandLine.addParameters(parameter);
        }

        commandLine.addParameter(this.virtualFile.getPath());

        return commandLine;
    }
}
