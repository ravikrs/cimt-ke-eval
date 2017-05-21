package de.rwth.i9.cimt.ke.eval.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import de.rwth.i9.cimt.ke.eval.model.Publication;
import de.rwth.i9.cimt.ke.eval.model.PublicationKeywords;
import de.rwth.i9.cimt.ke.eval.repository.AuthorInterestsRepository;
import de.rwth.i9.cimt.ke.eval.repository.AuthorPublicationsRepository;
import de.rwth.i9.cimt.ke.eval.repository.AuthorRepository;
import de.rwth.i9.cimt.ke.eval.repository.PublicationKeywordsRepository;
import de.rwth.i9.cimt.ke.eval.repository.PublicationRepository;
import de.rwth.i9.cimt.ke.eval.util.WordCount;
import de.rwth.i9.cimt.ke.lib.algorithm.kpextraction.jate.Jate;
import de.rwth.i9.cimt.ke.lib.algorithm.kpextraction.rake.Rake;
import de.rwth.i9.cimt.ke.lib.algorithm.kpextraction.textrank.LanguageEnglish;
import de.rwth.i9.cimt.ke.lib.algorithm.kpextraction.textrank.TextRankWordnet;
import de.rwth.i9.cimt.ke.lib.algorithm.kpextraction.unsupervised.graphranking.TextRank;
import de.rwth.i9.cimt.ke.lib.algorithm.kpextraction.unsupervised.graphranking.TopicRank;
import de.rwth.i9.cimt.ke.lib.algorithm.kpextraction.unsupervised.topicclustering.KeyCluster;
import de.rwth.i9.cimt.ke.lib.algorithm.kpextraction.unsupervised.topicclustering.TopicalPageRank;
import de.rwth.i9.cimt.ke.lib.constants.KeyphraseExtractionAlgorithm;
import de.rwth.i9.cimt.ke.lib.model.Keyword;
import de.rwth.i9.cimt.nlp.opennlp.OpenNLPImplSpring;
import uk.ac.shef.dcs.jate.model.JATETerm;

@Service("keExtractionService")
public class KEExtractionService {
	private static final Logger log = LoggerFactory.getLogger(KEExtractionService.class);

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
	@Autowired
	OpenNLPImplSpring openNLPImplSpring;
	private @Value("${cimt.home}") String cimtHome;
	@Autowired
	LanguageEnglish languageEnglish;

	public void runKEAlgorithm() {
		int pageSize = 10;
		long count = publicationRepository.count();
		for (int i = 0; i * pageSize < count; i++) {
			Page<Publication> publications = publicationRepository.findAll(new PageRequest(i, pageSize));
			for (Publication pub : publications) {
				log.info("keyphrase extraction. PubID -> " + pub.getId());
				KeyphraseExtractionAlgorithm[] alg = new KeyphraseExtractionAlgorithm[] {
						KeyphraseExtractionAlgorithm.KEY_CLUSTER, KeyphraseExtractionAlgorithm.RAKE,
						KeyphraseExtractionAlgorithm.TEXT_RANK, KeyphraseExtractionAlgorithm.TEXT_RANK_WORDNET,
						KeyphraseExtractionAlgorithm.TOPIC_RANK, KeyphraseExtractionAlgorithm.TOPICAL_PAGE_RANK };
				for (KeyphraseExtractionAlgorithm k : alg) {
					if (k.equals(KeyphraseExtractionAlgorithm.DEFAULT)) {
						continue;
					}
					try {
						String keyphrases = this.performKeyphraseExtraction(pub.getTextContent(), k);
						PublicationKeywords pubKw = new PublicationKeywords();
						pubKw.setKeAlgorithm(k);
						pubKw.setPublicationId(pub.getId());
						pubKw.setKeywordToken(keyphrases);
						publicationKeywordsRepository.save(pubKw);
					} catch (Exception e) {
						log.error("Couldnot perform keyphrase extraction. PubID -> " + pub.getId() + " algorithm -> "
								+ k.toString());
						e.printStackTrace();
					}

				}
			}

		}

	}

	public String performKeyphraseExtraction(String textContent, KeyphraseExtractionAlgorithm keAlgo) throws Exception {
		List<WordCount> refinedKeywords = new ArrayList<>();
		List<Keyword> keyphrases = new ArrayList<>();
		List<JATETerm> terms = new ArrayList<JATETerm>();
		switch (keAlgo) {
		case KEY_CLUSTER:
			keyphrases = KeyCluster.performKeyClusterKE(textContent, openNLPImplSpring);
			break;
		case JATE_ATTF:
			terms = Jate.TTFAlgo(textContent, cimtHome);
			break;
		case JATE_CHISQUARE:
			terms = Jate.ChiSquareAlgo(textContent, cimtHome);
			break;
		case JATE_CVALUE:
			terms = Jate.CValueAlgo(textContent, cimtHome);
			break;
		case JATE_GLOSSEX:
			terms = Jate.GlossExAlgo(textContent, cimtHome);
			break;
		case JATE_RAKE:
			terms = Jate.RAKEAlgo(textContent, cimtHome);
			break;
		case JATE_RIDF:
			terms = Jate.RIDFAlgo(textContent, cimtHome);
			break;
		case JATE_TERMEX:
			terms = Jate.TermExAlgo(textContent, cimtHome);
			break;
		case JATE_TTF:
			terms = Jate.TTFAlgo(textContent, cimtHome);
			break;
		case JATE_TFIDF:
			terms = Jate.TFIDFAlgo(textContent, cimtHome);
			break;
		case JATE_WEIRDNESS:
			terms = Jate.WeirdnessAlgo(textContent, cimtHome);
			break;
		case RAKE:
			keyphrases = Rake.extractKeyword(textContent, openNLPImplSpring);
			break;
		case TEXT_RANK:
			keyphrases = TextRank.performTextRankKE(textContent, openNLPImplSpring);
			break;
		case TEXT_RANK_WORDNET:
			keyphrases = TextRankWordnet.extractKeywordTextRankWordnet(textContent, openNLPImplSpring, languageEnglish,
					cimtHome + "/LexSemResources/WordNet3.0", true);
			break;
		case TOPIC_RANK:
			keyphrases = TopicRank.performTopicRankKE(textContent, openNLPImplSpring);
			break;
		case TOPICAL_PAGE_RANK:
			keyphrases = TopicalPageRank.performTopicalPageRankKE(textContent, openNLPImplSpring, cimtHome);
			break;
		default:
			break;
		}

		if (!keyphrases.isEmpty()) {
			Collections.sort(keyphrases, Keyword.KeywordComparatorDesc);

		} else if (!terms.isEmpty()) {
			for (JATETerm term : terms) {
				Keyword keyword = new Keyword(term.getString(), term.getScore());
				keyphrases.add(keyword);
			}
			Collections.sort(keyphrases, Keyword.KeywordComparatorDesc);
		}
		int i = 0;
		for (Keyword token : keyphrases) {
			refinedKeywords.add(new WordCount(token.getToken(), token.getWeight()));
			i++;
			if (i >= 20) {
				break;
			}

		}
		return WordCount.formatIntoString(refinedKeywords);
	}

}
