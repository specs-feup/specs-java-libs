package org.suikasoft.jOptions.cli;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.suikasoft.jOptions.app.AppKernel;
import org.suikasoft.jOptions.app.AppPersistence;
import org.suikasoft.jOptions.gui.panels.app.TabProvider;
import org.suikasoft.jOptions.persistence.XmlPersistence;
import org.suikasoft.jOptions.storedefinition.StoreDefinition;

import pt.up.fe.specs.util.providers.ResourceProvider;

/**
 * Unit tests for {@link GenericApp}.
 * 
 * Tests the generic implementation of the App interface for jOptions-based
 * applications, including configuration management, builder pattern methods,
 * and app metadata handling.
 * 
 * @author Generated Tests
 */
@MockitoSettings(strictness = Strictness.STRICT_STUBS)
@DisplayName("GenericApp")
class GenericAppTest {

    private StoreDefinition mockStoreDefinition;
    private AppPersistence mockPersistence;
    private AppKernel mockKernel;
    private TabProvider mockTabProvider1;
    private TabProvider mockTabProvider2;
    private ResourceProvider mockIcon;

    @BeforeEach
    void setUp() {
        mockStoreDefinition = mock(StoreDefinition.class);
        mockPersistence = mock(AppPersistence.class);
        mockKernel = mock(AppKernel.class);
        mockTabProvider1 = mock(TabProvider.class);
        mockTabProvider2 = mock(TabProvider.class);
        mockIcon = mock(ResourceProvider.class);
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Full constructor creates GenericApp with all parameters")
        void testFullConstructor_CreatesGenericAppWithAllParameters() {
            String appName = "TestApp";
            Collection<TabProvider> tabs = Arrays.asList(mockTabProvider1, mockTabProvider2);
            Class<?> nodeClass = String.class;

            GenericApp app = new GenericApp(appName, mockStoreDefinition, mockPersistence, mockKernel)
                    .setOtherTabs(tabs)
                    .setNodeClass(nodeClass)
                    .setIcon(mockIcon);

            assertThat(app.getName()).isEqualTo(appName);
            assertThat(app.getDefinition()).isSameAs(mockStoreDefinition);
            assertThat(app.getPersistence()).isSameAs(mockPersistence);
            assertThat(app.getKernel()).isSameAs(mockKernel);
            assertThat(app.getOtherTabs()).containsExactlyElementsOf(tabs);
            assertThat(app.getNodeClass()).isEqualTo(nodeClass);
            assertThat(app.getIcon()).contains(mockIcon);
        }

        @Test
        @DisplayName("Basic constructor creates GenericApp with default values")
        void testBasicConstructor_CreatesGenericAppWithDefaultValues() {
            String appName = "TestApp";

            GenericApp app = new GenericApp(appName, mockStoreDefinition, mockPersistence, mockKernel);

            assertThat(app.getName()).isEqualTo(appName);
            assertThat(app.getDefinition()).isSameAs(mockStoreDefinition);
            assertThat(app.getPersistence()).isSameAs(mockPersistence);
            assertThat(app.getKernel()).isSameAs(mockKernel);
            assertThat(app.getOtherTabs()).isEmpty();
            assertThat(app.getNodeClass()).isEqualTo(GenericApp.class);
            assertThat(app.getIcon()).isEmpty();
        }

        @Test
        @DisplayName("Constructor with XmlPersistence creates app with default persistence")
        void testConstructorWithXmlPersistence_CreatesAppWithDefaultPersistence() {
            String appName = "TestApp";

            GenericApp app = new GenericApp(appName, mockStoreDefinition, mockKernel);

            assertThat(app.getName()).isEqualTo(appName);
            assertThat(app.getDefinition()).isSameAs(mockStoreDefinition);
            assertThat(app.getPersistence()).isInstanceOf(XmlPersistence.class);
            assertThat(app.getKernel()).isSameAs(mockKernel);
            assertThat(app.getOtherTabs()).isEmpty();
            assertThat(app.getNodeClass()).isEqualTo(GenericApp.class);
            assertThat(app.getIcon()).isEmpty();
        }
    }

    @Nested
    @DisplayName("Getter Methods")
    class GetterMethodsTests {

        private GenericApp app;

        @BeforeEach
        void setUp() {
            app = new GenericApp("TestApp", mockStoreDefinition, mockPersistence, mockKernel);
        }

