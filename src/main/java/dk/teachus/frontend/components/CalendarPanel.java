package dk.teachus.frontend.components;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.DateMidnight;
import org.joda.time.DateTimeConstants;
import org.joda.time.format.DateTimeFormatter;

import wicket.behavior.SimpleAttributeModifier;
import wicket.extensions.yui.calendar.DateField;
import wicket.markup.html.WebMarkupContainer;
import wicket.markup.html.basic.Label;
import wicket.markup.html.form.Button;
import wicket.markup.html.form.Form;
import wicket.markup.html.image.Image;
import wicket.markup.html.link.Link;
import wicket.markup.html.panel.Panel;
import wicket.markup.repeater.RepeatingView;
import wicket.model.Model;
import wicket.model.PropertyModel;
import dk.teachus.domain.DatePeriod;
import dk.teachus.domain.Period;
import dk.teachus.domain.Periods;
import dk.teachus.frontend.TeachUsSession;
import dk.teachus.frontend.utils.Formatters;
import dk.teachus.frontend.utils.Resources;

public abstract class CalendarPanel extends Panel {
	private static final long serialVersionUID = 1L;
	
	public CalendarPanel(String wicketId, DateMidnight pageDate, final Periods periods) {
		super(wicketId);
		
		// Status message
		final boolean showCalendar = periods.getPeriods().isEmpty() == false;
		
		Label statusMessage = new Label("statusMessage", TeachUsSession.get().getString("General.noPeriodsConfigured")) { //$NON-NLS-1$ //$NON-NLS-2$
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible() {
				return showCalendar == false;
			}
		};
		add(statusMessage);
		
		
		// Calendar
		WebMarkupContainer calendar = new WebMarkupContainer("calendar") { //$NON-NLS-1$
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible() {
				return showCalendar;
			}
		};
		add(calendar);
		
		if (showCalendar) {
			// Navigation form
			Form navigationForm = new Form("navigationForm");
			calendar.add(navigationForm);
			
			final NavigationModel navigationModel = new NavigationModel(pageDate.toDate());
			navigationForm.add(new DateField("date", new PropertyModel(navigationModel, "date")));
			navigationForm.add(new Button("goto", new Model("Gå til")) {
				private static final long serialVersionUID = 1L;

				@Override
				public void onSubmit() {
					navigationDateSelected(new DateMidnight(navigationModel.getDate()));
				}
			});
			
			
			// Calendar
			DateMidnight weekDate = pageDate.withDayOfWeek(DateTimeConstants.MONDAY);
			final DateMidnight firstWeekDate = weekDate;
			List<DatePeriod> dates = new ArrayList<DatePeriod>();
			List<DatePeriod> weekDates = periods.generateDatesForWeek(weekDate.toDate());
			do {
				dates.addAll(weekDates);
				weekDate = weekDate.plusWeeks(1);
				weekDates = periods.generateDatesForWeek(weekDate.toDate());
			} while(dates.size()+weekDates.size() <= 7);
						
			fireIntervalDetermined(dates);
			
			// Weeks
			RepeatingView weeks = new RepeatingView("weeks"); //$NON-NLS-1$
			calendar.add(weeks);
			
			int w = -1;
			RepeatingView days = null;
			DateTimeFormatter formatWeekOfYear = Formatters.getFormatWeekOfYear();
			Label weekHeader = null;
			int daysInWeek = 0;
			for (DatePeriod datePeriod : dates) {
				Date date = datePeriod.getDate();
				DateMidnight dm = new DateMidnight(date);
				
				if (dm.getWeekOfWeekyear() != w) {
					if (weekHeader != null) {
						weekHeader.add(new SimpleAttributeModifier("colspan", ""+daysInWeek)); //$NON-NLS-1$ //$NON-NLS-2$
					}
					daysInWeek = 0;
					
					WebMarkupContainer week = new WebMarkupContainer(weeks.newChildId());
					weeks.add(week);
					
					weekHeader = new Label("weekHeader", formatWeekOfYear.print(dm)); //$NON-NLS-1$
					week.add(weekHeader);
					
					days = new RepeatingView("days"); //$NON-NLS-1$
					week.add(days);
					
					w = dm.getWeekOfWeekyear();
				}
				
				WebMarkupContainer day = new WebMarkupContainer(days.newChildId());
				days.add(day);
				
				// Periods
				RepeatingView periodsView = new RepeatingView("periods"); //$NON-NLS-1$
				day.add(periodsView);
				
				for (Period period : datePeriod.getPeriods()) {
					WebMarkupContainer periodCell = new WebMarkupContainer(periodsView.newChildId());
					periodsView.add(periodCell);
					
					periodCell.add(createPeriodDateComponent("period", period, date)); //$NON-NLS-1$
				}
				
				daysInWeek++;
			}
			
			if (weekHeader != null) {
				weekHeader.add(new SimpleAttributeModifier("colspan", ""+daysInWeek)); //$NON-NLS-1$ //$NON-NLS-2$
			}
			
					
			// Navigation
			Link backLink = createBackLink("backLink", firstWeekDate.minusWeeks(periods.calculateNumberOfWeeks(firstWeekDate.minusDays(1).toDate(), 7))); //$NON-NLS-1$
			backLink.add(new Image("backIcon", Resources.LEFT)); //$NON-NLS-1$ //$NON-NLS-2$
			calendar.add(backLink);
			
			Link forwardLink = createForwardLink("forwardLink", weekDate); //$NON-NLS-1$
			forwardLink.add(new Image("forwardIcon", Resources.RIGHT)); //$NON-NLS-1$ //$NON-NLS-2$
			calendar.add(forwardLink);
		}
	}

	protected abstract Link createBackLink(String wicketId, DateMidnight previousWeekDate);
	
	protected abstract Link createForwardLink(String wicketId, DateMidnight nextWeekDate);
	
	protected abstract PeriodDateComponent createPeriodDateComponent(String wicketId, Period period, Date date);
	
	protected abstract void navigationDateSelected(DateMidnight date);
	
	protected void onIntervalDetermined(List<DatePeriod> dates, DateMidnight firstDate, DateMidnight lastDate) {
	}
	
	private void fireIntervalDetermined(List<DatePeriod> dates) {
		// Find the minimum and maximum date
		DateMidnight firstDate = null;
		DateMidnight lastDate = null;
		
		for (DatePeriod datePeriod : dates) {
			DateMidnight date = new DateMidnight(datePeriod.getDate());
			
			if (firstDate == null) {
				firstDate = date;
			} else {
				if (firstDate.isAfter(date)) {
					firstDate = date;
				}
			}
			
			if (lastDate == null) {
				lastDate = date;
			} else {
				if (lastDate.isBefore(date)) {
					lastDate = date;
				}
			}
		}
		
		onIntervalDetermined(dates, firstDate, lastDate);
	}
		
	private static class NavigationModel implements Serializable {
		private static final long serialVersionUID = 1L;

		private Date date;

		public NavigationModel(Date date) {
			this.date = date;
		}

		public Date getDate() {
			return date;
		}

		public void setDate(Date date) {
			this.date = date;
		}
	}

}