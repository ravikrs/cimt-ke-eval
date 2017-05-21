package de.rwth.i9.cimt.ke.eval.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import de.rwth.i9.cimt.ke.eval.model.Publication;

public interface PublicationRepository extends PagingAndSortingRepository<Publication, Integer> {
	List<Publication> findByTitle(String title);

}
