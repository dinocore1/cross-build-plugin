package com.devsmart.crossbuild.plugins;

import org.gradle.api.artifacts.ModuleVersionIdentifier;
import org.gradle.api.component.PublishableComponent;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.tasks.bundling.Zip;

import javax.inject.Inject;

public class ExportHeaders implements PublishableComponent {

    private final DirectoryProperty includeDir;
    private final ModuleVersionIdentifier coordinates;
    private Zip zipHeadersTask;


    @Inject
    public ExportHeaders(ModuleVersionIdentifier coordinates, ObjectFactory objectFactory) {
        this.coordinates = coordinates;
        this.includeDir = objectFactory.directoryProperty();

    }

    @Override
    public String getName() {
        return coordinates.getName();
    }

    @Override
    public ModuleVersionIdentifier getCoordinates() {
        return coordinates;
    }

    public DirectoryProperty getIncludeDir() {
        return includeDir;
    }

    public Zip getTask() {
        return zipHeadersTask;
    }

    public void setTask(Zip zipHeaders) {
        this.zipHeadersTask = zipHeaders;
    }
}
