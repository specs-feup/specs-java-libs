package org.suikasoft.jOptions.persistence;

import static org.assertj.core.api.Assertions.*;
import java.io.File;
import java.nio.file.*;
import java.util.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.suikasoft.XStreamPlus.ObjectXml;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import org.suikasoft.jOptions.Options.FileList;
import org.suikasoft.jOptions.storedefinition.StoreDefinition;
import pt.up.fe.specs.util.utilities.StringList;

class ConfigPersistenceTest {
    @TempDir Path folder;
    @Test void savesAndLoadsRealStoresThroughThePublicPersistenceApi() throws Exception {
        var text = KeyFactory.string("text");
        var flags = KeyFactory.object("flags", StringList.class);
        var file = KeyFactory.object("relativeFile", File.class);
        var nested = KeyFactory.object("nested", DataStore.class);
        var files = KeyFactory.object("files", FileList.class);
        var definition = StoreDefinition.newInstance("config", text, flags, file, nested, files);
        DataStore data = DataStore.newInstance(definition);
        data.set(text," \nOlá 😀 <&>\" ");
        data.set(flags,new StringList(List.of("", "-DVALUE=a;b", "😀")));
        data.set(file,new File("../relative path/x"));
        DataStore child = DataStore.newInstance("child");
        child.setRaw("present",Optional.of(new File("../../x")));
        child.setRaw("empty",Optional.empty());
        child.setRaw("mixed",new ArrayList<>(Arrays.asList("a",1,"b",null)));
        child.setRaw("map",new LinkedHashMap<>(Map.of(7,"seven","7",7L)));
        data.set(nested,child);
        File existing = Files.writeString(folder.resolve("source.c"),"int x;").toFile();
        data.set(files,FileList.decode(folder + ";source.c"));
        var persistence = new XmlPersistence(definition);
        File config = folder.resolve("config.xml").toFile();
        assertThat(persistence.saveData(config,data,false)).isTrue();
        String serialized = Files.readString(config.toPath());
        assertThat(serialized).contains("specs-config").doesNotContain("StoreDefinition", "NormalKey", "persistence");
        DataStore loaded = persistence.loadData(config);
        assertThat(loaded.get(text)).isEqualTo(data.get(text));
        assertThat(loaded.get(flags).getStringList()).isEqualTo(data.get(flags).getStringList());
        assertThat(loaded.get(file).getPath()).isEqualTo("../relative path/x");
        assertThat(loaded.get(nested).get("present")).isEqualTo(Optional.of(new File("../../x")));
        assertThat(loaded.get(nested).get("empty")).isEqualTo(Optional.empty());
        assertThat(loaded.get(nested).get("mixed")).isEqualTo(child.get("mixed"));
        assertThat(loaded.get(nested).get("map")).isEqualTo(child.get("map"));
        assertThat(loaded.get(files).getFiles()).containsExactly(existing);
        assertThat(data.get(text)).isEqualTo(" \nOlá 😀 <&>\" ");
    }
    @Test void objectXmlDispatchAlwaysProjectsDefinitionsAndDoesNotPersistUnknownKeys() {
        var key = KeyFactory.string("keep");
        var definition = StoreDefinition.newInstance("config",key);
        DataStore data = DataStore.newInstance(definition);
        data.set(key,"kept"); data.setRaw("discard",new Object());
        ObjectXml<DataStore> xml = new DataStoreXml(definition);
        String serialized = xml.toXml(data);
        assertThat(serialized).contains("kept").doesNotContain("discard", "StoreDefinition");
        assertThat(xml.fromXml(serialized).get(key)).isEqualTo("kept");
    }
    @Test void failedSavesPreserveOriginalFileAndTemporaryOptions() throws Exception {
        var unsupported = KeyFactory.object("unsupported",Object.class);
        var definition = StoreDefinition.newInstance("config",unsupported);
        var data = DataStore.newInstance(definition);
        data.set(unsupported,new Object());
        var configKey = org.suikasoft.jOptions.gui.panels.app.AppKeys.CONFIG_FILE;
        var folderKey = org.suikasoft.jOptions.JOptionKeys.CURRENT_FOLDER_PATH;
        var relativeKey = org.suikasoft.jOptions.JOptionKeys.USE_RELATIVE_PATHS;
        data.set(configKey,new File("previous"));
        data.set(folderKey,Optional.of("previous-folder"));
        data.set(relativeKey,false);
        File target = Files.writeString(folder.resolve("existing.json"),"unchanged").toFile();
        assertThatThrownBy(() -> new XmlPersistence(definition).saveData(target,data,false))
                .isInstanceOf(IllegalArgumentException.class);
        assertThat(Files.readString(target.toPath())).isEqualTo("unchanged");
        assertThat(data.get(configKey.getName())).isEqualTo(new File("previous"));
        assertThat(data.get(folderKey.getName())).isEqualTo(Optional.of("previous-folder"));
        assertThat(data.get(relativeKey.getName())).isEqualTo(false);
    }
    @Test void oldXmlIsRejectedWithActionableMigrationInstructions() throws Exception {
        var definition = StoreDefinition.newInstance("config",KeyFactory.string("x"));
        File file = Files.writeString(folder.resolve("old.xml"),"<SimpleDataStore/>").toFile();
        assertThatThrownBy(() -> new XmlPersistence(definition).loadData(file))
                .hasMessageContaining("convert-xstream-config.mjs");
    }
}
