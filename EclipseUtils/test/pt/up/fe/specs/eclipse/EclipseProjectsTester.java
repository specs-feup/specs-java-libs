package pt.up.fe.specs.eclipse;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import pt.up.fe.specs.eclipse.Utilities.EclipseProjects;
import pt.up.fe.specs.util.SpecsSystem;
import pt.up.fe.specs.util.system.ProcessOutputAsString;
import pt.up.fe.specs.util.utilities.StringLines;

public class EclipseProjectsTester {

    @Test
    public void test() {
        File repositoryFolder = new File(
                "C:\\Users\\JoaoBispo\\Work\\Repositories\\specs-java");
        EclipseProjects p = EclipseProjects.newFromRepository(repositoryFolder);

        System.out.println(p.getProjectFolder("MatlabToCLV2Tester"));
        List<String> cmd = Arrays.asList("git", "remote", "show", "origin");
        ProcessOutputAsString output = SpecsSystem.runProcess(cmd, p.getProjectFolder("MatlabToCLV2Tester"), true,
                false);

        for (String line : StringLines.newInstance(output.getOutput())) {
            line = line.trim();
            if (!line.trim().startsWith("Fetch URL:")) {
                continue;
            }

            String url = line.substring("Fetch URL:".length()).trim();

            // Remove user information, if present
            int end = url.indexOf("@");
            if (end != -1) {
                int begin = url.indexOf("//");
                url = url.substring(0, begin + 2) + url.substring(end + 1, url.length());
            }
            System.out.println("URL:" + url);
            Assert.assertEquals("https://bitbucket.org/specsfeup/specs-java.git", url);
            return;
        }

        Assert.fail();
        // UserLibraries userLibraries = UserLibraries.newInstance(workspaceFolder, p);

        // System.out.println(userLibraries.getJars("jmatio"));
    }
}
