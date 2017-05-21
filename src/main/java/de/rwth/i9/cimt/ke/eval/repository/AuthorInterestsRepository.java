package de.rwth.i9.cimt.ke.eval.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import de.rwth.i9.cimt.ke.eval.model.AuthorInterests;

public interface AuthorInterestsRepository extends PagingAndSortingRepository<AuthorInterests, Integer> {
	List<AuthorInterests> findByAuthorId(Integer authorid);

	List<AuthorInterests> findById(Integer id);
}
