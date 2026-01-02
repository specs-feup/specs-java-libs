package org.suikasoft.jOptions.app;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.suikasoft.jOptions.cli.GenericApp;
import org.suikasoft.jOptions.gui.panels.app.TabProvider;
import org.suikasoft.jOptions.persistence.XmlPersistence;
import org.suikasoft.jOptions.storedefinition.StoreDefinition;

import pt.up.fe.specs.util.providers.ResourceProvider;

/**
 * Comprehensive test suite for the App interface.
 * Tests all default methods, factory methods, and interface behavior.
 * 
 * @author Generated Tests
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("App Interface Tests")
class AppTest {

    @Mock
    private AppKernel mockKernel;

    @Mock
    private StoreDefinition mockStoreDefinition;

    @Mock
    private AppPersistence mockPersistence;

    @Mock
    private TabProvider mockTabProvider;

    @Mock
    private ResourceProvider mockResourceProvider;

    private TestApp testApp;

    /**
     * Test implementation of App interface for testing purposes.
     */
    private static class TestApp implements App {
        private final AppKernel kernel;
        private final String customName;
        private final StoreDefinition customDefinition;
        private final AppPersistence customPersistence;
        private final Collection<TabProvider> customTabs;
        private final Class<?> customNodeClass;
        private final Optional<ResourceProvider> customIcon;

        public TestApp(AppKernel kernel) {
            this(kernel, null, null, null, null, null, null);
        }

        public TestApp(AppKernel kernel, String customName, StoreDefinition customDefinition,
                AppPersistence customPersistence, Collection<TabProvider> customTabs,
                Class<?> customNodeClass, Optional<ResourceProvider> customIcon) {
            this.kernel = kernel;
            this.customName = customName;
            this.customDefinition = customDefinition;
            this.customPersistence = customPersistence;
            this.customTabs = customTabs;
            this.customNodeClass = customNodeClass;
            this.customIcon = customIcon;
        }

        @Override
        public AppKernel getKernel() {
            return kernel;
        }

        @Override
        public String getName() {
            return customName != null ? customName : App.super.getName();
        }

        @Override
        public StoreDefinition getDefinition() {
            return customDefinition != null ? customDefinition : App.super.getDefinition();
        }

        @Override
        public AppPersistence getPersistence() {
            return customPersistence != null ? customPersistence : App.super.getPersistence();
        }

        @Override
        public Collection<TabProvider> getOtherTabs() {
            return customTabs != null ? customTabs : App.super.getOtherTabs();
        }

        @Override
        public Class<?> getNodeClass() {
            return customNodeClass != null ? customNodeClass : App.super.getNodeClass();
        }

        @Override
        public Optional<ResourceProvider> getIcon() {
            return customIcon != null ? customIcon : App.super.getIcon();
        }
    }

    @BeforeEach
    void setUp() {
        testApp = new TestApp(mockKernel);
    }

    @Nested
    @DisplayName("Core Interface Methods")
    class CoreInterfaceMethods {

        @Test
        @DisplayName("Should return kernel from getKernel")
        void testGetKernel_ReturnsMockKernel() {
            // when
            AppKernel result = testApp.getKernel();

            // then
            assertThat(result).isSameAs(mockKernel);
        }

        @Test
        @DisplayName("Should return class simple name as default name")
        void testGetName_DefaultImplementation_ReturnsClassSimpleName() {
            // when
            String result = testApp.getName();

            // then
            assertThat(result).isEqualTo("TestApp");
        }

        @Test
        @DisplayName("Should return custom name when overridden")
        void testGetName_CustomImplementation_ReturnsCustomName() {
            // given
            String customName = "CustomAppName";
            TestApp customApp = new TestApp(mockKernel, customName, null, null, null, null, null);

            // when
            String result = customApp.getName();

            // then
            assertThat(result).isEqualTo(customName);
        }
    }

    @Nested
    @DisplayName("Store Definition Methods")
    class StoreDefinitionMethods {

        @Test
        @DisplayName("Should create store definition from interface by default")
        void testGetDefinition_DefaultImplementation_CreatesFromInterface() {
            // when
            StoreDefinition result = testApp.getDefinition();

            // then
            assertThat(result).isNotNull();
            // Note: Actual StoreDefinition creation depends on the interface,
            // this test verifies the method doesn't throw and returns non-null
        }

        @Test
        @DisplayName("Should return custom definition when overridden")
        void testGetDefinition_CustomImplementation_ReturnsCustomDefinition() {
            // given
            TestApp customApp = new TestApp(mockKernel, null, mockStoreDefinition, null, null, null, null);

            // when
            StoreDefinition result = customApp.getDefinition();

            // then
            assertThat(result).isSameAs(mockStoreDefinition);
        }
    }

    @Nested
    @DisplayName("Persistence Methods")
    class PersistenceMethods {

        @Test
        @DisplayName("Should create XML persistence by default")
        void testGetPersistence_DefaultImplementation_CreatesXmlPersistence() {
            // when
            AppPersistence result = testApp.getPersistence();

            // then
            assertThat(result).isInstanceOf(XmlPersistence.class);
        }

        @Test
        @DisplayName("Should return custom persistence when overridden")
        void testGetPersistence_CustomImplementation_ReturnsCustomPersistence() {
            // given
            TestApp customApp = new TestApp(mockKernel, null, null, mockPersistence, null, null, null);

            // when
            AppPersistence result = customApp.getPersistence();

            // then
            assertThat(result).isSameAs(mockPersistence);
        }
    }

    @Nested
    @DisplayName("Tab Provider Methods")
    class TabProviderMethods {

        @Test
        @DisplayName("Should return empty collection by default")
        void testGetOtherTabs_DefaultImplementation_ReturnsEmptyCollection() {
            // when
            Collection<TabProvider> result = testApp.getOtherTabs();

            // then
            assertThat(result).isNotNull()
                    .isEmpty();
        }

        @Test
        @DisplayName("Should return custom tabs when overridden")
        void testGetOtherTabs_CustomImplementation_ReturnsCustomTabs() {
            // given
            Collection<TabProvider> customTabs = List.of(mockTabProvider);
            TestApp customApp = new TestApp(mockKernel, null, null, null, customTabs, null, null);

            // when
            Collection<TabProvider> result = customApp.getOtherTabs();

            // then
            assertThat(result).isSameAs(customTabs)
                    .hasSize(1)
                    .contains(mockTabProvider);
        }
    }

    @Nested
    @DisplayName("Node Class Methods")
    class NodeClassMethods {

        @Test
        @DisplayName("Should return app class by default")
        void testGetNodeClass_DefaultImplementation_ReturnsAppClass() {
            // when
            Class<?> result = testApp.getNodeClass();

            // then
            assertThat(result).isEqualTo(TestApp.class);
        }

        @Test
        @DisplayName("Should return custom node class when overridden")
        void testGetNodeClass_CustomImplementation_ReturnsCustomClass() {
            // given
            Class<?> customClass = String.class;
            TestApp customApp = new TestApp(mockKernel, null, null, null, null, customClass, null);

            // when
            Class<?> result = customApp.getNodeClass();

            // then
            assertThat(result).isSameAs(customClass);
        }
    }

    @Nested
    @DisplayName("Icon Methods")
    class IconMethods {

        @Test
        @DisplayName("Should return empty optional by default")
        void testGetIcon_DefaultImplementation_ReturnsEmptyOptional() {
            // when
            Optional<ResourceProvider> result = testApp.getIcon();

            // then
            assertThat(result).isNotNull()
                    .isEmpty();
        }

        @Test
        @DisplayName("Should return custom icon when overridden")
        void testGetIcon_CustomImplementation_ReturnsCustomIcon() {
            // given
            Optional<ResourceProvider> customIcon = Optional.of(mockResourceProvider);
            TestApp customApp = new TestApp(mockKernel, null, null, null, null, null, customIcon);

            // when
            Optional<ResourceProvider> result = customApp.getIcon();

            // then
            assertThat(result).isSameAs(customIcon)
                    .isPresent()
                    .contains(mockResourceProvider);
        }
    }

    @Nested
    @DisplayName("Factory Methods")
    class FactoryMethods {

        @Test
        @DisplayName("Should create GenericApp with all parameters")
        void testNewInstance_WithAllParameters_CreatesGenericApp() {
            // given
            String name = "TestAppName";

            // when
            GenericApp result = App.newInstance(name, mockStoreDefinition, mockPersistence, mockKernel);

            // then
            assertThat(result).isNotNull()
                    .isInstanceOf(GenericApp.class);
            assertThat(result.getName()).isEqualTo(name);
            assertThat(result.getKernel()).isSameAs(mockKernel);
            assertThat(result.getDefinition()).isSameAs(mockStoreDefinition);
            assertThat(result.getPersistence()).isSameAs(mockPersistence);
        }

        @Test
        @DisplayName("Should create GenericApp using store definition name")
        void testNewInstance_WithDefinitionName_CreatesGenericApp() {
            // given
            String definitionName = "DefinitionName";
            when(mockStoreDefinition.getName()).thenReturn(definitionName);

            // when
            GenericApp result = App.newInstance(mockStoreDefinition, mockPersistence, mockKernel);

            // then
            assertThat(result).isNotNull()
                    .isInstanceOf(GenericApp.class);
            assertThat(result.getName()).isEqualTo(definitionName);
            assertThat(result.getKernel()).isSameAs(mockKernel);
            assertThat(result.getDefinition()).isSameAs(mockStoreDefinition);
            assertThat(result.getPersistence()).isSameAs(mockPersistence);

            verify(mockStoreDefinition).getName();
        }

        @Test
        @DisplayName("Should create App with kernel only")
        void testNewInstance_WithKernelOnly_CreatesApp() {
            // when
            App result = App.newInstance(mockKernel);

            // then
            assertThat(result).isNotNull();
            assertThat(result.getKernel()).isSameAs(mockKernel);
            assertThat(result.getDefinition()).isNotNull();
            assertThat(result.getPersistence()).isNotNull()
                    .isInstanceOf(XmlPersistence.class);
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Conditions")
    class EdgeCasesAndErrorConditions {

        @Test
        @DisplayName("Should handle null kernel in test app")
        void testTestApp_WithNullKernel_ReturnsNull() {
            // given
            TestApp appWithNullKernel = new TestApp(null);

            // when
            AppKernel result = appWithNullKernel.getKernel();

            // then
            assertThat(result).isNull();
        }

        @Test
        @DisplayName("Should handle empty custom name")
        void testGetName_WithEmptyCustomName_ReturnsEmptyString() {
            // given
            String emptyName = "";
            TestApp customApp = new TestApp(mockKernel, emptyName, null, null, null, null, null);

            // when
            String result = customApp.getName();

            // then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Should handle null custom tabs collection")
        void testGetOtherTabs_WithNullCustomTabs_ReturnsDefaultEmptyCollection() {
            // given
            TestApp customApp = new TestApp(mockKernel, null, null, null, null, null, null);

            // when
            Collection<TabProvider> result = customApp.getOtherTabs();

            // then
            assertThat(result).isNotNull()
                    .isEmpty();
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should work with complete custom configuration")
        void testCompleteCustomConfiguration_AllMethodsWorkTogether() {
            // given
            String customName = "IntegrationTestApp";
            Collection<TabProvider> customTabs = List.of(mockTabProvider);
            Class<?> customNodeClass = Integer.class;
            Optional<ResourceProvider> customIcon = Optional.of(mockResourceProvider);

            TestApp customApp = new TestApp(mockKernel, customName, mockStoreDefinition,
                    mockPersistence, customTabs, customNodeClass, customIcon);

            // when & then
            assertThat(customApp.getKernel()).isSameAs(mockKernel);
            assertThat(customApp.getName()).isEqualTo(customName);
            assertThat(customApp.getDefinition()).isSameAs(mockStoreDefinition);
            assertThat(customApp.getPersistence()).isSameAs(mockPersistence);
            assertThat(customApp.getOtherTabs()).isSameAs(customTabs);
            assertThat(customApp.getNodeClass()).isSameAs(customNodeClass);
            assertThat(customApp.getIcon()).isSameAs(customIcon);
        }

        @Test
        @DisplayName("Should create complete app instance through factory")
        void testFactoryCreatedApp_WorksWithAllMethods() {
            // given
            String appName = "FactoryApp";

            // when
            GenericApp app = App.newInstance(appName, mockStoreDefinition, mockPersistence, mockKernel);

            // then
            assertThat(app.getName()).isEqualTo(appName);
            assertThat(app.getKernel()).isSameAs(mockKernel);
            assertThat(app.getDefinition()).isSameAs(mockStoreDefinition);
            assertThat(app.getPersistence()).isSameAs(mockPersistence);
            assertThat(app.getOtherTabs()).isNotNull().isEmpty();
            assertThat(app.getNodeClass()).isEqualTo(GenericApp.class);
            assertThat(app.getIcon()).isNotNull().isEmpty();
        }
    }
}
