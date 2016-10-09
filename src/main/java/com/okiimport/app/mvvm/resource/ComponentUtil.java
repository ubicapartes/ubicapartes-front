package com.okiimport.app.mvvm.resource;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.zkoss.zk.ui.Component;

public class ComponentUtil {
	
	public static <T extends Component> List<T> findParents(Class<T> clazz, Component parent, Integer depth) {
        return findParents(clazz, parent, depth, false);
    }

    public static <T extends Component> T findParent(Class<T> clazz, Component parent, Integer depth) {
        List<T> t = findParents(clazz, parent, depth, true);
        return (t.size() > 0) ? t.get(0) : null;
    }
	
	@SuppressWarnings({ "unchecked" })
	private static <T extends Component> List<T> findParents(Class<T> clazz, Component child, Integer depth, boolean exitOnFirst) {
		List<T> comps = new ArrayList<T>();
		System.out.println("nulo: "+(depth == null));
		Integer nextDepth = (depth == null) ? null : depth - 1;
		
		Component parent = child.getParent();
		if(parent != null) {
			if (parent.getClass().getName().equals(clazz.getName())) {
				comps.add((T) parent);
			}

			if (!exitOnFirst && (nextDepth == null || nextDepth > 0)) {
				comps.addAll(findParents(clazz, parent, nextDepth, exitOnFirst));
			}
		}
		return comps;
	}
	
	public static <T extends Component> List<T> findChilds(Class<T> clazz, Component parent, Integer depth) {
        return findChilds(clazz, parent, depth, false);
    }

    public static <T extends Component> T findChild(Class<T> clazz, Component parent, Integer depth) {
        List<T> t = findParents(clazz, parent, depth, true);
        return (t.size() > 0) ? t.get(0) : null;
    }

	@SuppressWarnings("unchecked")
	private static <T extends Component> List<T> findChilds(Class<T> clazz, Component parent, Integer depth, boolean exitOnFirst) {
        List<T> comps = new ArrayList<T>();
        Integer nextDepth = (depth == null) ? null : depth - 1;
        List<Component> children = parent.getChildren();
        
        for (Iterator<Component> iterator = children.iterator(); iterator.hasNext(); ) {
            Component child = iterator.next();
            if (child.getClass().getName().equals(clazz.getName())) {
                comps.add((T) child);
                if (exitOnFirst) {
                    break;
                }
            }
            if (nextDepth == null || nextDepth > 0) {
                comps.addAll(findChilds(clazz, child, nextDepth, exitOnFirst));
            }
        }
        return comps;
    }
}
