package dk.teachus.frontend.models;

import dk.teachus.domain.Admin;
import dk.teachus.frontend.TeachUsApplication;

public class AdminModel extends PersonModel<Admin> {
	private static final long serialVersionUID = 1L;

	public AdminModel(Long personId) {
		super(personId);
	}

	@Override
	protected Admin createNewPerson() {
		return TeachUsApplication.get().getPersonDAO().createAdminObject();
	}

}
