package com.devsmart.crossbuild;


import java.util.Objects;

public class BuildTarget {

    public final OS os;
    public final Arch arch;

    public BuildTarget(OS os, Arch arch) {
        this.os = os;
        this.arch = arch;
    }

    @Override
    public int hashCode() {
        return os.hashCode() ^ arch.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) {
            return true;
        }

        if(o == null || getClass() != o.getClass()) {
            return false;
        }

        BuildTarget that = (BuildTarget) o;
        return Objects.equals(os, that.os) && Objects.equals(arch, that.arch);
    }

    @Override
    public String toString() {
        return os.getName() + ":" + arch.getName();
    }
}
