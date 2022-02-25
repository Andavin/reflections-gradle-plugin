/*
 * MIT License
 *
 * Copyright (c) 2022 Andavin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.github.andavin.reflections.gradle.plugin;

import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.logging.Logger;
import org.gradle.api.tasks.SourceSetContainer;
import org.gradle.api.tasks.TaskAction;
import org.reflections.Reflections;

import java.io.File;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * A {@link Task} that uses {@link Reflections} to embed the metadata
 * into the project artifact.
 *
 * @since February 25, 2022
 * @author Andavin
 */
public class ReflectionsMetadataTask extends DefaultTask {

    public static final String TASK_NAME = "reflections";

    @TaskAction
    public void generateMetadata() {

        Project project = this.getProject();
        Logger logger = project.getLogger();
        try {
            Field log = Reflections.class.getField("log");
            log.trySetAccessible();
            log.set(null, logger);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            logger.warn("Unable to set project logger on Reflections");
        }

        File outputDir = resolveIndexFileDestinationDir(project);
        ReflectionsPluginExtension extension = findOrCreateExtension(project);
        Reflections reflections = new Reflections(concat(parseOutputDirUrl(project), extension.getParams()));
        reflections.save(outputDir + File.separator + extension.getIndexFilename());
    }

    private static ReflectionsPluginExtension findOrCreateExtension(Project project) {

        ReflectionsPluginExtension extension = project.getExtensions()
                .findByType(ReflectionsPluginExtension.class);
        if (extension == null) {
            extension = new ReflectionsPluginExtension();
        }

        if (extension.getIndexFilename() == null) {
            extension.setIndexFilename(project.getName() + "-reflections.xml");
        }

        return extension;
    }

    private static File resolveOutputDirectory(Project project) {
        SourceSetContainer sources = (SourceSetContainer) project.getProperties().get("sourceSets");
        return sources.getByName("main").getJava().getClassesDirectory().get().getAsFile();
    }

    private static URL parseOutputDirUrl(Project project) {
        try {
            return resolveOutputDirectory(project).toURI().toURL();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    private static File resolveIndexFileDestinationDir(Project project) {
        File javaOutputDir = resolveOutputDirectory(project);
        File outputDir = new File(javaOutputDir.getAbsolutePath() +
                File.separator + "META-INF" + File.separator + "reflections");
        outputDir.mkdirs();
        return outputDir;
    }

    private static Object[] concat(Object obj, Object[] array) {
        Object[] copy = new Object[array.length + 1];
        System.arraycopy(array, 0, copy, 1, array.length);
        copy[0] = obj;
        return copy;
    }
}
