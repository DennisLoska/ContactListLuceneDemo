package de.arktis.javafx.contact.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

/**
 * The persistent class for the contact database table.
 *
 */
@Entity
@Indexed
@Table(name = "contact")
public class Contact {
	private Integer id;
	private String name;
	private String email;

	public Contact() {

	}

	public Contact(Integer id, String name, String email) {
		this.id = id;
		this.name = name;
		this.email = email;
	}

	@Id
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder("Id: ").append(this.getId()).append(" | Name:").append(this.getName()).append(" | Email:").append(this.getEmail());

		return stringBuilder.toString();
	}
}