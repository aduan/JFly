package net.jfly.plugin.ActiveRecord;

import java.io.Serializable;
import java.util.List;

public class Page<T> implements Serializable {

	private static final long serialVersionUID = -5395997221963176643L;

	private List<T> list;
	private int pageNumber;
	private int pageSize;
	private int totalPage;
	private int totalRow;

	public Page(List<T> list, int pageNumber, int pageSize, int totalPage, int totalRow) {
		this.list = list;
		this.pageNumber = pageNumber;
		this.pageSize = pageSize;
		this.totalPage = totalPage;
		this.totalRow = totalRow;
	}

	public List<T> getList() {
		return list;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public int getPageSize() {
		return pageSize;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public int getTotalRow() {
		return totalRow;
	}
}