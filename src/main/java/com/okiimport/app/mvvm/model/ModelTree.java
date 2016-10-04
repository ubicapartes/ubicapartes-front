package com.okiimport.app.mvvm.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.zkoss.zul.DefaultTreeModel;
import org.zkoss.zul.DefaultTreeNode;
import org.zkoss.zul.TreeNode;

import com.okiimport.app.resource.model.ModelNavbar;

public class ModelTree<T extends ModelNavbar> extends DefaultTreeModel<T>{
	private static final long serialVersionUID = 1L;
	
	private Boolean justParentRoot = false;
	
	//private T[] data;
	
	@SuppressWarnings("unchecked")
	public ModelTree() {
		super(new DefaultTreeNode<T>(null, new DefaultTreeNode[]{}));
		//this.data = data;
	}
	
	@SuppressWarnings("unchecked")
	public ModelTree(T[] data) {
		super(new DefaultTreeNode<T>(null, new DefaultTreeNode[]{}));
		//this.data = data;
		configureModel(getRoot(), data);
	}

	@SuppressWarnings("unchecked")
	public ModelTree(T[] data, boolean emptyChildAsLeaf) {
		super(new DefaultTreeNode<T>(null, new DefaultTreeNode[]{}), emptyChildAsLeaf);
		//this.data = data;
		configureModel(getRoot(), data);
	}
	
	@SuppressWarnings("unchecked")
	public ModelTree(T[] data, boolean emptyChildAsLeaf, boolean multiple) {
		super(new DefaultTreeNode<T>(null, new DefaultTreeNode[]{}), emptyChildAsLeaf);
		//this.data = data;
		configureModel(getRoot(), data);
		setMultiple(multiple);
	}
	
	@SuppressWarnings("unchecked")
	public ModelTree(T[] data, boolean emptyChildAsLeaf, boolean multiple, Boolean justParentRoot) {
		super(new DefaultTreeNode<T>(null, new DefaultTreeNode[]{}), emptyChildAsLeaf);
		//this.data = data;
		this.justParentRoot=justParentRoot;
		configureModel(getRoot(), data);
		setMultiple(multiple);
	}
	
	/**SETTERS Y GETTERS*/
	public Boolean isJustParentRoot() {
		return justParentRoot;
	}

	public void setJustParentRoot(Boolean justParentRoot) {
		this.justParentRoot = justParentRoot;
	}
	
	/**METODOS PROPIOS DE LA CLASE*/
	public void sort(boolean ascending){
		super.sort(this.<T>getComparator(), ascending);
	}
	
	public void loadChild(TreeNode<T> nodo, T[] child){
		nodo.getChildren().clear();
		configureModel(nodo, child);
	}	

	public void addNode(T model, boolean sort){
		List<ModelNavbar> rootTree = model.getRootTree();
		TreeNode<T> nodo = this.getRoot();
		for(ModelNavbar nodoTree : rootTree){
			TreeNode<T> nodoTemp = findNode(nodoTree, nodo);
			if(nodoTemp==null){
				nodoTemp = new DefaultTreeNode<T>((T) nodoTree, new DefaultTreeNode[]{});
				nodo.add(nodoTemp);
			}
			nodo = nodoTemp;
		}
		
		if(sort)
			this.sort(true);
	}
	
	public void removeNode(DefaultTreeNode<T> nodo, boolean sort){
		removeNode(nodo.getChildren());
		nodo.removeFromParent();
	}
	
	public TreeNode<T> findNode(T model){
		return findNode(model, this.getRoot().getChildren());
	}
	
	public List<TreeNode<T>> getRootParent(){
		List<TreeNode<T>> models = new ArrayList<TreeNode<T>>();
		TreeNode<T> root = this.getRoot();
		if(root.getData()==null)
			models.addAll(root.getChildren());
		else
			models.add(root);
		return models;
	}
	
	public List<T> getParent(){
		List<T> models = new ArrayList<T>();
		TreeNode<T> root = this.getRoot();
		if(root.getData()==null)
			toList(models, null, root.getChildren(), true);
		else
			models.add(root.getData());
		return models;
	}
	
	public List<T> toList(){
		List<T> models = new ArrayList<T>();
		toList(models, null, this.getRoot().getChildren(), false);
		return models;
	}
	
	private void removeNode(List<TreeNode<T>> children){
		if(children!=null)	
			if(children.iterator().hasNext()){
				List<TreeNode<T>> temp = new ArrayList<TreeNode<T>>();
				temp.addAll(children);
				for(TreeNode<T> nodo : temp){
					if(nodo.getChildCount()>0)
						removeNode(nodo.getChildren());
					if(nodo instanceof DefaultTreeNode)
						((DefaultTreeNode<T>) nodo).removeFromParent();
				}
			}
	}
	
	private TreeNode<T> findNode(T model, List<TreeNode<T>> childs){
		for(TreeNode<T> child : childs){
			TreeNode<T> nodo = null;
			if(child.getData()!=null)
				if(child.getData().getIdNode()==model.getIdNode())
					nodo = child;
			//TreeNode<T> nodo = findNode(model, child);
			if(nodo!=null)
				return nodo;
			else if(child.getChildren()!=null)
				if(child.getChildren().iterator().hasNext())
					findNode(model, child.getChildren());
		}
		
		return null;
	}
	
	private TreeNode<T> findNode(ModelNavbar nodoTree, TreeNode<T> nodo) {
		try{
			for(TreeNode<T> nodoTemp : nodo.getChildren())
				if(nodoTemp.getData().getIdNode() == nodoTree.getIdNode())
					return nodoTemp;
		}
		catch(Exception e){
			return null;
		}
		return null;
	}
	
	private void toList(List<T> models, T parent, List<TreeNode<T>> childs, boolean justParent){
		T model = null;
		try{
			for(TreeNode<T> nodo : childs){
				model = nodo.getData();
				model.setParent(parent);
				models.add(model);
				if(nodo.getChildren()!=null && !justParent)
					if(nodo.getChildren().iterator().hasNext())
						toList(models, model, nodo.getChildren(), justParent);
			}
		}
		catch(Exception e){
			return;
		}
	}
	
	@SuppressWarnings("unchecked")
	private void configureModel(TreeNode<T> nodo, T[] data){
		//List<TreeNode<T>> children = nodo.getChildren();
		for(T dato : data){
			TreeNode<T>  tNodo = new DefaultTreeNode<T>(dato, new DefaultTreeNode[]{});
			if(dato.getChilds().size()>0 && !justParentRoot)
				configureModel(tNodo, (T[]) dato.childToArray(dato.getClass(), 0));
			else if(dato.getChilds().size()>0)
				tNodo.getChildren().add(new DefaultTreeNode<T>(null));
			nodo.add(tNodo);
		}
	}
	
	/**METODOS ESTATICOS DE LA CLASE*/
	public static <T extends ModelNavbar> Comparator<TreeNode<T>> getComparator(){
		return new Comparator<TreeNode<T>>() {

			@Override
			public int compare(TreeNode<T> tree1, TreeNode<T> tree2) {
				// TODO Auto-generated method stub
				if(tree1.getData()==null || tree2.getData()==null)
					return -1;
				return Integer.compare(tree1.getData().getIdNode(), tree2.getData().getIdNode());
			}
		};
	}
}
