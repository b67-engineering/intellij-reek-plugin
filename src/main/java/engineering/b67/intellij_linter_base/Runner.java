package engineering.b67.intellij_linter_base;

import com.intellij.openapi.vfs.VirtualFile;
import engineering.b67.intellij_linter_base.exception.ContextException;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public interface Runner {

    ArrayList<String> getParameters(Service state);

    CommandContext getDefaultCommandContext(ExecutorContext executorContext) throws ContextException;

    String getFileExtension();

    String getOutput(Process process);

    Executor createExecutor(CommandContext executable, VirtualFile virtualFile, String basePath, Service state);

    List<Warning> execute(@NotNull final ExecutorContext executorContext) throws ContextException;
}
