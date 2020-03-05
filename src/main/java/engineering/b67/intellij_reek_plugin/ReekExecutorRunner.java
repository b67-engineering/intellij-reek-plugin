package engineering.b67.intellij_reek_plugin;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import engineering.b67.intellij_linter_base.*;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReekExecutorRunner extends ExecutorRunner implements Runner {

    @Override
    public List<Warning> execute(@NotNull Editor editor) {
        VirtualFile file = createVirtualFile(editor.getDocument(), "rb");
        Project project = editor.getProject();
        ProjectService state = ProjectService.getInstance(project);

        Executor executor = new Executor(
                StringUtils.defaultIfEmpty(state.executable, getDefaultExecutable()),
                file,
                project.getBasePath()
        );

        ArrayList<String> parameters = getParameters();

        if (!state.config.equals("")) {
            parameters.add("--config");
            parameters.add(state.config);
        }

        executor.setParameters(parameters);

        try {
            Process process = executor.run();

            Reader inputStreamReader = new InputStreamReader(process.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String output = bufferedReader.lines().collect(Collectors.joining());

            if (output.equals("")) {
                // FIXME: Error occurred
            }

            Result result = new ReekResult(output);

            return result.getWarnings();
        } finally {
            try {
                Files.delete(Paths.get(file.getPath()));
            } catch (IOException ex) {
                // FIXME: Can't remove file exception
            }
        }
    }

    @Override
    public ArrayList<String> getParameters() {
        return new ArrayList<String>() {
            {
                add("--single-line");
                add("--no-progress");
                add("--no-empty-headings");
                add("--format");
                add("json");
            }
        };
    }

    @Override
    public String getDefaultExecutable() {
        return "reek";
    }
}
