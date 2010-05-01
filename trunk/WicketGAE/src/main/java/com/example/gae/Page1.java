package com.example.gae;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;

public class Page1 extends WebPage {

  public Page1() {
    add(new Label("page1", "Page 1"));
    add(new Link("homePageLink"){
  	  public void onClick(){
  	     setResponsePage(HomePage.class);
  	  }
  	});
  }

}
