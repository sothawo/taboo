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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * a component that displays a list of tags as a continuous flow of buttons. There can be a Listener registered (only
 * one).
 *
 * @author P.J. Meisch (pj.meisch@sothawo.com).
 */
public class TagListComponent extends CustomComponent {
// ------------------------------ FIELDS ------------------------------

    /** Logger */
    private static final Logger logger = LoggerFactory.getLogger(TagListComponent.class);
    /** the layout containing the tags. */
    private final Layout layout;
    /** the tags */
    private final Collection<String> tags = new ArrayList<>();
    /** the listener that is notified when a tag is selected. */
    private Listener listener;

// --------------------------- CONSTRUCTORS ---------------------------

    public TagListComponent() {
        layout = new CssLayout();
        layout.addStyleName("taglist");
        layout.setWidth("100%");

        setCompositionRoot(layout);
    }

// --------------------- GETTER / SETTER METHODS ---------------------

    public Collection<String> getTags() {
        return tags;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

// -------------------------- OTHER METHODS --------------------------

    public void setTags(Collection<String> tags) {
        this.tags.clear();
        layout.removeAllComponents();
        if (null != tags) {
            tags.stream()
                    .filter(Objects::nonNull)
                    .filter(tag -> !tag.isEmpty())
                    .forEach(tag -> {
                        Button tagButton = new Button(tag);
                        tagButton.addStyleName("taglist-entry");
                        tagButton.addStyleName(ValoTheme.BUTTON_TINY);
                        tagButton.addClickListener(clickEvent -> tagSelect(clickEvent.getButton().getCaption()));
                        layout.addComponent(tagButton);
                        this.tags.add(tag);
                    });
        }
    }

    /**
     * called when a tag  is selected in the tag list. Logging and calling a selcted Listener.
     *
     * @param tag
     *         the selected tag
     */
    private void tagSelect(String tag) {
        logger.info("tag {} selected", tag);
        if (null != listener) {
            listener.tagSelected(tag);
        }
    }

// -------------------------- INNER CLASSES --------------------------

    /**
     * a listener is notified when a tag is selected.
     */
    @FunctionalInterface
    public interface Listener {
// -------------------------- OTHER METHODS --------------------------

        /**
         * called when a tag was selected.
         *
         * @param tag
         *         the tag
         */
        void tagSelected(String tag);
    }
}
