package engineering.b67.intellij_linter_base;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.diagnostic.Logger;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class ExecutorRunner implements Runner {
    protected static final Logger log = Logger.getInstance(ExecutorRunner.class);

    public Executor createExecutor(String executable, VirtualFile virtualFile, String basePath, Service state) {
        Executor executor = new Executor(
                executable,
                virtualFile,
                basePath
        );

        executor.setParameters(getParameters(state));

        return executor;
    }

    // ref. https://intellij-support.jetbrains.com/hc/en-us/community/posts/360004284939-How-to-trigger-ExternalAnnotator-running-immediately-after-saving-the-code-change-
    protected VirtualFile createVirtualFile(Document document, String extension) {
        Path path;

        try {
            path = Files.createTempFile(null, String.format(".%s", extension));
        } catch (IOException exception) {
            throw new LinterException(exception);
        }

        try {
            try (BufferedWriter out = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
                out.write(document.getText());
            }

            return LocalFileSystem.getInstance().findFileByPath(path.toString());
        } catch (IOException exception) {
            throw new LinterException(exception);
        }
    }

    protected void deleteVirtualFile(VirtualFile file) {
        try {
            Files.delete(Paths.get(file.getPath()));
        } catch (IOException exception) {
            log.error("Cannot remove temporary file!", exception);
        }
    }
}
