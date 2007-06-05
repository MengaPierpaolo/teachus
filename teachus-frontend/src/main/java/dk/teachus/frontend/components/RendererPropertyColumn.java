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
package dk.teachus.frontend.components;

import wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import wicket.markup.html.form.IChoiceRenderer;
import wicket.markup.repeater.Item;
import wicket.model.IModel;
import wicket.model.PropertyModel;

public class RendererPropertyColumn extends AbstractColumn {
	private static final long serialVersionUID = 1L;
	
	private IChoiceRenderer renderer;
	private String propertyExpressions;

	public RendererPropertyColumn(IModel displayModel, String propertyExpressions) {
		super(displayModel);
		this.propertyExpressions = propertyExpressions;
	}
	
	public RendererPropertyColumn(IModel displayModel, String propertyExpressions, IChoiceRenderer renderer) {
		super(displayModel);
		this.renderer = renderer;
		this.propertyExpressions = propertyExpressions;
	}

	public void populateItem(Item cellItem, String componentId, IModel rowModel) {
		RenderingLabel renderingLabel = new RenderingLabel(componentId, new PropertyModel(rowModel, propertyExpressions), renderer);
		renderingLabel.setRenderBodyOnly(true);
		cellItem.add(renderingLabel);
	}
}
