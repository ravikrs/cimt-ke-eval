package de.rwth.i9.cimt.ke.eval.controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import de.rwth.i9.cimt.ke.eval.service.AuthorInterestExtractorService;
import de.rwth.i9.cimt.ke.eval.service.KEExtractionService;
import de.rwth.i9.cimt.ke.eval.service.SqlCorpusImporter;
import de.rwth.i9.cimt.ke.lib.model.Textbody;

@Configuration
@RestController
public class HomeController {

	private static final Logger log = LoggerFactory.getLogger(HomeController.class);

	@Autowired
	private SqlCorpusImporter sqlCorpusImporter;
	@Autowired
	private KEExtractionService keExtractionService;
	@Autowired
	AuthorInterestExtractorService authorInterestExtractorService;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView getKE(Model model) {
		log.info("Inside the getKPTR");
		model.addAttribute("textbody", new Textbody());
		try {
			sqlCorpusImporter.runCorpusImporter();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ModelAndView("home", "model", "objectName");
	}

	@RequestMapping(value = "/ke", method = RequestMethod.GET)
	public ModelAndView getKE1(Model model) {
		log.info("Inside the getKPTR");
		model.addAttribute("textbody", new Textbody());
		keExtractionService.runKEAlgorithm();
		return new ModelAndView("home", "model", "objectName");
	}

	@RequestMapping(value = "/ke1", method = RequestMethod.GET)
	public String getKE21() {
		authorInterestExtractorService.performInterestMingingForAllAuthors();
		return "done";
	}

}
