package com.okiimport.app.mvvm.resource;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.zkoss.zk.ui.Component;

public class ComponentUtil {

	/*@SuppressWarnings("unchecked")
	private static <T extends Component> List<T> find(Class<T> clazz, Component parent, int depth, boolean exitOnFirst) {
        List<T> comps = new ArrayList<T>();
        int nextDepth = depth - 1;
        List<Component> children = parent.getChildren();
        System.out.println("parent " + parent + ",   current depth: " + depth + ",  member: " + children.size());
        for (Iterator<Component> iterator = children.iterator(); iterator.hasNext(); ) {
            Component child = iterator.next();
            if (child.getClass().getName().equals(clazz.getName())) {
                comps.add((T) child);
                if (exitOnFirst) {
                    break;
                }
            }
            if (nextDepth > 0) {
                comps.addAll(find(clazz, child, nextDepth, exitOnFirst));
            }
        }
        return comps;
    }*/
	
	@SuppressWarnings({ "unchecked" })
	private static <T extends Component> List<T> find(Class<T> clazz, Component child, Integer depth, boolean exitOnFirst) {
		List<T> comps = new ArrayList<T>();
		System.out.println("nulo: "+(depth == null));
		Integer nextDepth = (depth == null) ? null : depth - 1;
		
		Component parent = child.getParent();
		if(parent != null) {
			if (parent.getClass().getName().equals(clazz.getName())) {
				comps.add((T) parent);
			}

			if (!exitOnFirst && (nextDepth == null || nextDepth > 0)) {
				comps.addAll(find(clazz, parent, nextDepth, exitOnFirst));
			}
		}
		return comps;
	}

    public static <T extends Component> List<T> find(Class<T> clazz, Component parent, Integer depth) {
        return find(clazz, parent, depth, false);
    }

    public static <T extends Component> T findFirst(Class<T> clazz, Component parent, Integer depth) {
        List<T> t = find(clazz, parent, depth, true);
        if (t.size() > 0)
            return t.get(0);
        return null;
    }

}
