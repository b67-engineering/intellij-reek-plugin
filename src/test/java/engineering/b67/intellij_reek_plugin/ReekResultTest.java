package engineering.b67.intellij_reek_plugin;

import engineering.b67.intellij_linter_base.Result;
import engineering.b67.intellij_linter_base.Warning;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class ReekResultTest {

    @Test
    public void testInvalidOutput() {
        // Given
        String output = "";
        Result reekResult = new ReekResult(output);

        // When
        List<Warning> warningList = reekResult.getWarnings();

        // Then
        Assert.assertEquals(0, warningList.size());
    }

    @Test
    public void testValidOutput() {
        // Given
        String output = "[{\"context\":\"Test\",\"lines\":[3],\"message\":\"has no descriptive comment\",\"smell_type\":\"IrresponsibleModule\",\"source\":\"test.rb\",\"documentation_link\":\"https://github.com/troessner/reek/blob/v5.6.0/docs/Irresponsible-Module.md\"},{\"context\":\"Test#x\",\"lines\":[4],\"message\":\n" +
                "\"has the name 'x'\",\"smell_type\":\"UncommunicativeMethodName\",\"source\":\"test.rb\",\"name\":\"x\",\"documentation_link\":\"https://github.com/troessner/reek/blob/v5.6.0/docs/Uncommunicative-Method-Name.md\"}]";
        Result reekResult = new ReekResult(output);

        // When
        List<Warning> warningList = reekResult.getWarnings();

        // Then
        Assert.assertEquals(2, warningList.size());
    }
}
