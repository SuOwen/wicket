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
package org.apache.wicket.core.util.string.componentrenderer;

import static org.junit.Assert.assertEquals;

import org.apache.wicket.core.util.string.ComponentRenderer;
import org.apache.wicket.markup.html.basic.Label;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link ComponentRenderer}
 */
public class ComponentRendererInstanceTest
{
	private ComponentRenderer renderer;

	@Before
	public void setup() {
		renderer = new ComponentRenderer();
	}
	
	@After
	public void destroy() {
		renderer.destroy();
	}
	
	@Test
	public void render()
	{
		CharSequence html = renderer.renderComponent(() -> new Label("id", "Hello renderer"));
		
		assertEquals("Hello renderer", html.toString());
	}
}