package ru.javawebinar.topjava.model;

import java.util.Objects;

public abstract class AbstractNamedEntity extends AbstractBaseEntity implements Comparable<AbstractNamedEntity> {

    protected String name;

    protected AbstractNamedEntity(Integer id, String name) {
        super(id);
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractNamedEntity that = (AbstractNamedEntity) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return String.format("Entity %s (%s, '%s')", getClass().getName(), id, name);
    }

    public int compareTo(AbstractNamedEntity o) {
        return name.compareTo(o.name);
    }
}