package de.rwth.i9.cimt.ke.eval.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import de.rwth.i9.cimt.ke.lib.constants.KeyphraseExtractionAlgorithm;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "authorId", "keAlgorithm" }))
public class AuthorInterests implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4421054505177881889L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column
	private Integer id;

	@Column
	private Integer authorId;

	@Column
	@Lob
	private String authorInterest;

	@Enumerated(EnumType.STRING)
	@Column(length = 50, columnDefinition = "varchar(50) default ''")
	private KeyphraseExtractionAlgorithm keAlgorithm;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getAuthorId() {
		return authorId;
	}

	public void setAuthorId(Integer authorId) {
		this.authorId = authorId;
	}

	public String getAuthorInterest() {
		return authorInterest;
	}

	public void setAuthorInterest(String authorInterest) {
		this.authorInterest = authorInterest;
	}

	public KeyphraseExtractionAlgorithm getKeAlgorithm() {
		return keAlgorithm;
	}

	public void setKeAlgorithm(KeyphraseExtractionAlgorithm keAlgorithm) {
		this.keAlgorithm = keAlgorithm;
	}

}
