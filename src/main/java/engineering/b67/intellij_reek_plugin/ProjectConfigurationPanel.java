package engineering.b67.intellij_reek_plugin;

import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.TextFieldWithHistoryWithBrowseButton;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;

// Configuration file moved from:
// https://github.com/yoheimuta/intellij-protolint/
public class ProjectConfigurationPanel implements SearchableConfigurable {
    private final ReekService state;
    private final Project project;

    private TextFieldWithHistoryWithBrowseButton executable;
    private TextFieldWithHistoryWithBrowseButton configuration;

    private JRadioButton searchForConfigRadioButton;
    private JRadioButton useSpecificConfigRadioButton;

    private JPanel rootPanel;

    public ProjectConfigurationPanel(@NotNull Project project) {
        this.state = ReekService.getInstance(project).getState();
        this.project = project;
    }

    @NotNull
    @Override
    public String getId() {
        return "Reek";
    }

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "Reek Linter";
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        loadSettings();
        addListeners();

        return rootPanel;
    }

    @Override
    public boolean isModified() {
        return !executable.getText().equals(state.executable)
            || !configuration.getText().equals(state.config);
    }

    @Override
    public void apply() {
        state.executable = executable.getText();
        state.config = configuration.getText();
    }

    @Override
    public void reset() {
        loadSettings();
    }

    private void loadSettings() {
        executable.setText(state.executable);
        configuration.setText(state.config);

        final boolean shouldSearchConfig = state.config.isEmpty();

        searchForConfigRadioButton.setSelected(shouldSearchConfig);
        useSpecificConfigRadioButton.setSelected(!shouldSearchConfig);

        configuration.setEnabled(!shouldSearchConfig);
    }

    private void addListeners() {
        searchForConfigRadioButton.addItemListener((ItemEvent event) -> {
            if (event.getStateChange() == ItemEvent.SELECTED) {
                configuration.setEnabled(false);
                configuration.setText("");

                useSpecificConfigRadioButton.setSelected(false);
            }
        });

        useSpecificConfigRadioButton.addItemListener((ItemEvent event) -> {
            if (event.getStateChange() == ItemEvent.SELECTED) {
                configuration.setEnabled(true);

                searchForConfigRadioButton.setSelected(false);
            }
        });

        executable.addActionListener((ActionEvent event) -> {
            final VirtualFile file = FileChooser.chooseFile(
                    FileChooserDescriptorFactory.createSingleFileDescriptor(),
                    project,
                    null
            );

            if (file == null) {
                return;
            }

            executable.setText(file.getPath());
        });

        configuration.addActionListener((ActionEvent event) -> {
            final VirtualFile file = FileChooser.chooseFile(
                    FileChooserDescriptorFactory.createSingleFolderDescriptor(),
                    project,
                    null
            );
            
            if (file == null) {
                return;
            }

            configuration.setText(file.getPath());
            
            useSpecificConfigRadioButton.setSelected(true);
        });
    }
}
