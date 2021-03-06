/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dk.teachus.frontend.components.person;

import dk.teachus.backend.domain.Person;
import dk.teachus.frontend.models.AdminModel;
import dk.teachus.frontend.pages.persons.AdminsPage;

public class AdminPanel extends PersonPanel {
	private static final long serialVersionUID = 1L;

	public AdminPanel(String id, AdminModel adminModel) {
		super(id, adminModel);
	}
	
	@Override
	protected Class<AdminsPage> getPersonsPageClass() {
		return AdminsPage.class;
	}

	@Override
	protected boolean allowUserEditing(Person loggedInPerson, Person editPerson) {
		return true;
	}

}
