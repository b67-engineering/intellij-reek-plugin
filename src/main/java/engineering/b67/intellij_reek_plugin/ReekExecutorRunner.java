package engineering.b67.intellij_reek_plugin;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import engineering.b67.intellij_linter_base.*;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.plugins.ruby.gem.GemUtil;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReekExecutorRunner extends ExecutorRunner implements Runner {

    @Override
    public List<Warning> execute(@NotNull ExecutorContext executorContext) {
        Editor editor = executorContext.getEditor();
        VirtualFile file = createVirtualFile(editor.getDocument(), getFileExtension());
        Project project = editor.getProject();
        ReekService state = ReekService.getInstance(project);

        Executor executor = createExecutor(
                StringUtils.defaultIfEmpty(state.getExecutable(), getDefaultExecutable(executorContext)),
                file,
                project.getBasePath(),
                state
        );

        try {
            Process process = executor.run();
            String output = getOutput(process);

            if (output.equals("")) {
                log.error("Empty output...");
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
    public String getDefaultExecutable(ExecutorContext executorContext) {
        // FIXME: Check if it is in bundler / sdk / whatever
        // sdk - GemSearchUtil.findGem(executorContext.getSdk(), "reek");
        // bundle - GemSearchUtil.findGem(executorContext.getModule(), "reek");

        String executable = GemUtil.getGemExecutableRubyScriptPath(executorContext.getModule(), executorContext.getSdk(), "reek", "reek");
        String interpreter = executorContext.getSdk().getHomePath();

        return String.format("%s|%s", interpreter, executable);
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
