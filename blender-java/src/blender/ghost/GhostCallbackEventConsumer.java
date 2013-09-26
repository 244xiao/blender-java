package blender.ghost;

public class GhostCallbackEventConsumer {
	
	GhostEventCallbackProcPtr eventCallback;
	Object userData;
	
	public GhostCallbackEventConsumer(GhostEventCallbackProcPtr eventCallback, Object userData) {
		this.eventCallback = eventCallback;
		this.userData = userData;
	}

}
