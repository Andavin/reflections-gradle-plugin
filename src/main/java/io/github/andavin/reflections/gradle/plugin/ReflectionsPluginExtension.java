package io.github.andavin.reflections.gradle.plugin;

import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ConfigurationBuilder;

/**
 * The configuration to use when creating the {@link Reflections}
 * instance and storing the metadata.
 *
 * @since February 25, 2022
 * @author Andavin
 */
public class ReflectionsPluginExtension {

    public static final String EXTENSION_NAME = "reflectionsConfig";

    /**
     * The file name for the reflections metadata file.
     * <p>
     * If {@code null}, the index file name will default to
     * {@code projectName-reflections.xml} where the project
     * name is the name of the executing Gradle project.
     */
    private String indexFilename;

    /**
     * An array of objects to use in {@link ConfigurationBuilder#build(Object...)}
     * <p>
     * Default: All scanners (i.e. {@link Scanners#values()}).
     *
     * @see ConfigurationBuilder#build(Object...)
     */
    private Object[] params = Scanners.values();

    public String getIndexFilename() {
        return indexFilename;
    }

    public void setIndexFilename(String indexFilename) {
        this.indexFilename = indexFilename;
    }

    public Object[] getParams() {
        return params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }
}
