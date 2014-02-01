/*
 * Copyright (C) 2012 THM webMedia
 *
 * This file is part of ARSnova.
 *
 * ARSnova is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ARSnova is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.thm.arsnova.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import de.thm.arsnova.entities.Answer;
import de.thm.arsnova.entities.Question;
import de.thm.arsnova.exceptions.BadRequestException;
import de.thm.arsnova.exceptions.NoContentException;
import de.thm.arsnova.exceptions.NotFoundException;
import de.thm.arsnova.services.IQuestionService;

@Controller
@RequestMapping("/lecturerquestion")
public class LecturerQuestionController extends AbstractController {

	public static final Logger LOGGER = LoggerFactory.getLogger(LecturerQuestionController.class);

	@Autowired
	private IQuestionService questionService;

	@RequestMapping(value = "/{questionId}", method = RequestMethod.GET)
	@ResponseBody
	public final Question getQuestion(
			@PathVariable final String questionId,
			final HttpServletResponse response
	) {
		Question question = questionService.getQuestion(questionId);
		if (question != null) {
			return question;
		}

		throw new NotFoundException();
	}

	@RequestMapping(value = "/", method = RequestMethod.POST)
	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	public final Question postQuestion(@RequestBody final Question question, final HttpServletResponse response) {
		if (questionService.saveQuestion(question) != null) {
			return question;
		}
		throw new BadRequestException();
	}
	
	

	@RequestMapping(value = "/{questionId}", method = RequestMethod.PUT)
	@ResponseBody
	public final Question updateQuestion(
			@PathVariable final String questionId,
			@RequestBody final Question question,
			final HttpServletResponse response
	) {
		return this.questionService.update(question);
	}

	@RequestMapping(value = "/{questionId}/publish", method = RequestMethod.POST)
	@ResponseBody
	public final void publishQuestion(
			@PathVariable final String questionId,
			@RequestParam(required = false) final Boolean publish,
			@RequestBody final Question question,
			final HttpServletResponse response
	) {
		if (publish != null) {
			question.setActive(publish);
		}
		this.questionService.update(question);
	}
	
	@RequestMapping(value = "/publish", method = RequestMethod.POST)
	@ResponseBody
	public final void publishAllQuestions(
			@RequestParam final String sessionkey,
			@RequestParam(required = false) final Boolean publish,
			final HttpServletResponse response
	) {
		boolean p = true;
		if (publish != null) {
			p = publish;
		}
		this.questionService.publishAll(sessionkey, p);
	}

	@RequestMapping(value = "/{questionId}/publishstatistics", method = RequestMethod.POST)
	@ResponseBody
	public final void publishStatistics(
			@PathVariable final String questionId,
			@RequestParam(required = false) final Boolean showStatistics,
			@RequestBody final Question question,
			final HttpServletResponse response
	) {
		if (showStatistics != null) {
			question.setShowStatistic(showStatistics);
		}
		this.questionService.update(question);
	}

	@RequestMapping(value = "/{questionId}/publishcorrectanswer", method = RequestMethod.POST)
	@ResponseBody
	public final void publishCorrectAnswer(
			@PathVariable final String questionId,
			@RequestParam(required = false) final Boolean showCorrectAnswer,
			@RequestBody final Question question,
			final HttpServletResponse response
	) {
		if (showCorrectAnswer != null) {
			question.setShowAnswer(showCorrectAnswer);
		}
		this.questionService.update(question);
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	@ResponseBody
	public final List<Question> getSkillQuestions(
			@RequestParam final String sessionkey,
			@RequestParam(value = "lecturequestionsonly", defaultValue = "false") final boolean lectureQuestionsOnly,
			@RequestParam(value = "flashcardsonly", defaultValue = "false") final boolean flashcardsOnly,
			@RequestParam(value = "preparationquestionsonly", defaultValue = "false") final boolean preparationQuestionsOnly,
			final HttpServletResponse response
	) {
		List<Question> questions;
		if (lectureQuestionsOnly) {
			questions = questionService.getLectureQuestions(sessionkey);
		} else if (flashcardsOnly) {
			questions = questionService.getFlashcards(sessionkey);
		} else if (preparationQuestionsOnly) {
			questions = questionService.getPreparationQuestions(sessionkey);
		} else {
			questions = questionService.getSkillQuestions(sessionkey);
		}
		if (questions == null || questions.isEmpty()) {
			response.setStatus(HttpStatus.NO_CONTENT.value());
			return null;
		}
		return questions;
	}

	@RequestMapping(value = { "/" }, method = RequestMethod.DELETE)
	@ResponseBody
	public final void deleteSkillQuestions(
			@RequestParam final String sessionkey,
			@RequestParam(value = "lecturequestionsonly", defaultValue = "false") final boolean lectureQuestionsOnly,
			@RequestParam(value = "flashcardsonly", defaultValue = "false") final boolean flashcardsOnly,
			@RequestParam(value = "preparationquestionsonly", defaultValue = "false") final boolean preparationQuestionsOnly,
			final HttpServletResponse response
	) {
		if (lectureQuestionsOnly) {
			this.questionService.deleteLectureQuestions(sessionkey);
		} else if (flashcardsOnly) {
			this.questionService.deleteFlashcards(sessionkey);
		} else if (preparationQuestionsOnly) {
			this.questionService.deletePreparationQuestions(sessionkey);
		} else {
			this.questionService.deleteAllQuestions(sessionkey);
		}
	}

	@RequestMapping(value = "/count", method = RequestMethod.GET)
	@ResponseBody
	public final int getSkillQuestionCount(
			@RequestParam final String sessionkey,
			@RequestParam(value = "lecturequestionsonly", defaultValue = "false") final boolean lectureQuestionsOnly,
			@RequestParam(value = "flashcardsonly", defaultValue = "false") final boolean flashcardsOnly,
			@RequestParam(value = "preparationquestionsonly", defaultValue = "false") final boolean preparationQuestionsOnly,
			final HttpServletResponse response) {
		response.addHeader("X-Deprecated-API", "1");

		if (lectureQuestionsOnly) {
			return questionService.getLectureQuestionCount(sessionkey);
		} else if (flashcardsOnly) {
			return questionService.getFlashcardCount(sessionkey);
		} else if (preparationQuestionsOnly) {
			return questionService.getPreparationQuestionCount(sessionkey);
		} else {
			return questionService.getSkillQuestionCount(sessionkey);
		}
	}

	@RequestMapping(value = "/{questionId}", method = RequestMethod.DELETE)
	@ResponseBody
	public final void deleteAnswersAndQuestion(
			@PathVariable final String questionId,
			final HttpServletResponse response
	) {
		questionService.deleteQuestion(questionId);
	}

	@RequestMapping(value = "/unanswered", method = RequestMethod.GET)
	@ResponseBody
	public final List<String> getUnAnsweredSkillQuestionIds(
			@RequestParam final String sessionkey,
			@RequestParam(value = "lecturequestionsonly", defaultValue = "false") final boolean lectureQuestionsOnly,
			@RequestParam(value = "preparationquestionsonly", defaultValue = "false") final boolean preparationQuestionsOnly,
			final HttpServletResponse response
	) {
		List<String> answers;
		if (lectureQuestionsOnly) {
			answers = questionService.getUnAnsweredLectureQuestionIds(sessionkey);
		} else if (preparationQuestionsOnly) {
			answers = questionService.getUnAnsweredPreparationQuestionIds(sessionkey);
		} else {
			answers = questionService.getUnAnsweredQuestionIds(sessionkey);
		}
		if (answers == null || answers.isEmpty()) {
			throw new NoContentException();
		}
		response.addHeader("X-Deprecated-API", "1");

		return answers;
	}

	/**
	 * returns a JSON document which represents the given answer of a question.
	 *
	 * @param sessionKey
	 *            Session Keyword to which the question belongs to
	 * @param questionId
	 *            CouchDB Question ID for which the given answer should be
	 *            retrieved
	 * @return JSON Document of {@link Answer} or {@link NotFoundException}
	 * @throws NotFoundException
	 *             if wrong session, wrong question or no answer was given by
	 *             the current user
	 * @throws ForbiddenException
	 *             if not logged in
	 */
	@RequestMapping(value = "/{questionId}/myanswer", method = RequestMethod.GET)
	@ResponseBody
	public final Answer getMyAnswer(
			@PathVariable final String questionId,
			final HttpServletResponse response
	) {
		Answer answer = questionService.getMyAnswer(questionId);
		if (answer == null) {
			response.setStatus(HttpStatus.NO_CONTENT.value());
			return null;
		}

		response.addHeader("X-Deprecated-API", "1");

		return answer;
	}

	/**
	 * returns a list of {@link Answer}s encoded as a JSON document for a given
	 * question id. In this case only {@link Answer} <tt>questionId</tt>,
	 * <tt>answerText</tt>, <tt>answerSubject</tt> and <tt>answerCount</tt>
	 * properties are set
	 *
	 * @param sessionKey
	 *            Session Keyword to which the question belongs to
	 * @param questionId
	 *            CouchDB Question ID for which the given answers should be
	 *            retrieved
	 * @return List<{@link Answer}> or {@link NotFoundException}
	 * @throws NotFoundException
	 *             if wrong session, wrong question or no answers was given
	 * @throws ForbiddenException
	 *             if not logged in
	 */
	@RequestMapping(value = "/{questionId}/answer/", method = RequestMethod.GET)
	@ResponseBody
	public final List<Answer> getAnswers(
			@PathVariable final String questionId,
			@RequestParam(value = "piround", required = false) final Integer piRound,
			final HttpServletResponse response
	) {
		List<Answer> answers = null;
		if (null == piRound) {
			answers = questionService.getAnswers(questionId);
		} else {
			if (piRound < 1 || piRound > 2) {
				response.setStatus(HttpStatus.BAD_REQUEST.value());

				return null;
			}
			answers = questionService.getAnswers(questionId, piRound);
		}
		if (answers == null) {
			return new ArrayList<Answer>();
		}
		return answers;
	}

	@RequestMapping(value = "/{questionId}/answer/", method = RequestMethod.POST)
	@ResponseBody
	public final Answer saveAnswer(
			@PathVariable final String questionId,
			@RequestBody final Answer answer,
			final HttpServletResponse response
		) {
		return questionService.saveAnswer(answer);
	}

	@RequestMapping(value = "/{questionId}/answer/{answerId}", method = RequestMethod.PUT)
	@ResponseBody
	public final Answer updateAnswer(
			@PathVariable final String questionId,
			@PathVariable final String answerId,
			@RequestBody final Answer answer,
			final HttpServletResponse response
		) {
		return questionService.updateAnswer(answer);
	}

	@RequestMapping(value = "/{questionId}/answer/{answerId}", method = RequestMethod.DELETE)
	@ResponseBody
	public final void deleteAnswer(
			@PathVariable final String questionId,
			@PathVariable final String answerId,
			final HttpServletResponse response
		) {
		questionService.deleteAnswer(questionId, answerId);
	}

	@RequestMapping(value = "/{questionId}/answer/", method = RequestMethod.DELETE)
	@ResponseBody
	public final void deleteAnswers(
			@PathVariable final String questionId,
			final HttpServletResponse response
	) {
		questionService.deleteAnswers(questionId);
	}

	@RequestMapping(value = "/answers", method = RequestMethod.DELETE)
	@ResponseBody
	public final void deleteAllQuestionsAnswers(
			@RequestParam final String sessionkey,
			final HttpServletResponse response
	) {
		questionService.deleteAllQuestionsAnswers(sessionkey);
	}

	/**
	 *
	 * @param sessionKey
	 *            Session Keyword to which the question belongs to
	 * @param questionId
	 *            CouchDB Question ID for which the given answers should be
	 *            retrieved
	 * @return count of answers for given question id
	 * @throws NotFoundException
	 *             if wrong session or wrong question
	 * @throws ForbiddenException
	 *             if not logged in
	 */
	@RequestMapping(value = "/{questionId}/answercount", method = RequestMethod.GET)
	@ResponseBody
	public final int getAnswerCount(
			@PathVariable final String questionId,
			final HttpServletResponse response
	) {
		response.addHeader("X-Deprecated-API", "1");

		return questionService.getAnswerCount(questionId);
	}

	@RequestMapping(value = "/{questionId}/freetextanswer/", method = RequestMethod.GET)
	@ResponseBody
	public final List<Answer> getFreetextAnswers(
			@PathVariable final String questionId,
			final HttpServletResponse response
	) {
		return questionService.getFreetextAnswers(questionId);
	}

	@RequestMapping(value = "/myanswers", method = RequestMethod.GET)
	@ResponseBody
	public final List<Answer> getMyAnswers(
			@RequestParam final String sessionkey,
			final HttpServletResponse response
	) {
		response.addHeader("X-Deprecated-API", "1");

		return questionService.getMyAnswers(sessionkey);
	}

	@RequestMapping(value = "/answercount", method = RequestMethod.GET)
	@ResponseBody
	public final int getTotalAnswerCount(
			@RequestParam final String sessionkey,
			@RequestParam(value = "lecturequestionsonly", defaultValue = "false") final boolean lectureQuestionsOnly,
			@RequestParam(value = "preparationquestionsonly", defaultValue = "false") final boolean preparationQuestionsOnly,
			final HttpServletResponse response
	) {
		response.addHeader("X-Deprecated-API", "1");

		if (lectureQuestionsOnly) {
			return questionService.countLectureQuestionAnswers(sessionkey);
		} else if (preparationQuestionsOnly) {
			return questionService.countPreparationQuestionAnswers(sessionkey);
		} else {
			return questionService.getTotalAnswerCount(sessionkey);
		}
	}

}
