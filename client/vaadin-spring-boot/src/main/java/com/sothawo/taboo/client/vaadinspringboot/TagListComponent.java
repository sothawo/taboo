/**
 * Copyright (c) 2015 sothawo
 *
 * http://www.sothawo.com
 */
package com.sothawo.taboo.client.vaadinspringboot;

import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Layout;
import com.vaadin.ui.themes.ValoTheme;

import java.util.Collection;

/**
 * a component that displays a list of tags as a continuous flow of buttons.
 *
 * @author P.J. Meisch (pj.meisch@sothawo.com).
 */
public class TagListComponent extends CustomComponent {
// ------------------------------ FIELDS ------------------------------

    /** the layout containing the tags. */
    private final Layout layout;

// --------------------------- CONSTRUCTORS ---------------------------

    public TagListComponent() {
        layout = new CssLayout();
        layout.addStyleName("taglist");
        layout.setWidth("100%");

        setCompositionRoot(layout);
    }

// -------------------------- OTHER METHODS --------------------------

    public void setTags(Collection<String> tags) {
        layout.removeAllComponents();
        if (null != tags) {
            tags.stream().forEach(tag -> {
                Button tagComponent = new Button(tag);
                tagComponent.addStyleName("taglist-entry");
                tagComponent.addStyleName(ValoTheme.BUTTON_TINY);
                layout.addComponent(tagComponent);
            });
        }
    }
}
