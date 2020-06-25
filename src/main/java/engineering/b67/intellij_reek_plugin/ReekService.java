package engineering.b67.intellij_reek_plugin;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import engineering.b67.intellij_linter_base.Service;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(name = "ReekService")
public class ReekService extends Service implements PersistentStateComponent<ReekService> {

    public String executable = "";
    public String config = "";

    static ReekService getInstance(Project project) {
        return ServiceManager.getService(project, ReekService.class);
    }

    @Nullable
    @Override
    public ReekService getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull ReekService state) {
        XmlSerializerUtil.copyBean(state, this);
    }

    @Override
    public String getConfig() {
        return this.config;
    }

    @Override
    public String getExecutable() {
        return this.executable;
    }
}

