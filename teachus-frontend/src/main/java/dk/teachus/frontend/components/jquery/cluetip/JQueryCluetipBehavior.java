package dk.teachus.frontend.components.jquery.cluetip;

import org.apache.wicket.Component;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.resources.JavascriptResourceReference;
import org.apache.wicket.model.Model;

import dk.teachus.frontend.components.jquery.dimensions.JQueryDimensionsBehavior;

public class JQueryCluetipBehavior extends JQueryDimensionsBehavior {
	private static final long serialVersionUID = 1L;
	
	public static final ResourceReference JS_CLUETIP_JQUERY = new JavascriptResourceReference(JQueryCluetipBehavior.class, "jquery.cluetip-0.9.6.min.js"); //$NON-NLS-1$
	
	@Override
	public void onRenderHead(IHeaderResponse response) {
		super.onRenderHead(response);
		
		response.renderJavascriptReference(JS_CLUETIP_JQUERY);
		
		StringBuilder tipConf = new StringBuilder();
		tipConf.append("$('.tooltip').cluetip({splitTitle: '|'})");
		
		response.renderOnDomReadyJavascript(tipConf.toString());
	}
	
	@Override
	public void bind(Component component) {
		component.add(new AttributeAppender("class", true, new Model("tooltip"), " "));
	}
	
}