package dk.frankbille.teachus.frontend.pages;

import java.util.ArrayList;
import java.util.List;

import wicket.ResourceReference;
import wicket.ajax.AjaxRequestTarget;
import wicket.ajax.markup.html.AjaxLink;
import wicket.markup.html.WebMarkupContainer;
import wicket.markup.html.basic.Label;
import wicket.markup.repeater.RepeatingView;
import wicket.model.CompoundPropertyModel;
import dk.frankbille.teachus.dao.PeriodDAO;
import dk.frankbille.teachus.domain.Period;
import dk.frankbille.teachus.domain.Periods;
import dk.frankbille.teachus.domain.Teacher;
import dk.frankbille.teachus.domain.impl.PeriodImpl;
import dk.frankbille.teachus.frontend.TeachUsApplication;
import dk.frankbille.teachus.frontend.TeachUsSession;
import dk.frankbille.teachus.frontend.UserLevel;
import dk.frankbille.teachus.frontend.components.RenderingLabel;
import dk.frankbille.teachus.frontend.components.Toolbar;
import dk.frankbille.teachus.frontend.components.Toolbar.ToolbarItem;
import dk.frankbille.teachus.frontend.utils.DateChoiceRenderer;
import dk.frankbille.teachus.frontend.utils.Resources;
import dk.frankbille.teachus.frontend.utils.TimeChoiceRenderer;
import dk.frankbille.teachus.frontend.utils.WeekDayChoiceRenderer;
import dk.frankbille.teachus.frontend.utils.WeekDayChoiceRenderer.Format;

public class PeriodsPage extends AuthenticatedBasePage {
	private static final long serialVersionUID = 1L;

	public PeriodsPage() {
		super(UserLevel.TEACHER, true);
				
		final Teacher teacher = (Teacher) TeachUsSession.get().getPerson();
		PeriodDAO periodDAO = TeachUsApplication.get().getPeriodDAO();
		
		// TOOLBAR
		List<ToolbarItem> items = new ArrayList<ToolbarItem>();
		items.add(new ToolbarItem(TeachUsSession.get().getString("PeriodPage.newPeriod")) { //$NON-NLS-1$
			private static final long serialVersionUID = 1L;

			@Override
			public void onEvent(AjaxRequestTarget target) {
				Period period = new PeriodImpl();
				period.setTeacher(teacher);
				getRequestCycle().setResponsePage(new PeriodPage(period));
			}			
		});
		add(new Toolbar("toolbar", items)); //$NON-NLS-1$
				
		// HEADERS
		add(new Label("name", TeachUsSession.get().getString("General.name"))); //$NON-NLS-1$ //$NON-NLS-2$
		add(new Label("startDate", TeachUsSession.get().getString("General.startDate"))); //$NON-NLS-1$ //$NON-NLS-2$
		add(new Label("endDate", TeachUsSession.get().getString("General.endDate"))); //$NON-NLS-1$ //$NON-NLS-2$
		add(new Label("startTime", TeachUsSession.get().getString("General.startTime"))); //$NON-NLS-1$ //$NON-NLS-2$
		add(new Label("endTime", TeachUsSession.get().getString("General.endTime"))); //$NON-NLS-1$ //$NON-NLS-2$
		add(new Label("weekDays", TeachUsSession.get().getString("General.weekDays"))); //$NON-NLS-1$ //$NON-NLS-2$
		
		// BODY
		RepeatingView rows = new RepeatingView("rows"); //$NON-NLS-1$
		add(rows);
		
		Periods periods = periodDAO.getPeriods(teacher);
		
		for (final Period period : periods.getPeriods()) {
			WebMarkupContainer row = new WebMarkupContainer(rows.newChildId(), new CompoundPropertyModel(period));
			rows.add(row);
			
			AjaxLink link = new AjaxLink("link") { //$NON-NLS-1$
				private static final long serialVersionUID = 1L;

				@Override
				public void onClick(AjaxRequestTarget target) {
					getRequestCycle().setResponsePage(new PeriodPage(period));
				}
			};
			link.add(new Label("name")); //$NON-NLS-1$
			row.add(link);
			DateChoiceRenderer dateChoiceRenderer = new DateChoiceRenderer();
			row.add(new RenderingLabel("beginDate", dateChoiceRenderer)); //$NON-NLS-1$
			row.add(new RenderingLabel("endDate", dateChoiceRenderer)); //$NON-NLS-1$
			TimeChoiceRenderer timeChoiceRenderer = new TimeChoiceRenderer();
			row.add(new RenderingLabel("startTime", timeChoiceRenderer)); //$NON-NLS-1$
			row.add(new RenderingLabel("endTime", timeChoiceRenderer)); //$NON-NLS-1$
			row.add(new RenderingLabel("weekDays", new WeekDayChoiceRenderer(Format.SHORT))); //$NON-NLS-1$
		}
	}

	@Override
	protected ResourceReference getPageIcon() {
		return Resources.PERIOD;
	}

	@Override
	protected String getPageLabel() {
		return TeachUsSession.get().getString("General.periods"); //$NON-NLS-1$
	}

}
