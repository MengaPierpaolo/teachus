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
package dk.teachus.frontend.pages;

import java.util.List;

import org.joda.time.DateMidnight;

import wicket.behavior.HeaderContributor;
import wicket.behavior.SimpleAttributeModifier;
import wicket.markup.html.WebMarkupContainer;
import wicket.markup.html.WebPage;
import wicket.markup.html.basic.Label;
import wicket.markup.html.link.BookmarkablePageLink;
import wicket.markup.html.link.Link;
import wicket.markup.repeater.RepeatingView;
import dk.teachus.backend.domain.Theme;
import dk.teachus.frontend.TeachUsApplication;
import dk.teachus.frontend.TeachUsSession;
import dk.teachus.frontend.utils.Resources;

public abstract class BasePage extends WebPage {	
	
	public static interface PageCategory {
	}

	boolean attached = false;
	
	public BasePage() {
		add(HeaderContributor.forCss(Resources.CSS_ANDREAS09));
		add(HeaderContributor.forCss(Resources.CSS_SCREEN));
		add(HeaderContributor.forCss(Resources.CSS_PRINT, "print"));
		
		Theme theme = getTheme();
		
		if (theme == null) {
			theme = Theme.BLUE;
		}
		
		setTheme(theme);
		
		StringBuilder sb = new StringBuilder(TeachUsSession.get().getString("General.teachUsTitle")); //$NON-NLS-1$
		sb.append(" "); //$NON-NLS-1$
		sb.append(TeachUsApplication.get().getVersion());
		add(new Label("title", sb.toString())); //$NON-NLS-1$
		
		createMenu();		
		
		add(new Label("copyright", "2006-"+new DateMidnight().getYear()+" TeachUs Booking Systems"));
	}
	
	private void setTheme(Theme theme) {
		switch (theme) {
			case BLUE:
				break;
			case RED:
				add(HeaderContributor.forCss(Resources.CSS_ANDREAS09_RED));
				break;
			case ORANGE:
				add(HeaderContributor.forCss(Resources.CSS_ANDREAS09_ORANGE));
				break;
			case BLACK:
				add(HeaderContributor.forCss(Resources.CSS_ANDREAS09_BLACK));
				break;
			case GREEN:
				add(HeaderContributor.forCss(Resources.CSS_ANDREAS09_GREEN));
				break;
			case PURPLE:
				add(HeaderContributor.forCss(Resources.CSS_ANDREAS09_PURPLE));
				break;
		}
	}

	private void createMenu() {
		RepeatingView menuItems = new RepeatingView("menuItems"); //$NON-NLS-1$
		add(menuItems);
		
		List<MenuItem> menuItemsList = createMenuItems();
		if (menuItemsList != null) {
			for (MenuItem menuItem : menuItemsList) {
				WebMarkupContainer menuItemContainer = new WebMarkupContainer(menuItems.newChildId());
				menuItems.add(menuItemContainer);
				
				Link menuLink = new BookmarkablePageLink("menuLink", menuItem.getBookmarkablePage());
				menuItemContainer.add(menuLink);
				
				if (menuItem.getPageCategory().equals(getPageCategory())) {
					menuLink.add(new SimpleAttributeModifier("class", "current"));
				}
				
				menuLink.add(new Label("menuLabel", menuItem.getHelpText()));
			}
		}
	}

	@Override
	protected final void onAttach() {
		if (attached == false) {
			add(new Label("pageLabel", getPageLabel())); //$NON-NLS-1$
			attached = true;
		}
		
		onAttach2();
	}

	protected void onAttach2() {
	}
	
	protected Theme getTheme() {
		return TeachUsApplication.get().getDefaultTheme();
	}

	protected abstract String getPageLabel();

	protected abstract PageCategory getPageCategory();
	
	protected abstract List<MenuItem> createMenuItems();
}
