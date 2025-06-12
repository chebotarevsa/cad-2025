package ru.bsuedu.cad.lab;

public class Category {
	public int category_id;
	public String name;
	public String description;

	public Category(int category_id, String name, String description) {
		this.category_id = category_id;
		this.name = name;
		this.description = description;
	}

	public Category() {
	}

	@Override
	public String toString() {
		return "Category {id= " + category_id + ", name= '" + name + "'" +
				", description='" + description + "'" + '}';
	}
}
