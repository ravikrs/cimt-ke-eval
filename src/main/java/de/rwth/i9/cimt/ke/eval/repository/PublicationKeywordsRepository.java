package de.rwth.i9.cimt.ke.eval.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import de.rwth.i9.cimt.ke.eval.model.PublicationKeywords;
import de.rwth.i9.cimt.ke.lib.constants.KeyphraseExtractionAlgorithm;

public interface PublicationKeywordsRepository extends PagingAndSortingRepository<PublicationKeywords, Integer> {
	List<PublicationKeywords> findByPublicationId(Integer publicationid);

	List<PublicationKeywords> findByPublicationIdAndKeAlgorithm(Integer publicationId,
			KeyphraseExtractionAlgorithm keAlgorithm);

}
