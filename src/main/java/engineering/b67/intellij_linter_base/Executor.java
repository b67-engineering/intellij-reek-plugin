package engineering.b67.intellij_linter_base;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.openapi.vfs.VirtualFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Executor {

    private String[] parameters;
    private String executable;
    private VirtualFile virtualFile;
    private String basePath;

    public Executor(String executable, VirtualFile virtualFile, String basePath) {
        this.executable = executable;
        this.virtualFile = virtualFile;
        this.basePath = basePath;
    }

    public void setParameters(String[] parameters) {
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

    // FIXME: Delegate suffix outside
    private Path temporaryFile() throws LinterException {
        try {
            return Files.createTempFile(null, ".rb");
        } catch (IOException ex) {
            throw new LinterException(ex);
        }
    }
}
