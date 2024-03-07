package engineering.b67.intellij_linter_base;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.roots.ModuleRootManager;

public class ExecutorContext {
    private final Editor editor;
    private final Module module;

    public ExecutorContext(
        Editor editor,
        Module module
    ) {
        this.editor = editor;
        this.module = module;
    }

    public Editor getEditor() {
        return editor;
    }

    public Module getModule() {
        return module;
    }

    public Sdk getSdk() {
        if (getModule() == null) {
            return null;
        } else {
            ModuleRootManager moduleRootManager = ModuleRootManager.getInstance(getModule());
            return moduleRootManager.getSdk();
        }
    }
}
