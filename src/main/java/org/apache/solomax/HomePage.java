package org.apache.solomax;

import java.util.List;

import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.resource.JQueryResourceReference;

public class HomePage extends WebPage {
	private static final long serialVersionUID = 1L;

	public HomePage(final PageParameters parameters) {
		super(parameters);
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();
		add(new Label("version", getApplication().getFrameworkSettings().getVersion()));
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(HomePage.class, "upload.js") {
			private static final long serialVersionUID = 1L;

			@Override
			public List<HeaderItem> getDependencies() {
				List<HeaderItem> list = super.getDependencies();
				list.add(JavaScriptHeaderItem.forReference(JQueryResourceReference.getV3()));
				return list;
			}
		}));
	}
}
