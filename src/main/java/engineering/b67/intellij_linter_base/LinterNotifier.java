package engineering.b67.intellij_linter_base;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationDisplayType;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.project.Project;

public class LinterNotifier {
    private final NotificationGroup NOTIFICATION_GROUP = new NotificationGroup("Linter error", NotificationDisplayType.BALLOON, true);

    public Notification notify(String content) {
        return notify(null, content);
    }

    public Notification notify(Project project, String content) {
        final Notification notification = NOTIFICATION_GROUP.createNotification(content, NotificationType.ERROR);

        notification.notify(project);

        return notification;
    }
}
