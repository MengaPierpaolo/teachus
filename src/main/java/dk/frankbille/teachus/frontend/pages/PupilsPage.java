package dk.frankbille.teachus.frontend.pages;

import java.util.ArrayList;
import java.util.List;

import wicket.ResourceReference;
import dk.frankbille.teachus.dao.PersonDAO;
import dk.frankbille.teachus.domain.Admin;
import dk.frankbille.teachus.domain.Person;
import dk.frankbille.teachus.domain.Pupil;
import dk.frankbille.teachus.domain.Teacher;
import dk.frankbille.teachus.domain.impl.PupilImpl;
import dk.frankbille.teachus.frontend.TeachUsApplication;
import dk.frankbille.teachus.frontend.TeachUsSession;
import dk.frankbille.teachus.frontend.UserLevel;
import dk.frankbille.teachus.frontend.utils.Icons;

public class PupilsPage extends PersonsPage<Pupil> {
	private static final long serialVersionUID = 1L;
	
	public PupilsPage() {
		super(UserLevel.TEACHER);
	}

	@Override
	protected ResourceReference getPageIcon() {
		return Icons.PUPIL;
	}

	@Override
	protected String getPageLabel() {
		return TeachUsSession.get().getString("General.pupils"); //$NON-NLS-1$
	}

	@Override
	protected List<Pupil> getPersons() {
		List<Pupil> persons = null;
		
		PersonDAO personDAO = TeachUsApplication.get().getPersonDAO();
		Person person = TeachUsSession.get().getPerson();
		if (person instanceof Teacher) {
			persons = personDAO.getPupils((Teacher) person);
		} else if (person instanceof Admin) {
			persons = personDAO.getPersons(Pupil.class);
		}
			
		return persons;
	}

	@Override
	protected Pupil getNewPerson() {
		Person person = TeachUsSession.get().getPerson();
		Teacher teacher = (Teacher) person;
		Pupil pupil = new PupilImpl();
		pupil.setTeacher(teacher);
		return pupil;
	}

	@Override
	protected String getNewPersonLabel() {
		return TeachUsSession.get().getString("PupilsPage.newPupil"); //$NON-NLS-1$
	}

	@Override
	protected boolean showNewPersonLink() {
		boolean showNewPersonLink = false;
		
		Person person = TeachUsSession.get().getPerson();
		if (person instanceof Teacher) {
			showNewPersonLink = true;
		}
		
		return showNewPersonLink;
	}
	
	@Override
	protected PersonPage getPersonPage(Pupil person) {
		return new PupilPage(person);
	}

	@Override
	protected List<FunctionItem> getFunctions() {
		List<FunctionItem> functions = new ArrayList<FunctionItem>();
		
		functions.add(new FunctionItem(TeachUsSession.get().getString("General.calendar")) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onEvent(Pupil pupil) {
				getRequestCycle().setResponsePage(new PupilCalendarPage(pupil));
			}			
		});
		
		return functions;
	}
	
}
