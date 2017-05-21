package de.rwth.i9.cimt.ke.eval.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import de.rwth.i9.cimt.ke.eval.model.Author;

public interface AuthorRepository extends PagingAndSortingRepository<Author, Integer> {
	List<Author> findByAuthorName(String authorname);

}
