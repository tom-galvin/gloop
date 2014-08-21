# gloop

Basic game loop and render functionality for 2D Java games.

## usage

Example usage:
	
	GameTimer gt = new GameTimer()
		.setTickHandler(new GameTimerTickHandler() {
			@Override
			public void tick(double delta, double total, boolean slow) {
				// TODO write update code here
			}
		})
		.setErrorHandler(new GameTimerErrorHandler() {
			@Override
			public void handle(Throwable e) {
				// TODO write error handler code here
			}
		})
		.setCleanupHandler(new GameTimerCleanupHandler() {
			@Override
			public void cleanup() {
				// TODO cleanup code here
			}
		})
		.setInterval(1.0 / 60.0) // 60 Hz
		.start();

Example Java 8 usage with Lambda expressions:

	GameTimer gt = new GameTimer()
	    .setTickHandler((delta, total, slow) -> { /* TODO write update code here */ })
	    .setErrorHandler(err -> { /* TODO write error handler code here */})
	    .setCleanupHandler(() -> { /* TODO cleanup code here */ })
	    .setInterval(1.0 / 60.0) // 60 Hz
	    .start();

You do not need to set all the handlers - in fact, only the tick handler is required. The error handler prints the stack trace by default, and the cleanup handler does nothing by default.