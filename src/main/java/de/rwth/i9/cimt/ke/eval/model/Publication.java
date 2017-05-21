package de.rwth.i9.cimt.ke.eval.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
public class Publication implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1243650604292744344L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column
	private Integer id;
	@Column
	@Lob
	private String title;
	@Column
	@Lob
	private String textContent;
	@Column
	@Lob
	private String defaultKeywords;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTextContent() {
		return textContent;
	}

	public void setTextContent(String textContent) {
		this.textContent = textContent;
	}

	public String getDefaultKeywords() {
		return defaultKeywords;
	}

	public void setDefaultKeywords(String defaultKeywords) {
		this.defaultKeywords = defaultKeywords;
	}

}
