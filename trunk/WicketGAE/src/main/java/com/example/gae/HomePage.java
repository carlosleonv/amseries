package com.example.gae;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;

public class HomePage extends WebPage {

  public HomePage() {
    add(new Label("hello", "Hello World"));
    add(new Link("page1link"){
    	  public void onClick(){
    	     setResponsePage(Page1.class);
    	  }
    	});
  }

}
