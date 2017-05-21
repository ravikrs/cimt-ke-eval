package de.rwth.i9.cimt.ke.eval.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import de.rwth.i9.cimt.ke.eval.model.PublicationKeywords;

public interface PublicationKeywordsRepository extends PagingAndSortingRepository<PublicationKeywords, Integer> {
	List<PublicationKeywords> findByPublicationId(Integer publicationid);

}