        @Test
        @DisplayName("getName returns correct application name")
        void testGetName_ReturnsCorrectApplicationName() {
            assertThat(app.getName()).isEqualTo("TestApp");
        }

        @Test
        @DisplayName("getDefinition returns store definition")
        void testGetDefinition_ReturnsStoreDefinition() {
            assertThat(app.getDefinition()).isSameAs(mockStoreDefinition);
        }

        @Test
        @DisplayName("getPersistence returns persistence mechanism")
        void testGetPersistence_ReturnsPersistenceMechanism() {
            assertThat(app.getPersistence()).isSameAs(mockPersistence);
        }

        @Test
        @DisplayName("getKernel returns application kernel")
        void testGetKernel_ReturnsApplicationKernel() {
            assertThat(app.getKernel()).isSameAs(mockKernel);
        }

        @Test
        @DisplayName("getOtherTabs returns empty collection by default")
        void testGetOtherTabs_ReturnsEmptyCollectionByDefault() {
            assertThat(app.getOtherTabs()).isEmpty();
        }

        @Test
        @DisplayName("getIcon returns empty optional by default")
        void testGetIcon_ReturnsEmptyOptionalByDefault() {
            assertThat(app.getIcon()).isEmpty();
        }

        @Test
        @DisplayName("getNodeClass returns GenericApp class by default")
        void testGetNodeClass_ReturnsGenericAppClassByDefault() {
            assertThat(app.getNodeClass()).isEqualTo(GenericApp.class);
        }
    }

    @Nested
    @DisplayName("Builder Pattern Methods")
    class BuilderPatternMethodsTests {

        private GenericApp baseApp;

        @BeforeEach
        void setUp() {
            baseApp = new GenericApp("TestApp", mockStoreDefinition, mockPersistence, mockKernel);
        }

        @Test
        @DisplayName("setOtherTabs with collection returns new instance with updated tabs")
        void testSetOtherTabs_WithCollection_ReturnsNewInstanceWithUpdatedTabs() {
            Collection<TabProvider> tabs = Arrays.asList(mockTabProvider1, mockTabProvider2);

            GenericApp updatedApp = baseApp.setOtherTabs(tabs);

            assertThat(updatedApp).isNotSameAs(baseApp);
            assertThat(updatedApp.getOtherTabs()).containsExactlyElementsOf(tabs);
            assertThat(baseApp.getOtherTabs()).isEmpty(); // Original unchanged
        }

        @Test
        @DisplayName("setOtherTabs with varargs returns new instance with updated tabs")
        void testSetOtherTabs_WithVarargs_ReturnsNewInstanceWithUpdatedTabs() {
            GenericApp updatedApp = baseApp.setOtherTabs(mockTabProvider1, mockTabProvider2);

            assertThat(updatedApp).isNotSameAs(baseApp);
            assertThat(updatedApp.getOtherTabs()).containsExactly(mockTabProvider1, mockTabProvider2);
            assertThat(baseApp.getOtherTabs()).isEmpty(); // Original unchanged
        }

        @Test
        @DisplayName("setOtherTabs with empty collection returns new instance with empty tabs")
        void testSetOtherTabs_WithEmptyCollection_ReturnsNewInstanceWithEmptyTabs() {
            GenericApp updatedApp = baseApp.setOtherTabs(Collections.emptyList());

            assertThat(updatedApp).isNotSameAs(baseApp);
            assertThat(updatedApp.getOtherTabs()).isEmpty();
        }

        @Test
        @DisplayName("setNodeClass returns new instance with updated node class")
        void testSetNodeClass_ReturnsNewInstanceWithUpdatedNodeClass() {
            Class<?> nodeClass = String.class;

            GenericApp updatedApp = baseApp.setNodeClass(nodeClass);

            assertThat(updatedApp).isNotSameAs(baseApp);
            assertThat(updatedApp.getNodeClass()).isEqualTo(nodeClass);
            assertThat(baseApp.getNodeClass()).isEqualTo(GenericApp.class); // Original unchanged
        }

        @Test
        @DisplayName("setIcon returns new instance with updated icon")
        void testSetIcon_ReturnsNewInstanceWithUpdatedIcon() {
            GenericApp updatedApp = baseApp.setIcon(mockIcon);

            assertThat(updatedApp).isNotSameAs(baseApp);
            assertThat(updatedApp.getIcon()).contains(mockIcon);
            assertThat(baseApp.getIcon()).isEmpty(); // Original unchanged
        }

