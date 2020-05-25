/**
 * Copyright 2012 SPeCS Research Group.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License. under the License.
 */

package pt.up.fe.specs.util;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Test;

public class IoUtilsTest {

    /**
     * Tests for getRelativePath
     */
    /*
    public void testGetRelativePathsUnix() {
        assertEquals("stuff/xyz.dat", ResourceUtils.getRelativePath("/var/data/stuff/xyz.dat", "/var/data/", "/"));
        assertEquals("../../b/c", ResourceUtils.getRelativePath("/a/b/c", "/a/x/y/", "/"));
        assertEquals("../../b/c", ResourceUtils.getRelativePath("/m/n/o/a/b/c", "/m/n/o/a/x/y/", "/"));
    }
    
    
    public void testGetRelativePathDirectoryToFile() {
        String target = "C:\\Windows\\Boot\\Fonts\\chs_boot.ttf";
        String base = "C:\\Windows\\Speech\\Common\\";
    
        String relPath = ResourceUtils.getRelativePath(target, base, "\\");
        assertEquals("..\\..\\Boot\\Fonts\\chs_boot.ttf", relPath);
    }
    
    public void testGetRelativePathFileToDirectory() {
        String target = "C:\\Windows\\Boot\\Fonts";
        String base = "C:\\Windows\\Speech\\Common\\foo.txt";
    
        String relPath = ResourceUtils.getRelativePath(target, base, "\\");
        assertEquals("..\\..\\Boot\\Fonts", relPath);
    }
    
    public void testGetRelativePathDirectoryToDirectory() {
        String target = "C:\\Windows\\Boot\\";
        String base = "C:\\Windows\\Speech\\Common\\";
        String expected = "..\\..\\Boot";
    
        String relPath = ResourceUtils.getRelativePath(target, base, "\\");
        assertEquals(expected, relPath);
    }
    
    public void testGetRelativePathDifferentDriveLetters() {
        String target = "D:\\sources\\recovery\\RecEnv.exe";
        String base = "C:\\Java\\workspace\\AcceptanceTests\\Standard test data\\geo\\";
    
        try {
            ResourceUtils.getRelativePath(target, base, "\\");
            fail();
    
        } catch (PathResolutionException ex) {
            // expected exception
        }
    }
     */
    /*
    @Test
    public void getRelativePathTest() {
    File file = new File("D:/temp/result.txt");
    String relativePath = IoUtils.getRelativePath(file);
    System.out.println(relativePath);
    //fail("Not yet implemented");
    }
     */

    @Test
    public void testGetRelativePathFileToFile() {
        File target = new File("Windows/Boot/Fonts/chs_boot.ttf");
        File base = new File("Windows/Speech/Common/sapisvr.exe");

        String relPath = SpecsIo.getRelativePath(target, base);
        assertEquals("../../Boot/Fonts/chs_boot.ttf", relPath);
    }

    @Test
    public void testGetRelativePathRelativeFile() {
        File target = new File("SharedLibrary/../a/b/test.dat");
        File base = new File("./");

        String relPath = SpecsIo.getRelativePath(target, base);
        assertEquals("a/b/test.dat", relPath);
    }

    @Test
    public void testGetRelativePathRelativeSameFolder() {
        File target = new File("lib/b.h");
        File base = new File("lib/a.h");

        String relPath = SpecsIo.getRelativePath(target, base);
        assertEquals("b.h", relPath);
    }

    @Test
    public void testGetRelativePathRelativeDiffFolder() {
        File target = new File("lib/b.h");
        File base = new File("lib2/a.h");

        String relPath = SpecsIo.getRelativePath(target, base);
        assertEquals("../lib/b.h", relPath);
    }

    public void testMd5() {
        System.out.println(
                "MD5:" + SpecsIo
                        .getMd5(new File("C:\\Users\\JoaoBispo\\Desktop\\jstest\\auto-doc\\assets\\anchor.js")));
        // assertEquals("../lib/b.h", relPath);
    }

    /*
    @Test
    public void getParentNamesTest() {
    File file = new File("D:/temp/result.txt");
    List<String> names = IoUtils.getParentNames(file);
    System.out.println("Names:" + names);
    }
     */

    /*
    @Test
    public void resourceTest() {
    LineReader lineReader = LineReader.createLineReader(IoUtils.getResourceString(""));
    for (String line : lineReader) {
        if (line.endsWith(".properties")) {
    	System.out.println("RESOURCE " + lineReader.getLastLineIndex() + ":");
    	System.out.println(IoUtils.getResourceString(line));
        }
    }
    
    assertTrue(true);
    }
     */
    @Test
    public void testParsePathList() {
        assertEquals("{=[path1, path2, path3]}", SpecsStrings.parsePathList("path1; path2  ; path3", ";").toString());
        assertEquals("{=[path1, path4], prefix/=[path2, path3]}",
                SpecsStrings.parsePathList("path1$prefix/$path2;path3$$path4", ";").toString());

    }

    @Test
    public void testParseUrl() {
        var urlString = "https://github.com/specs-feup/clava.git?folder=benchmarks/NAS&another=stuff";

        var url = SpecsIo.parseUrl(urlString).get();
        var query = SpecsIo.parseUrlQuery(url);

        assertEquals("https", url.getProtocol());
        assertEquals("github.com", url.getHost());
        assertEquals("/specs-feup/clava.git", url.getPath());
        assertEquals("benchmarks/NAS", query.get("folder"));
        assertEquals("stuff", query.get("another"));
    }
}
