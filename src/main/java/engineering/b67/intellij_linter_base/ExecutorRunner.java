package engineering.b67.intellij_linter_base;

import com.intellij.openapi.vfs.VirtualFile;

public abstract class ExecutorRunner implements Runner {

    private Executor createExecutor(String executable, VirtualFile virtualFile, String basePath) {
        Executor executor = new Executor(
                executable,
                virtualFile,
                basePath
        );

        executor.setParameters(parameters());

        return executor;
    }

}
