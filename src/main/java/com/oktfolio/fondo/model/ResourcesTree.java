package com.oktfolio.fondo.model;

import java.util.List;

/**
 * @author Oktfolio oktfolio@gmail.com
 * @date 2019/09/18
 */
public class ResourcesTree {

    private Integer id;
    private String label;
    private boolean disabled;
    private ResourcesTree children;
    private List<Integer> defaultChecked;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public ResourcesTree getChildren() {
        return children;
    }

    public void setChildren(ResourcesTree children) {
        this.children = children;
    }

    public List<Integer> getDefaultChecked() {
        return defaultChecked;
    }

    public void setDefaultChecked(List<Integer> defaultChecked) {
        this.defaultChecked = defaultChecked;
    }
}