        @Test
        @DisplayName("Chain multiple setters creates correctly configured app")
        void testChainMultipleSetters_CreatesCorrectlyConfiguredApp() {
            Collection<TabProvider> tabs = Arrays.asList(mockTabProvider1);
            Class<?> nodeClass = String.class;

            GenericApp chainedApp = baseApp
                    .setOtherTabs(tabs)
                    .setNodeClass(nodeClass)
                    .setIcon(mockIcon);

            assertThat(chainedApp).isNotSameAs(baseApp);
            assertThat(chainedApp.getOtherTabs()).containsExactlyElementsOf(tabs);
            assertThat(chainedApp.getNodeClass()).isEqualTo(nodeClass);
            assertThat(chainedApp.getIcon()).contains(mockIcon);

            // Original app unchanged
            assertThat(baseApp.getOtherTabs()).isEmpty();
            assertThat(baseApp.getNodeClass()).isEqualTo(GenericApp.class);
            assertThat(baseApp.getIcon()).isEmpty();
        }
    }

    @Nested
    @DisplayName("Special Behavior Tests")
    class SpecialBehaviorTests {

        @Test
        @DisplayName("getNodeClass returns set class when explicitly set")
        void testGetNodeClass_ReturnsSetClassWhenExplicitlySet() {
            Class<?> nodeClass = String.class;

            GenericApp app = new GenericApp("TestApp", mockStoreDefinition, mockPersistence, mockKernel)
                    .setNodeClass(nodeClass);

            assertThat(app.getNodeClass()).isEqualTo(nodeClass);
        }

        @Test
        @DisplayName("getNodeClass returns GenericApp class when not set")
        void testGetNodeClass_ReturnsGenericAppClassWhenNotSet() {
            GenericApp app = new GenericApp("TestApp", mockStoreDefinition, mockPersistence, mockKernel);

            assertThat(app.getNodeClass()).isEqualTo(GenericApp.class);
        }

        @Test
        @DisplayName("setNodeClass with null returns app that uses GenericApp class")
        void testSetNodeClass_WithNull_ReturnsAppThatUsesGenericAppClass() {
            GenericApp app = new GenericApp("TestApp", mockStoreDefinition, mockPersistence, mockKernel)
                    .setNodeClass(String.class) // Set it first
                    .setNodeClass(null); // Then set to null

            assertThat(app.getNodeClass()).isEqualTo(GenericApp.class);
        }

