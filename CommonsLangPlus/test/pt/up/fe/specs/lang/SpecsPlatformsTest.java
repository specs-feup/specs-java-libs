package pt.up.fe.specs.lang;

import org.apache.commons.lang3.SystemUtils;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link SpecsPlatforms} utility class.
 * 
 * Tests platform detection functionality using Apache Commons Lang integration.
 * These tests focus on actual system behavior rather than mocking to avoid
 * System class mocking restrictions.
 * 
 * @author Generated Tests
 */
class SpecsPlatformsTest {

    @Test
    void testIsWindows_ActualSystem() {
        // When
        boolean result = SpecsPlatforms.isWindows();

        // Then
        assertThat(result).isEqualTo(SystemUtils.IS_OS_WINDOWS);
    }

    @Test
    void testIsLinux_ActualSystem() {
        // When
        boolean result = SpecsPlatforms.isLinux();

        // Then
        assertThat(result).isEqualTo(SystemUtils.IS_OS_LINUX);
    }

    @Test
    void testIsUnix_ActualSystem() {
        // When
        boolean result = SpecsPlatforms.isUnix();

        // Then
        assertThat(result).isEqualTo(SystemUtils.IS_OS_UNIX);
    }

    @Test
    void testIsMac_ActualSystem() {
        // When
        boolean result = SpecsPlatforms.isMac();

        // Then
        assertThat(result).isEqualTo(SystemUtils.IS_OS_MAC);
    }

    @Test
    void testGetPlatformName_ActualSystem() {
        // When
        String result = SpecsPlatforms.getPlatformName();

        // Then
        assertThat(result).isEqualTo(SystemUtils.OS_NAME);
        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
    }

    @Test
    void testIsLinuxArm_ActualSystem() {
        // When
        boolean result = SpecsPlatforms.isLinuxArm();
        boolean isLinux = SpecsPlatforms.isLinux();
        String osArch = System.getProperty("os.arch");

        // Then - verify the logic is correct
        boolean expectedResult = isLinux && "arm".equals(osArch.toLowerCase());
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testIsCentos6_ActualSystem() {
        // When
        boolean result = SpecsPlatforms.isCentos6();
        String osVersion = System.getProperty("os.version");

        // Then - verify the logic is correct (avoiding null pointer)
        if (osVersion != null) {
            boolean expectedResult = osVersion.contains(".el6.");
            assertThat(result).isEqualTo(expectedResult);
        } else {
            // If os.version is null, method should handle it gracefully
            // In this case, we just verify it doesn't throw an exception
            assertThat(result).isFalse(); // Most likely result for null version
        }
    }

    @Test
    void testIsCentos6_LogicValidation() {
        // Given - we test the core logic without mocking
        String currentOsVersion = System.getProperty("os.version");

        // When
        boolean result = SpecsPlatforms.isCentos6();

        // Then - verify consistency with manual check
        if (currentOsVersion != null && currentOsVersion.contains(".el6.")) {
            assertThat(result).isTrue();
        } else {
            assertThat(result).isFalse();
        }
    }

    // Integration test for actual system properties
    @Test
    void testActualSystemProperties() {
        // When
        String platformName = SpecsPlatforms.getPlatformName();
        boolean isWindows = SpecsPlatforms.isWindows();
        boolean isLinux = SpecsPlatforms.isLinux();
        boolean isMac = SpecsPlatforms.isMac();
        boolean isUnix = SpecsPlatforms.isUnix();

        // Then - verify consistency
        assertThat(platformName).isNotNull().isNotEmpty();

        // Only one primary OS should be true
        int osCount = 0;
        if (isWindows)
            osCount++;
        if (isLinux)
            osCount++;
        if (isMac)
            osCount++;

        assertThat(osCount).isLessThanOrEqualTo(1);

        // Unix should be true if Linux is true (Linux is a form of Unix)
        if (isLinux) {
            assertThat(isUnix).isTrue();
        }
    }

    @Test
    void testPlatformConsistency() {
        // Given
        boolean actualIsLinux = SpecsPlatforms.isLinux();

        // When
        boolean isLinuxArm = SpecsPlatforms.isLinuxArm();

        // Then - isLinuxArm should only be true if isLinux is also true
        if (isLinuxArm) {
            assertThat(actualIsLinux).isTrue();
        }
    }

    @Test
    void testSystemUtilsConsistency() {
        // Test that our wrapper methods return the same values as SystemUtils
        assertThat(SpecsPlatforms.isWindows()).isEqualTo(SystemUtils.IS_OS_WINDOWS);
        assertThat(SpecsPlatforms.isLinux()).isEqualTo(SystemUtils.IS_OS_LINUX);
        assertThat(SpecsPlatforms.isMac()).isEqualTo(SystemUtils.IS_OS_MAC);
        assertThat(SpecsPlatforms.isUnix()).isEqualTo(SystemUtils.IS_OS_UNIX);
        assertThat(SpecsPlatforms.getPlatformName()).isEqualTo(SystemUtils.OS_NAME);
    }

    @Test
    void testArchitectureHandling() {
        // When
        String osArch = System.getProperty("os.arch");
        boolean isLinuxArm = SpecsPlatforms.isLinuxArm();
        boolean isLinux = SpecsPlatforms.isLinux();

        // Then - verify architecture logic
        if (isLinux && osArch != null) {
            boolean shouldBeArm = "arm".equals(osArch.toLowerCase());
            assertThat(isLinuxArm).isEqualTo(shouldBeArm);
        }

        // ARM detection should be case-insensitive
        if (osArch != null && osArch.toLowerCase().equals("arm") && isLinux) {
            assertThat(isLinuxArm).isTrue();
        }
    }

    @Test
    void testMethodReturnTypes() {
        // Test that all methods return non-null values of expected types
        assertThat(SpecsPlatforms.isWindows()).isInstanceOf(Boolean.class);
        assertThat(SpecsPlatforms.isLinux()).isInstanceOf(Boolean.class);
        assertThat(SpecsPlatforms.isMac()).isInstanceOf(Boolean.class);
        assertThat(SpecsPlatforms.isUnix()).isInstanceOf(Boolean.class);
        assertThat(SpecsPlatforms.isLinuxArm()).isInstanceOf(Boolean.class);
        assertThat(SpecsPlatforms.isCentos6()).isInstanceOf(Boolean.class);
        assertThat(SpecsPlatforms.getPlatformName()).isInstanceOf(String.class).isNotNull();
    }
}
