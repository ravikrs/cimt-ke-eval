package de.rwth.i9.cimt.ke.eval.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.rwth.i9.cimt.ke.eval.model.Author;
import de.rwth.i9.cimt.ke.eval.model.AuthorPublications;
import de.rwth.i9.cimt.ke.eval.model.Publication;
import de.rwth.i9.cimt.ke.eval.repository.AuthorInterestsRepository;
import de.rwth.i9.cimt.ke.eval.repository.AuthorPublicationsRepository;
import de.rwth.i9.cimt.ke.eval.repository.AuthorRepository;
import de.rwth.i9.cimt.ke.eval.repository.PublicationKeywordsRepository;
import de.rwth.i9.cimt.ke.eval.repository.PublicationRepository;

@Service("sqlCorpusImporter")
public class SqlCorpusImporter {
	private static final Logger log = LoggerFactory.getLogger(SqlCorpusImporter.class);

	@Autowired
	AuthorInterestsRepository authorInterestsRepository;
	@Autowired
	AuthorPublicationsRepository authorPublicationsRepository;
	@Autowired
	AuthorRepository authorRepository;
	@Autowired
	PublicationKeywordsRepository publicationKeywordsRepository;
	@Autowired
	PublicationRepository publicationRepository;

	public void runCorpusImporter() throws FileNotFoundException, IOException {
		String inputDirectory = "C:\\Users\\singh\\Desktop\\RWTH Corpus";
		File dir = new File(inputDirectory);
		String[] extensions = new String[] { "abs" };
		Collection<File> files = FileUtils.listFiles(dir, extensions, true);
		Set<String> fileNamesSet = new HashSet<String>();
		for (File file : files) {
			log.info("###########################");
			log.info("Starting processing: " + file.getName());
			String title = FilenameUtils.removeExtension(file.getName()).trim();
			if (!publicationRepository.findByTitle(title).isEmpty()) {
				log.info("Publication already exists" + title);
			}
			FileReader fr = new FileReader(file);
			List<String> lines = IOUtils.readLines(fr);
			fr.close();
			fileNamesSet.add(file.getName());
			StringBuilder textContent = new StringBuilder();

			String[] authors = new String[0];
			boolean flagContainsAuthors = false;
			for (String line : lines) {
				if (line.isEmpty()) {
					continue;
				}
				if (line.startsWith("Author")) {
					String[] authorsc = line.split(":");
					authors = authorsc[1].split(",");
					flagContainsAuthors = true;
				} else {
					textContent.append(line);
				}
			}
			if (!flagContainsAuthors) {
				System.out.println("File doesnot contain Author: " + title);
				log.info("File doesnot contain Author: " + title);
			}

			Publication pub = new Publication();
			pub.setTitle(title);
			pub.setTextContent(textContent.toString().trim());
			pub = publicationRepository.save(pub);
			int pubId = pub.getId();

			for (int i = 0; i < authors.length; i++) {
				String authorName = authors[i];
				if (authorName.isEmpty()) {
					continue;
				}
				authorName = authors[i].trim().toLowerCase();
				int authorId;
				List<Author> existingAuthors = authorRepository.findByAuthorName(authorName);
				if (existingAuthors.isEmpty()) {
					Author auth = new Author();
					auth.setAuthorName(authorName);
					auth = authorRepository.save(auth);
					log.info("Author Added : " + authorName);
					authorId = auth.getId();
				} else {
					authorId = existingAuthors.get(0).getId();
					log.info("Author exists : " + authorName);
				}
				//update author publications
				AuthorPublications authpub = new AuthorPublications();
				authpub.setAuthorPosition(i + 1);
				authpub.setAuthorId(authorId);
				authpub.setPublicationId(pubId);
				authpub = authorPublicationsRepository.save(authpub);
				log.info("Author publication Added for : " + authorName + " pub id : " + pubId);
				log.info("###########################");

			}
		}

		extensions = new String[] { "kwds" };
		files = FileUtils.listFiles(dir, extensions, true);
		for (File file : files) {
			log.info("###########################");
			log.info("Starting Keyword processing: " + file.getName());
			String title = FilenameUtils.removeExtension(file.getName()).trim();
			String defaultKeywords = "";
			StringBuilder keywordContent = new StringBuilder();
			List<Publication> publications = publicationRepository.findByTitle(title);
			if (!publications.isEmpty()) {
				List<String> lines = IOUtils.readLines(new FileReader(file));
				for (String line : lines) {
					if (lines.isEmpty()) {
						continue;
					}
					keywordContent.append(line.trim());
				}
				Publication pub = publications.get(0);
				pub.setDefaultKeywords(keywordContent.toString());
				publicationRepository.save(pub);

			} else {
				log.info("Publication doesnot exists" + title);
			}

		}

		//add a publication

		//update authors table

		//update keyword token table
		//		String token = "";
		//		token = token.trim().toLowerCase();
		//		int tokenId;
		//		List<KeywordToken> kwTokens = keywordTokensRepository.findByToken(token);
		//		if (kwTokens.isEmpty()) {
		//			KeywordToken kwt = new KeywordToken();
		//			kwt.setToken(token);
		//			kwt = keywordTokensRepository.save(kwt);
		//			tokenId = kwt.getId();
		//		} else {
		//			tokenId = kwTokens.get(0).getId();
		//		}

	}

}
