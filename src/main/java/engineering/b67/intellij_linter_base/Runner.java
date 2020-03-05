package engineering.b67.intellij_linter_base;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public interface Runner {

    ArrayList<String> getParameters();

    String getDefaultExecutable();

    Executor createExecutor(String executable, VirtualFile virtualFile, String basePath);

    List<Warning> execute(@NotNull final Editor editor);
}
