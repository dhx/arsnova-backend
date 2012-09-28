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
package de.thm.arsnova.socket.message;

import java.util.List;

public class Question {

	private String questionType;
	private String subject;
	private String text;
	private boolean active;
	private String releasedFor;
	private List<PossibleAnswer> possibleAnswers;
	private boolean noCorrect;
	private String session;
	
	public String getQuestionType() {
		return questionType;
	}

	public void setQuestionType(String questionType) {
		this.questionType = questionType;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getReleasedFor() {
		return releasedFor;
	}

	public void setReleasedFor(String releasedFor) {
		this.releasedFor = releasedFor;
	}

	public List<PossibleAnswer> getPossibleAnswers() {
		return possibleAnswers;
	}

	public void setPossibleAnswers(List<PossibleAnswer> possibleAnswers) {
		this.possibleAnswers = possibleAnswers;
	}

	public boolean isNoCorrect() {
		return noCorrect;
	}

	public void setNoCorrect(boolean noCorrect) {
		this.noCorrect = noCorrect;
	}
	
	public String getSession() {
		return session;
	}

	public void setSession(String session) {
		this.session = session;
	}

	@Override
	public String toString() {
		return "Question type '" + this.questionType + "': " + this.subject + ";\n" + this.text;
	}
}