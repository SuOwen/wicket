/*
 * $Id$ $Revision:
 * 1.1 $ $Date$
 * 
 * ==================================================================== Licensed
 * under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the
 * License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package wicket.extensions.markup.html.tabs;

import java.util.List;

import wicket.AttributeModifier;
import wicket.WicketRuntimeException;
import wicket.markup.html.basic.Label;
import wicket.markup.html.link.Link;
import wicket.markup.html.list.Loop;
import wicket.markup.html.panel.Panel;
import wicket.model.Model;

/**
 * TabbedPanel component represets a panel with tabs that are used to switch
 * between different content panels inside the TabbedPanel panel.
 * <p>
 * Example:
 * 
 * <pre>
 *                
 *                List tabs=new ArrayList();
 *                
 *                tabs.add(new AbstractTab(new Model(&quot;first tab&quot;)) {
 *               
 *                public Panel getPanel(String panelId)
 *                {
 *                return new TabPanel1(panelId);
 *                }
 *                
 *                });
 *               
 *                tabs.add(new AbstractTab(new Model(&quot;second tab&quot;)) {
 *               
 *                public Panel getPanel(String panelId)
 *                {
 *                return new TabPanel2(panelId);
 *                }
 *                
 *                });
 *               
 *                add(new TabbedPanel(&quot;tabs&quot;, tabs);
 *            
 *                
 *                &lt;span wicket:id=&quot;tabs&quot; class=&quot;tabpanel&quot;&gt;[tabbed panel will be here]&lt;/span&gt;
 *            
 * </pre>
 * 
 * </p>
 * 
 * <p>
 * For a complete example see the component references in wicket-examples
 * project
 * </p>
 * 
 * @see wicket.extensions.markup.html.tabs.ITab
 * 
 * @author Igor Vaynberg (ivaynberg)
 * 
 */
public class TabbedPanel extends Panel
{
	private static final long serialVersionUID = 1L;

	/**
	 * id used for child panels
	 */
	public static final String TAB_PANEL_ID = "panel";


	private List tabs;

	/**
	 * Constructor
	 * 
	 * @param id
	 *            component id
	 * @param tabs
	 *            list of ITab objects used to represent tabs
	 */
	public TabbedPanel(String id, List tabs)
	{
		super(id, new Model(new Integer(-1)));

		if (tabs == null)
		{
			throw new IllegalArgumentException("argument [tabs] cannot be null");
		}

		if (tabs.size() < 1)
		{
			throw new IllegalArgumentException(
					"argument [tabs] must contain a list of at least one tab");
		}

		this.tabs = tabs;

		// add the loop used to generate tab names
		add(new Loop("tabs", tabs.size())
		{
			private static final long serialVersionUID = 1L;

			protected void populateItem(LoopItem item)
			{
				final int index = item.getIteration();
				final ITab tab = ((ITab)TabbedPanel.this.tabs.get(index));
				final int selected = getSelectedTab();

				final Link titleLink = new Link("link")
				{
					private static final long serialVersionUID = 1L;

					public void onClick()
					{
						setSelectedTab(index);
					}
				};

				titleLink.add(new Label("title", tab.getTitle()));
				item.add(titleLink);

				item.add(new AttributeModifier("class", true, new Model("selected"))
				{
					private static final long serialVersionUID = 1L;

					public boolean isEnabled()
					{
						return index == selected;
					}

				});
			}

		});

		// select the first tab by default
		setSelectedTab(0);

	}

	/**
	 * sets the selected tab
	 * 
	 * @param index
	 *            index of the tab to select
	 * 
	 */
	public final void setSelectedTab(int index)
	{
		if (index < 0 || index >= tabs.size())
		{
			throw new IndexOutOfBoundsException();
		}

		setModelObject(new Integer(index));

		ITab tab = (ITab)tabs.get(index);

		Panel panel = tab.getPanel(TAB_PANEL_ID);

		if (panel == null)
		{
			throw new WicketRuntimeException("ITab.getPanel() returned null. TabbedPanel ["
					+ getPath() + "] ITab index [" + index + "]");

		}

		if (!panel.getId().equals(TAB_PANEL_ID))
		{
			throw new WicketRuntimeException(
					"ITab.getPanel() returned a panel with invalid id ["
							+ panel.getId()
							+ "]. You must always return a panel with id equal to the provided panelId parameter. TabbedPanel ["
							+ getPath() + "] ITab index [" + index + "]");
		}


		if (get(TAB_PANEL_ID) == null)
		{
			add(panel);
		}
		else
		{
			replace(panel);
		}
	}

	/**
	 * @return index of the selected tab
	 */
	public final int getSelectedTab()
	{
		return ((Integer)getModelObject()).intValue();
	}

}
