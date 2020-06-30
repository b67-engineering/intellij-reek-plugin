package engineering.b67.intellij_reek_plugin;

import com.intellij.lang.annotation.Annotation;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.ExternalAnnotator;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import engineering.b67.intellij_linter_base.ExecutorContext;
import engineering.b67.intellij_linter_base.LinterNotifier;
import engineering.b67.intellij_linter_base.Warning;
import engineering.b67.intellij_linter_base.exception.ContextException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class ReekAnnotator extends ExternalAnnotator<ExecutorContext, List<Warning>> {
    private static final Logger LOGGER = Logger.getInstance(ReekAnnotator.class);

    @Nullable
    @Override
    public ExecutorContext collectInformation(@NotNull PsiFile file, @NotNull Editor editor, boolean hasErrors) {
        return new ExecutorContext(
                editor,
                ModuleUtil.findModuleForFile(file)
        );
    }

    @Nullable
    @Override
    public List<Warning> doAnnotate(ExecutorContext executorContext) {
        try {
            return (new ReekExecutorRunner()).execute(executorContext);
        } catch (ContextException exception) {
            LinterNotifier linterNotifier = new LinterNotifier();

            linterNotifier.notify(
                    "Missing reek linter executable! " +
                    "Provide path to reek in \"Reek Linter\" settings tab."
            );
        }

        return Collections.emptyList();
    }

    @Override
    public void apply(@NotNull PsiFile file, List<Warning> warnings, @NotNull AnnotationHolder holder) {
        Document document = PsiDocumentManager.getInstance(file.getProject()).getDocument(file);

        if (document == null) {
            LOGGER.warn("Document not found");
            return;
        }

        warnings.forEach(warning -> {
            int line = warning.getLine() -1;

            if (line < 0) {
                line = 1;
            }

            // FIXME: No column in reek output
            // int startOffset = StringUtil.lineColToOffset(file.getText(), line, warning.getColumn());
            int endOffset = document.getLineEndOffset(line);
            int startOffset = document.getLineStartOffset(line);

            // See https://github.com/Hannah-Sten/TeXiFy-IDEA/pull/844
            if (!isProperRange(startOffset, endOffset)) {
                LOGGER.warn("Skip negative text range");
                return;
            }

            TextRange range = new TextRange(startOffset, endOffset);
            Annotation annotation = holder.createWarningAnnotation(range, warning.getMessage());
            annotation.setTooltip(warning.getFormattedMessage());
        });
    }

    private boolean isProperRange(int startOffset, int endOffset) {
        return startOffset <= endOffset && startOffset >= 0;
    }
}
