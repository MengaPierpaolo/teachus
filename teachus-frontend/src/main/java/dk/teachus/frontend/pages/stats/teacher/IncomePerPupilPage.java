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
package dk.teachus.frontend.pages.stats.teacher;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.jfree.data.general.DefaultPieDataset;

import wicket.extensions.markup.html.repeater.data.table.DataTable;
import wicket.extensions.markup.html.repeater.data.table.HeadersToolbar;
import wicket.extensions.markup.html.repeater.data.table.IColumn;
import wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import wicket.extensions.yui.calendar.DateField;
import wicket.markup.html.basic.Label;
import wicket.markup.html.form.Button;
import wicket.markup.html.form.Form;
import wicket.markup.repeater.data.ListDataProvider;
import wicket.model.CompoundPropertyModel;
import wicket.model.Model;
import dk.teachus.backend.dao.BookingDAO;
import dk.teachus.backend.domain.Pupil;
import dk.teachus.backend.domain.PupilBooking;
import dk.teachus.frontend.TeachUsApplication;
import dk.teachus.frontend.TeachUsSession;
import dk.teachus.frontend.components.RendererPropertyColumn;
import dk.teachus.frontend.components.jfreechart.JFreeChartImage;
import dk.teachus.frontend.components.jfreechart.PieChartResource;
import dk.teachus.frontend.utils.CurrencyChoiceRenderer;
import dk.teachus.frontend.utils.PercentChoiceRenderer;

public class IncomePerPupilPage extends AbstractTeacherStatisticsPage {
	private static final long serialVersionUID = 1L;

	private Date startDate;
	
	private Date endDate;
	
	public IncomePerPupilPage() {
		this(null, null);
	}
	
	public IncomePerPupilPage(Date startDate, Date endDate) {
		this.startDate = startDate;
		this.endDate = endDate;
		
		Form form = new Form("form", new CompoundPropertyModel(this)); //$NON-NLS-1$
		add(form);
		
		form.add(new Label("startDateLabel", TeachUsSession.get().getString("General.startDate"))); //$NON-NLS-1$ //$NON-NLS-2$
		form.add(new DateField("startDate")); //$NON-NLS-1$
		
		form.add(new Label("endDateLabel", TeachUsSession.get().getString("General.endDate"))); //$NON-NLS-1$ //$NON-NLS-2$
		form.add(new DateField("endDate")); //$NON-NLS-1$
		
		form.add(new Button("execute", new Model(TeachUsSession.get().getString("IncomePerPupilPage.execute"))) { //$NON-NLS-1$ //$NON-NLS-2$
			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit() {
				getRequestCycle().setResponsePage(new IncomePerPupilPage(getStartDate(), getEndDate()));
			}
		});
		
		createPercentDistribution();
	}

	private void createPercentDistribution() {
		add(new Label("pctDistribution", TeachUsSession.get().getString("IncomePerPupilPage.percentageDistribution"))); //$NON-NLS-1$ //$NON-NLS-2$
		
		BookingDAO bookingDAO = TeachUsApplication.get().getBookingDAO();
		List<PupilBooking> bookings = bookingDAO.getPaidBookings(getPerson(), startDate, endDate);
		
		// Build the dataset
		List<PupilSummary> sumList = new ArrayList<PupilSummary>();
		double total = 0;
		for (PupilBooking booking : bookings) {
			PupilSummary pupilSummary = new PupilSummary(booking.getPupil());
			if (sumList.contains(pupilSummary)) {
				pupilSummary = sumList.get(sumList.indexOf(pupilSummary));
			} else {
				sumList.add(pupilSummary);
			}
			
			total += booking.getPeriod().getPrice();
			pupilSummary.addAmount(booking.getPeriod().getPrice());
		}
		for (PupilSummary summary : sumList) {
			summary.calculatePercent(total);
		}
		
		Collections.sort(sumList, new PupilSummaryComparator());
		
		// Sheet
		IColumn[] columns = new IColumn[] {
				new PropertyColumn(new Model(TeachUsSession.get().getString("General.pupil")), "pupil.name"), //$NON-NLS-1$ //$NON-NLS-2$
				new RendererPropertyColumn(new Model(TeachUsSession.get().getString("General.total")), "total", new CurrencyChoiceRenderer()), //$NON-NLS-1$ //$NON-NLS-2$
				new RendererPropertyColumn(new Model(TeachUsSession.get().getString("General.percent")), "percent", new PercentChoiceRenderer()) //$NON-NLS-1$ //$NON-NLS-2$
		};
		
		DataTable pctDistributionSheet = new DataTable("pctDistributionSheet", columns, new ListDataProvider(sumList), 20); //$NON-NLS-1$
		pctDistributionSheet.addTopToolbar(new HeadersToolbar(pctDistributionSheet, null));
		add(pctDistributionSheet);
		
		// Chart
		DefaultPieDataset pieDataset = new DefaultPieDataset();
		for (PupilSummary summary : sumList) {
			pieDataset.setValue(summary.getPupil().getName(), summary.getTotal());
		}
		
		add(new JFreeChartImage("pctDistributionChart", new PieChartResource(600, 300, pieDataset))); //$NON-NLS-1$
	}

	@Override
	protected String getPageLabel() {
		return TeachUsSession.get().getString("General.incomePerPupil"); //$NON-NLS-1$
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	private static class PupilSummary implements Serializable {
		private static final long serialVersionUID = 1L;

		private double total;

		private Pupil pupil;
		
		private double percent;

		public PupilSummary(Pupil pupil) {
			this.pupil = pupil;
		}

		public Pupil getPupil() {
			return pupil;
		}

		public double getTotal() {
			return total;
		}

		public void setTotal(double total) {
			this.total = total;
		}

		public void addAmount(double amount) {
			total += amount;
		}

		public double getPercent() {
			return percent;
		}
		
		public void calculatePercent(double overAllTotal) {
			percent = total / overAllTotal;
		}

		@Override
		public boolean equals(Object o) {
			boolean equals = false;

			if (this == o) {
				equals = true;
			} else if (o != null) {
				if (o instanceof PupilSummary) {
					PupilSummary pupilSummary = (PupilSummary) o;

					if (pupilSummary.getPupil() == getPupil()) {
						equals = true;
					}
				}
			}

			return equals;
		}
	}
	
	private static class PupilSummaryComparator implements Comparator<PupilSummary> {
		public int compare(PupilSummary o1, PupilSummary o2) {
			int compare = 0;
			
			if (o1 != null && o2 != null) {
				compare = new Double(o2.getTotal()).compareTo(o1.getTotal());
				if (compare == 0) {
					if (o1.getPupil() != null && o2.getPupil() != null) {
						compare = o1.getPupil().getName().compareTo(o2.getPupil().getName());
					} else if (o1.getPupil() != null) {
						compare = -1;
					} else if (o2.getPupil() != null) {
						compare = 1;
					}
				}
			} else if (o1 != null) {
				compare = -1;
			} else if (o2 != null) {
				compare = 1;
			}
			
			return compare;
		}
	}
	
}
