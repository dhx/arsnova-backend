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
package de.thm.arsnova;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import de.thm.arsnova.entities.Session;
import de.thm.arsnova.services.ISessionService;

@Controller
public class SessionController {
	
	@Autowired
	ISessionService sessionService;
	
	@RequestMapping(value="/session/{sessionkey}", method=RequestMethod.GET)
	public Session getSession(@PathVariable String sessionkey, HttpServletResponse response) {
		Session session = sessionService.getSession(sessionkey);
		if (session != null) return session;
		
		response.setStatus(HttpStatus.NOT_FOUND.value());
		return null;
	}
	
	@RequestMapping(value="/session", method=RequestMethod.POST)
	public Session postNewSession(@RequestBody Session session, HttpServletResponse response) {
		Session newSession = sessionService.saveSession(session);
		if (session != null) {
			response.setStatus(HttpStatus.CREATED.value());
			return newSession;
		}

		response.setStatus(HttpStatus.SERVICE_UNAVAILABLE.value());
		return null;
	}
}
