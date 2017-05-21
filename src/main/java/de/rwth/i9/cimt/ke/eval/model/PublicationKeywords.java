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
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "publicationId", "keAlgorithm" }))
public class PublicationKeywords implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8541564310607798873L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column
	private Integer id;

	@Column(columnDefinition = "bit default 0")
	private boolean isDefault = false;

	@Column
	private Integer publicationId;

	@Column
	@Lob
	private String keywordTokens;

	@Enumerated(EnumType.STRING)
	@Column(length = 50, columnDefinition = "varchar(50) default ''")
	private KeyphraseExtractionAlgorithm keAlgorithm;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public boolean isDefault() {
		return isDefault;
	}

	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	public Integer getPublicationId() {
		return publicationId;
	}

	public void setPublicationId(Integer publicationId) {
		this.publicationId = publicationId;
	}

	public String getKeywordTokens() {
		return keywordTokens;
	}

	public void setKeywordToken(String keywordTokens) {
		this.keywordTokens = keywordTokens;
	}

	public KeyphraseExtractionAlgorithm getKeAlgorithm() {
		return keAlgorithm;
	}

	public void setKeAlgorithm(KeyphraseExtractionAlgorithm keAlgorithm) {
		this.keAlgorithm = keAlgorithm;
	}

}