        @Test
        @DisplayName("getIcon returns Optional.empty when icon is null")
        void testGetIcon_ReturnsOptionalEmptyWhenIconIsNull() {
            GenericApp app = new GenericApp("TestApp", mockStoreDefinition, mockPersistence, mockKernel)
                    .setIcon(mockIcon) // Set it first
                    .setIcon(null); // Then set to null

            assertThat(app.getIcon()).isEmpty();
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Handling")
    class EdgeCasesAndErrorHandlingTests {

        @Test
        @DisplayName("Constructor with null name throws exception")
        void testConstructor_NullName_ThrowsException() {
            // Fixed: GenericApp constructor validates null parameters
            assertThatThrownBy(() -> new GenericApp(null, mockStoreDefinition, mockPersistence, mockKernel))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Application name cannot be null");
        }

        @Test
        @DisplayName("Constructor with null store definition throws exception")
        void testConstructor_NullStoreDefinition_ThrowsException() {
            // Fixed: GenericApp constructor validates null parameters
            assertThatThrownBy(() -> new GenericApp("TestApp", null, mockPersistence, mockKernel))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Store definition cannot be null");
        }

        @Test
        @DisplayName("Constructor with null persistence throws exception")
        void testConstructor_NullPersistence_ThrowsException() {
            // Fixed: GenericApp constructor validates null parameters
            assertThatThrownBy(() -> new GenericApp("TestApp", mockStoreDefinition, null, mockKernel))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Persistence mechanism cannot be null");
        }

        @Test
        @DisplayName("Constructor with null kernel throws exception")
        void testConstructor_NullKernel_ThrowsException() {
            // Fixed: GenericApp constructor validates null parameters
            assertThatThrownBy(() -> new GenericApp("TestApp", mockStoreDefinition, mockPersistence, null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Application kernel cannot be null");
        }

        @Test
        @DisplayName("setOtherTabs with null collection throws exception")
        void testSetOtherTabs_NullCollection_ThrowsException() {
            // Fixed: setOtherTabs method validates null parameters
            GenericApp baseApp = new GenericApp("TestApp", mockStoreDefinition, mockPersistence, mockKernel);
            assertThatThrownBy(() -> baseApp.setOtherTabs((Collection<TabProvider>) null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Other tabs collection cannot be null");
        }

        @Test
        @DisplayName("setOtherTabs with null varargs throws NullPointerException")
        void testSetOtherTabs_NullVarargs_ThrowsNullPointerException() {
            GenericApp app = new GenericApp("TestApp", mockStoreDefinition, mockPersistence, mockKernel);

            // Arrays.asList(null) throws NullPointerException
            assertThatThrownBy(() -> app.setOtherTabs((TabProvider[]) null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("setOtherTabs with collection containing null elements handles gracefully")
        void testSetOtherTabs_CollectionWithNullElements_HandlesGracefully() {
            GenericApp app = new GenericApp("TestApp", mockStoreDefinition, mockPersistence, mockKernel);
            Collection<TabProvider> tabsWithNull = Arrays.asList(mockTabProvider1, null, mockTabProvider2);

            GenericApp updatedApp = app.setOtherTabs(tabsWithNull);

            assertThat(updatedApp.getOtherTabs()).containsExactly(mockTabProvider1, null, mockTabProvider2);
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Complete app configuration workflow")
        void testCompleteAppConfigurationWorkflow() {
            // Create base app
            GenericApp baseApp = new GenericApp("CompleteTestApp", mockStoreDefinition, mockKernel);

            // Configure all optional components
            Collection<TabProvider> tabs = Arrays.asList(mockTabProvider1, mockTabProvider2);
            Class<?> nodeClass = String.class;

            GenericApp fullyConfiguredApp = baseApp
                    .setOtherTabs(tabs)
                    .setNodeClass(nodeClass)
                    .setIcon(mockIcon);

            // Verify all configurations
            assertThat(fullyConfiguredApp.getName()).isEqualTo("CompleteTestApp");
            assertThat(fullyConfiguredApp.getDefinition()).isSameAs(mockStoreDefinition);
            assertThat(fullyConfiguredApp.getPersistence()).isInstanceOf(XmlPersistence.class);
            assertThat(fullyConfiguredApp.getKernel()).isSameAs(mockKernel);
            assertThat(fullyConfiguredApp.getOtherTabs()).containsExactlyElementsOf(tabs);
            assertThat(fullyConfiguredApp.getNodeClass()).isEqualTo(nodeClass);
            assertThat(fullyConfiguredApp.getIcon()).contains(mockIcon);

            // Verify immutability
            assertThat(baseApp.getOtherTabs()).isEmpty();
            assertThat(baseApp.getNodeClass()).isEqualTo(GenericApp.class);
            assertThat(baseApp.getIcon()).isEmpty();
        }

        @Test
        @DisplayName("Multiple modifications create separate instances")
        void testMultipleModifications_CreateSeparateInstances() {
            GenericApp baseApp = new GenericApp("TestApp", mockStoreDefinition, mockPersistence, mockKernel);

            GenericApp app1 = baseApp.setOtherTabs(mockTabProvider1);
            GenericApp app2 = baseApp.setNodeClass(String.class);
            GenericApp app3 = baseApp.setIcon(mockIcon);

            // All should be different instances
            assertThat(app1).isNotSameAs(baseApp);
            assertThat(app2).isNotSameAs(baseApp);
            assertThat(app3).isNotSameAs(baseApp);
            assertThat(app1).isNotSameAs(app2);
            assertThat(app1).isNotSameAs(app3);
            assertThat(app2).isNotSameAs(app3);

            // Each should have only their specific modification
            assertThat(app1.getOtherTabs()).containsExactly(mockTabProvider1);
            assertThat(app1.getNodeClass()).isEqualTo(GenericApp.class);
            assertThat(app1.getIcon()).isEmpty();

            assertThat(app2.getOtherTabs()).isEmpty();
            assertThat(app2.getNodeClass()).isEqualTo(String.class);
            assertThat(app2.getIcon()).isEmpty();

            assertThat(app3.getOtherTabs()).isEmpty();
            assertThat(app3.getNodeClass()).isEqualTo(GenericApp.class);
            assertThat(app3.getIcon()).contains(mockIcon);
        }
    }
}
