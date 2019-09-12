package com.gin.admin.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gin.admin.model.Tree;

public class TreeBeanUtil {
	static Logger logger = LoggerFactory.getLogger(TreeBeanUtil.class);

	/**
	 * 根据树形结构列表和根节点id获取一棵树
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Tree<T>> T listToTree(List<T> list, Object rootId) {
		if (list == null) {
			logger.warn("传入的List不能为空");
			return null;
		}
		if (rootId == null) {
			logger.warn("传入的节点id不能为空");
			return null;
		}
		List<T> rootList = list.stream().filter(t -> rootId.equals(ClassUtils.getValue("id", t)))
				.collect(Collectors.toList());
		if (rootList == null || rootList.size() <= 0) {
			logger.warn("传入的List中不包含id为[" + rootId + "]的元素");
			return (T) ClassUtils.newInstance(list.get(0).getClass());
		}
		T root = rootList.get(0);
		List<T> children = new ArrayList<>();
		List<T> childList = list.stream().filter(t -> rootId.equals(ClassUtils.getValue("parentId", t)))
				.collect(Collectors.toList());
		Collections.sort(childList, (c1, c2) -> {
			return c1.getTreeSort().compareTo(c2.getTreeSort());
		});
		for (T child : childList) {
			T childTree = listToTree(list, ClassUtils.getValue("id", child));
			childTree.setParent(root);
			children.add(childTree);
		}
		root.setChildren(children);
		return root;
	}

	/**
	 * 根据树形结构列表和父节点id获取树的列表
	 */
	public static <T extends Tree<T>> List<T> listToTreeList(List<T> list, Object parentId) {
		List<T> treeList = new ArrayList<>();
		if (list == null) {
			logger.warn("传入的List不能为空");
			return treeList;
		}
		List<T> rootList;
		if (parentId == null) {
			rootList = list.stream().filter(t -> ClassUtils.getValue("parentId", t) == null)
					.collect(Collectors.toList());
		} else {
			rootList = list.stream().filter(t -> parentId.equals(ClassUtils.getValue("parentId", t)))
					.collect(Collectors.toList());
		}
		if (rootList.size() > 0) {
			Collections.sort(rootList, (r1, r2) -> {
				return r1.getTreeSort().compareTo(r2.getTreeSort());
			});
			for (T root : rootList) {
				treeList.add(listToTree(list, ClassUtils.getValue("id", root)));
			}
		}
		return treeList;
	}
}
