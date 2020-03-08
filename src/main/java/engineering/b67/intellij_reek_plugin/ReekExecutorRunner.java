package engineering.b67.intellij_reek_plugin;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import engineering.b67.intellij_linter_base.*;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReekExecutorRunner extends ExecutorRunner implements Runner {

    @Override
    public List<Warning> execute(@NotNull Editor editor) {
        VirtualFile file = createVirtualFile(editor.getDocument(), getFileExtension());
        Project project = editor.getProject();
        ReekService state = ReekService.getInstance(project);

        Executor executor = createExecutor(
                StringUtils.defaultIfEmpty(state.getExecutable(), getDefaultExecutable()),
                file,
                project.getBasePath(),
                state
        );

        try {
            Process process = executor.run();
            String output = getOutput(process);

            if (output.equals("")) {
                // FIXME: Error occurred
            }

            Result result = new ReekResult(output);

            return result.getWarnings();
        } finally {
            this.deleteVirtualFile(file);
        }
    }

    @Override
    public ArrayList<String> getParameters(Service state) {
        ArrayList<String> parameters = new ArrayList<String>() {
            {
                add("--single-line");
                add("--no-progress");
                add("--no-empty-headings");
                add("--format");
                add("json");
            }
        };

        if (!state.getConfig().equals("")) {
            parameters.add("--config");
            parameters.add(state.getConfig());
        }

        return parameters;
    }

    @Override
    public String getDefaultExecutable() {
        return "reek";
    }

    @Override
    public String getFileExtension() {
        return "rb";
    }

    @Override
    public String getOutput(Process process) {
        Reader inputStreamReader = new InputStreamReader(process.getInputStream());
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        return bufferedReader.lines().collect(Collectors.joining());
    }
}
