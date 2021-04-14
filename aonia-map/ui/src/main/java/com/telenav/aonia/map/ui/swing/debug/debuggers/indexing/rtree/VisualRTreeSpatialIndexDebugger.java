////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.aonia.map.ui.swing.debug.debuggers.indexing.rtree;

import com.telenav.aonia.map.geography.indexing.rtree.InteriorNode;
import com.telenav.aonia.map.geography.indexing.rtree.Leaf;
import com.telenav.aonia.map.geography.indexing.rtree.Node;
import com.telenav.aonia.map.geography.indexing.rtree.RTreeSpatialIndexDebugger;
import com.telenav.aonia.map.geography.shape.rectangle.Bounded;
import com.telenav.aonia.map.geography.shape.rectangle.Intersectable;
import com.telenav.aonia.map.ui.swing.debug.InteractiveView;
import com.telenav.aonia.map.ui.swing.debug.View;
import com.telenav.aonia.map.ui.swing.debug.ViewableIdentifier;
import com.telenav.aonia.map.ui.swing.debug.Viewer;
import com.telenav.aonia.map.ui.swing.debug.viewables.ViewableRectangle;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

/**
 * @author jonathanl (shibo)
 */
public class VisualRTreeSpatialIndexDebugger<T extends Bounded & Intersectable> implements RTreeSpatialIndexDebugger<T>
{
    /** View for debugging */
    private transient View view;

    /** Identifier maps for debugging */
    private final transient Map<T, ViewableIdentifier> identifierForElement = new HashMap<>();

    private final transient Map<InteriorNode<T>, ViewableIdentifier> identifierForNode = new HashMap<>();

    private final transient Map<Leaf<T>, ViewableIdentifier> identifierForLeaf = new HashMap<>();

    @Override
    public void remove(final Node<T> node)
    {

    }

    public void removeNodeFromView(final Node<T> node)
    {
        if (view != null)
        {
            view.remove(identifier(node));
            frameComplete();
        }
    }

    @Override
    public void update(final Leaf<T> leaf, final T element)
    {

    }

    @Override
    public void update(final Node<T> node)
    {

    }

    public void updateElementInView(final Leaf<T> leaf, final T element)
    {
        if (view != null)
        {
            final var identifier = identifier(leaf, element);
            final var label = identifier + "-" + element.toString();
            view.update(identifier, new ViewableRectangle(
                    element.bounds(), Color.LIGHT_GRAY, Color.GREEN.darker(), label));
            frameComplete();
        }
    }

    public void updateNodeInView(final Node<T> node)
    {
        if (view != null)
        {
            view.update(identifier(node), new ViewableRectangle(
                    node.bounds(), Color.LIGHT_GRAY, Color.ORANGE.darker(), identifier(node).toString()));
        }
        frameComplete();
    }

    public void viewer(final Viewer viewer)
    {
        view = viewer.view("debug");
    }

    private void frameComplete()
    {
        if (view instanceof InteractiveView)
        {
            ((InteractiveView) view).frameComplete();
        }
    }

    private ViewableIdentifier identifier(final InteriorNode<T> node)
    {
        var identifier = identifierForNode.get(node);
        if (identifier == null)
        {
            identifier = new ViewableIdentifier("Node-" + identifierForNode.size());
            identifierForNode.put(node, identifier);
        }
        return identifier;
    }

    private ViewableIdentifier identifier(final Leaf<T> leaf)
    {
        var identifier = identifierForLeaf.get(leaf);
        if (identifier == null)
        {
            identifier = new ViewableIdentifier("Leaf-" + identifierForLeaf.size());
            identifierForLeaf.put(leaf, identifier);
        }
        return identifier;
    }

    private ViewableIdentifier identifier(final Leaf<T> leaf, final T element)
    {
        var identifier = identifierForElement.get(element);
        if (identifier == null)
        {
            identifier = new ViewableIdentifier(identifier(leaf) + ":" + identifierForElement.size());
            identifierForElement.put(element, identifier);
        }
        return identifier;
    }

    private ViewableIdentifier identifier(final Node<T> node)
    {
        return node.isLeaf() ? identifier((Leaf<T>) node) : identifier((InteriorNode<T>) node);
    }
}
