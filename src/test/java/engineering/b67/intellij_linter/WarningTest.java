package engineering.b67.intellij_linter;

import engineering.b67.intellij_linter.command.Warning;
import org.junit.Assert;
import org.junit.Test;

public class WarningTest {

    @Test
    public void testMessageAndFormattedMessageWithoutUrl() {
        // Given
        Warning warning = new Warning(1, 1, "Type", "Description");

        // When
        String message = warning.getMessage();
        String formattedMessage = warning.getFormattedMessage();

        // Then
        Assert.assertEquals(message, "Reek: Type: Description.");
        Assert.assertEquals(formattedMessage, "Reek: Type: Description.");
    }

    @Test
    public void testFormattedMessage() {
        // Given
        Warning warning = new Warning(1, 1, "Type", "Description");
        warning.setUrl("https://google.com");

        // When
        String formattedMessage = warning.getFormattedMessage();

        // Then
        Assert.assertEquals(formattedMessage, "Reek: Type: Description. <a href=\"https://google.com\">Documentation</a>.");
    }

    @Test
    public void testLine() {
        // Given
        Warning warning = new Warning(1, 2, "Type", "Description");

        // When
        Object line = warning.getLine();

        // Then
        Assert.assertEquals(line, 1);
    }

    @Test
    public void testColumn() {
        // Given
        Warning warning = new Warning(1, 2, "Type", "Description");

        // When
        Object column = warning.getColumn();

        // Then
        Assert.assertEquals(column, 2);
    }
}
