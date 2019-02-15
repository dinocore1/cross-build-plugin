package com.devsmart.crossbuild.plugins;

import com.devsmart.crossbuild.plugins.cmake.ComponentWithCMakeArgs;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.provider.Property;
import org.gradle.nativeplatform.TargetMachine;

import javax.inject.Inject;
import java.util.List;

public class TargetConfig implements ComponentWithCMakeArgs {

    private final String name;
    private final Property<TargetMachine> machine;
    private final ListProperty<String> cmakeArgs;

    public String getName() {
        return name;
    }

    public Property<TargetMachine> getMachine() {
        return machine;
    }

    public void setMachine(TargetMachine machine) {
        this.machine.set(machine);
    }

    @Override
    public ListProperty<String> getCmakeArgs() {
        return cmakeArgs;
    }

    public void setCmakeArgs(List<String> args) {
        this.cmakeArgs.set(args);
    }

    @Inject
    public TargetConfig(String name, ObjectFactory objectFactory) {
        this.name = name;
        this.machine = objectFactory.property(TargetMachine.class);
        this.cmakeArgs = objectFactory.listProperty(String.class).empty();
    }


}
