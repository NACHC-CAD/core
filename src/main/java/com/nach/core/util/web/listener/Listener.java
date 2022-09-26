package com.nach.core.util.web.listener;

import java.io.OutputStream;

public interface Listener {

	public void notify(Object obj);

	public OutputStream getOut();
	
}
